package com.ivanfranchin.delayedmessageproducerconsumer.jms;

import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class ActiveMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(ActiveMQConsumer.class);

    @JmsListener(destination = "${activemq.queue.delayedMessage}")
    public void onMessage(@Payload DelayedMessage delayedMessage) {
        log.info("Received {} with lag of {} ms",
                delayedMessage.id(),
                Duration.between(delayedMessage.expectedReturnTime(), Instant.now()).toMillis());
    }
}
