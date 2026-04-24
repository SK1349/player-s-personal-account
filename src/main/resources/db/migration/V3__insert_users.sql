INSERT INTO users (
    nickname,
    email,
    password_hash,
    gender,
    full_name,
    avatar_url,
    birth_date,
    country,
    city,
    phone,
    bio
)
VALUES
    (
        'User1',
        'user1@example.com',
        '$2a$10$7DWauKK3tdM1R/kA82m90OA2USttNx2dSbJeihsfq/R8eFijGPJsC',  --сделала всем 123456--
        'M',
        null, null, null, null, null, null, null
    ),
    (
        'User2',
        'user2@example.com',
        '$2a$10$yZLTCXRroiGnA4SbfSYKyuUF3VC7Dy2UtlPJmVRNwwkLKnKUkrOhK',
        'F',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL
    ),
    (
        'User3',
        'user3@example.com',
        '$2a$10$qOV8m.eCrvbSnwrtxuWPE.ZPDzwwoMokmjaiWkwaPWCs9XXadVE0u',
        'M',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL
    ),
    (
        'User4',
        'user4@example.com',
        '$2a$10$O33fVp.foJm5l2iF73ysJOKnrpgoLQP/DrtIkD.pN5VmBjoO5vMva',
        'F',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL
    ),
    (
        'User5',
        'user5@example.com',
        '$2a$10$NaHDDmp0kR2X3fv6YxO1yuRPQykumGl.T7rXHz8SKR.UUrwS3gr6S',
        'M',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL
    )