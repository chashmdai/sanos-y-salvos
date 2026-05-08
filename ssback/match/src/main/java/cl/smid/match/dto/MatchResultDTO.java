package cl.smid.match.dto;

import java.time.LocalDateTime;

public record MatchResultDTO(
    Long idMatch,
    Long idReportePerdido,
    Long idReporteEncontrado,
    Double porcentajeSimilitud,
    String razonamiento,
    String estado, // PENDIENTE, CONFIRMADO_POR_DUEÑO, DESCARTADO
    LocalDateTime fechaCreacion
) {}