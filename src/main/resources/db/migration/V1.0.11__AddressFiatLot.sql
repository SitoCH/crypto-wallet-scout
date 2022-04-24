create table AddressFiatLot
(
    id       bigint not null auto_increment,
    address  varchar(255),
    dateTime datetime(6),
    usdValue decimal(19, 2),
    primary key (id)
) engine = InnoDB;
