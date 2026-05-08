package cl.smid.match.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalMatchExceptionHandler {

    // Maneja errores específicos de nuestra comunicación con la IA (OpenAI)
    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<Map<String, Object>> handleAiServiceException(AiServiceException ex) {
        return buildErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE, 
                "Error en Motor de Emparejamiento IA", 
                ex.getMessage()
        );
    }

    // Maneja errores si el mascotas-service o geo-service están caídos o dan error 404/500
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        String mensaje = "Error al comunicarse con un servicio interno (Mascotas/Geo).";
        if (ex.status() == 404) {
            mensaje = "El recurso solicitado en el servicio interno no existe.";
        }
        
        return buildErrorResponse(
                HttpStatus.valueOf(ex.status() == -1 ? 503 : ex.status()), 
                "Fallo en Comunicación Interna", 
                mensaje
        );
    }

    // Manejador global para cualquier otra excepción no contemplada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error Interno del Servidor", 
                "Ocurrió un error inesperado en el servicio de Match."
        );
    }

    // Método utilitario para construir el JSON estandarizado
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}