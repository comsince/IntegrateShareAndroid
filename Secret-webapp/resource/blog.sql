
--用户表
create table T_USER(
userId varchar(13)  primary key,
alias varchar(16) unique  not null,
password varchar(32) not null,
createTime varchar(20) not null,
imei varchar(15) unique,
status varchar(2) not null 
)DEFAULT CHARSET=utf8;


--内容表
create table T_MATTER(
matterId varchar(13)  primary key,
userId varchar(13) not null,
content varchar(5000) not null,
alias varchar(32) not null,
timestamp varchar(20),
lastretime varchar(20),
type varchar(1) not null ,
recount INT not null,
file varchar(64),
fileType varchar(1),
vote varchar(20)  Default '00000000000000000000'
)DEFAULT CHARSET=utf8;
 
 --评论表
create table T_COMMENT(
	commentId varchar(13)  primary key,
	matterId varchar(13) not null,
	userId varchar(13) not null,
	content varchar(2000) not null,
	rank INT ,
	alias varchar(32),
	timestamp varchar(20)
)DEFAULT CHARSET=utf8;

--配置
CREATE TABLE T_CONFIG (
sequenceId varchar(13) ,
 IKEY VARCHAR(32)  NOT NULL, 
VALUE VARCHAR(1024)  NOT NULL,
DOMAIN VARCHAR(32)  NOT NULL,
description VARCHAR(10) 
)DEFAULT CHARSET=utf8;
 
-- 

create table T_BLACKLIST(
sequenceId INT AUTO_INCREMENT primary key,
account varchar(16)  ,
sourceIp varchar(16) ,
type smallint(1) not null
)DEFAULT CHARSET=utf8;




 --消息表
create table T_MESSAGE(
	messageId INT  primary key,
	receiveId INT not null,
	senderId INT not null,
	content varchar(300) not null,
	alias varchar(32),
	createTime datetime not null,
	status smallint(2) not null 
)DEFAULT CHARSET=utf8;