# Demo

Aplicacion Spring Boot conectada a PostgreSQL/Neon mediante variables de entorno.

## Configuracion local

1. Copia `.env.example` a `.env`.
2. Completa los valores de Neon en `.env`.
3. Ejecuta la aplicacion:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicacion queda disponible en `http://localhost:8080`.

## Docker

```powershell
docker compose up --build
```

## Render

El archivo `render.yaml` crea un Web Service con Docker. En Render, agrega los valores reales de:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

No subas `.env` a GitHub.
