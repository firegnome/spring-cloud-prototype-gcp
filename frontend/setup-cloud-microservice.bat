@echo off

REM set global variables:
CALL ../set-variables

SET microservice=frontend

REM npm
CALL npm install

REM build
CALL node_modules\.bin\ng build --prod

REM create docker file
CALL docker build -t eu.gcr.io/%project%/%microservice%:latest .

REM push docker file
CALL gcloud docker -- push eu.gcr.io/%project%/%microservice%:latest

REM generate kubernetes files from templates
IF NOT EXIST "kubernetes\generated\" mkdir kubernetes\generated\
COPY kubernetes\template\frontend.yaml kubernetes\generated\
REM replace template variables
powershell -Command "(gc kubernetes\generated\frontend.yaml) -replace 'PROJECT_NAME', '%project%' | Out-File kubernetes\generated\frontend.yaml"

REM apply kubernetes components and services
ECHO apply kubernetes components and services
CALL kubectl apply -f kubernetes/generated/
CALL kubectl patch deployment %microservice% -p "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"modified\":\"%date%_%time::=-%\"}}}}}"