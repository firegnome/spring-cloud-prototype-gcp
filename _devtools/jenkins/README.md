# Jenkins

Jenkins is an open source automation server used for CI (continuous integration) and CD (continuous delivery).

## Jenkins on Kubernetes with GCP

Sources:

* [Jenkins on Kubernetes Engine](https://cloud.google.com/solutions/jenkins-on-kubernetes-engine)
* [Jenkins on Kubernetes Engine Tutorial](https://cloud.google.com/solutions/jenkins-on-kubernetes-engine-tutorial)
* [CD with Jenkins on Kubernetes](https://github.com/GoogleCloudPlatform/continuous-deployment-on-kubernetes/)

## Prequisites

* Google Cloud Project
  ```
  gcloud projects create [GCP_PROJECT_NAME]
  gcloud config set project [GCP_PROJECT_NAME]
  gcloud config set compute/zone europe-west3-a
  ```

* Enable API Manager for Compute Engine, Container Engine
  ```
  gcloud services enable compute.googleapis.com
  gcloud services enable container.googleapis.com
  ```

## Create Kubernetes Engine Cluster

Create network `jenkins`
```
gcloud compute networks create jenkins
```

Create Kubernetes Cluster `devtools` with a network `jenkins` and scopes of `Google Cloud Storage` and `Google Cloud Source Repositorys`
```
gcloud container clusters create devtools --network jenkins --scopes "https://www.googleapis.com/auth/projecthosting,storage-rw" --machine-type=n1-standard-2 --num-nodes=1
```

Get Credentials
```
gcloud container clusters get-credentials devtools
```

Create Clusterrolebinding
```
kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=[MY_GCLOUD_LOGIN_USER]
```

## Create Jenkins Image

Create Base Image `jenkins-home-image`
```
gcloud compute images create jenkins-home-image --source-uri https://storage.googleapis.com/solutions-public-assets/jenkins-cd/jenkins-home-v3.tar.gz
```

Create Disk `jenkins-home`
```
gcloud compute disks create jenkins-home --image jenkins-home-image
```

## Create Secrets

Create namespace `jenkins`
```
kubectl create namespace jenkins
```

Create secrete `jenkins-secret`
```
kubectl create secret generic jenkins-secret --from-file=configurations/options --namespace=jenkins
```

## Service Account

Create Service Account for Jenkins
```
gcloud iam service-accounts create jenkins-docker-registry --display-name "Access account for jenkins to push docker images"
gcloud projects add-iam-policy-binding [GCP_PROJECT_NAME] --member serviceAccount:jenkins-docker-registry@[GCP_PROJECT_NAME].iam.gserviceaccount.com --role roles/storage.admin
gcloud projects add-iam-policy-binding [GCP_PROJECT_NAME] --member serviceAccount:jenkins-docker-registry@[GCP_PROJECT_NAME].iam.gserviceaccount.com --role roles/storage.objectViewer
gcloud iam service-accounts keys create --iam-account jenkins-docker-registry@[GCP_PROJECT_NAME].iam.gserviceaccount.com jenkins-docker-credentials.json
```

## Build Custom Jenkins Docker Image

Build Docker Image
```
docker build -t eu.gcr.io/[GCP_PROJECT_NAME]/jenkins:latest .
```

Push Docker Image
```
gcloud docker -- push eu.gcr.io/[GCP_PROJECT_NAME]/jenkins:latest
```

## Deploy Jenkins

Create Jenkins services and deployments
```
kubectl apply -f configurations/
```

## Configure external load balancing

Create SSL-Certificate
```
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout tmp/tls.key -out tmp/tls.crt -subj "/CN=jenkins/O=jenkins"
```

Upload Certificate as secret `tls`
```
kubectl create secret generic tls --from-file=tmp/tls.crt --from-file=tmp/tls.key --namespace jenkins
```

Create load balancer
```
kubectl apply -f configurations/loadbalancer/ingress.yaml
```

After successful deployment, navigate to `http://[EXTERNAL_IP]:[PORT]` and login with user `jenkins` and configured password in [options](configurations/options)

# Jenkins-CLI

> jenkins api key is located at `http://YOUR_JENKINS_HOSTNAME/user/YOUR_USERNAME/configure` and show API token

## test auth api token
```
java -jar jenkins-cli.jar -s http://35.190.40.217/ -auth jenkins:82b2b5a57239b88040920cfff4269846 who-am-i
```

## Plugins

### gitlab-plugin

install plugin `gitlab-plugin` with restart (required)
```
java -jar jenkins-cli.jar -s http://35.190.40.217/ -auth jenkins:82b2b5a57239b88040920cfff4269846 install-plugin gitlab-plugin -restart
```

# Configure Jenkins

## Plugins

At first, all plugins should be updated and Jenkins restarted.

### Install Plugins

* GitLab
* Google Container Registry Auth
* Kubernetes Cli

After intallation, restart Jenkins.

## GitLab

Add Gitlab API token:
* Go to `Manage Jenkins > Configure System` and scroll down to `Gitlab` section.
* On Credentials click new and add a new with kind `Gitlab API token`

Add Username and Password:
* Go to `Global Credentials` and click `add credentials`
* Select kind `Username and Password`
* Add Gitlab user credentials

## Google Cloud Registry Configuration

* Go to `Global Credentials` and click `add credentials`
* Select kind `Google Service Account from private key`
* Select `JSON key` and select your generated file `jenkins-docker-credentials.json`

## Kubernetes Cli

In order to be able to use kubernetes commands, following steps are required:

Create Username Password credentials for kubernetes cluster user
* Go to `Global Credentials` and click `add credentials`
* Select kind `Username and Password`
* From Google Console, navigate to `Kubernetes > Clusters > [yourcluster]`, there is the IP, and you can show username and password

To use `kubectl` commands in Jenkinsfile:
```
withKubeConfig([credentialsId: 'added_credentials', 
            serverUrl: 'https://YOUR_CLUSTER_IP']) {
    sh "kubectl get pods"
}
```

If no kubectl is installed on the Jenkins, the docker image `gcr.io/cloud-builders/kubectl` can be used: 
```
docker.image('gcr.io/cloud-builders/kubectl').inside('--entrypoint ""') {
	withKubeConfig([credentialsId: 'added_credentials', 
            serverUrl: 'https://YOUR_CLUSTER_IP']) {
		sh "kubectl get pods"
	}
}
```
>>>
NOTE: `.inside('--entrypoint ""')` is used to be able to start image
>>>

## SonarQube

To use the SonarQube by API, an API-Token is required:
* Login to SonarQube
* Go to `MyAccount > Security`
* Generate token

This token can be used in jenkinsfile:
```
def SONAR_PARAMETERS = '-Dsonar.host.url=http://SONAR_IP/sonar -Dsonar.login=API_TOKEN'
sh "./gradlew clean test sonarqube ${SONAR_PARAMETERS}"
```

## Pipelines

Create new Pipeline:
* Go to `Jenkins > new element`
* Select `Multibranch` Pipeline and click create
* Under `Branch Sources` click `add source > Git`
* Under `credentials` select before created gitlab user credentials
* Unser `Build Configuration` specify Jenkinsfile eg: `blogmicroservice/Jenkinsfile`

To be able to run the pipeline, a node must be running:
* Go to `Jenkins > Nodes` and klick `Master` and `Configure`
* Add CPU count: `2`
* Add Label `docker`

# Configure GitLab-Webhook
* Go to `Your GitLab Project > Settings > Integrations`
* Under URL insert http://[jenkins-ip-or-url]/git/notifyCommit?url=https://[url-to-your-git-repo].git
* Click on `Add webhook`
