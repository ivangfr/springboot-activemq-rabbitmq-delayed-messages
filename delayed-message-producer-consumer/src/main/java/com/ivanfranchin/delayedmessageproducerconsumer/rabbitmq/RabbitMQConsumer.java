package com.ivanfranchin.delayedmessageproducerconsumer.rabbitmq;

import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

@Configuration
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Bean
    public Consumer<DelayedMessage> delayedMessage() {
        return delayedMessage -> {
            log.info("Received from RabbitMQ {} with lag of {} ms",
                    delayedMessage,
                    Duration.between(delayedMessage.expectedReturnTime(), Instant.now()).toMillis());
        };
    }
}
