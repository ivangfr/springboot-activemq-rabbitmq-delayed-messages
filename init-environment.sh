#!/usr/bin/env bash

source scripts/my-functions.sh

POSTGRES_VERSION="15.3"
UNLEASH_VERSION="5.1.9"
ACTIVEMQ_VERSION="5.14.3"
RABBITMQ_VERSION="3.11.20-management"

ADMIN_API_TOKEN="*:*.some-random-string"
CLIENT_API_TOKEN="*:development.some-random-string"

echo
echo "Starting environment"
echo "===================="

echo
echo "Creating network"
echo "----------------"
docker network create springboot-activemq-rabbitmq-delayed-messages_default

echo
echo "Starting Postgres"
echo "-----------------"
docker run -d --name postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=unleash_user \
  -e POSTGRES_PASSWORD=unleash_password \
  -e POSTGRES_DB=unleash \
  --restart=unless-stopped \
  --network=springboot-activemq-rabbitmq-delayed-messages_default \
  --health-cmd="pg_isready -U postgres" \
  postgres:${POSTGRES_VERSION}

echo
wait_for_container_log "postgres" "port 5432"

echo
echo "Starting Unleash"
echo "----------------"
docker run -d --name unleash \
  -p 4242:4242 \
  -e DATABASE_URL=postgres://unleash_user:unleash_password@postgres/unleash \
  -e DATABASE_SSL=false \
  -e INIT_ADMIN_API_TOKENS=$ADMIN_API_TOKEN \
  -e INIT_CLIENT_API_TOKENS=$CLIENT_API_TOKEN \
  -e LOG_LEVEL=debug \
  --restart=unless-stopped \
  --network=springboot-activemq-rabbitmq-delayed-messages_default \
  --health-cmd="nc -z localhost:4242" \
  unleashorg/unleash-server:${UNLEASH_VERSION}

echo
echo "Starting ActiveMQ"
echo "-----------------"
docker run -d --name activemq \
  -p 61616:61616 \
  -p 8161:8161 \
  --restart=unless-stopped \
  --network=springboot-activemq-rabbitmq-delayed-messages_default \
  --health-cmd="curl -f localhost:8161 || exit 1" \
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
  --restart=unless-stopped \
  --network=springboot-activemq-rabbitmq-delayed-messages_default \
  --health-cmd="rabbitmq-diagnostics -q ping" \
  rabbitmq-delayed-message:${RABBITMQ_VERSION}

echo
wait_for_container_log "unleash" "Unleash has started"

echo
wait_for_container_log "activemq" "activemq entered RUNNING state"

echo
wait_for_container_log "rabbitmq" "Server startup complete"

echo
echo "Create rabbitMQEnabled feature toggle in Unleash"
echo "------------------------------------------------"
curl -i -X POST http://localhost:4242/api/admin/projects/default/features \
  -H "Authorization: $ADMIN_API_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{ "type": "release", "name": "rabbitMQEnabled", "description": "", "impressionData": false }'

echo
echo
echo "------------------------------------------------"
echo "UNLEASH_API_KEY=$CLIENT_API_TOKEN"
echo "------------------------------------------------"

echo
echo "Environment Up and Running"
echo "=========================="
echo