DROP TABLE IF EXISTS reimbursements cascade;

DROP TABLE IF EXISTS reimbursement_status;
DROP TABLE IF EXISTS reimbursement_type;
DROP TABLE IF EXISTS user_roles cascade;
drop table if EXISTS users;




CREATE TABLE reimbursement_status (
	REIMB_STATUS_ID INTEGER PRIMARY KEY,
	REIMB_STATUS VARCHAR(10) not null,


	CHECK (REIMB_STATUS IN ('Pending', 'Approved', 'Rejected'))
);

CREATE TABLE reimbursement_type (
	REIMB_TYPE_ID INTEGER PRIMARY KEY,
	REIMB_TYPE VARCHAR(10) not NULL,
	
	CHECK (REIMB_TYPE IN('Lodging', 'Travel', 'Food', 'Other'))

);

CREATE TABLE user_roles (
	USER_ROLE_ID INTEGER PRIMARY KEY,
	USER_ROLE VARCHAR(50) not NULL,
	
	CHECK (USER_ROLE IN('Finance', 'HR', 'IT', 'Marketing', 'Sales', 'Management', 'Quality Assurance'))
);

create TABLE users (
	USER_ID SERIAL primary KEY,
	USERNAME VARCHAR(50) not NULL,
	USER_PASSWORD VARCHAR(50) not null,
	FIRST_NAME VARCHAR(100) not null,
	LAST_NAME VARCHAR(100) not null,
	USER_EMAIL VARCHAR(100) not null,
	USER_ROLE_ID INTEGER not null,
	UNIQUE(USERNAME, USER_EMAIL),
	
	constraint fk_USER_ROLE_ID foreign key(USER_ROLE_ID) REFERENCES user_roles(USER_ROLE_ID) on delete cascade
	
);


CREATE TABLE reimbursements (
	REIMB_ID SERIAL PRIMARY KEY,
	REIMB_AMOUNT numeric(12,2) NOT NULL CHECK (REIMB_AMOUNT > 0),
	REIMB_SUBMITTED TIMESTAMPTZ NOT NULL,
	REIMB_RESOLVED TIMESTAMPTZ,
	REIMB_DESCRIPTION VARCHAR(250),
	REIMB_RECEIPT text NOT NULL,
	REIMB_AUTHOR INTEGER NOT NULL,
	REIMB_RESOLVER INTEGER CHECK (REIMB_RESOLVER < 300),
	REIMB_STATUS_ID INTEGER default 202,
	REIMB_TYPE_ID INTEGER NOT NULL,
	
	CONSTRAINT fk_REIMB_AUTHOR foreign key(REIMB_AUTHOR) references users(USER_ID) on delete CASCADE,
	constraint fk_REIMB_RESOLVER foreign key(REIMB_RESOLVER) references users(USER_ID) on delete cascade,
	CONSTRAINT fk_REIMB_STATUS foreign key(REIMB_STATUS_ID) references reimbursement_status(REIMB_STATUS_ID) on delete cascade,
	CONSTRAINT fk_REIMB_TYPE_ID foreign key(REIMB_TYPE_ID) references reimbursement_type(REIMB_TYPE_ID) on delete cascade

);

SET timezone TO 'EST';

SELECT NOW();
