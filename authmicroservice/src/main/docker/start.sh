#!/bin/sh
./wait-for-it.sh $POSTGRES_DB_HOST -t 15
java -Djava.security.egd=file:/dev/./urandom -jar app.jar --spring.datasource.url=jdbc:postgresql://$POSTGRES_DB_HOST/$POSTGRES_DB_DATABASE --spring.datasource.username=$POSTGRES_DB_USER --spring.datasource.password=$POSTGRES_DB_PASSWORD