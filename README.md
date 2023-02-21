# event-rider-rest

## Configuración
(Se necesita tener PostgreSQL instalado). Abrir pgAdmin y crear una base de datos con el nombre `event_rider`.
Crear un usuario con el nombre `event_rider_user` y la contraseña `1234` y darle todos los privilegios.

Modificar el fichero `src/main/resources/application.yml` con las credenciales de la base de datos. Modificar
en el mismo fichero
la propiedad `imagesPath` con la ruta de la carpeta donde se guardan las imágenes (C:\\carpeta\\... en sistemas
Windows o /carpeta/... en sistemas UNIX/Linux).

## Ejecutar

```
 ./mvnw spring-boot:run
```
