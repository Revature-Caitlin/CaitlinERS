insert into reimbursement_status (REIMB_STATUS_ID, REIMB_STATUS)
values
(101, 'Approved'),
(202, 'Pending'),
(303, 'Rejected');

insert into reimbursement_type (REIMB_TYPE_ID, REIMB_TYPE)
values
(10, 'Lodging'),
(20, 'Travel'),
(30, 'Food'),
(40, 'Other');

insert into user_roles (user_role_id, user_role)
values
(100, 'Management'),
(200, 'Finance'),
(300, 'HR'),
(400, 'IT'),
(500, 'Marketing'),
(600, 'Sales'),
(700, 'Quality Assurance');

create extension pgcrypto;

INSERT INTO users (username, user_password, first_name, last_name, user_email, user_role_id)
values
('CatMom1', crypt('ilovemycats!', gen_salt('md5')), 'Angela', 'Martin', 'angela_martin1@dundermifflen.net', 200),
('oscar_m', crypt('04jgi87uy', gen_salt('md5')), 'Oscar', 'Martinez', 'oscar12@dundermifflen.net', 200),
('cookie_monster', crypt('password', gen_salt('md5')), 'Kevin', 'Malone', 'kevin32@dundermifflin.net', 200),
('costa_toby', crypt('costarica2022',gen_salt('md5')), 'Toby', 'Flenderson', 'toby_flenderson@dundermiffin.com', 300),
('it_guy', crypt('P@55word', gen_salt('md5')), 'Nick', 'Glasseu', 'nick11@dundermifflin.net', 400),
('hip_hop', crypt('it4life', gen_salt('md5')), 'Sadiq', 'Vaidya', 'sadiq@dundermifflin.com', 400),
('PrincessKelly', crypt('kellyandryan', gen_salt('md5')), 'Kelly', 'Kapoor', 'cute_kelly@dundermifflin.com', 500),
('BeetFarmer', crypt('battleStarGalactica1', gen_salt('md5')), 'Dwight', 'Schrute', 'dwight_schrute@dundermifflin.net', 600),
('jim_hal', crypt('P@mB33sly', gen_salt('md5')), 'Jim', 'Halpert', 'jim_halpert@dundermifflin.com', 600),
('not_creed', crypt('notapassword', gen_salt('md5')), 'Creed', 'Bratton', 'creed_bratt@dundermifflin.com', 700),
('number1boss', crypt('12345', gen_salt('md5')), 'Michael', 'Scott', 'michael_scott@dundermifflin.net', 100);

select *
from users;



insert into reimbursements  (REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR, REIMB_TYPE_ID)
VALUES
(1007.67, '2022-03-18 13:49:51.873 -0600', 'Sales training bootcamp','https://storage.googleapis.com/reimb-receipt-images/sample_receipt_1.png', 11, 40),
(567.43, '2022-02-17 10:48:51.873 -0600', 'Company Dinner','https://storage.googleapis.com/reimb-receipt-images/sample_receipt_2.png',11, 30),
(113.56, '2022-01-16 09:35:22.873 -0400', 'Car rental','https://storage.googleapis.com/reimb-receipt-images/sample_receipt_1.png',9, 20);

SELECT *
FROM reimbursements;

insert into reimbursements (REIMB_AMOUNT, REIMB_SUBMITTED, reimb_resolved,  REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR, REIMB_RESOLVER, REIMB_TYPE_ID, REIMB_STATUS_ID)
VALUES 
(113.56, '2022-01-16 09:35:22.873 -0400', '2022-01-26 09:35:22.873 -0400', 'Car rental','https://storage.googleapis.com/reimb-receipt-images/sample_receipt_1.png',9, 1, 20, 303);



