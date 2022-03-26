create table TerraTokenContract (id bigint not null auto_increment, contractId varchar(255), decimals bigint, symbol varchar(255), primary key (id)) engine=InnoDB;

insert into TerraTokenContract (contractId, decimals, symbol) VALUES
    ('terra14z56l0fp2lsf86zy3hty2z47ezkhnthtr9yq76', 6, 'ANC'),
    ('terra1mddcdx0ujx89f38gu7zspk2r2ffdl5enyz2u03', 8, 'ORION');