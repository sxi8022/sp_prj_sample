create database boarddb;
create user 'guest'@'%' identified by 'board247#';
grant all privileges on boarddb.* to 'guest'@'%';
flush privileges;

use boarddb;
SELECT * FROM comment;


use boarddb;

create table post (
     post_no int unsigned not null primary key auto_increment,
     title varchar(150) not null,
     content text not null,
     author varchar(150) not null,
     post_password varchar(255) not null, 
     create_date datetime not null,
     update_date datetime null,
     hit int unsigned not null default 0
     );

    drop table post;
    
INSERT INTO boarddb.post
(title, content, author, post_password, create_date, hit)
VALUES('테스트1', '테스트중입니다','태훈킴', 'ko2457#',now(), 0);

INSERT INTO boarddb.post
(title, content, author, post_password, create_date, hit)
VALUES('테스트2', '테스트 하면서 보람있었습니다','이지킴', 'ko2457#',now(), 0);
