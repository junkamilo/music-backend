-- V1: esquema inicial music_platform_db (PostgreSQL)

CREATE TABLE users (
    id                BIGSERIAL PRIMARY KEY,
    username          VARCHAR(50)  UNIQUE NOT NULL,
    email             VARCHAR(100) UNIQUE NOT NULL,
    password_hash     VARCHAR(255) NOT NULL,
    role              VARCHAR(20)  NOT NULL DEFAULT 'LISTENER',
    is_active         BOOLEAN      NOT NULL DEFAULT TRUE,
    email_verified_at TIMESTAMP    NULL DEFAULT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at        TIMESTAMP    NULL DEFAULT NULL,

    CONSTRAINT chk_role CHECK (role IN ('LISTENER', 'CREATOR', 'ADMIN'))
);

CREATE INDEX idx_role ON users (role);
CREATE INDEX idx_active ON users (is_active);
CREATE INDEX idx_deleted_at ON users (deleted_at);

CREATE TABLE email_verification_tokens (
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT       NOT NULL,
    token             VARCHAR(255) NOT NULL UNIQUE,
    verification_code VARCHAR(6)   NOT NULL UNIQUE,
    expires_at        TIMESTAMP    NOT NULL,
    verified_at       TIMESTAMP    NULL DEFAULT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_evt_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_evt_user ON email_verification_tokens (user_id);
CREATE INDEX idx_evt_token ON email_verification_tokens (token);
CREATE INDEX idx_evt_code ON email_verification_tokens (verification_code);
CREATE INDEX idx_evt_expires ON email_verification_tokens (expires_at);

CREATE TABLE refresh_tokens (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    token_hash    VARCHAR(255) NOT NULL,
    device_info   VARCHAR(255) NULL,
    ip_address    VARCHAR(45)  NULL,
    expires_at    TIMESTAMP    NOT NULL,
    revoked_at    TIMESTAMP    NULL DEFAULT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rt_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_rt_user ON refresh_tokens (user_id);
CREATE INDEX idx_rt_token ON refresh_tokens (token_hash);
CREATE INDEX idx_rt_expires ON refresh_tokens (expires_at);

CREATE TABLE artist_profiles (
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT        UNIQUE NULL,
    stage_name        VARCHAR(100)  NOT NULL,
    stage_name_origin TEXT          NULL,
    bio               TEXT          NULL,
    avatar_url        VARCHAR(500)  NULL,
    banner_url        VARCHAR(500)  NULL,
    birth_date        DATE          NULL,
    birth_city        VARCHAR(100)  NULL,
    birth_country     VARCHAR(100)  NULL,
    current_city      VARCHAR(100)  NULL,
    current_country   VARCHAR(100)  NULL,
    website_url       VARCHAR(500)  NULL,
    instagram_url     VARCHAR(500)  NULL,
    spotify_url       VARCHAR(500)  NULL,
    spotify_id        VARCHAR(100)  UNIQUE NULL,
    artist_type       VARCHAR(20)   NOT NULL DEFAULT 'INDEPENDENT',
    is_verified       BOOLEAN       NOT NULL DEFAULT FALSE,
    is_active         BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ap_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_artist_type CHECK (artist_type IN ('INDEPENDENT', 'FEATURED'))
);

CREATE INDEX idx_ap_stage_name ON artist_profiles (stage_name);
CREATE INDEX idx_ap_country ON artist_profiles (current_country);
CREATE INDEX idx_ap_verified ON artist_profiles (is_verified);
CREATE INDEX idx_ap_type ON artist_profiles (artist_type);
CREATE INDEX idx_ap_spotify ON artist_profiles (spotify_id);

CREATE TABLE artist_milestones (
    id               BIGSERIAL PRIMARY KEY,
    artist_id        BIGINT        NOT NULL,
    title            VARCHAR(200)  NOT NULL,
    description      TEXT          NULL,
    milestone_type   VARCHAR(30)   NOT NULL,
    date_achieved    DATE          NOT NULL,
    image_url        VARCHAR(500)  NULL,
    order_position   INT           NULL,
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ms_artist FOREIGN KEY (artist_id) REFERENCES artist_profiles (id) ON DELETE CASCADE,
    CONSTRAINT chk_milestone_type CHECK (milestone_type IN ('AWARD', 'COLLABORATION', 'RECORD', 'ACHIEVEMENT'))
);

CREATE INDEX idx_ms_artist ON artist_milestones (artist_id);
CREATE INDEX idx_ms_type ON artist_milestones (milestone_type);
CREATE INDEX idx_ms_date ON artist_milestones (date_achieved);

CREATE TABLE artist_timeline_events (
    id                BIGSERIAL PRIMARY KEY,
    artist_id         BIGINT        NOT NULL,
    event_title       VARCHAR(200)  NOT NULL,
    event_description TEXT          NULL,
    event_type        VARCHAR(30)   NOT NULL,
    event_date        DATE          NOT NULL,
    event_image_url   VARCHAR(500)  NULL,
    order_position    INT           NULL,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_te_artist FOREIGN KEY (artist_id) REFERENCES artist_profiles (id) ON DELETE CASCADE
);

CREATE INDEX idx_te_artist ON artist_timeline_events (artist_id);
CREATE INDEX idx_te_date ON artist_timeline_events (event_date);
CREATE INDEX idx_te_type ON artist_timeline_events (event_type);

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_artist_profiles_updated_at
    BEFORE UPDATE ON artist_profiles
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
