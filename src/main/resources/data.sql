--INSERT INTO authority (authority_name) VALUES ('ADMIN');
--INSERT INTO authority (authority_name) VALUES ('USER');

INSERT INTO course (title, description, content, type, created_at, updated_at)
VALUES
('올바른 환전 방법', '현지 화폐 준비와 환전 팁', '여행을 떠나기 전에 올바른 환전 방법을 익히세요. 공항에서 환전하는 것보다 은행이나 환전소에서 환전하는 것이 더 유리할 수 있습니다. ATM을 활용해 현지 화폐를 인출할 때 수수료와 환율을 꼭 체크하세요.', '공통', now(), now()),
('공항 근처 비행기 대기시간 활용법', '대기시간을 효율적으로 활용하기', '공항에서 비행기를 기다리는 동안 시간 낭비하지 말고 쇼핑이나 식사, 간단한 마사지를 즐기며 대기 시간을 활용하세요. 편안한 공항 문화 속에서 여유를 느끼는 것이 여행의 첫걸음입니다.', '공통', now(), now()),
('칼리보만의 특별한 공항 문화 미리 알아보기', '칼리보 공항에서의 독특한 문화', '내용 추후 작성 예정', '공통', now(), now());

--INSERT INTO category (category_name) VALUES ('가족여행');
--INSERT INTO category (category_name) VALUES ('자유여행');
--INSERT INTO category (category_name) VALUES ('패키지여행');
--
--INSERT INTO traveler (traveler_name, real_name, email, contact, password, registration_date, traveler_authority)
--VALUES
--('admin', 'administrator', 'admin@gmail.com', '010-11-11', '1234', now(), 'ADMIN'),
--('traveler', '아무튼이름', 'abName@naver.com', '010-1241-1112', '4321', now(), 'USER');