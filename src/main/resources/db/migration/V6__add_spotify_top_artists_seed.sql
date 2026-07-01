CREATE TABLE IF NOT EXISTS spotify_top_artists_seed (
    id               BIGSERIAL     PRIMARY KEY,
    spotify_id       VARCHAR(100)  UNIQUE NOT NULL,
    stage_name       VARCHAR(100)  NOT NULL,
    last_synced_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sync_priority    INT           NOT NULL DEFAULT 1,
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_seed_sync_priority CHECK (sync_priority BETWEEN 1 AND 3)
);

CREATE INDEX IF NOT EXISTS idx_seed_spotify_id ON spotify_top_artists_seed (spotify_id);
CREATE INDEX IF NOT EXISTS idx_seed_priority ON spotify_top_artists_seed (sync_priority);
CREATE INDEX IF NOT EXISTS idx_seed_last_synced ON spotify_top_artists_seed (last_synced_at);

INSERT INTO spotify_top_artists_seed (spotify_id, stage_name, sync_priority)
VALUES
    ('0EmeFodog0BfCgMzAIvKQp', 'Shakira', 1),
    ('4q3ewBCX7sLwd24euuV69X', 'Bad Bunny', 1),
    ('790FomKkXshlbRYZFtlgla', 'KAROL G', 2)
ON CONFLICT (spotify_id) DO NOTHING;
