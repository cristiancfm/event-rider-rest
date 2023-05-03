# event-rider-rest

## Dependecies
Install the following dependencies:
- JDK 17
- PostgreSQL with the PostGIS extension

## Configuration
Open pgAdmin and create a database named `event_rider`.
Create an user named `event_rider_user` with the password `1234` and grant it all privileges.

Modify file `src/main/resources/application.yml` with the database credentials. Modify also
the `imagesPath` property with the path to the folder where the images are stored 
(C:\folder\... on Windows systems or /folder/... on UNIX/Linux systems).

It may be necessary to execute the command `CREATE EXTENSION postgis;` by opening pgAdmin, selecting the
database event_rider, and clicking on the "Query Tool" icon.

## Running

```
 ./mvnw spring-boot:run
```
