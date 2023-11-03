FROM openjdk:17
ADD target/*.jar app.jar
EXPOSE 8763
ENTRYPOINT [ "java", "-jar", "app.jar" ]

