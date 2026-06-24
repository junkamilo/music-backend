# Variables de entorno para Render (Web Service)

Configura estas variables en el panel de Render del backend. **No commitear credenciales reales.**

## Configuración local (desarrollo en PC)

[`application.properties`](src/main/resources/application.properties) apunta por defecto a la BD de pruebas en Render (URL externa + `sslmode=require`). Solo necesitas la contraseña en un archivo `.env` local (copia desde [`.env.example`](.env.example)):

```env
SPRING_DATASOURCE_PASSWORD=tu_password_de_render
```

Spring Boot carga `.env` automáticamente vía `spring.config.import`. Con eso basta:

```bash
./mvnw spring-boot:run
```

Si prefieres no usar `.env`, exporta la variable antes de arrancar:

```powershell
$env:SPRING_DATASOURCE_PASSWORD="tu_password_de_render"
.\mvnw.cmd spring-boot:run
```

## Base de datos PostgreSQL en Render

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_PASSWORD` | Contraseña (local: `.env`; en Render: Secret) |

URL y usuario ya están en `application.properties` para desarrollo local. En el deploy en Render, sobrescribe la URL con el hostname interno:

### Backend desplegado en Render (Internal URL)

```
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d8u2pkog4nts73di57q0-a:5432/music_platform_db_e5vz?sslmode=require
SPRING_DATASOURCE_USERNAME=music_platform_db
SPRING_DATASOURCE_PASSWORD=<tu-password-desde-panel-render>
```

### Conexión desde tu PC (ya en application.properties)

```
jdbc:postgresql://dpg-d8u2pkog4nts73di57q0-a.oregon-postgres.render.com:5432/music_platform_db_e5vz?sslmode=require
```

## Otras variables recomendadas

| Variable | Descripción |
|----------|-------------|
| `APP_JWT_SECRET` | Clave JWT (mín. 32 caracteres) |
| `APP_EMAIL_PROVIDER` | `log` o `resend` |
| `APP_EMAIL_FROM` | Email remitente |
| `APP_EMAIL_FRONTEND_BASE_URL` | URL del frontend en producción |

Render inyecta `PORT` automáticamente; Spring Boot lo usa para el servidor web.

## Primer deploy

Con una BD Postgres vacía en Render, Flyway ejecutará `V1__initial_schema.sql` al arrancar el backend.

## Seguridad

Rota la contraseña de la BD en Render si fue expuesta. Nunca subas `.env` con secretos al repositorio.
