DROP TABLE IF EXISTS  Members, Author, Publisher, Book, Phone;	 

CREATE TABLE Member (
    member_id      INT             NOT NULL,
    first_name  VARCHAR(14)        NOT NULL,
    last_name   VARCHAR(16)        NOT NULL,  
    DOB  	 DATE              NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE Author (
    Author_id      INT             NOT NULL,
    first_name  VARCHAR(14)        NOT NULL,
    last_name   VARCHAR(16)        NOT NULL,  
    PRIMARY KEY (Author_id)
);

CREATE TABLE Publisher (
    pub_id      INT             NOT NULL,
    name  VARCHAR(50)           NOT NULL,
    PRIMARY KEY (pub_id)
);

CREATE TABLE Book (

    isbn       VARCHAR(50)              NOT NULL,
    title      VARCHAR(50)	NOT NULL,
    pub_id      INT             NOT NULL,
    year       DATE              NOT NULL,
    Author_id   INT             NOT NULL,
    FOREIGN KEY (pub_id)  REFERENCES Publisher (pub_id)  ON DELETE CASCADE,
    FOREIGN KEY (Author_id )  REFERENCES Author (Author_id )  ON DELETE CASCADE,
    PRIMARY KEY (isbn,Author_id)
 );

CREATE TABLE Writtenby (
    isbn       VARCHAR(50)              NOT NULL,
    Author_id   INT             NOT NULL,
    FOREIGN KEY (isbn)  REFERENCES Book (isbn)  ON DELETE CASCADE,
    PRIMARY KEY (isbn,Author_id)
 );


CREATE TABLE Phone (
    Author_id    INT                   NOT NULL,
    pub_num      VARCHAR(50)           NOT NULL,
    type         VARCHAR(50),
    FOREIGN KEY (Author_id)  REFERENCES Author (Author_id)  ON DELETE CASCADE,
    PRIMARY KEY (pub_num, Author_id)
);

CREATE TABLE Borrowedby (
    member_id    INT             NOT NULL,
    checkin      DATE,
    checkout     DATE,
    FOREIGN KEY (member_id)  REFERENCES Member (member_id)  ON DELETE CASCADE,
    PRIMARY KEY (member_id,isbn)
);



SELECT 
	distinct b.isbn
FROM
	Book b;

SELECT 
	*
FROM
	Author a
ORDER BY 
	a.last_name ASC, a.first_name ASC;

SELECT 
	*
FROM
	Member m
ORDER BY 
	m.last_name ASC, m.first_name ASC;


SELECT 
	*
FROM
	Publisher p
ORDER BY 
	p.name ASC;

SELECT 
	*
FROM
	Phone p
ORDER BY 
	p.pub_num ASC;



SELECT 
	m.first_name, m.last_name
FROM
	Member m
WHERE
	m.last_name like "b%";

SELECT 
	distinct b.title
FROM
	Book b natural join Publisher p
WHERE
	p.name = 'Coyote Publishing';


SELECT 
	distinct a.Author_id, a.first_name, a.last_name, b.title
FROM
	Book b natural join Publisher p  natural join Author a
ORDER BY 
	a.first_name ASC;






