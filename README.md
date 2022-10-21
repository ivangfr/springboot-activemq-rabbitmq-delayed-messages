# springboot-activemq-scheduler-load-testing

The goal of this project is to load testing [ActiveMQ](https://activemq.apache.org/) when it has its scheduler enabled.

## Application

- ### delayed-message-producer-consumer

  [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that produces and consumes messages with random delays.

  The amount of messages per second produced can be specified using the environment variable `MESSAGES_PER_SECOND` (default is `10`).

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Start Environment

Open a terminal and inside `springboot-activemq-scheduler-load-testing` root folder run
```
docker-compose up -d
```

## Running application with Maven

- In a terminal, make sure you are inside `springboot-activemq-scheduler-load-testing` folder
- Run the following command
  ```
  ./mvnw clean spring-boot:run --projects delayed-message-producer-consumer
  ```

## Running application as Docker container

- ### Build Docker image

  - In a terminal, make sure you are inside `springboot-activemq-scheduler-load-testing` root folder
  - Run the following script to build the image
    ```
    ./docker-build.sh
    ```

- ### Environment variables
  
  | Environment Variable   | Description                                                                 |
  |------------------------|-----------------------------------------------------------------------------|
  | `ACTIVE_MQ_BROKER_URL` | Specify URL of the ActiveMQ broker to use (default `tcp://localhost:61616`) |
  | `ACTIVE_MQ_USER`       | Specify user of the ActiveMQ broker (default `admin`)                       |
  | `ACTIVE_MQ_PASSWORD`   | Specify password of the ActiveMQ broker (default `admin`)                   |
  | `MESSAGES_PER_SECOND`  | Number of messages per second produced (default `10`)                       |

- ### Start Docker container

  - In a terminal, run the following command to start the Docker container
    ```
    docker run --rm --name delayed-message-producer-consumer \
      -e ACTIVE_MQ_BROKER_URL=tcp://activemq:61616 \
      --network=springboot-activemq-scheduler-load-testing_default \
      ivanfranchin/delayed-message-producer-consumer:1.0.0
    ```

  - \[Optional\] To start a 2nd Docker container, in another terminal, run the following command
    ```
    docker run --rm --name delayed-message-producer-consumer-2 \
      -e ACTIVE_MQ_BROKER_URL=tcp://activemq:61616 \
      --network=springboot-activemq-scheduler-load-testing_default \
      ivanfranchin/delayed-message-producer-consumer:1.0.0
    ```

## Useful links

- **ActiveMQ**

  - Access http://localhost:8161
  - Click `Manage ActiveMQ broker`
  - To login, type `admin` for both username and password

## Shutdown

- To stop `springboot-activemq-scheduler-load-testing` application, go to the terminal where it is running and press `Ctrl+C`
- To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `springboot-activemq-scheduler-load-testing` root folder, run the following command
  ```
  docker-compose down -v
  ```
## Cleanup

To remove the Docker image create by this project, go to a terminal and, inside `springboot-activemq-scheduler-load-testing` root folder, run the following script
```
./remove-docker-images.sh
```
