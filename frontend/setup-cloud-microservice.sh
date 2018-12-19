#!/bin/bash

# set global variables:
source ../variables.properties

microservice=frontend

# npm
npm install

# build
./node_modules/.bin/ng build --prod

# create docker file
docker build -t eu.gcr.io/$project/$microservice:latest .

# push docker file
docker push eu.gcr.io/$project/$microservice:latest


# generate kubernetes files from templates
mkdir -p kubernetes/generated
yes | cp kubernetes/template/frontend.yaml kubernetes/generated
# replace template variables
sed "s/PROJECT_NAME/$project/g" kubernetes/template/frontend.yaml > kubernetes/generated/frontend.yaml

RANDOM_STRING=$(cat /dev/urandom | tr -dc "a-zA-Z0-9" | fold -w 32 | head -n 1)

kubectl apply -f kubernetes/generated/
kubectl patch deployment $microservice -p "{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"modified\":\"$RANDOM_STRING\"}}}}}"

