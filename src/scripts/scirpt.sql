--SKRYPT WDROŻENIOWY BSK MAJ 2017 PROJEKT POLICJA 
--WYKONAWCY:
--KRZYŻAŃSKI BARTŁOMIEJ
--ROGUSKI PRZEMYSŁAW


ALTER ROLE postgres WITH password 'admin';
DROP ROLE IF EXISTS login;
CREATE ROLE login WITH password 'pass';

UPDATE pg_authid 
SET rolcanlogin = true
WHERE rolname = 'login';



DROP TABLE IF EXISTS user_label;
DROP TABLE IF EXISTS tables_labels;
DROP TABLE IF EXISTS security_label;

CREATE TABLE tables_labels
(
  id integer NOT NULL,
  table_name text NOT NULL,
  label_value int NOT NULL,
  CONSTRAINT tables_labels_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tables_labels
  OWNER TO postgres;
GRANT ALL ON public.tables_labels TO PUBLIC;



INSERT INTO tables_labels(id, table_name, label_value) VALUES(1,'announcement',5);
INSERT INTO tables_labels(id, table_name, label_value) VALUES(2,'policeman',10);
INSERT INTO tables_labels(id, table_name, label_value) VALUES(3,'dispatcher',20);
INSERT INTO tables_labels(id, table_name, label_value) VALUES(4,'accountant',30);
INSERT INTO tables_labels(id, table_name, label_value) VALUES(5,'commander',40);
INSERT INTO tables_labels(id, table_name, label_value) VALUES(6,'tables_labels',1);
INSERT INTO tables_labels(id, table_name, label_value) VALUES(7,'user_label',1);

CREATE TABLE public.user_label
(
  id integer NOT NULL,
  user_name text NOT NULL,
  label_value integer NOT NULL,
  CONSTRAINT user_label_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_label
  OWNER TO postgres;
GRANT ALL ON public.user_label TO PUBLIC;

INSERT INTO public.user_label
(id, user_name, label_value)
VALUES(1, 'postgres', 1);


INSERT INTO public.user_label
(id, user_name, label_value)
VALUES(2, 'login', 20);



GRANT ALL ON pg_authid TO PUBLIC;

DROP TABLE IF EXISTS policeman;
CREATE TABLE public.policeman
(
  id serial NOT NULL,
  name text NOT NULL,
  birth date NOT NULL,
  CONSTRAINT policeman_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.policeman
  OWNER TO postgres;
REVOKE ALL ON public.policeman FROM PUBLIC;

INSERT INTO policeman(name,birth) VALUES ('Przemek','1995-05-14'::date);
INSERT INTO policeman(name,birth) VALUES ('Maiami','1982-08-23'::date);
INSERT INTO policeman(name,birth) VALUES ('Gebels','1962-02-13'::date);


DROP TABLE IF EXISTS announcement;
CREATE TABLE public.announcement
(
  id SERIAL NOT NULL,
  announcement text NOT NULL,
  announce_date date NOT NULL,
  CONSTRAINT announcement_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.announcement
  OWNER TO postgres;
REVOKE ALL ON public.announcement FROM PUBLIC;

INSERT INTO public.announcement(announcement, announce_date)
VALUES('UWAGA NA DERBY TRÓJMIASTA.', '2017-04-17'::date);
INSERT INTO public.announcement(announcement, announce_date)
VALUES('Nowe policyjne wozy', '2016-02-23'::date);


DROP TABLE IF EXISTS dispatcher;
CREATE TABLE public.dispatcher
(
  id SERIAL NOT NULL,
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
REVOKE ALL ON public.dispatcher FROM PUBLIC;

INSERT INTO public.dispatcher( place, intervention_date, patrol)
VALUES('Gdańsk Zaspa Hynka 12', now()::date, 'Patrol 13');
INSERT INTO public.dispatcher(place, intervention_date, patrol)
VALUES('Gdańsk Wrzeszcz Waryńskiego 24', now()::date - interval '2 weeks', 'Patrol 7');


DROP TABLE IF EXISTS commander;
CREATE TABLE public.commander
(
  id serial NOT NULL,
  worker text NOT NULL,
  employment_date date NOT NULL,
  CONSTRAINT commander_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.commander
  OWNER TO postgres;
REVOKE ALL ON public.commander FROM PUBLIC;

INSERT INTO public.commander(worker, employment_date) 
VALUES('Adam Kot', '2002-05-20'::date);


DROP TABLE IF EXISTS accountant;
CREATE TABLE public.accountant
(
  id serial NOT NULL,
  accounting_document text NOT NULL,
  fiscal_date date NOT NULL,
  CONSTRAINT accountant_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountant
  OWNER TO postgres;
REVOKE ALL ON public.accountant FROM PUBLIC;

INSERT INTO public.accountant(accounting_document, fiscal_date)
VALUES('FV/170410/0001', '2017-04-10'::date);
INSERT INTO public.accountant(accounting_document, fiscal_date)
VALUES('FVK/170413/0002', '2017-04-13'::date);
INSERT INTO public.accountant(accounting_document, fiscal_date)
VALUES('FV/170415/0003', '2017-04-15'::date);

GRANT ALL ON accountant_id_seq TO PUBLIC;
GRANT ALL ON announcement_id_seq TO PUBLIC;
GRANT ALL ON commander_id_seq TO PUBLIC;
GRANT ALL ON dispatcher_id_seq TO PUBLIC;
GRANT ALL ON policeman_id_seq TO PUBLIC;



--WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
--TESTY I SPRAWDZENIA
--WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
-- SELECT sl.value
-- FROM user_label ul
-- JOIN security_label sl ON sl.id = ul.security_label_id
-- WHERE user_name = 'bizon'

-- GRANT SELECT ON TABLE public.accountant TO baran;
-- 
-- SELECT 4 BETWEEN start_value AND last_value FROM accountant_id_seq
-- 
-- SELECT 4 BETWEEN start_value AND last_value as new FROM accountant_id_seq;
-- SELECT *
-- FROM public.security_label;
-- 
-- SELECT *
-- FROM public.user_label;
-- 
-- SELECT ul.user_name,
-- 	sl.value,
-- 	sl.name AS role_name
-- FROM pg_roles pr
-- JOIN public.user_label ul ON ul.user_name = pr.rolname
-- JOIN public.security_label sl ON sl.id = ul.security_label_id;
-- 
-- SELECT sl.value 
-- FROM public.user_label ul
-- JOIN public.security_label sl ON sl.id = ul.security_label_id
-- WHERE user_name = session_user;
-- 
-- SELECT session_user
-- SELECT value
-- FROM public.security_label
-- ORDER BY value;
-- 
-- SELECT name
-- FROM public.security_label
-- ORDER BY value;
-- 
-- ALTER ROLE session_user with password 'Test1234'
-- 
-- SELECT rolpassword
-- FROM pg_authid 
-- WHERE rolname = current_user;
-- 
-- 21232f297a57a5a743894a0e4a801fc3
-- "md5b48320d0d5be0ee11ebc1fba8f4d7770"
-- b48320d0d5be0ee11ebc1fba8f4d7770
-- 2c9341ca4cf3d87b9e4eb905d6a3ec45
-- 
-- SELECT current_user;
-- 
-- 
-- INSERT INTO announcement as a(id, announcement, announce_date) 
-- VALUES (3, 'Anvil Distribution', '2015-01-02'::date)
--     ON CONFLICT (id) DO UPDATE
--     SET announcement = 'Anvil Distribution', announce_date = '2015-01-02'::date
--     WHERE a.id = 3;
-- 
-- 
-- 
--     CREATE TABLE public.announcement
-- (
--   id integer NOT NULL,
--   announcement text NOT NULL,
--   announce_date date NOT NULL,
-- 
--   DELETE FROM user_label WHERE user_name = 'qwer';
-- DROP ROLE "qwer";
-- tables_labels
-- 
-- SELECT table_name
--   FROM information_schema.tables
--  WHERE table_schema='public'
--    AND table_type='BASE TABLE';
-- 
-- SELECT table_name
--   FROM information_schema.tables
--  WHERE table_schema='public'
--    AND table_type='BASE TABLE';
-- security_label
-- user_label
-- 
-- SELECT sl.value
-- FROM tables_labels tl
-- JOIN security_label sl ON sl.id = tl.security_label_id
-- WHERE tl.table_name = 'policeman'

-- UPDATE user_label
-- SET security_label_id = x.id
-- FROM (
-- SELECT id
-- FROM security_label
-- WHERE value = 40
-- ) as x
-- WHERE user_name = 'darek'

--REVOKE ALL ON policeman FROM dominik;
--GRANT SELECT ON policeman TO dominik;