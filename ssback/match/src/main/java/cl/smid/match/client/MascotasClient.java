package cl.smid.match.client;

import cl.smid.match.dto.ReporteExternoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Usamos el nombre del servicio y la URL directa para desarrollo local.
// En un entorno productivo con Eureka/Consul, solo usaríamos el 'name'.
@FeignClient(name = "mascotas-service", url = "${mascotas.service.url:http://localhost:8082}")
public interface MascotasClient {

    // Obtiene los detalles completos de un reporte (el que se perdió)
    @GetMapping("/mascotas/reportes/{id}")
    ReporteExternoDTO obtenerReportePorId(@PathVariable("id") Long id);

    // Dada una lista de IDs (los que nos dirá el geo-service que están cerca),
    // le pedimos al mascotas-service que nos devuelva los datos completos de esas mascotas encontradas.
    @GetMapping("/mascotas/reportes/encontrados")
    List<ReporteExternoDTO> obtenerReportesEncontradosPorIds(@RequestParam("ids") List<Long> ids);
}