FROM openjdk:21
EXPOSE 8080
WORKDIR /app
COPY target/techtask.jar techtask.jar
ENTRYPOINT ["java", "-jar", "/techtask.jar"]
