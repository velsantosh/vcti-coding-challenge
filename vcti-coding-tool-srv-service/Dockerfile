FROM mcr.microsoft.com/java/jdk:8-zulu-ubuntu-18.04
EXPOSE 8083
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
