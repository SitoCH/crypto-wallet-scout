DELETE
FROM `Token`
WHERE `symbol` IN ('AAVE', 'AAVE.e');

INSERT INTO `Token` (`coinGeckoId`, `excludeFromBalance`, `name`, `symbol`, `allocation`, `parentId`)
VALUES ('aave', b'0', 'Aave', 'AAVE', NULL, NULL);

INSERT INTO `Token` (`coinGeckoId`, `excludeFromBalance`, `name`, `symbol`, `allocation`, `parentId`)
VALUES ('aave', b'0', 'Aave', 'AAVE.e', NULL, (select Id from (SELECT id FROM `Token` where symbol = 'AAVE') as t) );
