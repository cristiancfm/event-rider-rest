# Event Rider (Web Server)

## Introduction
**Event Rider** is a web application to manage and visualize events, which allows
users to create and find events happening at a specific place in a simple way.

![homepage-screenshot](https://github.com/cristiancfm/tfg-rest/assets/72354794/9718c617-40c9-43a5-8724-239cad2332a3)

Users may filter events or view them using an interactive map, and also save events
into their profiles or get email notifications by subscribing to events and categories or
following other users.

Administrators can manage users, events and categories using a separate section of the
application. They can approve or reject events created by users, create or remove
categories, suspend or mark users as verified, designate new administrators, etc.

Developed for my Bachelor's Final Project at the University of A Coruña (UDC).

## Dependecies
Install the following dependencies:
- JDK 17
- PostgreSQL with the PostGIS extension

## Configuration
Open pgAdmin and create a database named `event_rider`. By default, the username and password
used to access the database are `postgres`.

Modify file `src/main/resources/application.yml` with the database credentials. Modify also
the `imagesPath` property with the path to the folder where the images are stored 
(C:\folder\... on Windows systems or /folder/... on UNIX/Linux systems). 

If you want the application to send email notifications, you need to set up the `mailUsername`
and `mailPassword` properties as well.

It may be necessary to execute the command `CREATE EXTENSION postgis;` by opening pgAdmin, 
selecting the database event_rider, and clicking on the "Query Tool" icon.

## Running

```
 ./mvnw spring-boot:run
```
