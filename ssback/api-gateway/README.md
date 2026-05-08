# API Gateway — Sanos y Salvos

BFF centralizado que gestiona el enrutamiento hacia todos los microservicios internos.

## Puerto
`8080`

## Tecnologías
- Spring Cloud Gateway (WebFlux)
- Resilience4j (Circuit Breaker)
- Java 21 con Virtual Threads

## Rutas Configuradas

| Ruta | Destino |
|---|---|
| `/auth/**` | Auth Service :8081 |
| `/match/**` | Match Service :8084 |
| `/mascotas/**` | Mascotas Service :8082 |
| `/geo/**` | Geo Service :8083 |
| `/fallback/general` | FallbackController (503) |

## Circuit Breaker
Configurado en rutas críticas con ventana deslizante y umbral de fallo del 50%.
Ante caída de un servicio retorna HTTP 503 desde `/fallback/general`.

## Ejecución

```bash
cd ssback/api-gateway
mvn spring-boot:run
```