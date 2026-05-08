package cl.smid.match.service;

import cl.smid.match.dto.AiMatchResponseDTO;
import cl.smid.match.dto.AiPromptRequestDTO;
import cl.smid.match.exception.AiServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AiEngineService {

    private static final Logger log = LoggerFactory.getLogger(AiEngineService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    
    @Value("${openai.api.key}")
    private String apiKey;

    // Patrón simple para detectar posibles números de teléfono en las descripciones (Ej: +56912345678 o 912345678)
    private static final Pattern PATRON_TELEFONO = Pattern.compile("(\\+?56)?\\s?9\\s?\\d{4}\\s?\\d{4}");

    public AiEngineService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // Se asume que el baseUrl se maneja igual que en tu otro proyecto
        this.restClient = RestClient.builder().baseUrl("https://api.openai.com/v1").build();
    }

    /**
     * Flujo principal de análisis orquestado para hacer Match de Mascotas.
     */
    public AiMatchResponseDTO analizarCoincidencias(AiPromptRequestDTO request) {
        // CORRECCIÓN: Se utiliza getIdReporte() generado por el @Data de Lombok
        log.info("Iniciando análisis GPT-5.4 para reporte perdido ID: {}", request.mascotaPerdida().getIdReporte());

        try {
            // 1. Transformar el objeto a JSON de entrada
            String datosBrutos = objectMapper.writeValueAsString(request);

            // 2. Escudo de Privacidad: Anonimización de datos sensibles en descripciones libres
            String datosAnonimizados = anonimizarDatosSensibles(datosBrutos);

            // 3. Construcción del Prompt Dinámico
            String promptFinal = construirPromptDinamico(datosAnonimizados);

            // 4. Ejecución del motor de IA (Usamos esfuerzo 'high' por la naturaleza deductiva visual del caso)
            return callOpenAi(promptFinal, "high", "medium");

        } catch (Exception e) {
            log.error("Error al procesar el motor GPT-5.4: {}", e.getMessage(), e);
            throw new AiServiceException("Fallo en la comunicación o procesamiento con el modelo de IA.", e);
        }
    }

    /**
     * Motor de Prompt Dinámico: Inyecta reglas de emparejamiento.
     */
    private String construirPromptDinamico(String datosAnonimizados) {
        String contextoNegocio = """
            REGLAS DE EMPAREJAMIENTO 'SANOS Y SALVOS' (Aplica estrictamente esto):
            - Tolerancia Humana: Sé tolerante a sinónimos en colores ('café'/'marrón', 'beige'/'crema') y razas ('mestizo'/'quiltro').
            - Bloqueo por Especie: Si la especie es distinta (Ej: Gato vs Perro), el porcentaje de similitud es 0.0 obligatoriamente.
            - Peso de Señales Particulares: Las 'senalesParticulares' (manchas, cicatrices, collares) tienen un impacto crítico en el cálculo.
            - Umbral Mínimo: Descarta automáticamente cualquier candidato con similitud inferior al 70.0%. No lo incluyas en el JSON final.
            """;

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("ERES EL MOTOR ANALÍTICO DE MATCH DE MASCOTAS.\n\n");
        
        promptBuilder.append(contextoNegocio).append("\n");
        
        promptBuilder.append("PASOS DEL ANÁLISIS:\n")
                     .append("1. NORMALIZACIÓN: Lee las características de la 'mascotaPerdida'.\n")
                     .append("2. COMPARACIÓN: Cruza estos datos con cada objeto dentro de 'mascotasEncontradasCandidatas'.\n")
                     .append("3. CÁLCULO: Define un porcentaje decimal (0.0 a 100.0) de similitud.\n")
                     .append("4. FILTRADO: Retén solo aquellos >= 70.0%.\n\n");
        
        promptBuilder.append("DEVUELVE ESTRICTAMENTE UN JSON CON ESTA ESTRUCTURA (Si no hay matches, devuelve la lista vacía []):\n")
                     .append("{ 'coincidencias': [ { 'idReporteEncontrado': 0, 'porcentajeSimilitud': 0.0, 'razonamiento': 'explicación breve' } ] }\n\n");

        promptBuilder.append("DATOS A EVALUAR:\n")
                     .append(datosAnonimizados);

        return promptBuilder.toString();
    }

    /**
     * Filtro de seguridad para evitar que datos personales (ej. teléfonos en señales particulares) viajen a la IA.
     */
    private String anonimizarDatosSensibles(String texto) {
        if (texto == null || texto.isBlank()) return "";
        // Reemplaza teléfonos por la etiqueta segura
        return PATRON_TELEFONO.matcher(texto).replaceAll("[TELEFONO_PROTEGIDO]");
    }

    /**
     * Integración con la API moderna de OpenAI usando payload de modelo 5.x
     */
    @SuppressWarnings("null")
    private AiMatchResponseDTO callOpenAi(String input, String effort, String verbosity) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", "gpt-5.4");
            payload.put("input", input);
            
            payload.put("reasoning", Map.of("effort", effort));
            payload.put("text", Map.of(
                "verbosity", verbosity,
                "format", Map.of("type", "json_object")
            ));

            String rawResponse = restClient.post()
                    .uri("/responses")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(String.class);

            JsonNode rootNode = objectMapper.readTree(rawResponse);
            String jsonOutput = "";

            // Navegación en el arreglo "output" buscando el "message"
            for (JsonNode node : rootNode.path("output")) {
                if ("message".equals(node.path("type").asText())) {
                    jsonOutput = node.path("content").get(0).path("text").asText();
                }
            }

            // Mapeo directo al DTO de respuestas de Match
            return objectMapper.readValue(jsonOutput, AiMatchResponseDTO.class);
            
        } catch (Exception e) {
            throw new RuntimeException("Fallo en la llamada al motor GPT-5.4: " + e.getMessage(), e);
        }
    }
}