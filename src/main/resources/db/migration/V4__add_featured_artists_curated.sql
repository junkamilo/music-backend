CREATE TABLE IF NOT EXISTS featured_artists_curated (
    id                    BIGSERIAL     PRIMARY KEY,
    featured_artist_id    BIGINT        NOT NULL,
    curator_reason        VARCHAR(255)  NULL,
    spotlight_order       INT           NOT NULL DEFAULT 1,
    is_active             BOOLEAN       NOT NULL DEFAULT TRUE,
    featured_from         DATE          NOT NULL,
    featured_until        DATE          NULL,
    created_at            TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fac_featured FOREIGN KEY (featured_artist_id)
        REFERENCES featured_artists (id) ON DELETE CASCADE,
    CONSTRAINT chk_fac_spotlight_order CHECK (spotlight_order > 0),
    CONSTRAINT chk_fac_date_range CHECK (featured_from <= featured_until OR featured_until IS NULL)
);

CREATE INDEX IF NOT EXISTS idx_fac_featured_artist ON featured_artists_curated (featured_artist_id);
CREATE INDEX IF NOT EXISTS idx_fac_spotlight_order ON featured_artists_curated (spotlight_order);
CREATE INDEX IF NOT EXISTS idx_fac_active ON featured_artists_curated (is_active);
CREATE INDEX IF NOT EXISTS idx_fac_featured_from ON featured_artists_curated (featured_from);
CREATE INDEX IF NOT EXISTS idx_fac_featured_until ON featured_artists_curated (featured_until);

DROP TRIGGER IF EXISTS trg_featured_artists_curated_updated_at ON featured_artists_curated;
CREATE TRIGGER trg_featured_artists_curated_updated_at
    BEFORE UPDATE ON featured_artists_curated
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
