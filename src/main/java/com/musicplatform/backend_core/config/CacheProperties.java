package com.musicplatform.backend_core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {

    private int highlightedArtistsTtlMinutes = 5;

    public int getHighlightedArtistsTtlMinutes() {
        return highlightedArtistsTtlMinutes;
    }

    public void setHighlightedArtistsTtlMinutes(int highlightedArtistsTtlMinutes) {
        this.highlightedArtistsTtlMinutes = highlightedArtistsTtlMinutes;
    }
}
