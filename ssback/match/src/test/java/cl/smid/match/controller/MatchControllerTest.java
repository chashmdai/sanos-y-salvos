package cl.smid.match.controller;

import cl.smid.match.dto.AiMatchResponseDTO;
import cl.smid.match.service.MatchOrchestratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private MatchOrchestratorService orchestratorService;

    // Verifica 200 OK con body cuando hay coincidencias
    @Test
    void dispararAnalisis_cuandoHayCoincidencias_retorna200() throws Exception {
        AiMatchResponseDTO.CoincidenciaDTO coincidencia =
                new AiMatchResponseDTO.CoincidenciaDTO(99L, 85.0, "Coinciden características");
        AiMatchResponseDTO response = new AiMatchResponseDTO(List.of(coincidencia));

        when(orchestratorService.procesarMatchIA(1L)).thenReturn(response);

        mockMvc.perform(get("/match/analizar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coincidencias[0].idReporteEncontrado").value(99))
                .andExpect(jsonPath("$.coincidencias[0].porcentajeSimilitud").value(85.0));
    }

    // Verifica 204 No Content cuando la lista viene vacía
    @Test
    void dispararAnalisis_cuandoListaVacia_retorna204() throws Exception {
        AiMatchResponseDTO response = new AiMatchResponseDTO(List.of());
        when(orchestratorService.procesarMatchIA(1L)).thenReturn(response);

        mockMvc.perform(get("/match/analizar/1"))
                .andExpect(status().isNoContent());
    }

    // Verifica 204 No Content cuando el servicio retorna null
    @Test
    void dispararAnalisis_cuandoRespuestaEsNull_retorna204() throws Exception {
        when(orchestratorService.procesarMatchIA(1L)).thenReturn(null);

        mockMvc.perform(get("/match/analizar/1"))
                .andExpect(status().isNoContent());
    }

    // Verifica 204 cuando coincidencias es null dentro del DTO
    @Test
    void dispararAnalisis_cuandoCoincidenciasEsNull_retorna204() throws Exception {
        AiMatchResponseDTO response = new AiMatchResponseDTO(null);
        when(orchestratorService.procesarMatchIA(1L)).thenReturn(response);

        mockMvc.perform(get("/match/analizar/1"))
                .andExpect(status().isNoContent());
    }
}