FROM openjdk:21
WORKDIR /app
COPY target/techtask.jar techtask.jar
EXPOSE 8080
CMD ["java", "-jar", "techtask.jar"]
