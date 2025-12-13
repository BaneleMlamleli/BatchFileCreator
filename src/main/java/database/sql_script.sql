DROP TABLE IF EXISTS natural_person;

DROP TABLE IF EXISTS legal_entity;

CREATE TABLE IF NOT EXISTS natural_person (
    party_counter INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    date_of_birth TEXT,
    nationality TEXT,
    citizenship TEXT,
    id_type TEXT,
    party_id TEXT NOT NULL,
    country TEXT,
    address_field TEXT,
    city TEXT,
    province TEXT,
    postal_code TEXT,
    address_country TEXT
);

CREATE TABLE IF NOT EXISTS legal_entity (
    party_counter INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_name TEXT,
    id_type TEXT,
    party_id TEXT NOT NULL,
    country TEXT,
    address_field TEXT,
    city TEXT,
    province TEXT,
    postal_code TEXT,
    address_country TEXT
);