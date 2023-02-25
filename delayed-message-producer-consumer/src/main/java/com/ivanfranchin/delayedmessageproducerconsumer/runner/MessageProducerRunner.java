package com.ivanfranchin.delayedmessageproducerconsumer.runner;

import com.ivanfranchin.delayedmessageproducerconsumer.activemq.ActiveMQProducer;
import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import com.ivanfranchin.delayedmessageproducerconsumer.rabbitmq.RabbitMQProducer;
import io.getunleash.Unleash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "app.producer.runner.enabled", havingValue = "true", matchIfMissing = true)
public class MessageProducerRunner implements CommandLineRunner {

    private final ActiveMQProducer activeMQProducer;
    private final RabbitMQProducer rabbitMQProducer;
    private final Unleash unleash;

    public MessageProducerRunner(ActiveMQProducer activeMQProducer, RabbitMQProducer rabbitMQProducer, Unleash unleash) {
        this.activeMQProducer = activeMQProducer;
        this.rabbitMQProducer = rabbitMQProducer;
        this.unleash = unleash;
    }

    @Value("${app.simulate.messages-per-second}")
    private int messagesPerSecond;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            int delayMinutes = random.nextInt(5) + 1;
            DelayedMessage delayedMessage = new DelayedMessage(
                    UUID.randomUUID().toString(),
                    Instant.now().plus(delayMinutes, ChronoUnit.MINUTES)
            );
            if (unleash.isEnabled("rabbitMQEnabled")) {
                rabbitMQProducer.sendMessage(delayedMessage, Duration.ofMinutes(delayMinutes));
            } else {
                activeMQProducer.sendMessage(delayedMessage, Duration.ofMinutes(delayMinutes));
            }
            Thread.sleep(1000 / messagesPerSecond);
        }
    }

    private static final SecureRandom random = new SecureRandom();
}
