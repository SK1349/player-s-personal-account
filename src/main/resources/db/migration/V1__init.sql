ALTER TABLE IF EXISTS match_players DROP CONSTRAINT IF EXISTS fk_match_players_match;
ALTER TABLE IF EXISTS match_players DROP CONSTRAINT IF EXISTS fk_match_players_user;
ALTER TABLE IF EXISTS user_achievements DROP CONSTRAINT IF EXISTS fk_user_achievements_achievement;
ALTER TABLE IF EXISTS user_achievements DROP CONSTRAINT IF EXISTS fk_user_achievements_user;
ALTER TABLE IF EXISTS user_stats DROP CONSTRAINT IF EXISTS fk_user_stats_user;

DROP TABLE IF EXISTS match_players CASCADE;
DROP TABLE IF EXISTS user_achievements CASCADE;
DROP TABLE IF EXISTS user_stats CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS achievements CASCADE;
DROP TABLE IF EXISTS matches CASCADE;

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    birth_date DATE,
    gender VARCHAR(1) NOT NULL,
    country VARCHAR(100),
    city VARCHAR(100),
    phone VARCHAR(20),
    bio TEXT,
    rating INTEGER NOT NULL DEFAULT 1000,
    level INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE achievements (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(500)
);

CREATE TABLE matches (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    played_at TIMESTAMP(6) NOT NULL,
    map_or_mode VARCHAR(50) NOT NULL,
    notes TEXT
);

CREATE TABLE user_stats (
    user_id BIGINT NOT NULL PRIMARY KEY,
    draws INTEGER NOT NULL DEFAULT 0,
    losses INTEGER NOT NULL DEFAULT 0,
    matches_played INTEGER NOT NULL DEFAULT 0,
    total_deaths INTEGER NOT NULL DEFAULT 0,
    total_kills INTEGER NOT NULL DEFAULT 0,
    wins INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE match_players (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    assists INTEGER NOT NULL DEFAULT 0,
    deaths INTEGER NOT NULL DEFAULT 0,
    kills INTEGER NOT NULL DEFAULT 0,
    match_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    match_result VARCHAR(10) NOT NULL,
    UNIQUE (match_id, user_id)
);

CREATE TABLE user_achievements (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    achievement_id BIGINT NOT NULL,
    unlocked_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    UNIQUE (user_id, achievement_id)
);

ALTER TABLE match_players
    ADD CONSTRAINT fk_match_players_match
    FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE;

ALTER TABLE match_players
    ADD CONSTRAINT fk_match_players_user
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_achievements
    ADD CONSTRAINT fk_user_achievements_achievement
    FOREIGN KEY (achievement_id) REFERENCES achievements (id) ON DELETE CASCADE;

ALTER TABLE user_achievements
    ADD CONSTRAINT fk_user_achievements_user
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_stats
    ADD CONSTRAINT fk_user_stats_user
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

CREATE INDEX idx_match_players_user ON match_players (user_id);
CREATE INDEX idx_matches_played_at ON matches (played_at DESC);