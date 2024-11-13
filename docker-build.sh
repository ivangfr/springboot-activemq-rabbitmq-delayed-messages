#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_NAME="delayed-message-producer-consumer"
APP_VERSION="1.0.0"
DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${APP_NAME}:${APP_VERSION}"
SKIP_TESTS="true"

./mvnw clean spring-boot:build-image --projects "$APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$DOCKER_IMAGE_NAME"
