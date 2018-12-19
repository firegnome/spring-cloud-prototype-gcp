
# set global variables
echo ###
echo ### setting variable ###
echo ###
source variables.properties
echo project: $project
echo cluster: $cluster
echo zone: $zone

# initialize cloud project
	echo ###
	echo ### create project $project ###
	echo ###
	gcloud projects create $project
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

	echo ###
	echo ### activate cloud apis ###
	echo ###
	gcloud services enable datastore.googleapis.com

	echo ###
	echo ### create app engine ###
	echo ###
	# hardcoded region. app enginge needed for datastore creation. region shouldn't affect any performance
	gcloud app create --region=europe-west

# run microservices

npm install npx
    
./node_modules/.bin/npx recursive-install
  
./node_modules/.bin/npx concurrently \
  --kill-others \
  --names "\
configmicroservice,\
frontend,\
authmicroservice,\
blogmicroservice,\
commentmicroservice,\
gatewaymicroservice,\
statisticmicroservice," \
  --prefix-colors "\
red.bold,\
green.bold,\
yellow.bold,\
blue.bold,\
magenta.bold,\
cyan.bold,\
white.bold" \
  \
  "cd configmicroservice && SPRING_PROFILES_ACTIVE='dev, native' ./gradlew bootRun" \
  "sleep 10 && cd frontend && ./node_modules/.bin/ng serve" \
  "sleep 10 && cd authmicroservice && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun"\
  "sleep 10 && cd blogmicroservice && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun" \
  "sleep 10 && cd commentmicroservice && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun"\
  "sleep 10 && cd gatewaymicroservice && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun" \
  "sleep 10 && cd statisticmicroservice && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun"