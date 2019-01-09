
SELECT 
	*
FROM
	Borrowedby bb


SELECT 
	m.last_name, m.first_name, m.member_id, b.title
FROM
	Borrowedby bb natural join  Member m natural join Book b
WHERE
	bb.checkin IS NULL;



SELECT 
	so.copies
FROM
	 Storedon so
WHERE
	 isbn = '96-42103-10109'

UPDATE Storedon 
SET copies = copies - 1 
WHERE 
isbn = '96-42103-10109'

SELECT 
	b.title, a.first_name, a.last_name
FROM 
	Book b, Writtenby w, Author a
WHERE 
	a.first_name = '%red%' AND b.isbn = w.isbn AND w.author = a.author;