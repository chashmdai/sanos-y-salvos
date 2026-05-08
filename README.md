# 🐾 Sanos y Salvos — Monorepo

Plataforma inteligente para la localización y recuperación de mascotas perdidas.
Arquitectura basada en microservicios con integración de IA (GPT-5.4) para detección automática de coincidencias.

## Estructura del Repositorio
sanos-y-salvos/
├── ssback/
│   ├── api-gateway/       → BFF Gateway (Puerto 8080)
│   ├── auth/              → Auth Service (Puerto 8081)
│   └── match/             → Match Service (Puerto 8084)
└── ssfront/
└── front-sanosysalvos/ → Frontend React + Vite (Puerto 5173)
## Microservicios

| Servicio | Puerto | Descripción |
|---|---|---|
| API Gateway | 8080 | Enrutamiento, Circuit Breaker, Auditoría |
| Auth Service | 8081 | Autenticación JWT |
| Match Service | 8084 | Motor IA de coincidencias con GPT-5.4 |
| Frontend | 5173 | Interfaz React + Vite |

## Requisitos Globales

- Java 21
- Maven 3.9+
- MySQL 8
- Node.js 18+
- API Key de OpenAI (GPT-5.4)

## Orden de Arranque

1. Auth Service
2. Match Service
3. API Gateway
4. Frontend

## Patrones Implementados

- **API Gateway / BFF** — Spring Cloud Gateway con WebFlux
- **Circuit Breaker** — Resilience4j en rutas críticas
- **Repository** — Spring Data JPA en cada microservicio
- **Builder** — Lombok @Builder para construcción de DTOs