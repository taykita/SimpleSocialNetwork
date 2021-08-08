create table Accounts (
	id int4 not null,
	EMAIL varchar(50),
	PASS varchar(50),
	USER_NAME varchar(50),
	primary key (id)
);

create table Accounts_Accounts (
	ACC_ID int4 not null,
	USER_ID int4 not null,
	primary key (ACC_ID, USER_ID)
);

create table POST (
	id int4 not null,
	ACC_ID int4,
	TEXT varchar(200),
	DATE varchar(50),
	USERNAME varchar(50),
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