#!/bin/bash
#npm run buildProd
#docker rm --force training-aws-queuemanagerfront
#docker build -t training-aws-queuemanagerfront .
#docker container run --publish 8080:80 --detach --name training-aws-queuemanagerfront training-aws-queuemanagerfront
$(aws ecr get-login --no-include-email --region us-east-1)
docker tag training-aws-queuemanagerfront:latest 834318500728.dkr.ecr.us-east-1.amazonaws.com/ta-ecr-queuemanagerfront:latest
docker push 834318500728.dkr.ecr.us-east-1.amazonaws.com/ta-ecr-queuemanagerfront:latest