alter table Token
    add column allocation varchar(255);

UPDATE `Token`
SET allocation = 'STACKED'
WHERE `symbol` IN ('amAAVE', 'amUSDC', 'amWETH', 'amWMATIC', 'avWAVAX');
