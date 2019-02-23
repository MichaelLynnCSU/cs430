# Libaray GUI

## Overview
It should first ask for the members id and verify that the member has a valid entry. If the id is not currently in the system, it should ask the questions to add the member. It should have a termination condition here as well.
It should then ask for the book they want to check out. This can be done one of three ways.
- ISBN
- Name - this can be a partial name, if more than one name matches, allow the user to select.
- Author, then choosing from a list of books by that author

If the libraries have the book and there are copies available, the program should print a message telling the member what library and shelf the book is on (there may be more than one).
If either library has the book and all copies are checked out, the program should print a message to the member that all copies are currently checked out.
If neither library has the book, a message telling the member that the library does not currently have the book in stock.
Note: this system is providing info only, do not check the book out as a part of these actions.
Loop back to ask for the next member's id

### UML
https://github.com/MichaelLynnCSU/cs430/blob/master/LibraryUML.pdf

#### Running the Jar
run the below via command line:

without package / move inside lab5 directory
javac lab5.java
java -cp .:/usr/share/java/mysql-connector-java.jar lab5


with package / move outside of lab5 directory
javac lab5/lab5.java
java -cp .:/usr/share/java/mysql-connector-java.jar lab5.lab5

## Dependencies
