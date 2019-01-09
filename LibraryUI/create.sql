CREATE TABLE Author (
    author          INT             NOT NULL,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    PRIMARY KEY (author)
);

CREATE TABLE Member (
    member_id      INT             NOT NULL,
    first_name  VARCHAR(14)        NOT NULL,
    last_name   VARCHAR(16)        NOT NULL,  
    DOB  	 DATE              NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE Publisher (
    pub_id      INT             NOT NULL,
    name  VARCHAR(50)           NOT NULL,
    PRIMARY KEY (pub_id)
);

CREATE TABLE Book (
    isbn       VARCHAR(50)       NOT NULL,
    title      VARCHAR(50)	NOT NULL,
    pub_id     INT             NOT NULL,
    year       DATE              NOT NULL,
    FOREIGN KEY (pub_id)  REFERENCES Publisher (pub_id)  ON DELETE CASCADE,
    PRIMARY KEY (isbn)
 );

CREATE TABLE Writtenby (
    isbn       VARCHAR(50)              NOT NULL,
    author      INT             NOT NULL,
    FOREIGN KEY (isbn)  REFERENCES Book (isbn)  ON DELETE CASCADE,
    FOREIGN KEY (author)  REFERENCES Author (author)  ON DELETE CASCADE, 
    PRIMARY KEY (isbn,author)
 );

CREATE TABLE Borrowedby (
    Borrowed_id  INT             NOT NULL AUTO_INCREMENT,
    member_id    INT             NOT NULL,
    isbn       VARCHAR(50)       NULL,
    checkout   VARCHAR(50)       NULL,
    checkin    VARCHAR(50)	 NULL,
    FOREIGN KEY (isbn)  REFERENCES Book (isbn)  ON DELETE CASCADE,
    FOREIGN KEY (member_id)  REFERENCES Member (member_id)  ON DELETE CASCADE,
    PRIMARY KEY (Borrowed_id)
);

CREATE TABLE Phone (
    pub_num      VARCHAR(50)           NOT NULL,
    type         VARCHAR(50)		NOT NULL,
    PRIMARY KEY (pub_num,type)
);

CREATE TABLE Library (
    name      VARCHAR(50)           NOT NULL,
    street     VARCHAR(50)           NOT NULL,
    city       VARCHAR(50)           NOT NULL,
    state      VARCHAR(50)           NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE Phonetype (
    Author       INT             NOT NULL,
    pub_num      VARCHAR(50)           NOT NULL,
    FOREIGN KEY (Author)  REFERENCES Author (Author)  ON DELETE CASCADE,
    FOREIGN KEY (pub_num)  REFERENCES Phone (pub_num)  ON DELETE CASCADE,
    PRIMARY KEY (Author,pub_num)
);


CREATE TABLE Shelf (
    shelf_id    INT             NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50)           NOT NULL,
    shelf_num   VARCHAR(50)           NOT NULL,
    floor       VARCHAR(50)           NOT NULL,
    FOREIGN KEY (name)  REFERENCES Library  (name)  ON DELETE CASCADE,
    PRIMARY KEY (shelf_id,name)
);

CREATE TABLE Storedon (
    storein_id    INT             NOT NULL AUTO_INCREMENT,
    isbn       VARCHAR(50)       NOT NULL,
    name        VARCHAR(50)           NOT NULL,
    copies        int           NOT NULL,
    FOREIGN KEY (storein_id,name)  REFERENCES Shelf (shelf_id,name)  ON DELETE CASCADE,
    FOREIGN KEY (isbn)  REFERENCES Book (isbn)  ON DELETE CASCADE,
    PRIMARY KEY (storein_id,isbn,name)
 );

CREATE TABLE Triggers (
  trigger_id      INT                   NOT NULL AUTO_INCREMENT,
  action          VARCHAR(50)           NOT NULL,
  exec_time       DATETIME              NOT NULL,
  PRIMARY KEY (trigger_id)	
 );

