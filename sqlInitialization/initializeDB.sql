drop table if exists Orders;
drop table if exists Person, PizzaOrder;

--create the table to hold people and their pws
create table Person(
	id int,

	name varchar(32) constraint NULL_Order_Name not null,
	password varchar(32) constraint NULL_Order_PW not null,

    constraint PK_Person_ID primary key(id)
);

--create the table to have an order -> ties it to a Person
create table PizzaOrder(
    id int,

	pizza varchar(64) constraint NULL_Order_Pizza not null,
	location varchar(64) constraint NULL_Order_Loc not null,
	price numeric constraint NULL_Order_Price not null,

    constraint FK_Order_ID foreign key(id) references Person 
);



--Load some people into the table
insert into Person values
	(0,'Alex', 'Alex Password (hard to guess)'),
	(1, 'Susanna', 'Susanna Password (goodest pw)'),
	(2, 'Bob', 'bad password'),
	(3, 'Unsuspecting Victim 1', 'Vict1 - password'),
	(4, 'Unsuspecting Victim 2', 'Vict2 - password'),
	(5, 'Unsuspecting Victim 3', 'Vict3 - password')
;
--template to add more
--(, 'Unsuspecting Victim {ID-2}', 'Vict{ID-2} - password')


--Load some 'za orders into our tables
insert into PizzaOrder VALUES
	(0, 'pizza', 'location', 12.34),
	(0, 'pizza', 'location', 12.34),
	(0, 'pizza', 'location', 12.34),
	(0, 'pizza', 'location', 12.34),
	(0, 'pizza', 'location', 12.34),
	(1, 'pizza', 'location', 12.34),
	(1, 'pizza', 'location', 12.34),
	(2, 'pizza', 'location', 12.34),
	(2, 'pizza', 'location', 12.34),
	(2, 'pizza', 'location', 12.34),
	(3, 'pizza', 'location', 12.34),
	(3, 'pizza', 'location', 12.34),
	(4, 'pizza', 'location', 12.34),
	(4, 'pizza', 'location', 12.34),
	(4, 'pizza', 'location', 12.34)
;
--template to add more
--(ID{int, foreign on Person}, 'pizza', 'location', 12.34)