FROM tomcat:latest
EXPOSE 8080
ENV JAR_FILE=target/techtask-*.jar
COPY ${JAR_FILE} /techtask.jar
ENTRYPOINT ["java", "-jar", "/techtask.jar"]
