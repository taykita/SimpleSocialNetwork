create table if not exists Accounts (
	id int4 not null,
	EMAIL varchar(50),
	PASS varchar(100),
	USER_NAME varchar(50),
	primary key (id)
);

create table if not exists Accounts_Accounts (
	ACC_ID int4 not null,
	USER_ID int4 not null,
	primary key (ACC_ID, USER_ID)
);

create table if not exists POST (
	id serial not null,
	ACC_ID int4,
	TEXT varchar(200),
	DATE timestamp without time zone,
	primary key (id)
);

CREATE SEQUENCE if not exists  hibernate_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;