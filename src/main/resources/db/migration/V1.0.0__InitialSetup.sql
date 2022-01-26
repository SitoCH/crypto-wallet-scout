create table Token (id bigint not null auto_increment, coinGeckoId varchar(255), excludeFromBalance bit not null, name varchar(255), symbol varchar(255) not null, primary key (id)) engine=InnoDB;
create table User (id bigint not null auto_increment, oidcId varchar(255), primary key (id)) engine=InnoDB;
create table UserCollection (id bigint not null auto_increment, name varchar(255), user_id bigint, primary key (id)) engine=InnoDB;
create table UserCollectionAddress (id bigint not null auto_increment, address varchar(255), userCollection_id bigint, primary key (id)) engine=InnoDB;
alter table Token add constraint UK_token_symbol unique (symbol);
alter table UserCollection add constraint FKqce966ixthice1irabmx7qawf foreign key (user_id) references User (id);
alter table UserCollectionAddress add constraint FKi03w67sv1mxctjq05sriks1b9 foreign key (userCollection_id) references UserCollection (id);
