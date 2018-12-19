# SonarQube

SonarQube is an open source continuous inspection server of code quality used for static code analysis to detect bugs, code smells and security vulnerabilities.

## SonarQube on Kubernetes with GCP

Sources:

* [From Pet to Cattle – Running Sonar on Kubernetes](https://container-solutions.com/pet-cattle-running-sonar-kubernetes/)
* [installing sonarqube in kubernetes](https://coderise.io/installing-sonarqube-in-kubernetes/)
* [GitHub - k8s-sonarqube](https://github.com/coderise-lab/k8s-sonarqube)

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

### Create Kubernetes Engine Cluster

Create Kubernetes Cluster `sonarqube-cluster`
```
gcloud container clusters create sonarqube-cluster
```

Get Credentials
```
gcloud container clusters get-credentials sonarqube-cluster
```

### Create Secrets

Create Secret `postgres-secret` for postgres database
```
kubectl create secret generic postgres-secret --from-literal=password=1234
```

### Deploy SonarQube and PostgreSQL

Create SonarQube and PostgreSQL Deployments and Services
```
kubectl apply -f configurations/
```

After successful deployment, navigate to `http://[EXTERNAL_IP]:[PORT]/sonar` and login with user `admin` and password `admin`
