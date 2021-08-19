DO $$
BEGIN

  BEGIN
    alter table if exists Accounts_Accounts
    	add constraint USER_ID_CONST
    	foreign key (USER_ID)
    	references Accounts;
  EXCEPTION
    WHEN duplicate_object THEN RAISE NOTICE 'Table constraint already exists';
  END;

END $$;

DO $$
BEGIN

  BEGIN
    alter table if exists Accounts_Accounts
    	add constraint ACC_ID_CONST
    	foreign key (ACC_ID)
    	references Accounts;
  EXCEPTION
    WHEN duplicate_object THEN RAISE NOTICE 'Table constraint already exists';
  END;

END $$;

DO $$
BEGIN

  BEGIN
    alter table if exists POST
    	add constraint POST_ACC_ID_CONST
    	foreign key (ACC_ID)
    	references Accounts;
  EXCEPTION
    WHEN duplicate_object THEN RAISE NOTICE 'Table constraint already exists';
  END;

END $$;