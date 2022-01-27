DELETE
FROM Token
WHERE symbol IN ('LUNA', 'avWAVAX', 'UNI');

INSERT INTO `Token` (`coinGeckoId`, `excludeFromBalance`, `name`, `symbol`)
VALUES ('terra-luna', '', NULL, 'LUNA'),
       ('wavax', '', NULL, 'avWAVAX'),
       ('uniswap', '', NULL, 'UNI');
