package cl.smid.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuditLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuditLoggingFilter.class);

    @SuppressWarnings("null")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        
        String ipAddress = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = exchange.getRequest().getRemoteAddress() != null ? 
                        exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() : "Desconocida";
        }

        log.info("▶ [BFF AUDITORÍA INICIO] Request: {} {} | IP Cliente: {}", method, path, ipAddress);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            
            int statusCode = exchange.getResponse().getStatusCode() != null ? 
                             exchange.getResponse().getStatusCode().value() : 500;
            
            log.info("◀ [BFF AUDITORÍA FIN] Request: {} {} | Status: {} | Tiempo: {}ms", 
                     method, path, statusCode, duration);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}