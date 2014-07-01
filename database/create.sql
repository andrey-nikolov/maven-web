use MAVEN_WEB;

create table USER (
   ID int not null auto_increment,
   NAME varchar(32) not null,
   GENDER varchar(8) not null,
   AGE int,
   PASSWORD varchar(32),
   primary key (ID)
);
