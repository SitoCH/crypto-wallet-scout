create table hibernate_sequence (
    next_val bigint
) engine=InnoDB;

insert into hibernate_sequence values ( 1 );

create table User (
                      id bigint not null,
                      oidcId varchar(255),
                      primary key (id)
) engine=InnoDB;
