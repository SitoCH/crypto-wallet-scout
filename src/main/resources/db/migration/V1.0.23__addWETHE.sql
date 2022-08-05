DELETE
FROM `Token`
WHERE `symbol` IN ('WETH.e');

INSERT INTO `Token` (`coinGeckoId`, `name`, `symbol`, `parentId`)
VALUES ('aave-weth', 'Aave WETH', 'WETH.e', (select Id from (SELECT id FROM `Token` where symbol = 'ETH') as t) );
