CREATE TABLE IF NOT EXISTS featured_artists (
    id               BIGSERIAL     PRIMARY KEY,
    spotify_id       VARCHAR(100)  UNIQUE NOT NULL,
    stage_name       VARCHAR(100)  NOT NULL,
    image_url        VARCHAR(500)  NULL,
    spotify_url      VARCHAR(500)  NULL,
    popularity       INT           NULL,
    genres           VARCHAR(255)  NULL,
    sync_status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    last_synced_at   TIMESTAMP     NULL,
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_fa_sync_status CHECK (sync_status IN ('PENDING', 'SYNCED', 'FAILED'))
);

CREATE INDEX IF NOT EXISTS idx_featured_spotify_id ON featured_artists (spotify_id);
CREATE INDEX IF NOT EXISTS idx_featured_stage_name ON featured_artists (stage_name);
CREATE INDEX IF NOT EXISTS idx_featured_last_synced ON featured_artists (last_synced_at);

DROP TRIGGER IF EXISTS trg_featured_artists_updated_at ON featured_artists;
CREATE TRIGGER trg_featured_artists_updated_at
    BEFORE UPDATE ON featured_artists
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
