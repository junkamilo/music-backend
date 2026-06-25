package com.musicplatform.backend_core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(SpotifyProperties.class)
public class SpotifyConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
