FROM openjdk:17
EXPOSE 4000
ADD ./target/creaciones-joaquin-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]