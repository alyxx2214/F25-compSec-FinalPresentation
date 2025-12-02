--search for un/pw
select name 
from Person
where username = 'alyxx' and passwd = 'Alex Password (hard to guess)';
--'alyxx' and 'Alex Password (hard to guess)' need to be replaced with user input

--show a person's previous orders
select location, pizza, price
from PizzaOrder, Person
where PizzaOrder.id = Person.id and Person.username = 'alyxx' and Person.passwd = 'Alex Password (hard to guess)';
--'alyxx' and 'Alex Password (hard to guess)' need to be replaced with user input


/*
Replace using fstring
*/

/*
//--search for un/pw
String.format("select name " + 
    "from Person " + 
    "where username = '%s' and passwd = '%s';", usernameInput, passwordInput);

//--show a person's previous orders
String.format("select location, pizza, price " +
    "from PizzaOrder, Person " + 
    "where PizzaOrder.id = Person.id and Person.username = '%s' and Person.passwd = '%s';", usernameInput, passwordInput);
*/