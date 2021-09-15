CREATE SCHEMA pmb;

CREATE TABLE pmb.scripts (
    filename    VARCHAR NOT NULL PRIMARY KEY,
    passed      timestamp with time zone NOT NULL DEFAULT now()
);

CREATE TABLE pmb.etablissement (
    id                  bigserial PRIMARY KEY,
    idneo               VARCHAR(36),
    uai                 VARCHAR(8),
    nom                 VARCHAR(50) NOT NULL,
    principal           boolean NOT NULL DEFAULT FALSE,
    id_principal        bigint
);