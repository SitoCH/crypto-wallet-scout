DELETE
FROM `Token`
WHERE `symbol` IN ('WBTC');

INSERT INTO `Token` (`coinGeckoId`, `name`, `symbol`, `parentId`)
VALUES ('wrapped-bitcoin', 'Wrapped Bitcoin', 'WBTC', (select Id from (SELECT id FROM `Token` where symbol = 'BTC') as t) );
