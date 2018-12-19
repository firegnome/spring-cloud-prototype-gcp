@echo off

REM set global variables:
CALL ../set-variables

SET microservice=auth-microservice
SET jarfile=build/libs/authmicroservice-0.0.1-SNAPSHOT.jar

ECHO ### create cloud sql database ###
REM create cloud sql instance
CALL gcloud sql instances create userdb --cpu=1 --memory=3840MiB --database-version=POSTGRES_9_6 --region=%zone%
CALL gcloud sql users set-password postgres --host=no-host --instance=userdb --password=1234
REM create db
CALL gcloud sql users create dbuser --host=no-host --instance=userdb --password=1234
CALL gcloud sql databases create user_database --instance=userdb
REM create service account
CALL gcloud iam service-accounts create %microservice%
CALL gcloud projects add-iam-policy-binding %project% --member serviceAccount:%microservice%@%project%.iam.gserviceaccount.com --role roles/editor
CALL gcloud iam service-accounts keys create --iam-account %microservice%@%project%.iam.gserviceaccount.com credentials.json
CALL kubectl create secret generic cloudsql-instance-credentials --from-file=credentials.json=credentials.json
CALL kubectl create secret generic cloudsql-db-credentials --from-literal=username=dbuser --from-literal=password=1234

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
powershell -Command "(gc kubernetes\generated\deployment.yaml) -replace 'ZONE', '%zone%' | Out-File kubernetes\generated\deployment.yaml"

REM apply kubernetes components and services
ECHO apply kubernetes components and services
CALL kubectl apply -f kubernetes/generated/
CALL kubectl patch deployment %microservice% -p "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"modified\":\"%date%_%time::=-%\"}}}}}"