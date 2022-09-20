DELETE
FROM `Token`
WHERE `symbol` IN ('WMATIC');

INSERT INTO `Token` (`coinGeckoId`, `name`, `symbol`, `parentId`)
VALUES ('wmatic', 'Wrapped Matic', 'WMATIC', (select Id from (SELECT id FROM `Token` where symbol = 'MATIC') as t) );
