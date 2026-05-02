FROM eclipse-temurin:21-jre

WORKDIR /app
COPY target/servicio-pedidos-*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
