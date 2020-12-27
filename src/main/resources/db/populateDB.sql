DELETE
FROM users;
DELETE
FROM user_roles;
DELETE
FROM restaurant;
DELETE
FROM dish;
DELETE
FROM vote;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurant (name)
VALUES ('Debasus'),
       ('Colonies'),
       ('The Lounge Cafe');

INSERT INTO dish (date, name, price, restaurant_id)
VALUES (NOW(), 'Bear', '120', '100002'),
       (NOW(), 'Garlic bread', '670', '100002'),
       (NOW(), 'BBQ ribs', '340', '100002'),
       (NOW(), 'Tea', '60', '100003'),
       (NOW(), 'Pizza', '500', '100003'),
       (NOW(), 'Bacon and eggs', '70', '100003'),
       (NOW(), 'Fresh juice', '160', '100004'),
       (NOW(), 'Tomato soup', '920', '100004'),
       (NOW(), 'Pasta Carbonara', '270', '100004');

INSERT INTO vote (date, user_id, restaurant_id)
VALUES ('2020-10-23', 100000, 100004),
       ('2020-10-19', 100001, 100004),
       ('2020-11-06', 100000, 100003),
       ('2020-11-09', 100000, 100004),
       (NOW(), 100000, 100002),
       (NOW(),100001,100002);
