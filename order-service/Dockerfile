FROM eclipse-temurin:17-jdk

WORKDIR /app

# netcat yükle

COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar



# Ortak environment variable’lar Docker Compose üzerinden gelecek
ENV SPRING_PROFILES_ACTIVE=docker

# RabbitMQ, Redis ve Postgres hazır olana kadar bekle
ENTRYPOINT ["java", "-jar", "app.jar"]
