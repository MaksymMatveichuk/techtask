FROM openjdk:21
EXPOSE 8080
ENV JAR_FILE=target/techtask-*.jar
COPY ${JAR_FILE} /techtask.jar
ENTRYPOINT ["java", "-jar", "/techtask.jar"]
RUN apt-get update 
RUN apt-get --yes install curl
HEALTHCHECK --interval=5s --timeout=10s --retries=3 CMD curl --silent --fail http://localhost/health || exit 1
