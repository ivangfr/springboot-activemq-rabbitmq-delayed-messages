package com.ivanfranchin.delayedmessageproducerconsumer.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.delayedmessageproducerconsumer.model.DelayedMessage;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.time.Duration;

@Component
public class ActiveMQProducer {

    private static final Logger log = LoggerFactory.getLogger(ActiveMQProducer.class);

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public ActiveMQProducer(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${activemq.queue.delayedMessage}")
    private String queue;

    public void sendMessage(DelayedMessage delayedMessage, Duration delay) {
        try {
            final String cmdStr = objectMapper.writeValueAsString(delayedMessage);
            jmsTemplate.send(queue, messageCreator -> {
                Message message = messageCreator.createTextMessage(cmdStr);
                message.setObjectProperty("_type", delayedMessage.getClass().getName());
                message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay.toMillis());
                return message;
            });
        } catch (Exception e) {
            log.error("An exception was thrown while sending message", e);
        }
    }
}
