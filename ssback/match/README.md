# Match Service — Sanos y Salvos

Motor de coincidencias impulsado por IA. Orquesta datos de mascotas perdidas y encontradas,
anonimiza información sensible y consulta GPT-5.4 para determinar porcentajes de similitud.

## Puerto
`8084`

## Tecnologías
- Spring Boot 3.x
- Spring Data JPA
- OpenFeign (clientes hacia Mascotas y Geo)
- OpenAI API (GPT-5.4 — Responses API)
- MySQL 8

## Base de Datos
Nombre: db_sanosysalvos_match
Host:   localhost:3306
## Variables de Entorno
OPENAI_API_KEY=tu_api_key_aqui
## Configuración
```sql
CREATE DATABASE db_sanosysalvos_match;
```

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/match/analizar/{idReportePerdido}` | Ejecuta análisis IA y retorna coincidencias |

## Ejecución

```bash
cd ssback/match
mvn spring-boot:run
```

## Pruebas Unitarias

```bash
mvn test
```

Cobertura: AiEngineService, MatchOrchestratorService, MatchController
Tests: 13 — Failures: 0