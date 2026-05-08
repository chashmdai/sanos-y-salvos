# Auth Service — Sanos y Salvos

Microservicio de autenticación y autorización mediante tokens JWT.

## Puerto
`8081`

## Tecnologías
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- MySQL 8

## Base de Datos
Nombre: db_sanosysalvos_auth
Host:   localhost:3306
## Configuración
Crear la base de datos antes de levantar el servicio:
```sql
CREATE DATABASE db_sanosysalvos_auth;
```

## Ejecución

```bash
cd ssback/auth
mvn spring-boot:run
```