alter table Token  drop column parentCoinGeckoId;

alter table Token add column parentId varchar(255);

update Token set  parentId = (select Id from (SELECT id FROM Token where symbol = 'AVAX') as t) WHERE symbol = 'avWAVAX';

update Token set  parentId = (select Id from (SELECT id FROM Token where symbol = 'AVAX') as t) WHERE symbol = 'WAVAX';

update Token set  parentId = (select Id from (SELECT id FROM Token where symbol = 'MATIC') as t) WHERE symbol = 'amWMATIC';

update Token set  parentId = (select Id from (SELECT id FROM Token where symbol = 'AAVE') as t)WHERE symbol = 'amAAVE';
