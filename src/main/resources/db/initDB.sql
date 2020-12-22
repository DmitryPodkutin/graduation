DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS restaurant;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'::regclass),
    name       VARCHAR                           NOT NULL,
    email      VARCHAR                           NOT NULL,
    password   VARCHAR                           NOT NULL,
    registered TIMESTAMP           DEFAULT now() NOT NULL,
    enabled    BOOL                DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id   INTEGER DEFAULT nextval('global_seq'::regclass) NOT NULL
        CONSTRAINT restaurant_pk PRIMARY KEY,
    name VARCHAR                                         NOT NULL
);

CREATE UNIQUE INDEX restaurant_unique_name_index ON restaurant (name);

CREATE TABLE dish
(
    id            INTEGER DEFAULT nextval('global_seq'::regclass),
    date          DATE    NOT NULL,
    name          VARCHAR NOT NULL,
    price         NUMERIC NOT NULL,
    restaurant_id INTEGER NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX dish_unique_restaurantId_date_name_idx ON dish (restaurant_id, date, name);

CREATE TABLE vote
(
    id            INTEGER DEFAULT nextval('global_seq'::regclass),
    date          DATE    DEFAULT now() NOT NULL,
    user_id       INTEGER               NOT NULL,
    restaurant_id INTEGER               NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX vote_user_id_date_u_index ON vote (user_id, date);



