create table user
(
    user_id int auto_increment primary key not null,
    name varchar(30) not null,
    password varchar(30) not null

);

insert into user(name,password) values ("tom","123");