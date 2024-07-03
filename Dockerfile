# Используйте официальное изображение OpenJDK в качестве базового изображения
FROM openjdk:21

# Установите рабочую директорию внутри контейнера
WORKDIR /app

# Копируйте jar файл в контейнер
COPY target/techtask-*.jar /app/techtask.jar

# Установите команду запуска контейнера
ENTRYPOINT ["java", "-jar", "/app/techtask.jar"]
