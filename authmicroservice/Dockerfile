FROM openjdk:8-jdk-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ADD src/main/docker/start.sh start.sh
ADD src/main/docker/wait-for-it.sh wait-for-it.sh
ENTRYPOINT ./start.sh