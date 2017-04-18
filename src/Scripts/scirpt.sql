-- Table: public.policeman

-- DROP TABLE public.policeman;

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


ALTER ROLE postgres WITH password 'admin';
CREATE ROLE login WITH password 'pass';
CREATE ROLE adam WITH password 'hasloAdama';

UPDATE pg_authid 
SET rolcanlogin = true
WHERE rolname IN ('adam', 'login');

INSERT INTO policeman(id,name,birth)
VALUES (1,'Przemek','1995-05-14'::date);


INSERT INTO policeman(id,name,birth)
VALUES (2,'Maiami','1982-08-23'::date);

INSERT INTO policeman(id,name,birth)
VALUES (3,'Gebels','1962-02-13'::date);

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

SELECT *
FROM public.security_label;

SELECT *
FROM public.user_label;

INSERT INTO public.security_label
(id, value, name)
VALUES(1, 10, 'Basic');

INSERT INTO public.security_label
(id, value, name)
VALUES(2, 20, 'Advanced');

INSERT INTO public.security_label
(id, value, name)
VALUES(3, 30, 'Expert');

INSERT INTO public.security_label
(id, value, name)
VALUES(4, 40, 'Commandor');

INSERT INTO public.security_label
(id, value, name)
VALUES(5, 50, 'Administrator');

INSERT INTO public.security_label
(id, value, name)
VALUES(6, 0, 'New User');

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

INSERT INTO public.user_label
(id, user_name, security_label_id)
SELECT
	3,
	pr.rolname,
	sl.id
FROM pg_roles pr, public.security_label sl
WHERE pr.rolname = 'adam'
AND sl.name = 'Expert';



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


INSERT INTO public.announcement
(id, announcement, announce_date)
VALUES(1, 'UWAGA NA DERBY TRÓJMIASTA.', '2017-04-17'::date)

INSERT INTO public.announcement
(id, announcement, announce_date)
VALUES(2, 'Nowe policyjne wozy', '2016-02-23'::date)


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

INSERT INTO public.dispatcher
(id, place, intervention_date, patrol)
VALUES(1, 'Gdańsk Zaspa Hynka 12', now()::date, 'Patrol 13')

INSERT INTO public.dispatcher
(id, place, intervention_date, patrol)
VALUES(2, 'Gdańsk Wrzeszcz Waryńskiego 24', now()::date - interval '2 weeks', 'Patrol 7')