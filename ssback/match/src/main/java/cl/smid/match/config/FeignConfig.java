package cl.smid.match.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    // FULL nos mostrará en consola los headers, body y status de cada llamada interna.
    // En producción se suele cambiar a BASIC o NONE para no saturar los logs.
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}