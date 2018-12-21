# Statistic Service

The [Statistic Service](../statisticmicroservice) backend is a [Spring Boot](https://spring.io/projects/spring-boot) application. It is responsible for keeping statistic over blog activities, such as viewing a blog post.
On startup it loads the configuration of the [Config Service](../configmicroservice) and subscribes to the `Event Bus` to get the events.
To store the statistic data, it uses the `Statistic Database`, a [Google Cloud Datastore](https://cloud.google.com/datastore/).

![Statistic Service Deployment](../_resources/deployment_statistic.png)

## Technologies

This microservice consists of following technologies:
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Google Cloud Datastore](https://cloud.google.com/datastore/)
* [Google Cloud PubSub](https://cloud.google.com/pubsub/)

## Dependencies

### Microservices:

* [Config Service](../configmicroservice)

### Components:

* `Statistic Database`
* `Event Bus`

## Local Deploy

On the local deploy, the application get started with profile `dev` with command `gradlew bootRun`.

## Cloud Deploy

The following steps are made for cloud deployment:
* create service account and secret
* build
* create docker image
* push docker image to google cloud
* create deployment and service in kubernetes cluster
* actualize deployment

For further information have a look at the files `setup-cloud-microservice.bat` & `setup-cloud-microservice.sh` as they are being executed.
