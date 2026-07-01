package com.musicplatform.backend_core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.musicplatform.backend_core.auth.repository",
        "com.musicplatform.backend_core.artist.repository",
        "com.musicplatform.backend_core.artist.milestone.repository",
        "com.musicplatform.backend_core.artist.timeline.repository",
        "com.musicplatform.backend_core.artist.featured.repository"
})
public class DatabaseConfig {
}
