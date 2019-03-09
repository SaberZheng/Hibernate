
drop table if exists t_student ;

drop table if exists t_class ;

create table t_class (
	id int(5) primary key ,
	name varchar(150)
) ;

create table t_student (
	id int(10) primary key ,
	name varchar(150) ,
	class_id int(5) ,
	foreign key ( class_id ) references t_class( id )
) ;

insert into t_class ( id , name ) values ( 1 , '计算机一班' );
insert into t_class ( id , name ) values ( 2 , '计算机二班' );
insert into t_class ( id , name ) values ( 3 , '网络一班' );
insert into t_class ( id , name ) values ( 4 , '网络二班' );

insert into t_student ( id , name , class_id ) values ( 1 , '张翠山' , 2);
insert into t_student ( id , name , class_id ) values ( 2 , '曾阿牛' , 2 );
insert into t_student ( id , name , class_id ) values ( 3 , '赵敏' , 1 );
insert into t_student ( id , name , class_id ) values ( 4 , '小昭'  , 3 );

