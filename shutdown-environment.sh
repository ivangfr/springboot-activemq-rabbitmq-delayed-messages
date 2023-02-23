#!/usr/bin/env bash

echo
echo "Starting the environment shutdown"
echo "================================="

echo
echo "Removing containers"
echo "-------------------"
docker rm -fv activemq rabbitmq

echo
echo "Removing network"
echo "----------------"
docker network rm springboot-activemq-rabbitmq-delayed-messages_default

echo
echo "Environment shutdown successfully"
echo "================================="
echo
