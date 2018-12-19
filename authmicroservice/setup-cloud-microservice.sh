#!/bin/bash

# set global variables:
source ../variables.properties

microservice=auth-microservice
jarfile=build/libs/authmicroservice-0.0.1-SNAPSHOT.jar

echo ### create cloud sql database ###
# create cloud sql instance
gcloud sql instances create userdb --cpu=1 --memory=3840MiB --database-version=POSTGRES_9_6 --region=$zone
gcloud sql users set-password postgres --host=no-host --instance=userdb --password=1234
# create db
gcloud sql users create dbuser --host=no-host --instance=userdb --password=1234
gcloud sql databases create user_database --instance=userdb
# create service account
gcloud iam service-accounts create $microservice
gcloud projects add-iam-policy-binding $project --member serviceAccount:$microservice@$project.iam.gserviceaccount.com --role roles/editor
gcloud iam service-accounts keys create --iam-account $microservice@$project.iam.gserviceaccount.com credentials.json
kubectl create secret generic cloudsql-instance-credentials --from-file=credentials.json=credentials.json
kubectl create secret generic cloudsql-db-credentials --from-literal=username=dbuser --from-literal=password=1234

# build
./gradlew build

# create docker file
docker build -t eu.gcr.io/$project/$microservice:latest --build-arg JAR_FILE=$jarfile .

# push docker file
gcloud docker -- push eu.gcr.io/$project/$microservice:latest

# generate kubernetes files from templates
mkdir -p kubernetes/generated
sed -e "s/PROJECT_NAME/$project/g" -e "s/ZONE/$zone/g" kubernetes/template/deployment.yaml > kubernetes/generated/deployment.yaml
yes | cp kubernetes/template/service.yaml kubernetes/generated

RANDOM_STRING=$(cat /dev/urandom | tr -dc "a-zA-Z0-9" | fold -w 32 | head -n 1)

kubectl apply -f kubernetes/generated/
kubectl patch deployment $microservice -p "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"modified\":\"$RANDOM_STRING\"}}}}}"
