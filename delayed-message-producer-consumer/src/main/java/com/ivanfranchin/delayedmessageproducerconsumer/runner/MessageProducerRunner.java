package com.ivanfranchin.delayedmessageproducerconsumer.runner;

import com.ivanfranchin.delayedmessageproducerconsumer.activemq.ActiveMQProducer;
import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class MessageProducerRunner implements CommandLineRunner {

    private final ActiveMQProducer activeMQProducer;

    public MessageProducerRunner(ActiveMQProducer activeMQProducer) {
        this.activeMQProducer = activeMQProducer;
    }

    @Value("${app.simulate.messages-per-second}")
    private int messagesPerSecond;

    @Value("${app.simulate.message-delay-minutes}")
    private String messageDelayMinutes;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            int iMessageDelayMinutes = messageDelayMinutes.isEmpty() ?
                    random.nextInt(5) + 1 : Integer.parseInt(messageDelayMinutes);
            DelayedMessage delayedMessage = new DelayedMessage(
                    UUID.randomUUID().toString(),
                    Instant.now().plus(iMessageDelayMinutes, ChronoUnit.MINUTES)
            );
            activeMQProducer.sendMessage(delayedMessage, Duration.ofMinutes(iMessageDelayMinutes));
            Thread.sleep(1000 / messagesPerSecond);
        }
    }

    private static final SecureRandom random = new SecureRandom();
}
