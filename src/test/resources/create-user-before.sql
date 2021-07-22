delete
from user_role;
delete
from usr;

insert into usr(id, active, password, username)
values (1, true, '$2a$08$fFDE4yB8gq35QGS18t0LCOQhtY8eQH/PBEGD2Anb47EwDUH8C6UUm', 'admin'),
       (2, true, '$2a$08$U0xL80QljXKO5gJC29Fq6OU8O7JcK2iA52S90kmBdm/e1f2ehEgiK', 'q');

insert into user_role(user_id, roles)
values (1, 'USER'),
       (1, 'ADMIN'),
       (2, 'USER');