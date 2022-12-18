INSERT INTO users (id, first_name) VALUES (0, 'John');
INSERT INTO users (id, first_name) VALUES (1, 'Maria');
INSERT INTO users (id, first_name) VALUES (2, 'Mark');
INSERT INTO users (id, first_name) VALUES (3, 'Will');

INSERT INTO addresses (id, user_id, address_line, city, country, postal_code) VALUES (0, 0, 'some street 1', 'New York', 'US', '1234');
INSERT INTO addresses (id, user_id, address_line, city, country, postal_code) VALUES (1, 1, 'some street 2', 'Miami', 'US', '12345');
INSERT INTO addresses (id, user_id, address_line, city, country, postal_code) VALUES (2, 2, 'some street 3', 'Berlin', 'DE', '2234');
INSERT INTO addresses (id, user_id, address_line, city, country, postal_code) VALUES (3, 3, 'some street 4', 'Helsinki', 'FI', '1134');