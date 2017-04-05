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


ALTER ROLE postgres WITH password 'admin';


INSERT INTO policeman(id,name,birth)
VALUES (1,'Przemek','1995-05-14'::date);


INSERT INTO policeman(id,name,birth)
VALUES (2,'Maiami','1982-08-23'::date);