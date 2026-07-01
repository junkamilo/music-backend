CREATE TABLE IF NOT EXISTS artist_inspirations (
    id                  BIGSERIAL     PRIMARY KEY,
    emergent_artist_id  BIGINT        NOT NULL,
    featured_artist_id  BIGINT        NOT NULL,
    similarity_reason   VARCHAR(200)  NULL,
    created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ai_emergent FOREIGN KEY (emergent_artist_id)
        REFERENCES artist_profiles (id) ON DELETE CASCADE,
    CONSTRAINT fk_ai_featured FOREIGN KEY (featured_artist_id)
        REFERENCES featured_artists (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_ai_emergent ON artist_inspirations (emergent_artist_id);
CREATE INDEX IF NOT EXISTS idx_ai_featured ON artist_inspirations (featured_artist_id);
