package cl.smid.match.service;

import cl.smid.match.dto.AiPromptRequestDTO;
import cl.smid.match.dto.MascotaDTO;
import cl.smid.match.exception.AiServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiEngineServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    private AiEngineService aiEngineService;

    @BeforeEach
    void setUp() {
        aiEngineService = new AiEngineService(objectMapper);
        ReflectionTestUtils.setField(aiEngineService, "apiKey", "test-key-fake");
    }

    // Verifica que si el ObjectMapper falla al serializar, se lanza AiServiceException
    @Test
    void analizarCoincidencias_cuandoObjectMapperFalla_lanzaAiServiceException() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Error de serialización"));

        AiPromptRequestDTO request = buildRequest();

        assertThrows(AiServiceException.class, () -> aiEngineService.analizarCoincidencias(request));
    }

    // Verifica que el mensaje de la excepción contiene información útil
    @Test
    void analizarCoincidencias_cuandoFalla_mensajeExcepcionNoEsVacio() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("timeout"));

        AiPromptRequestDTO request = buildRequest();

        AiServiceException ex = assertThrows(AiServiceException.class,
                () -> aiEngineService.analizarCoincidencias(request));

        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // Verifica que el patrón de anonimización detecta teléfonos chilenos
    @Test
    void anonimizarDatosSensibles_reemplazaTelefonoChileno() throws Exception {
        // Accedemos al método privado via reflexión para testear la lógica de seguridad directamente
        java.lang.reflect.Method metodo = AiEngineService.class
                .getDeclaredMethod("anonimizarDatosSensibles", String.class);
        metodo.setAccessible(true);

        String textoConTelefono = "Mascota encontrada, llamar al +56912345678";
        String resultado = (String) metodo.invoke(aiEngineService, textoConTelefono);

        assertFalse(resultado.contains("56912345678"));
        assertTrue(resultado.contains("[TELEFONO_PROTEGIDO]"));
    }

    // Verifica que texto sin teléfono no es modificado
    @Test
    void anonimizarDatosSensibles_sinTelefono_noModificaTexto() throws Exception {
        java.lang.reflect.Method metodo = AiEngineService.class
                .getDeclaredMethod("anonimizarDatosSensibles", String.class);
        metodo.setAccessible(true);

        String textoLimpio = "Perro café con cicatriz en oreja";
        String resultado = (String) metodo.invoke(aiEngineService, textoLimpio);

        assertEquals(textoLimpio, resultado);
    }

    private AiPromptRequestDTO buildRequest() {
        MascotaDTO perdida = MascotaDTO.builder()
                .idReporte(1L).especie("Perro").raza("Mestizo")
                .color("Café").senalesParticulares("Sin señales").estado("PERDIDO")
                .build();
        return new AiPromptRequestDTO(perdida, List.of());
    }
}