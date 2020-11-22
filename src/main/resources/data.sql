insert into authors (author_id, first_name, last_name) values(1,'William', 'Shakespeare');
insert into authors (author_id, first_name, last_name) values(2,'Agatha', 'Christie');
insert into authors (author_id, first_name, last_name) values(3,'K.Sam', 'Shanmugan');
insert into authors (author_id, first_name, last_name) values(4,'A.M.', 'Breipohl');

insert into books(book_id, title) values(1, 'The Tragical History of Hamlet');
insert into books(book_id, title) values(2, 'The Tempest');
insert into books(book_id, title) values(3, 'The Taming of the Shrew');

insert into books(book_id, title) values(4, 'The Mysterious Affair at Styles');
insert into books(book_id, title) values(5, 'Murder on the Orient Express');
insert into books(book_id, title) values(6, 'The Murder of Roger Ackroyd');

insert into books(book_id, title) values(7, 'Random Signals Detection,Estimation, and Data Analysis');

insert into book_author(book_id,author_id) values(1,1);
insert into book_author(book_id,author_id) values(2,1);
insert into book_author(book_id,author_id) values(3,1);
insert into book_author(book_id,author_id) values(4,2);
insert into book_author(book_id,author_id) values(5,2);
insert into book_author(book_id,author_id) values(6,2);
insert into book_author(book_id,author_id) values(7,3);
insert into book_author(book_id,author_id) values(7,4);