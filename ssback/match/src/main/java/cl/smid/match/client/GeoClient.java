package cl.smid.match.client;

import cl.smid.match.dto.GeoDistanciaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "geo-service", url = "${geo.service.url:http://localhost:8083}")
public interface GeoClient {

    // Le pasamos el ID del reporte de la mascota perdida y el radio de búsqueda (ej. 5.0 km).
    // Nos devolverá una lista con los IDs de las mascotas encontradas cerca y a qué distancia están.
    @GetMapping("/geo/cercanos")
    List<GeoDistanciaDTO> buscarReportesCercanos(
            @RequestParam("idReporte") Long idReporte, 
            @RequestParam("radioKm") Double radioKm
    );
}