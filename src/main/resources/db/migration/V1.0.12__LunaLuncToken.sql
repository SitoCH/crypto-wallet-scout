DELETE
FROM `Token`
WHERE `symbol` IN ('LUNA', 'UST');

INSERT INTO `Token` (`coinGeckoId`, `excludeFromBalance`, `name`, `symbol`, `allocation`, `parentId`)
VALUES ('terra-luna', b'0', 'Terra Luna Classic', 'LUNC', NULL, NULL),
       ('terra-luna-2', b'0', 'Terra', 'LUNA', NULL, NULL),
       ('terrausd', b'0', 'TerraClassicUSD', 'USTC', NULL, NULL);
