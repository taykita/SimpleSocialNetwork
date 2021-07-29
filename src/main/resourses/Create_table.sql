CREATE TABLE IF NOT EXISTS public.accounts
(
    id serial NOT NULL,
    email character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    pass character varying(50) NOT NULL,
    PRIMARY KEY (id, email),
    CONSTRAINT id UNIQUE (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.accounts
    OWNER to admin;
	



CREATE TABLE IF NOT EXISTS public.user_list
(
    id serial NOT NULL,
    name character varying(50) NOT NULL,
    avatar character varying(100),
    PRIMARY KEY (id),
    CONSTRAINT id UNIQUE (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.user_list
    OWNER to admin;
	



CREATE TABLE IF NOT EXISTS public.accounts_user_list
(
    acc_id integer,
    list_id integer,
    CONSTRAINT acc_id FOREIGN KEY (acc_id)
        REFERENCES public.accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT list_id FOREIGN KEY (list_id)
        REFERENCES public.user_list (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.accounts_user_list
    OWNER to admin;
