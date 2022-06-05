
update Token set  parentId = (select Id from (SELECT id FROM Token where symbol = 'ETH') as t) WHERE symbol IN ('WETH', 'W''ETH.e', 'amWETH', 'anyETH');
