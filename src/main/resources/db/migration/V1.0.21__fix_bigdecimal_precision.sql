truncate table AddressTokenValue;

alter table AddressTokenValue modify column nativeValue decimal(30, 10);

alter table AddressSnapshot modify column usdValue decimal(30, 10);