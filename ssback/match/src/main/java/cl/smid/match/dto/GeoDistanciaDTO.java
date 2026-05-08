package cl.smid.match.dto;

public record GeoDistanciaDTO(
    Long idReporte,
    Double latitud,
    Double longitud,
    Double distanciaKm // A cuántos KM de distancia está del reporte original
) {}