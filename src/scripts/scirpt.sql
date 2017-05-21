--SKRYPT WDROŻENIOWY BSK MAJ 2017 PROJEKT POLICJA 
--WYKONAWCY:
--KRZYŻAŃSKI BARTŁOMIEJ
--ROGUSKI PRZEMYSŁAW


ALTER ROLE postgres WITH password 'admin';
CREATE ROLE login WITH password 'pass';

UPDATE pg_authid 
SET rolcanlogin = true
WHERE rolname = 'login';


CREATE TABLE public.policeman
(
  id integer NOT NULL,
  name text NOT NULL,
  birth date NOT NULL,
  CONSTRAINT policeman_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.policeman
  OWNER TO postgres;
GRANT ALL ON public.policeman TO PUBLIC;

INSERT INTO policeman(id,name,birth) VALUES (1,'Przemek','1995-05-14'::date);
INSERT INTO policeman(id,name,birth) VALUES (2,'Maiami','1982-08-23'::date);
INSERT INTO policeman(id,name,birth) VALUES (3,'Gebels','1962-02-13'::date);

CREATE TABLE public.security_label
(
  id integer NOT NULL,
  value integer NOT NULL,
  role_name text NOT NULL,
  CONSTRAINT security_label_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.security_label
  OWNER TO postgres;
GRANT ALL ON public.security_label TO PUBLIC;

CREATE TABLE public.user_label
(
  id integer NOT NULL,
  user_name text NOT NULL,
  security_label_id integer NOT NULL,
  CONSTRAINT user_label_pkey PRIMARY KEY (id),
  CONSTRAINT user_label_security_label_id_fkey
	  FOREIGN KEY (security_label_id) 
	  REFERENCES public.security_label (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_label
  OWNER TO postgres;
GRANT ALL ON public.user_label TO PUBLIC;

INSERT INTO public.security_label(id, value, name) VALUES(1, 10, 'Basic');
INSERT INTO public.security_label(id, value, name) VALUES(2, 20, 'Advanced');
INSERT INTO public.security_label(id, value, name) VALUES(3, 30, 'Expert');
INSERT INTO public.security_label(id, value, name) VALUES(4, 40, 'Commandor');
INSERT INTO public.security_label(id, value, name) VALUES(5, 50, 'Administrator');
INSERT INTO public.security_label(id, value, name) VALUES(6, 0, 'New User');

INSERT INTO public.user_label
(id, user_name, security_label_id)
SELECT
	1,
	pr.rolname,
	sl.id
FROM pg_roles pr, public.security_label sl
WHERE pr.rolname = 'postgres'
AND sl.name = 'Administrator';

INSERT INTO public.user_label
(id, user_name, security_label_id)
SELECT
	2,
	pr.rolname,
	sl.id
FROM pg_roles pr, public.security_label sl
WHERE pr.rolname = 'login'
AND sl.name = 'New User';

CREATE TABLE public.announcement
(
  id integer NOT NULL,
  announcement text NOT NULL,
  announce_date date NOT NULL,
  CONSTRAINT announcement_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.announcement
  OWNER TO postgres;
GRANT ALL ON public.announcement TO PUBLIC;

INSERT INTO public.announcement(id, announcement, announce_date)
VALUES(1, 'UWAGA NA DERBY TRÓJMIASTA.', '2017-04-17'::date);
INSERT INTO public.announcement(id, announcement, announce_date)
VALUES(2, 'Nowe policyjne wozy', '2016-02-23'::date);

CREATE TABLE public.dispatcher
(
  id integer NOT NULL,
  place text NOT NULL,
  intervention_date date NOT NULL,
  patrol text NOT NULL,
  CONSTRAINT dispatcher_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.dispatcher
  OWNER TO postgres;
GRANT ALL ON public.dispatcher TO PUBLIC;

INSERT INTO public.dispatcher(id, place, intervention_date, patrol)
VALUES(1, 'Gdańsk Zaspa Hynka 12', now()::date, 'Patrol 13');
INSERT INTO public.dispatcher(id, place, intervention_date, patrol)
VALUES(2, 'Gdańsk Wrzeszcz Waryńskiego 24', now()::date - interval '2 weeks', 'Patrol 7');

CREATE TABLE public.commander
(
  id integer NOT NULL,
  worker text NOT NULL,
  employment_date date NOT NULL,
  CONSTRAINT commander_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.commander
  OWNER TO postgres;
GRANT ALL ON public.commander TO PUBLIC;

INSERT INTO public.commander(id, worker, employment_date) 
VALUES(1, 'Adam Kot', '2002-05-20'::date);

CREATE TABLE public.accountant
(
  id integer NOT NULL,
  accounting_document text NOT NULL,
  fiscal_date date NOT NULL,
  CONSTRAINT accountant_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountant
  OWNER TO postgres;
GRANT ALL ON public.accountant TO PUBLIC;

INSERT INTO public.accountant(id, accounting_document, fiscal_date)
VALUES(1, 'FV/170410/0001', '2017-04-10'::date);
INSERT INTO public.accountant(id, accounting_document, fiscal_date)
VALUES(2, 'FVK/170413/0002', '2017-04-13'::date);
INSERT INTO public.accountant(id, accounting_document, fiscal_date)
VALUES(3, 'FV/170415/0003', '2017-04-15'::date);


--WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
--TESTY I SPRAWDZENIA
--WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
SELECT *
FROM public.security_label;

SELECT *
FROM public.user_label;

SELECT ul.user_name,
	sl.value,
	sl.name AS role_name
FROM pg_roles pr
JOIN public.user_label ul ON ul.user_name = pr.rolname
JOIN public.security_label sl ON sl.id = ul.security_label_id;

SELECT sl.value 
FROM public.user_label ul
JOIN public.security_label sl ON sl.id = ul.security_label_id
WHERE user_name = session_user;

SELECT session_user
SELECT value
FROM public.security_label
ORDER BY value;

SELECT name
FROM public.security_label
ORDER BY value;

ALTER ROLE session_user with password 'Test1234'

SELECT rolpassword
FROM pg_authid 
WHERE rolname = current_user;

21232f297a57a5a743894a0e4a801fc3
"md5b48320d0d5be0ee11ebc1fba8f4d7770"
b48320d0d5be0ee11ebc1fba8f4d7770
2c9341ca4cf3d87b9e4eb905d6a3ec45

SELECT current_user;