package com.ivanfranchin.delayedmessageproducerconsumer.config;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureToggleConfig {

    @Bean
    Unleash unleash(@Value("${spring.application.name}") String appName,
                    @Value("${unleash.api.url}") String unleashApiUrl,
                    @Value("${unleash.api.key}") String unleashApiKey) {
        UnleashConfig config = UnleashConfig.builder()
                .appName(appName)
                .unleashAPI(unleashApiUrl)
                .apiKey(unleashApiKey)
                .build();
        return new DefaultUnleash(config);
    }
}
