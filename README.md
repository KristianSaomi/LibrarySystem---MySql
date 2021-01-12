# LibrarySystem---MySql & noSql MongoDb

/*Kristian Saomi*/
_____________________________
Library system for MySql and noMongo
I'v bouilt a SQL database and connected it on a Java application through JDBC connector. 

The system is compatible to do following things: 
*Insert a author 
*Display authors 
*Create authors
*View specific author and the books they'v made 
*Update author name

*Insert a book and connect to existing author or create unlimited amount of authors through same creation steps. 
-Add additional editions of same creations with different 'Book ids' but same ISBN nummer. 
(Book title, publishing year, grade, amount of authors, amount of editions)
*View specifications on a book 
*Update book titles on existing books 

*Create and view customer list 
*See specifications on a specific customer and what they'v borrowed and what returning status is.
*Update customer information (database my SQL: many things are Uniqe / not so smart to change)

*Borrow a book
*Return a book 

*Deleting options that has to be in following order because the SQL ER-diagram is complex and very much on a N3+
witch makes it a little bit confusing on how to actually delete and on what order. 
1. To delete a book/s on edition level you'l have to make sure all editions of the book are return from customers 
= return from customer (option 1) Once all books is return you can move on to step 2.

2. Now you can actually delete the book complete out of the system. 

3. At this point if you'd like to delete the author for the book, you'll have to make sure the author is not connected to other books, if that's the cause you will not be able to delete the author which is very reasonable. 
Once author don't have any connection to books you can actually delete the author. 
= Go to books, see what books are made from what authors, if the author is connected to anything more do following step 1-2 and then step 3 on deleting function. 

4. To delete a customer after step 1-3 you can now if you want delete a customer, just make sure the current customer isn't borrowing another book, if so you cant delete a customer while hes actually renting a book. 


