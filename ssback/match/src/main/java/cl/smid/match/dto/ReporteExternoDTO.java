package cl.smid.match.dto;

import java.time.LocalDateTime;

public record ReporteExternoDTO(
    Long idReporte,
    String tipoReporte, // "PERDIDO" o "ENCONTRADO"
    LocalDateTime fechaIncidente,
    Long idMascota,
    String especie,
    String raza,
    String tamano,
    String colorPrincipal,
    String senalesParticulares
) {}