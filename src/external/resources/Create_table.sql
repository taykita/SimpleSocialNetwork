create table if not exists Accounts (
	ID serial not null,
	EMAIL varchar(50),
	PASS varchar(100),
	USER_NAME varchar(50),
	primary key (id)
);

CREATE INDEX IF NOT EXISTS accounts_email_idx ON
	Accounts
		(email);

create table if not exists Accounts_Accounts (
	ACC_ID int4 not null,
	USER_ID int4 not null,
	primary key (ACC_ID, USER_ID),
	constraint USER_ID_CONST
        foreign key (USER_ID)
        references Accounts,
    constraint ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts
);

create table if not exists Post (
	ID serial not null,
	ACC_ID int4,
	TEXT varchar(200),
	DATE timestamp without time zone,
	primary key (id),
	constraint POST_ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts
);

CREATE INDEX IF NOT EXISTS post_acc_id_idx ON
	Post
		(ACC_ID);

create table if not exists Chat (
	ID serial not null,
	NAME varchar(50),
	TYPE int4,
	OWNER_ID int4,
	primary key (id),
    constraint CHAT_OWNER_ID_CONST
        foreign key (OWNER_ID)
        references Accounts
);

create table if not exists Message (
	ID serial not null,
	TEXT varchar(200),
	DATE timestamp without time zone,
	CHAT_ID int4,
	ACC_ID int4,
	primary key (id),
	constraint MESSAGE_CHAT_ID_CONST
        foreign key (CHAT_ID)
        references Chat,
	constraint MESSAGE_ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts
);

create table if not exists Accounts_Chat (
	CHAT_ID int4 not null,
	ACC_ID int4 not null,
	primary key (CHAT_ID, ACC_ID),
	constraint ACCOUNTS_CHAT_CHAT_ID_CONST
        foreign key (CHAT_ID)
        references Chat,
    constraint ACCOUNTS_CHAT_ACC_ID_CONST
        foreign key (ACC_ID)
        references Accounts
);

create table if not exists Data (
	ID serial not null,
	DATA_NAME varchar(50),
	ACTION varchar(50),
	DATA varchar(100),
	primary key (id)
);