# Event Rider (Web Server)

## Introduction
**Event Rider** is a web application to manage and visualize events, which allows
users to create and find events happening at a specific place in a simple way.

![homepage](https://github.com/cristiancfm/tfg-rest/assets/72354794/e1946c9e-a677-45d9-a243-a88aaddcc627)

Users may filter events or view them using an interactive map, and also save events
into their profiles or get email notifications by subscribing to events and categories or
following other users.

Administrators can manage users, events and categories using a separate section of the
application. They can approve or reject events created by users, create or remove
categories, suspend or mark users as verified, designate new administrators, etc.

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

It may be necessary to execute the command `CREATE EXTENSION postgis;` by opening pgAdmin, 
selecting the database event_rider, and clicking on the "Query Tool" icon.

## Running

```
 ./mvnw spring-boot:run
```
