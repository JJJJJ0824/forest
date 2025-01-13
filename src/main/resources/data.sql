INSERT INTO authority (authority_name) VALUES ('ADMIN');
INSERT INTO authority (authority_name) VALUES ('USER');

INSERT INTO traveler (traveler_name, real_name, email, contact, password, registration_date, traveler_authority)
VALUES
('admin', 'administrator', 'eee@22e.ccc', '010-11-11', '1234', now(), 'ADMIN'),
('traveler', '아무튼이름', 'eeee@22ee.ccccc', '010-1241-1112', '4321', now(), 'USER');

INSERT INTO category (category_name) values ('');