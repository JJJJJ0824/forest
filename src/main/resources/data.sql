INSERT INTO authority (authority_name) VALUES ('ADMIN');
INSERT INTO authority (authority_name) VALUES ('USER');

INSERT INTO category (category_name) VALUES ('공통');
INSERT INTO category (category_name) VALUES ('가족여행');
INSERT INTO category (category_name) VALUES ('자유여행');
INSERT INTO category (category_name) VALUES ('패키지여행');

INSERT INTO course (title, description, content, price, created_at, updated_at, category_name)
VALUES
('올바른 환전 방법', '현지 화폐 준비와 환전 팁', '여행을 떠나기 전에 올바른 환전 방법을 익히세요. 공항에서 환전하는 것보다 은행이나 환전소에서 환전하는 것이 더 유리할 수 있습니다. ATM을 활용해 현지 화폐를 인출할 때 수수료와 환율을 꼭 체크하세요.', 600, now(), null, '공통'),
('공항 근처 비행기 대기시간 활용법', '대기시간을 효율적으로 활용하기', '공항에서 비행기를 기다리는 동안 시간 낭비하지 말고 쇼핑이나 식사, 간단한 마사지를 즐기며 대기 시간을 활용하세요. 편안한 공항 문화 속에서 여유를 느끼는 것이 여행의 첫걸음입니다.', 600, now(), null, '공통'),
('칼리보만의 특별한 공항 문화 미리 알아보기', '칼리보 공항에서의 독특한 문화', '칼리보에서 한국으로 복귀하는 출국날 준비해야하는 칼리보 공항세 , 이런 건 칼리보 국제공항 에서는 유효한 사항으로 꼭 준비하셔야 합니다', 600, now(), null, '공통'),
('출발 전 준비사항', '가족 여행의 시작은 철저한 준비로부터', '가족과 함께 떠나는 보라카이 여행을 위한 출발 전 준비사항은 여권, 비자, 여행 보험 등을 포함하여 가족 구성원의 편안한 여행을 위해 필요한 모든 준비물을 체크해야 합니다. 아이들의 건강과 안전을 위한 준비도 빠짐없이 고려하세요.', 700, now(), null, '가족여행'),
('출발 전 준비사항', '자유로운 여행을 위한 준비 단계', '자유 여행자는 보라카이 여행을 자신만의 스타일로 즐기기 위해 일정을 유연하게 짜는 것이 중요합니다. 숙소 예약, 교통편 계획, 여행자 보험 등 자신에게 맞는 여행 준비를 통해 자유로운 여행을 시작하세요.', 700, now(), null, '자유여행'),
('출발 전 준비사항', '편리한 여행을 위한 완벽한 준비', '패키지 여행은 모든 일정을 미리 준비하고 제공받을 수 있어 편리합니다. 비행기, 숙박, 교통까지 모두 포함된 패키지를 선택하여 미리 준비된 일정대로 안심하고 여행을 즐길 수 있습니다.', 700, now(), null, '패키지여행'),
('현지 교통편 예약 및 선별 방법', '가족 단위 여행에 맞는 교통편 선택', '가족 여행에서는 편안하고 안전한 교통이 필수입니다. 프라이빗 차량을 예약하거나 공항 픽업 서비스를 이용해 이동의 불편함을 최소화하세요. 특히 어린이가 있다면 안전한 교통수단을 선택하는 것이 중요합니다.', 700, now(), null, '가족여행'),
('현지 교통편 예약 및 선별 방법', '자유로운 여행을 위한 교통 선택', '자유 여행자는 다양한 교통 수단을 선택할 수 있습니다. 자전거 렌탈, 오토바이, 트라이시클 등 현지 교통을 활용하여 자유롭게 이동하는 방법을 고민해보세요. 다만, 교통수단 예약 시 미리 준비하여 불편함이 없도록 하는 것이 좋습니다.', 700, now(), null, '자유여행'),
('현지 교통편 예약 및 선별 방법', '패키지 여행에서 제공되는 편리한 교통', '패키지 여행은 대부분 모든 교통편이 포함되어 있어 이동에 대한 걱정을 덜 수 있습니다. 호텔-공항 셔틀, 관광지 이동이 기본으로 제공되므로 별도의 교통편을 고민할 필요 없이 편리하게 여행을 즐길 수 있습니다.', 700, now(), null, '패키지여행'),
('유형별 숙박 예약 및 선별 방법', '가족 단위 여행에 적합한 숙소 선택', '가족 여행에서는 숙소 선택이 매우 중요합니다. 아이들과 함께 편안하게 쉴 수 있는 가족 친화적인 리조트나 풀장이 있는 숙소를 선택하는 것이 좋습니다. 다양한 편의 시설을 갖춘 숙소에서 편안한 여행을 즐기세요.', 700, now(), null, '가족여행'),
('유형별 숙박 예약 및 선별 방법', '나만의 스타일에 맞는 숙소 찾기', '자유 여행자는 숙소를 본인의 취향과 일정에 맞춰 선택할 수 있습니다. 부티크 호텔, 게스트하우스, 에어비앤비 등 다양한 숙소 옵션을 고려해 자유롭게 여행을 즐기세요. 위치, 가격, 분위기를 잘 맞추는 것이 중요합니다.', 700, now(), null, '자유여행'),
('유형별 숙박 예약 및 선별 방법', '편안한 숙소와 함께하는 패키지 여행', '패키지 여행에서는 숙박이 미리 예약되어 있으며, 여행 일정에 맞춰 편리한 숙소에서 묵게 됩니다. 고급 리조트에서의 숙박을 통해 여행 중에도 편안하게 휴식을 취할 수 있습니다.', 700, now(), null, '패키지여행'),
('유형별 액티비티 추천', '가족이 함께 즐길 수 있는 액티비티', '가족 여행에서는 모두가 즐길 수 있는 액티비티가 중요합니다. 가족 구성원 모두가 즐길수 있으며 안전을 추구하고 기념이 되는 여행계획', 700, now(), null, '가족여행'),
('유형별 액티비티 추천', '자유로운 여행을 위한 액티비티', '자유 여행자는 자신만의 방식으로 액티비티를 즐길 수 있습니다. 스노클링, 패러세일링, 스쿠버 다이빙 등 액티브한 활동부터 편안한 해변에서의 여유까지 다양하게 선택할 수 있습니다.', 700, now(), null, '자유여행'),
('유형별 액티비티 추천','패키지 여행에서 제공하는 액티비티', '패키지 여행은 이미 계획된 일정에 맞춰 다양한 액티비티를 즐길 수 있습니다. 유명 관광지 투어나 해양 스포츠 체험 등을 통해 여행 중에도 알차게 즐길 수 있습니다.', 700, now(), null, '패키지여행'),
('유형별 관광명소 및 코스 루트 추천', '가족과 함께 즐길 수 있는 보라카이 명소', '가족 여행자는 해변에서의 여유로운 시간과 함께 아이들이 즐길 수 있는 보라카이의 명소를 중심으로 관광 계획을 세우는 것이 좋습니다. 스테이션 1-2-3, 보라카이 동물농장 등 다양한 활동을 즐기세요.', 700, now(), null, '가족여행'),
('유형별 관광명소 및 코스 루트 추천', '자유 여행자를 위한 관광 코스', '자유 여행자는 본인의 취향에 맞춰 관광 코스를 선택할 수 있습니다. 보라카이의 핫한 해변과 숨겨진 명소를 탐험하며 자신만의 여행을 완성해 보세요. 섬 투어나 자연 탐방도 추천합니다.', 700, now(), null, '자유여행'),
('유형별 관광명소 및 코스 루트 추천', '패키지 여행에서의 완벽한 관광 루트', '패키지 여행에서는 일정이 미리 정해진 관광 루트를 따라 주요 명소를 방문하게 됩니다. 화이트 비치, 보라카이 전통 마을, 스카이가든 등 인기 명소를 빠짐없이 둘러볼 수 있습니다.', 700, now(), null, '패키지여행');

INSERT INTO traveler (traveler_name, real_name, email, contact, password, created_at, traveler_authority)
VALUES
('admin', 'administrator', 'admin@gmail.com', '010-0000-0000', '$2b$12$A0kgVpplgbH3ZZ1E89441eacUXljTTt7nP8I3RdLtW0P6/CXdEnCm', '2020-10-10', 'ADMIN'),
('traveler', '아무튼이름', 'abName@naver.com', '010-1111-1111', '$2a$10$AbLNyFNNFBekwJMX.833/ugdQLaB4Tv3DHGsGyQip1bJJrAFm9ufC', '2023-12-25', 'USER'),
('traveler2', '아무튼이름2', 'abName2@naver.com', '010-2222-2222', '$2a$10$AbLNyFNNFBekwJMX.833/ugdQLaB4Tv3DHGsGyQip1bJJrAFm9ufC', '2022-10-11', 'USER'),
('traveler3', '아무튼이름3', 'abName3@naver.com', '010-3333-3333', '$2a$10$AbLNyFNNFBekwJMX.833/ugdQLaB4Tv3DHGsGyQip1bJJrAFm9ufC', '2023-11-12', 'USER');

INSERT INTO checklist (traveler_name, direction, response, is_checked, category_name)
VALUES
('traveler', '보라카이에 가는 주된 목적은 무엇인가요?', '휴식과 여유', true, '자유여행'),
('traveler', '보라카이에서 선호하는 숙소 유형은 무엇인가요?', '럭셔리 리조트', true, '자유여행'),
('traveler', '보라카이 여행을 계획하면서 어떤 스타일을 선호하시나요?', '완전 자유 여행', true, '자유여행');

INSERT INTO point (traveler_name, action_type, points, event_date)
VALUES
('admin', 'test', 9999999999999, now());

INSERT INTO q (traveler_name, title, content, created_at)
VALUES
('traveler', '점심 뭐 드셨나요', '저는 순대국밥이요', now());

INSERT INTO a (traveler_name, title, content, created_at, q_id)
VALUES
('admin', '제 점심은요', '저는 컵라면 먹었습니다 ㅎㅎ 맛있더라구요', now(), 1);

INSERT INTO notice (traveler_name, title, content, created_at)
VALUES
('admin', '중요 공지', '오늘 오후 8시부터 오후 8시 30분까지 약 30분간 사이트를 점검합니다. 이용에 불편을 드려 죄송합니다.', '2024-11-20');

INSERT INTO cart (course_id, traveler_name, purchase_status)
VALUES (1, 'traveler', false), (2, 'traveler', false), (3, 'traveler', false);

INSERT INTO completion (traveler_name, course_id, completion_date)
VALUES ('traveler', 1, '2024-12-11'), ('traveler', 2, null), ('traveler', 3, null);