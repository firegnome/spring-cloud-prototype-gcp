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
	ECHO ### activate cloud apis ###
	ECHO ###
	CALL gcloud services enable datastore.googleapis.com

	ECHO ###
	ECHO ### create app engine ###
	ECHO ###
	REM hardcoded region. app enginge needed for datastore creation. region shouldn't affect any performance
	CALL gcloud app create --region=europe-west

REM run microservices
	REM set active spring profiles (native, dev is used for config-microservice, others only need dev)
	SET SPRING_PROFILES_ACTIVE=native, dev
	
	CALL npx recursive-install
	
	REM ping 127.0.0.1 -n 11 > NUL is used to wait 10 seconds. Timeout command can't be used as it redirects the input and therefore throws an exception
	CALL npx concurrently ^
	  --kill-others ^
	  --names "configmicroservice,frontend,authmicroservice,blogmicroservice,commentmicroservice,gatewaymicroservice,statisticmicroservice" ^
	  --prefix-colors "red.bold,green.bold,yellow.bold,blue.bold,magenta.bold,cyan.bold,white.bold" ^
	  "CD configmicroservice && SET SPRING_PROFILES_ACTIVE=native, dev && gradlew bootRun" ^
	  "ping 127.0.0.1 -n 11 > NUL && CD frontend && node_modules\.bin\ng serve" ^
	  "ping 127.0.0.1 -n 11 > NUL && CD authmicroservice && SET SPRING_PROFILES_ACTIVE=dev && gradlew bootRun" ^
	  "ping 127.0.0.1 -n 11 > NUL && CD blogmicroservice && SET SPRING_PROFILES_ACTIVE=dev && gradlew bootRun" ^
	  "ping 127.0.0.1 -n 12 > NUL && CD commentmicroservice && SET SPRING_PROFILES_ACTIVE=dev && gradlew bootRun" ^
	  "ping 127.0.0.1 -n 12 > NUL && CD gatewaymicroservice && SET SPRING_PROFILES_ACTIVE=dev && gradlew bootRun" ^
	  "ping 127.0.0.1 -n 12 > NUL && CD statisticmicroservice && SET SPRING_PROFILES_ACTIVE=dev && gradlew bootRun" ^
	  "ping 127.0.0.1 -n 61 > NUL && START \"\" http://localhost:8181 && PAUSE > NUL"
	
REM reset start location
CD %startlocation%
