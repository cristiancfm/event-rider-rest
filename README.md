# event-rider-rest

## Configuración
(Se necesita tener PostgreSQL instalado). Abrir pgAdmin y crear una base de datos con el nombre `event_rider`.
Crear un usuario con el nombre `event_rider_user` y la contraseña `1234` y darle todos los privilegios.

Modificar el fichero `src/main/resources/application.yml` con las credenciales de la base de datos. Modificar
en el mismo fichero
la propiedad `imagesPath` con la ruta de la carpeta donde se guardan las imágenes (C:\\carpeta\\... en sistemas
Windows o /carpeta/... en sistemas UNIX/Linux).

Es posible que sea necesario ejecutar el comando `CREATE EXTENSION postgis;` abriendo pgAdmin, seleccionando la
base de datos `event_rider` y haciendo click en el icono de "Query Tool".

## Ejecutar

```
 ./mvnw spring-boot:run
```
