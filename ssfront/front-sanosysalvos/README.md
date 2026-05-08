# Frontend — Sanos y Salvos

Interfaz de usuario desarrollada en React + TypeScript con Vite.
Consume los microservicios a través del API Gateway en puerto 8080.

## Puerto
`5173`

## Tecnologías
- React 18 + TypeScript
- Vite
- Tailwind CSS

## Vistas Disponibles

| Vista | Descripción |
|---|---|
| Login | Autenticación de usuarios |
| CRUD Usuarios | Gestión de usuarios del sistema |
| Match Panel | Orquestador IA — análisis de coincidencias |

## Requisitos
- Node.js 18+
- API Gateway corriendo en `localhost:8080`

## Instalación y Ejecución

```bash
cd ssfront/front-sanosysalvos
npm install
npm run dev
```

## Variables de Entorno
Crear archivo `.env` en la raíz del frontend:
```
VITE_API_BASE_URL=http://localhost:8080
```