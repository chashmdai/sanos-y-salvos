package cl.smid.apigateway.controller;

import cl.smid.apigateway.dto.GatewayErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/general")
    public ResponseEntity<GatewayErrorResponse> generalFallback() {
        GatewayErrorResponse errorResponse = GatewayErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .error("Servicio No Disponible (Circuit Breaker Abierto)")
                .message("Uno de los módulos de Sanos y Salvos está experimentando alta carga o está apagado. Por favor, reintente en unos momentos.")
                .path("Desconocido - Interceptado por BFF")
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}