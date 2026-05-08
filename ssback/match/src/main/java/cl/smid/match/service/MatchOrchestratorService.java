package cl.smid.match.service;

import cl.smid.match.client.GeoClient;
import cl.smid.match.client.MascotasClient;
import cl.smid.match.dto.*;
import cl.smid.match.entity.Match;
import cl.smid.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchOrchestratorService {

    private static final Logger log = LoggerFactory.getLogger(MatchOrchestratorService.class);

    private final MascotasClient mascotasClient;
    private final GeoClient geoClient;
    private final AiEngineService aiEngineService;
    private final MatchRepository matchRepository;

    public AiMatchResponseDTO procesarMatchIA(Long idReportePerdido) {
        log.info("Iniciando orquestación MOCK de Match para reporte: {}", idReportePerdido);

        /*
        // --- FLUJO REAL (descomentar cuando 8082 y 8083 estén disponibles) ---

        ReporteExternoDTO reportePerdido = mascotasClient.obtenerReportePorId(idReportePerdido);
        MascotaDTO perdida = mapearAMascotaDTO(reportePerdido);

        List<GeoDistanciaDTO> cercanos = geoClient.buscarReportesCercanos(idReportePerdido, 5.0);

        List<Long> idsCercanos = cercanos.stream()
                .map(GeoDistanciaDTO::idReporte)
                .toList();

        List<MascotaDTO> candidatas = mascotasClient.obtenerReportesEncontradosPorIds(idsCercanos)
                .stream()
                .map(this::mapearAMascotaDTO)
                .toList();
        */

        // --- MOCK ---
        MascotaDTO perdida = MascotaDTO.builder()
                .idReporte(idReportePerdido)
                .especie("Perro")
                .raza("Mestizo")
                .color("Café")
                .senalesParticulares("Cicatriz en la oreja izquierda y collar rojo.")
                .estado("PERDIDO")
                .build();

        List<MascotaDTO> candidatas = List.of(
                MascotaDTO.builder()
                        .idReporte(99L)
                        .especie("Perro")
                        .raza("Quiltro")
                        .color("Marrón")
                        .senalesParticulares("Oreja izquierda lastimada, sin collar.")
                        .estado("ENCONTRADO")
                        .build()
        );

        AiPromptRequestDTO request = new AiPromptRequestDTO(perdida, candidatas);
        AiMatchResponseDTO response = aiEngineService.analizarCoincidencias(request);

        if (response != null && response.coincidencias() != null) {
            for (AiMatchResponseDTO.CoincidenciaDTO coincidencia : response.coincidencias()) {

                // Dedup: evitar persistir un match que ya existe
                if (matchRepository.existsByIdReportePerdidoAndIdReporteEncontrado(
                        idReportePerdido, coincidencia.idReporteEncontrado())) {
                    log.warn("Match duplicado ignorado: perdido={} encontrado={}",
                            idReportePerdido, coincidencia.idReporteEncontrado());
                    continue;
                }

                Match nuevoMatch = Match.builder()
                        .idReportePerdido(idReportePerdido)
                        .idReporteEncontrado(coincidencia.idReporteEncontrado())
                        .porcentajeSimilitud(coincidencia.porcentajeSimilitud())
                        .razonamiento(coincidencia.razonamiento())
                        .build();

                matchRepository.save(nuevoMatch);
            }

            log.info("Se guardaron {} coincidencias en la base de datos.", response.coincidencias().size());
        }

        return response;
    }

    private MascotaDTO mapearAMascotaDTO(ReporteExternoDTO externo) {
        return MascotaDTO.builder()
                .idReporte(externo.idReporte())
                .especie(externo.especie())
                .raza(externo.raza())
                .color(externo.colorPrincipal())
                .senalesParticulares(externo.senalesParticulares())
                .estado(externo.tipoReporte())
                .build();
    }
}