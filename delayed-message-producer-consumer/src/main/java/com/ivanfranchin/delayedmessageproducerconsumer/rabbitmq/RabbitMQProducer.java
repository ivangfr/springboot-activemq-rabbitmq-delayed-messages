package com.ivanfranchin.delayedmessageproducerconsumer.rabbitmq;

import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.time.Duration;

@Component
public class RabbitMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final StreamBridge streamBridge;

    public RabbitMQProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Value("${app.producer.logging.enabled}")
    private boolean producerLoggingEnabled;

    @Async
    public void sendMessage(DelayedMessage delayedMessage, Duration delay) {
        try {
            if (producerLoggingEnabled) {
                log.info("Sending to RabbitMQ {} with delay of {}", delayedMessage, delay);
            }
            streamBridge.send("delayedMessage-out-0",
                    MessageBuilder
                            .withPayload(delayedMessage)
                            .setHeader("x-delay", delay.toMillis())
                            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                            .build());
        } catch (Exception e) {
            log.error("An exception was thrown while sending message to RabbitMQ", e);
        }
    }
}
