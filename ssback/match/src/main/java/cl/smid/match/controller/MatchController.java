package cl.smid.match.controller;

import cl.smid.match.dto.AiMatchResponseDTO;
import cl.smid.match.service.MatchOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchOrchestratorService orchestratorService;

    @GetMapping("/analizar/{idReportePerdido}")
    public ResponseEntity<AiMatchResponseDTO> dispararAnalisis(@PathVariable Long idReportePerdido) {
        AiMatchResponseDTO response = orchestratorService.procesarMatchIA(idReportePerdido);

        if (response == null || response.coincidencias() == null || response.coincidencias().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}