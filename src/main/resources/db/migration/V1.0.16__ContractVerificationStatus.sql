
rename table BannedContract to ContractVerificationStatus;

alter table ContractVerificationStatus add column status varchar(255) default('BANNED');