select u.user_id, u.username, ur.user_role
from users u
inner join user_roles ur 
on ur.user_role_id = u.user_role_id;

select *
from reimbursements r;

select r.reimb_id, r.reimb_amount, r.reimb_submitted, r.reimb_description, r.reimb_receipt, ur.first_name, ur.last_name, rt.reimb_type, rs.reimb_status 
from reimbursements r 
inner join users ur 
on r.reimb_author = ur.user_id 
inner join reimbursement_type rt 
on r.reimb_type_id = rt.reimb_type_id 
inner join reimbursement_status rs 
on r.reimb_status_id = rs.reimb_status_id; 

DROP VIEW IF EXISTS tickets;

CREATE VIEW tickets AS
	select r.reimb_id, r.reimb_amount, r.reimb_submitted, r.reimb_resolved, r.reimb_description, r.reimb_receipt, ur.user_id, ur.first_name, ur.last_name, ur.user_email,  usr.user_role, r.reimb_resolver, rt.reimb_type, rs.reimb_status 
	from reimbursements r 
	inner join users ur 
	on r.reimb_author = ur.user_id 
	inner join reimbursement_type rt 
	on r.reimb_type_id = rt.reimb_type_id 
	inner join reimbursement_status rs 
	on r.reimb_status_id = rs.reimb_status_id
	inner join user_roles usr
	on ur.user_role_id = usr.user_role_id; 

DROP VIEW IF EXISTS employees;

CREATE VIEW employees AS 
	SELECT ur.user_id, ur.first_name, ur.last_name, ur.user_email, usr.user_role, usr.user_role_id 
	FROM users ur
	INNER JOIN user_roles usr 
	ON ur.user_role_id = usr.user_role_id;

SELECT *
FROM employees e;