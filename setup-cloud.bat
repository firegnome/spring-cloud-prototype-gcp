@echo off

REM change directory to batchfile location
SET startlocation=%cd%
CD %~dp0

REM set global variables
ECHO ###
ECHO ### setting variable ###
ECHO ###
CALL set-variables
ECHO project: %project%
ECHO cluster: %cluster%
ECHO zone: %zone%

REM initialize cloud project
	ECHO ###
	ECHO ### create project %project% ###
	ECHO ###
	CALL gcloud projects create %project%
	CALL gcloud config set project %project%
	CALL gcloud config set compute/zone %zone%
	REM enable payment
	ECHO you must activate payment for the project %project% before you continue.
	START "" https://console.cloud.google.com/billing/linkedaccount?project=%project%
	PAUSE

	ECHO ###
	ECHO ### activate cloud apis (this can take up to 5 minutes) ###
	ECHO ###
	CALL gcloud services enable compute.googleapis.com
	CALL gcloud services enable container.googleapis.com
	CALL gcloud services enable datastore.googleapis.com
	CALL gcloud services enable storage-component.googleapis.com
	CALL gcloud services enable pubsub.googleapis.com
	CALL gcloud services enable redis.googleapis.com
	CALL gcloud services enable sqladmin.googleapis.com
	
	ECHO ###
	ECHO ### create cluster %cluster% ###
	ECHO ###
	CALL gcloud container clusters create %cluster% --machine-type=n1-standard-2 --num-nodes=1
	CALL gcloud container clusters get-credentials %cluster%
	
	ECHO ###
	ECHO ### create app engine ###
	ECHO ###
	REM hardcoded region. app enginge needed for datastore creation. region shouldn't affect any performance
	CALL gcloud app create --region=europe-west

REM deploy microservices
	ECHO ###
	ECHO ### deploy config-microservice ###
	ECHO ###
	CD configmicroservice
	CALL setup-cloud-microservice
	CD ..
	
	ECHO ###
	ECHO ### deploy auth-microservice ###
	ECHO ###
	CD authmicroservice
	CALL setup-cloud-microservice
	CD ..
	
	ECHO ###
	ECHO ### deploy blog-microservice ###
	ECHO ###
	CD blogmicroservice
	CALL setup-cloud-microservice
	CD ..
	
	ECHO ###
	ECHO ### deploy comment-microservice ###
	ECHO ###
	CD commentmicroservice
	CALL setup-cloud-microservice
	CD ..
	
	ECHO ###
	ECHO ### deploy statistic-microservice ###
	ECHO ###
	CD statisticmicroservice
	CALL setup-cloud-microservice
	CD ..
	
	ECHO ###
	ECHO ### deploy frontend ###
	ECHO ###
	CD frontend
	CALL setup-cloud-microservice
	CD ..

	ECHO ###
	ECHO ### deploy gateway-microservice ###
	ECHO ###
	CD gatewaymicroservice
	CALL setup-cloud-microservice
	CD ..
	
REM reset start location
CD %startlocation%
