INSERT INTO achievements (code, name, description, icon_url) VALUES
('FIRST_MATCH', 'Первый матч', 'Сыграйте свой первый матч', '/achievements/free-icon-achievement-1312197.png'),
('FIRST_WIN', 'Первая победа', 'Выиграйте свой первый матч', '/achievements/free-icon-achievement-3194327.png'),
('FIRST_KILL', 'Первая кровь', 'Совершите первое убийство', '/achievements/free-icon-target-achieved-18652247.png'),
('FIRST_DEATH', 'Первая неудача', 'Подарите свою первую смерть сопернику', '/achievements/free-icon-achievement-1006554.png'),
('LEVEL_2', 'Новичок', 'Достигните 2-го уровня', '/achievements/free-icon-quality-7340754.png'),
('LEVEL_5', 'Опытный', 'Достигните 5-го уровня', '/achievements/free-icon-quality-7340754.png'),
('RATING_1500', 'Серебряный рейтинг', 'Достигните рейтинга 1500', '/achievements/free-icon-achievement-6179902.png'),
('RATING_2000', 'Золотой рейтинг', 'Достигните рейтинга 2000', '/achievements/free-icon-achievement-6179902.png'),
('KILLS_5', 'Охотник', 'Совершите 5 убийств', '/achievements/free-icon-target-achieved-18652247.png'),
('KILLS_10', 'Истребитель', 'Совершите 10 убийств', '/achievements/free-icon-target-achieved-18652247.png'),
('MATCHES_5', 'Любитель', 'Сыграйте 5 матчей', '/achievements/free-icon-achievement-1312197.png'),
('MATCHES_50', 'Ветеран', 'Сыграйте 50 матчей', '/achievements/free-icon-achievement-1312197.png')

ON CONFLICT (code) DO NOTHING;