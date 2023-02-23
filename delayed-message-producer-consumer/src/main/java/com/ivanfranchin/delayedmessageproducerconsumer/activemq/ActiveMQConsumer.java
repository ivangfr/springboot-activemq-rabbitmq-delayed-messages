package com.ivanfranchin.delayedmessageproducerconsumer.activemq;

import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class ActiveMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(ActiveMQConsumer.class);

    @Value("${app.consumer.logging.enabled}")
    private boolean consumerLoggingEnabled;

    @JmsListener(destination = "${activemq.queue.delayedMessage}")
    public void onMessage(@Payload DelayedMessage delayedMessage) {
        if (consumerLoggingEnabled) {
            log.info("Received from ActiveMQ {} with lag of {} ms",
                    delayedMessage,
                    Duration.between(delayedMessage.expectedReturnTime(), Instant.now()).toMillis());
        }
    }
}
