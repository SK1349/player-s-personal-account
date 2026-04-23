INSERT INTO achievements (code, name, description, icon_url) VALUES
('FIRST_WIN', 'Первая победа', 'Выиграйте свой первый матч', '/achievements/free-icon-achievement-1006554.png'),
('SNIPER_MASTER', 'Снайпер', 'Сделайте 100 хедшотов подряд', '/achievements/free-icon-achievement-1312197.png'),
('VETERAN', 'Ветеран', 'Сыграйте 50 рейтинговых матчей', '/achievements/free-icon-achievement-3194327.png'),
('UNDEFEATED', 'Непобедимый', 'Выиграйте 10 матчей подряд', '/achievements/free-icon-achievement-6179902.png'),
('TEAM_PLAYER', 'Командный игрок', 'Сыграйте 20 матчей в одной команде', '/achievements/free-icon-excellence-4185520.png'),
('COMEBACK_KING', 'Король камбэка', 'Выиграйте матч, уступая 0:10', '/achievements/free-icon-goal-7554571.png'),
('PERFECT_GAME', 'Идеальная игра', 'Выиграйте матч со счётом 16:0', '/achievements/free-icon-quality-7340754.png'),
('LEGEND', 'Легенда', 'Войдите в топ-10 глобального рейтинга', '/achievements/free-icon-target-achieved-18652247.png')

ON CONFLICT (code) DO NOTHING;