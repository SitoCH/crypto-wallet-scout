DELETE
FROM `Token`
WHERE `symbol` IN ('aAvaAAVE');

INSERT INTO `Token` (`coinGeckoId`, `name`, `symbol`, `allocation`, `parentId`)
VALUES ('aave', 'Aave', 'aAvaAAVE', 'STACKED', (select Id from (SELECT id FROM `Token` where symbol = 'AAVE') as t) )
