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
	add constraint USER_ID_CONST
        foreign key (USER_ID)
        references Accounts;
    add constraint ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts;
);

create table if not exists Post (
	ID serial not null,
	ACC_ID int4,
	TEXT varchar(200),
	DATE timestamp without time zone,
	primary key (id)
	add constraint POST_ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts;
);

--create table if not exists Message (
--	ID serial not null,
--	ACC_ID int4,
--	CHAT_ID int4,
--	TEXT varchar(200),
--	primary key (ID)
--    add constraint MESSAGE_ACC_ID_CONST
--        foreign key (ACC_ID)
--        references Accounts;
--);