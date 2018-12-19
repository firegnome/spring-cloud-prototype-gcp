@echo off

REM set global variables:
CALL ../set-variables

SET microservice=gateway-microservice
SET jarfile=build/libs/gatewaymicroservice-0.0.1-SNAPSHOT.jar

REM build
CALL gradlew build

REM create docker file
CALL docker build -t eu.gcr.io/%project%/%microservice%:latest --build-arg JAR_FILE=%jarfile% .

REM push docker file
CALL gcloud docker -- push eu.gcr.io/%project%/%microservice%:latest

REM generate kubernetes files from templates
IF NOT EXIST "kubernetes\generated\" mkdir kubernetes\generated\
COPY kubernetes\template\deployment.yaml kubernetes\generated\
COPY kubernetes\template\service.yaml kubernetes\generated\
REM replace template variables
powershell -Command "(gc kubernetes\generated\deployment.yaml) -replace 'PROJECT_NAME', '%project%' | Out-File kubernetes\generated\deployment.yaml"

REM apply kubernetes components and services
ECHO apply kubernetes components and services
CALL kubectl apply -f kubernetes/generated/
CALL kubectl patch deployment %microservice% -p "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"modified\":\"%date%_%time::=-%\"}}}}}"