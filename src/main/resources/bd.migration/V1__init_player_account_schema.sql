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

DROP SEQUENCE IF EXISTS achievements_seq;
DROP SEQUENCE IF EXISTS match_players_seq;
DROP SEQUENCE IF EXISTS matches_seq;
DROP SEQUENCE IF EXISTS user_achievements_seq;
DROP SEQUENCE IF EXISTS users_seq;

CREATE SEQUENCE achievements_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE match_players_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE matches_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE user_achievements_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE achievements (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('achievements_seq'),
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    icon_url VARCHAR(500),
    description TEXT
);

CREATE TABLE matches (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('matches_seq'),
    played_at TIMESTAMP(6) NOT NULL,
    map_or_mode VARCHAR(50) NOT NULL,
    notes TEXT
);

CREATE TABLE users (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('users_seq'),
    birth_date DATE,
    gender VARCHAR(1) NOT NULL,
    level INTEGER NOT NULL DEFAULT 1,
    rating INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    phone VARCHAR(20),
    nickname VARCHAR(50) NOT NULL UNIQUE,
    city VARCHAR(100),
    country VARCHAR(100),
    full_name VARCHAR(100),
    avatar_url VARCHAR(500),
    bio TEXT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE match_players (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('match_players_seq'),
    assists INTEGER NOT NULL DEFAULT 0,
    deaths INTEGER NOT NULL DEFAULT 0,
    kills INTEGER NOT NULL DEFAULT 0,
    match_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    match_result VARCHAR(10) NOT NULL,
    UNIQUE (match_id, user_id)
);

CREATE TABLE user_achievements (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('user_achievements_seq'),
    achievement_id BIGINT NOT NULL,
    unlocked_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    UNIQUE (user_id, achievement_id)
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
