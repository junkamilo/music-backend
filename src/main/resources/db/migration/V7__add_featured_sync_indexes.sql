CREATE INDEX IF NOT EXISTS idx_featured_popularity ON featured_artists (popularity DESC);
CREATE INDEX IF NOT EXISTS idx_featured_curated_active_from
    ON featured_artists_curated (is_active, featured_from);
