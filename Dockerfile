FROM openjdk:21
WORKDIR /app
COPY target/techtask-*.jar /app/techtask.jar
ENTRYPOINT ["java", "-jar", "/app/techtask.jar"]

