#!/bin/bash

# set global variables:
source ../variables.properties

microservice=gateway-microservice
jarfile=build/libs/gatewaymicroservice-0.0.1-SNAPSHOT.jar

# build
./gradlew build

# create docker file
docker build -t eu.gcr.io/$project/$microservice:latest --build-arg JAR_FILE=$jarfile .

# push docker file
docker push eu.gcr.io/$project/$microservice:latest

# generate kubernetes files from templates
mkdir -p kubernetes/generated
sed "s/PROJECT_NAME/$project/g" kubernetes/template/deployment.yaml > kubernetes/generated/deployment.yaml
yes | cp kubernetes/template/service.yaml kubernetes/generated

RANDOM_STRING=$(cat /dev/urandom | tr -dc "a-zA-Z0-9" | fold -w 32 | head -n 1)

kubectl apply -f kubernetes/generated/
kubectl patch deployment $microservice -p "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"modified\":\"$RANDOM_STRING\"}}}}}"
