create table if not exists Accounts (
	ID serial not null,
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
	ID serial not null,
	ACC_ID int4,
	TEXT varchar(200),
	DATE timestamp without time zone,
	primary key (id)
);

alter table if exists Accounts_Accounts
    	add constraint USER_ID_CONST
    	foreign key (USER_ID)
    	references Accounts;

alter table if exists Accounts_Accounts
        add constraint ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts;

alter table if exists POST
    	add constraint POST_ACC_ID_CONST
    	foreign key (ACC_ID)
    	references Accounts;
