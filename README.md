# API REST - Gestor de Videojuegos

Proyecto Spring Boot que implementa:

- Registro de usuarios con password hasheada.
- Login con JWT.
- CRUD de videojuegos.
- Endpoints protegidos con token para `POST`, `PUT` y `DELETE`.
- Persistencia con H2 en fichero (no en memoria).
- Documentacion OpenAPI/Swagger.

## Ejecutar

```bash
mvn spring-boot:run
```

Swagger UI:

- `http://localhost:8080/swagger-ui.html`

## Endpoints principales

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/games`
- `GET /api/games/{id}`
- `POST /api/games` (requiere `Authorization: Bearer <token>`)
- `PUT /api/games/{id}` (requiere token y ser propietario)
- `DELETE /api/games/{id}` (requiere token y ser propietario)
