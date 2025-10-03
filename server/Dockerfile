FROM openjdk:17

RUN mkdir -p /app/server
WORKDIR /app/server
COPY . .

CMD chmod +x mvnw && ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
