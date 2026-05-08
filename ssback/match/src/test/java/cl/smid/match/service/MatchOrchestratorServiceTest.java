package cl.smid.match.service;

import cl.smid.match.client.GeoClient;
import cl.smid.match.client.MascotasClient;
import cl.smid.match.dto.AiMatchResponseDTO;
import cl.smid.match.dto.AiPromptRequestDTO;
import cl.smid.match.entity.Match;
import cl.smid.match.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchOrchestratorServiceTest {

    @Mock private MascotasClient mascotasClient;
    @Mock private GeoClient geoClient;
    @Mock private AiEngineService aiEngineService;
    @Mock private MatchRepository matchRepository;

    @InjectMocks
    private MatchOrchestratorService orchestratorService;

    // Verifica que cuando la IA retorna coincidencias, se persisten en la BD
    @Test
    void procesarMatchIA_cuandoHayCoincidencias_guardaEnBD() {
        AiMatchResponseDTO.CoincidenciaDTO coincidencia =
                new AiMatchResponseDTO.CoincidenciaDTO(99L, 85.0, "Coinciden especie y señales");
        AiMatchResponseDTO responseIA = new AiMatchResponseDTO(List.of(coincidencia));

        when(aiEngineService.analizarCoincidencias(any(AiPromptRequestDTO.class))).thenReturn(responseIA);
        when(matchRepository.existsByIdReportePerdidoAndIdReporteEncontrado(anyLong(), anyLong())).thenReturn(false);

        orchestratorService.procesarMatchIA(1L);

        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository, times(1)).save(captor.capture());

        Match guardado = captor.getValue();
        assertEquals(1L, guardado.getIdReportePerdido());
        assertEquals(99L, guardado.getIdReporteEncontrado());
        assertEquals(85.0, guardado.getPorcentajeSimilitud());
    }

    // Verifica que el dedup funciona: si el match ya existe, no se llama save()
    @Test
    void procesarMatchIA_cuandoMatchYaExiste_noGuardaDuplicado() {
        AiMatchResponseDTO.CoincidenciaDTO coincidencia =
                new AiMatchResponseDTO.CoincidenciaDTO(99L, 85.0, "Duplicado");
        AiMatchResponseDTO responseIA = new AiMatchResponseDTO(List.of(coincidencia));

        when(aiEngineService.analizarCoincidencias(any())).thenReturn(responseIA);
        when(matchRepository.existsByIdReportePerdidoAndIdReporteEncontrado(1L, 99L)).thenReturn(true);

        orchestratorService.procesarMatchIA(1L);

        verify(matchRepository, never()).save(any());
    }

    // Verifica que si la IA retorna null, el servicio no lanza excepción
    @Test
    void procesarMatchIA_cuandoRespuestaEsNull_noExplota() {
        when(aiEngineService.analizarCoincidencias(any())).thenReturn(null);

        assertDoesNotThrow(() -> orchestratorService.procesarMatchIA(1L));
        verify(matchRepository, never()).save(any());
    }

    // Verifica que si la IA retorna lista vacía, no se intenta guardar nada
    @Test
    void procesarMatchIA_cuandoListaVacia_noGuardaNada() {
        AiMatchResponseDTO responseIA = new AiMatchResponseDTO(List.of());
        when(aiEngineService.analizarCoincidencias(any())).thenReturn(responseIA);

        orchestratorService.procesarMatchIA(1L);

        verify(matchRepository, never()).save(any());
    }
}