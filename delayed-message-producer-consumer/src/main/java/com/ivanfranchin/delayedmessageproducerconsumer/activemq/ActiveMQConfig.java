package com.ivanfranchin.delayedmessageproducerconsumer.activemq;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import java.util.List;

@EnableJms
@Configuration
public class ActiveMQConfig {

    @Bean
    ConnectionFactory connectionFactory(@Value("${spring.activemq.broker-url}") String brokerURL) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
        activeMQConnectionFactory.setTrustedPackages(TRUSTED_PACKAGES);
        return activeMQConnectionFactory;
    }

    private static final List<String> TRUSTED_PACKAGES = List.of(
            "java.time",
            "com.ivanfranchin.delayedmessageproducerconsumer.model"
    );
}
