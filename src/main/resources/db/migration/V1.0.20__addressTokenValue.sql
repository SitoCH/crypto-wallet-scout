create table AddressTokenValue (id bigint not null auto_increment, address varchar(255), dateTime datetime(6), nativeValue decimal(19,2), network varchar(255), tokenSymbol varchar(255), primary key (id)) engine=InnoDB;