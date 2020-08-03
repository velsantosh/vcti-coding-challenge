FROM openjdk:8-jdk-alpine
EXPOSE 8761
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
#ADD /target/netflix-eureka-naming-server-1.0.jar netflix-eureka-naming-server-1.0.jar
#ENTRYPOINT ["java","-jar","netflix-eureka-naming-server-1.0.jar"]
