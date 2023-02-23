#!/usr/bin/env bash

source scripts/my-functions.sh

ACTIVEMQ_VERSION="5.14.3"
RABBITMQ_VERSION="3.11.9-management"

echo
echo "Starting environment"
echo "===================="

echo
echo "Creating network"
echo "----------------"
docker network create springboot-activemq-rabbitmq-delayed-messages_default

echo
echo "Starting ActiveMQ"
echo "-----------------"
docker run -d --name activemq \
  -p 61616:61616 \
  -p 8161:8161 \
  --network=springboot-activemq-rabbitmq-delayed-messages_default \
  webcenter/activemq:${ACTIVEMQ_VERSION}


if [[ "$(docker images -q rabbitmq-delayed-message:${RABBITMQ_VERSION} 2> /dev/null)" == "" ]]; then
  echo
  echo "Building RabbitMQ docker image with delayed-message plugin"
  echo "----------------------------------------------------------"
  docker build -t rabbitmq-delayed-message:${RABBITMQ_VERSION} docker/rabbitmq
fi

echo
echo "Starting RabbitMQ"
echo "-----------------"
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin \
  --network=springboot-activemq-rabbitmq-delayed-messages_default \
  rabbitmq-delayed-message:${RABBITMQ_VERSION}

echo
wait_for_container_log "activemq" "activemq entered RUNNING state"

echo
wait_for_container_log "rabbitmq" "Server startup complete"

echo
echo "Environment Up and Running"
echo "=========================="
echo