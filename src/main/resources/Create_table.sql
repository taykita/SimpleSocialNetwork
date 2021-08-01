CREATE TABLE IF NOT EXISTS public.accounts
(
    id serial NOT NULL,
    email character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    pass character varying(50) NOT NULL,
    PRIMARY KEY (id, email),
    CONSTRAINT id UNIQUE (acc_id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.accounts
    OWNER to admin;
	


CREATE TABLE IF NOT EXISTS public.accounts_accounts
(
    acc_id integer NOT NULL,
    user_id integer NOT NULL,
    CONSTRAINT accounts PRIMARY KEY (acc_id, user_id),
    CONSTRAINT acc_id FOREIGN KEY (acc_id)
        REFERENCES public.accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT user_id FOREIGN KEY (acc_id)
        REFERENCES public.accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.accounts_accounts
    OWNER to admin;