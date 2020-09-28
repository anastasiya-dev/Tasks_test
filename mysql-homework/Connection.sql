select * from sql_homework.expenses;
insert into sql_homework.receivers (num, name) values (1, 'Solo');
select * from sql_homework.receivers;
insert into sql_homework.receivers (num, name) values (2, 'Korona');
insert into sql_homework.receivers (num, name) values (3, 'MTS');
select * from sql_homework.receivers;
select * from sql_homework.expenses where value>=20000;
select * from sql_homework.expenses where receiver=3;
select * from sql_homework.expenses order by value;
select paydate, value, name from expenses, receivers where receiver=receivers.num;
select expenses.num, paydate, name, value from expenses, receivers where receiver=receivers.num and value>10000;
delete from expenses where value<2000;
select * from expenses;
select paydate, value, name from expenses, receivers where receiver=receivers.num;
select name, sum(value) from expenses, receivers rs where receiver=rs.num group by name;
select sum(value) from expenses where paydate = '2011-05-10';

//сумма платежей за тот день, когда был наибольший платеж (работает в workbench, здесь ошибка)
select sum(value) from expenses where paydate=(select paydate from expenses where value=(select max(value) from expenses));
//наибольший платеж за тот день, когда была наибольшая сумма платежей (работает в workbench, здесь ошибка)
select sum(value) from expenses where paydate=(select paydate from expenses where value=(select max(value) from expenses));

