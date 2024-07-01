FROM tomcat:latest
EXPOSE 8080
ENV JAR_FILE=target/techtask-*.jar
COPY ${JAR_FILE} /techtask.jar
ENTRYPOINT ["java", "-jar", "/techtask.jar"]
HEALTHCHECK --interval=5s --timeout=10s --retries=3 CMD curl --silent --fail http://localhost/health || exit 1
