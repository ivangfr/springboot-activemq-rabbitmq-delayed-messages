# springboot-activemq-rabbitmq-delayed-messages

The goal of this project is to handle delayed messages using [`ActiveMQ`](https://activemq.apache.org/) and [`RabbitMQ`](https://www.rabbitmq.com/). For it, we enable the scheduler in `ActiveMQ` and install, in `RabbitMQ`, the [Delayed Message Plugin](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/).

## Application

- ### delayed-message-producer-consumer

  [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that produces and consumes messages with random delays.

  The amount of messages per second produced can be specified using the environment variable `MESSAGES_PER_SECOND` (default is `100`).

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Initialize Environment

Open a terminal and inside `springboot-activemq-rabbitmq-delayed-messages` root folder run
```
./init-environment.sh
```

## Running application with Maven

- In a terminal, make sure you are inside `springboot-activemq-rabbitmq-delayed-messages` folder
- Run the following command
  ```
  ./mvnw clean spring-boot:run --projects delayed-message-producer-consumer
  ```

## Running application as Docker container

- ### Build Docker image

  - In a terminal, make sure you are inside `springboot-activemq-rabbitmq-delayed-messages` root folder
  - Run the following script
    ```
    ./docker-build.sh
    ```

- ### Environment variables
  
| Environment Variable       | Description                                                                                            |
|----------------------------|--------------------------------------------------------------------------------------------------------|
| `ACTIVEMQ_BROKER_URL`      | Specify URL of the ActiveMQ broker to use (default `tcp://localhost:61616`)                            |
| `ACTIVEMQ_USER`            | Specify user of the ActiveMQ broker (default `admin`)                                                  |
| `ACTIVEMQ_PASSWORD`        | Specify password of the ActiveMQ broker (default `admin`)                                              |
| `RABBITMQ_ADDRESSES`       | Specify addresses of the RabbitMQ broker to use (default `localhost:5672`)                             |
| `RABBITMQ_USER`            | Specify user of the RabbitMQ broker (default `admin`)                                                  |
| `RABBITMQ_PASSWORD`        | Specify password of the RabbitMQ broker (default `admin`)                                              |
| `MESSAGES_PER_SECOND`      | Specify the number of messages per second to be produced (default `100`)                               |
| `PRODUCER_SEND_TO`         | Specify to which broker the producer sends the messages, `activemq` or `rabbitmq` (default `activemq`) |
| `PRODUCER_RUNNER_ENABLED`  | Specify if producer runner is enabled to produce messages (default `true`)                             |
| `PRODUCER_LOGGING_ENABLED` | Specify if producer logging is enabled (default `false`)                                               |
| `CONSUMER_LOGGING_ENABLED` | Specify if consumer logging is enabled (default `true`)                                                |

- ### Start Docker container

  - In a terminal, run the following command to start the Docker container
    ```
    docker run --rm --name delayed-message-producer-consumer \
      -e ACTIVEMQ_BROKER_URL=tcp://activemq:61616 \
      -e RABBITMQ_ADDRESSES=rabbitmq:5672 \
      --network=springboot-activemq-rabbitmq-delayed-messages_default \
      ivanfranchin/delayed-message-producer-consumer:1.0.0
    ```

  - \[Optional\] To start a 2nd Docker container, in another terminal, run the following command
    ```
    docker run --rm --name delayed-message-producer-consumer-2 \
      -e ACTIVEMQ_BROKER_URL=tcp://activemq:61616 \
      -e RABBITMQ_ADDRESSES=rabbitmq:5672 \
      --network=springboot-activemq-rabbitmq-delayed-messages_default \
      ivanfranchin/delayed-message-producer-consumer:1.0.0
    ```

## Useful links

- **ActiveMQ**

  - Access http://localhost:8161
  - Click `Manage ActiveMQ broker`
  - To login, type `admin` for both username and password

- **RabbitMQ**

  - Access http://localhost:15672
  - To login, type `admin` for both username and password

## Shutdown

- To stop `delayed-message-producer-consumer` application, go to the terminal where it is running and press `Ctrl+C`
- To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `springboot-activemq-rabbitmq-delayed-messages` root folder, run the following command
  ```
  ./shutdown-environment.sh
  ```
## Cleanup

To remove the Docker image create by this project, go to a terminal and, inside `springboot-activemq-rabbitmq-delayed-messages` root folder, run the following script
```
./remove-docker-images.sh
```
