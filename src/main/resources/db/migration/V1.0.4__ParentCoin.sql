alter table Token
    add column parentCoinGeckoId varchar(255);

DELETE
FROM `Token`
WHERE `coinGeckoId` IN ('avalanche-2', 'aave-polygon-aave', 'aave-polygon-usdc', 'aave-polygon-weth',
                        'aave-polygon-wmatic', 'weth', 'wrapped-avax');

INSERT INTO `Token` (`coinGeckoId`, `excludeFromBalance`, `name`, `symbol`, `parentCoinGeckoId`)
VALUES ('avalanche-2', b'0', 'Avalanche', 'avWAVAX', 'avalanche-2'),
       ('aave-polygon-aave', b'0', 'Aave Polygon AAVE', 'amAAVE', 'aave'),
       ('aave-polygon-usdc', b'0', 'Aave Polygon USDC', 'amUSDC', 'usd-coin'),
       ('aave-polygon-weth', b'0', 'Aave Polygon WETH', 'amWETH', 'ethereum'),
       ('aave-polygon-wmatic', b'0', 'Aave Polygon WMATIC', 'amWMATIC', 'matic-network'),
       ('weth', b'0', 'WETH', 'WETH', 'ethereum'),
       ('wrapped-avax', b'0', 'Wrapped AVAX', 'WAVAX', 'avalanche-2');
