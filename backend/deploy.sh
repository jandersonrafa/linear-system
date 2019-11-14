#!/bin/bash
#mvn clean install -Dspring.profiles.active=test -DskipTests
docker build -t training-aws-queuemanagerback .
#docker rm --force training-aws-queuemanagerback
#docker container run --publish 8081:8081 -e "SPRING_PROFILES_ACTIVE=test" --detach --name training-aws-queuemanagerback --net=host training-aws-queuemanagerback
$(aws ecr get-login --no-include-email --region us-east-1)
docker tag training-aws-queuemanagerback:latest 834318500728.dkr.ecr.us-east-1.amazonaws.com/ta-ecr-queuemanagerback:latest
docker push 834318500728.dkr.ecr.us-east-1.amazonaws.com/ta-ecr-queuemanagerback:latest
