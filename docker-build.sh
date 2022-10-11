#!/usr/bin/env bash

./mvnw clean compile jib:dockerBuild --projects delayed-message-producer-consumer
