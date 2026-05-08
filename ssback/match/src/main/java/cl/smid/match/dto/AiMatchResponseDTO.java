package cl.smid.match.dto;

import java.util.List;

public record AiMatchResponseDTO(
    List<CoincidenciaDTO> coincidencias
) {
    public record CoincidenciaDTO(
        Long idReporteEncontrado,
        Double porcentajeSimilitud,
        String razonamiento
    ) {}
}