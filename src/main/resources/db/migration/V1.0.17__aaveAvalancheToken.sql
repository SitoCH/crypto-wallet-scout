DELETE
FROM `Token`
WHERE `symbol` IN ('aAvaWAVAX');

INSERT INTO `Token` (`coinGeckoId`, `name`, `symbol`, `allocation`, `parentId`)
VALUES ('avalanche-2', 'Aave Avalanche', 'aAvaWAVAX', 'STACKED', (select Id from (SELECT id FROM `Token` where symbol = 'AVAX') as t) )
