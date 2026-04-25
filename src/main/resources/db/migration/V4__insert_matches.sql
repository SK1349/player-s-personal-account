INSERT INTO matches (map_or_mode, played_at, notes)
VALUES
    ('de_dust2', NOW() - INTERVAL '30 days', 'Турнирный матч'),
    ('de_mirage', NOW() - INTERVAL '28 days', 'Плей-офф'),
    ('de_inferno', NOW() - INTERVAL '25 days', 'Тренировка'),
    ('de_nuke', NOW() - INTERVAL '20 days', 'Рейтинг'),
    ('de_ancient', NOW() - INTERVAL '15 days', 'Новая карта'),
    ('de_mirage', NOW() - INTERVAL '10 days', 'Реванш'),
    ('de_dust2', NOW() - INTERVAL '7 days', 'Быстрая игра'),
    ('de_overpass', NOW() - INTERVAL '3 days', 'Последний матч'),
    ('de_vertigo', NOW() - INTERVAL '1 day', 'Тест'),
    ('de_anubis', NOW() - INTERVAL '6 hours', 'Свежий результат')
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (1, 1, 'WIN', 16, 12, 1000),
    (1, 2, 'LOSS', 12, 16, 1000)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (2, 3, 'WIN', 18, 9, 1000),
    (2, 4, 'LOSS', 9, 18, 1000)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (3, 1, 'LOSS', 10, 16, 1020),
    (3, 3, 'WIN', 16, 10, 1020)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (4, 2, 'WIN', 14, 11, 980),
    (4, 4, 'LOSS', 11, 14, 980)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (5, 1, 'WIN', 15, 8, 960),
    (5, 4, 'LOSS', 8, 15, 1000)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (6, 3, 'LOSS', 13, 16, 1000),
    (6, 2, 'WIN', 16, 13, 1040)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (7, 1, 'WIN', 16, 5, 1000),
    (7, 5, 'LOSS', 5, 16, 1018)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (8, 3, 'WIN', 17, 7, 981),
    (8, 5, 'LOSS', 7, 17, 1018)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (9, 2, 'WIN', 15, 10, 963),
    (9, 5, 'LOSS', 10, 15, 1022)
ON CONFLICT DO NOTHING;

INSERT INTO match_players (match_id, user_id, match_result, kills, deaths, opponent_rating_snapshot)
VALUES
    (10, 1, 'DRAW', 14, 14, 1036),
    (10, 3, 'DRAW', 14, 14, 1037)
ON CONFLICT DO NOTHING;