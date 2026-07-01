package com.musicplatform.backend_core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableConfigurationProperties({SpotifySyncProperties.class, CacheProperties.class})
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(CacheProperties cacheProperties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("highlightedArtists");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getHighlightedArtistsTtlMinutes(), TimeUnit.MINUTES)
                .maximumSize(100));
        return cacheManager;
    }
}
