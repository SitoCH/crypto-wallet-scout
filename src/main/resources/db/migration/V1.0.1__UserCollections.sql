create table UserCollection
(
    id      bigint not null,
    name    varchar(255),
    user_id bigint,
    primary key (id)
) engine = InnoDB;
create table UserCollectionAddress
(
    id                bigint not null,
    address           varchar(255),
    userCollection_id bigint,
    primary key (id)
) engine = InnoDB;

alter table UserCollection
    add constraint FKqce966ixthice1irabmx7qawf foreign key (user_id) references User (id);

alter table UserCollectionAddress
    add constraint FKi03w67sv1mxctjq05sriks1b9 foreign key (userCollection_id) references UserCollection (id);