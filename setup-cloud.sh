#!/bin/bash

# set global variables
echo '###'
echo '### setting variable ###'
echo '###'
source ./variables.properties
echo project: $project
echo cluster: $cluster
echo zone: $zone

# initialize cloud project
	echo '###'
	echo '### create project' $project '###'
	echo '###'
	gcloud projects create $project || { echo 'Create project failed, verify that the PROJECT_NAME in variables.properties is unique.' ; exit 1; }
	gcloud config set project $project
	gcloud config set compute/zone $zone
  # enable payment (open browser on differrent distributions)
	echo you must activate payment for the project $project before you continue. Press enter to continue ...
  if which xdg-open > /dev/null
  then
    xdg-open https://console.cloud.google.com/billing/linkedaccount?project=$project
  elif which gnome-open > /dev/null
  then
    gnome-open https://console.cloud.google.com/billing/linkedaccount?project=$project
  elif which open > /dev/null
  then 
    open https://console.cloud.google.com/billing/linkedaccount?project=$project
  fi
  read -p ""
  
	echo '###'
	echo '### activate cloud apis (this can take up to 5 minutes) ###'
	echo '###'
	gcloud services enable compute.googleapis.com
	gcloud services enable container.googleapis.com
	gcloud services enable datastore.googleapis.com
	gcloud services enable storage-component.googleapis.com
	gcloud services enable pubsub.googleapis.com
	gcloud services enable redis.googleapis.com
  gcloud services enable sqladmin.googleapis.com
	
	echo '###'
	echo '### create cluster' $cluster '###'
	echo '###'
	gcloud container clusters create $cluster --machine-type=n1-standard-2 --num-nodes=1
	gcloud container clusters get-credentials $cluster
  
	echo '###'
	echo '### create app engine ###'
	echo '###'
	# hardcoded region. app enginge needed for datastore functionality. region shouldn't affect any performance
	gcloud app create --region=europe-west

# deploy microservices
	echo '###'
	echo '### deploy config-microservice ###'
	echo '###'
	cd configmicroservice
	./setup-cloud-microservice.sh
	cd ..
	
	echo '###'
	echo '### deploy blog-microservice ###'
	echo '###'
	cd blogmicroservice
	./setup-cloud-microservice.sh
	cd ..
	
	echo '###'
	echo '### deploy comment-microservice ###'
	echo '###'
	cd commentmicroservice
	./setup-cloud-microservice.sh
	cd ..
	
	echo '###'
	echo '### deploy statistic-microservice ###'
	echo '###'
	cd statisticmicroservice
	./setup-cloud-microservice.sh
	cd ..
	
	echo '###'
	echo '### deploy auth-microservice ###'
	echo '###'
	cd authmicroservice
	./setup-cloud-microservice.sh
	cd ..
	
	echo '###'
	echo '### deploy frontend ###'
	echo '###'
	cd frontend
	./setup-cloud-microservice.sh
	cd ..

	echo '###'
	echo '### deploy gateway-microservice ###'
	echo '###'
	cd gatewaymicroservice
	./setup-cloud-microservice.sh
	cd ..
