package lab5;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


public class lab5 {
	
    Connection con = null;
    Statement stmt;
    ResultSet rs;
    ArrayList<String> al;
    int copiesget;
	String isbnget;
		String nameget;
		String shelfget;
		String bookget;
		String title;
		String fname;
		String lname;
    
    
	public void readXML(String fileName)
	{
		try {
			File file = new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Borrowed_by");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = (Node) nodeLst.item(s);

				if (((org.w3c.dom.Node) fstNode).getNodeType() == Node.ELEMENT_NODE) {

					Element sectionNode = (Element) fstNode;
				      // Get a Statement object
				     stmt = con.createStatement();
				     
					NodeList memberIdElementList = sectionNode.getElementsByTagName("MemberID");
					Element memberIdElmnt = (Element) memberIdElementList.item(0);
					NodeList memberIdNodeList = memberIdElmnt.getChildNodes();
					System.out.println("MemberID : "  + ((org.w3c.dom.Node) memberIdNodeList.item(0)).getNodeValue().trim());
					String memberID = ((org.w3c.dom.Node) memberIdNodeList.item(0)).getNodeValue().trim();
					
					NodeList secnoElementList = sectionNode.getElementsByTagName("ISBN");
					Element secnoElmnt = (Element) secnoElementList.item(0);
					NodeList secno = secnoElmnt.getChildNodes();
					System.out.println("ISBN : "  + ((org.w3c.dom.Node) secno.item(0)).getNodeValue().trim());
					String ISBN = ((org.w3c.dom.Node) secno.item(0)).getNodeValue().trim();
					String sqlexist = "SELECT isbn "
							+ "FROM Book"
							+ " WHERE isbn = '" + ISBN + "'";
					//System.out.println(sqlexist);
			 		
					rs = stmt.executeQuery(sqlexist);
					if(rs.next() == false)
					{
						
						System.out.println("Error ISBN doesn't exist: " + ISBN);
						System.out.println("a foreign key constraint fails");
						System.out.println("");
						// continue; key word would start the next iteration upon invocation
						continue;
					}
					else
					{
				 		System.out.println("ISBN found: " + rs.getString(1));	
					}

			 
			 	    
			 		
					

					NodeList codateElementList = sectionNode.getElementsByTagName("Checkout_date");
					Element codElmnt = (Element) codateElementList.item(0);
					NodeList cod = codElmnt.getChildNodes();
					System.out.println("Checkout_date : "  + ((org.w3c.dom.Node) cod.item(0)).getNodeValue().trim());
					String checkout = ((org.w3c.dom.Node) cod.item(0)).getNodeValue().trim();
					
			   	     	
					if(!checkout.equals("N/A")){
				  	
					try{
						
						String checkcopy = "SELECT copies "
								+ "FROM Storedon "
								+ "WHERE isbn = '" + ISBN + "'";
				 		System.out.println(checkcopy);
				 		rs = stmt.executeQuery(checkcopy);
				 		rs.next();
				 		int copies = Integer.parseInt(rs.getString(1));
				 		System.out.println("copies: " + copies);	
							
						String sqlinsert = "INSERT INTO `Borrowedby`(member_id,isbn,checkout,checkin)"
									+ "VALUES(" + memberID + ",'" + ISBN + "','" + checkout + "',NULL)";
						
						 int rows = 0;
							System.out.println(sqlinsert);
					 		if(copies >= 1)
					 		{
					 		rows = stmt.executeUpdate(sqlinsert);
					 		String usecopies = "UPDATE Storedon SET copies = copies - 1 WHERE isbn = '" + ISBN + "'";
							System.out.println(usecopies);	
						    int copes = stmt.executeUpdate(usecopies);
					     	System.out.println(copes + " copies left....");
					 		}
					 		else{
					 			System.out.println(rows + " no copies avalible....");
					 			rows = 0;
					 		}
					 		if(rows > 0){
					 		System.out.println(rows + " Rows Updated Successfully....");
					 		}
					 		else{
					 			System.out.println(rows + " Rows Updated Successfully....");
					 			System.out.println("Error Insert Failed.....");
					 		}
							
					       }catch(Exception e){
						         System.out.print(e);
						   }//end catch		
									
				}
					
										
					NodeList cidateElementList = sectionNode.getElementsByTagName("Checkin_date");
					Element cidElmnt = (Element) cidateElementList.item(0);
					NodeList cid = cidElmnt.getChildNodes();
					System.out.println("Checkin_date : "  + ((org.w3c.dom.Node) cid.item(0)).getNodeValue().trim());
					String checkin = ((org.w3c.dom.Node) cid.item(0)).getNodeValue().trim();
					     	
			     	
					if(!checkin.equals("N/A")){
					 	try{
					 		
//					 		String copies = "UPDATE Storedon SET copies = copies + 1 WHERE isbn = '" + ISBN + "'";
//							System.out.println(copies);	
//							int copes = stmt.executeUpdate(copies);
//					     	System.out.println(copes + " copies Updated Successfully....");
						
					 		String sql = "UPDATE Borrowedby"
					 				+ " SET checkin = '" + checkin + 
					 				"' WHERE member_id = " + memberID + " AND isbn = '" + ISBN + "' AND checkin IS NULL";
					 		System.out.println(sql);							
					 		int rows = stmt.executeUpdate(sql);
					 		if(rows > 0){
					 		System.out.println(rows + " Rows Updated Successfully....");
					 		}
					 		else{
					 			System.out.println(rows + " Rows Updated Successfully....");
					 			System.out.println("Error no corresponding checkout record.....");
					 		}

					       }catch(Exception e){
					         System.out.print(e);
					       }//end catch		     
					}

					System.out.println();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
  public static void main(String args[]){

	lab5 showXML = new lab5();

    try {


      // Register the JDBC driver for MySQL.
      Class.forName("com.mysql.jdbc.Driver");

      // Define URL of database server for
      // database named 'user' on the faure.
      String url =
            "jdbc:mysql://faure/mikelynn";

      // Get a connection to the database for a
      // user named 'user' with the password
      // 123456789.
      showXML.con = DriverManager.getConnection(
                        url,"mikelynn", "830204831");

      // Display URL and connection information
      System.out.println("URL: " + url);
      System.out.println("Connection: " + showXML.con);


  	try {

  		showXML.readXML ("/s/bach/a/class/cs430dl/Current/more_assignments/LabData/Libdata.xml");
  		
  		/// menu
  		Boolean menuloop = true;
  		
  		while(menuloop) {
  		int memberid = 0;
  		Scanner input ;
  		System.out.println();  
	  		while(true) {
		  		try {
		  	  	System.out.println("Enter Memeber ID:");
		  	    input = new Scanner(System.in);
		  	    memberid = Integer.parseInt(input.next());
		  		break;
		  		}
		  		catch(NumberFormatException ignore)
		  		{
		  			System.out.println("bad id format try again");
		  		}
	  		}
  		
  		System.out.println("member id is: " + memberid);  
  		
  		String sqlexist = "SELECT member_id "
				+ "FROM Member"
				+ " WHERE member_id = '" + memberid + "'";
  		showXML.stmt = showXML.con.createStatement();
  		showXML.rs = showXML.stmt.executeQuery(sqlexist);
  			if(showXML.rs.next() == false)
			{
				// no member found
				System.out.println("member doesn't exist: " + memberid);
				System.out.println("");
				System.out.println("1: Add Memeber");
				System.out.println("2: Exit");
				Scanner input2 = new Scanner(System.in);
		  		int selection = input.nextInt();
		  		
		  		// add member
			  		if(selection == 1){
			  			System.out.println("Adding new memeber: " + memberid);
			  			System.out.println("enter first name: ");
			  			Scanner fn = new Scanner(System.in);
				  		String first_name = fn.next();
			  			System.out.println("enter last name: ");
						Scanner ln = new Scanner(System.in);
						String last_name = ln.next();
			  			System.out.println("enter dob format 0-0-0000: ");
						Scanner db = new Scanner(System.in);
						String dob = db.next();
			  			String sqlinsert = "INSERT INTO `Member`(member_id,first_name,last_name,dob)"
								+ "VALUES(" + memberid + ",'" + first_name + "','" + last_name + "', STR_TO_DATE('" + dob + "', '%m-%d-%Y'))";
						int rows = 0;
						rows = showXML.stmt.executeUpdate(sqlinsert);
						if(rows == 1){
							// new member pick a book
							System.out.println("added member," + memberid + " pick a number from the menu: ");
							System.out.println("1: ISBN");
							System.out.println("2: name");
							System.out.println("3: author");
							Scanner booksel = new Scanner(System.in);
					  		int bs = booksel.nextInt();
							if(bs == 1) {
								System.out.println("enter isbn:");
								Scanner isbnsel = new Scanner(System.in);
						  		String isbnname = isbnsel.next();
						  		String checkcopy = "SELECT so.copies, so.isbn, so.name, s.shelf_num "
										+ "FROM Storedon so, Shelf s "
										+ "WHERE so.isbn = '" + isbnname + "' AND so.storein_id = s.shelf_id";
						  	//	System.out.println(checkcopy);
						  		showXML.rs = showXML.stmt.executeQuery(checkcopy);
						  		int count = 0;
							  		while(showXML.rs.next()) {
							 		int copiesget = Integer.parseInt(showXML.rs.getString(1));
							 		String isbnget = showXML.rs.getString(2);
							 		String nameget = showXML.rs.getString(3);
							 		String shelfget = showXML.rs.getString(4);						 		
								 		if(copiesget >= 1) {
								 		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
								 		System.out.println(" Library: " + nameget + " Shelf: " + shelfget);
								 		}
								 		else {
								 			System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
								 			System.out.println(" Library: " + nameget + " Shelf: " + shelfget + " no copies available");
								 			count = count + 1;
								 		}
								 			if(count == 2) {
								 				System.out.println("neither library seems to have your book");
								 			}
							  		}
						  		
							}
							else if(bs == 2)  {
							    ResultSet rs2;
								System.out.println("enter name: ");
								Scanner book = new Scanner(System.in);
						  		String bookname2 = book.nextLine();
						  		String checkcopy2 = "SELECT so.copies, so.isbn, so.name, s.shelf_num, b.title "
										+ "FROM Storedon so, Shelf s, Book b "
										+ "WHERE b.title LIKE '%" + bookname2 + "%' AND so.storein_id = s.shelf_id AND b.isbn = so.isbn";
						  		//System.out.println(checkcopy2);
						  		rs2 = showXML.stmt.executeQuery(checkcopy2);
						  		int count = 0;
						  		showXML.al = new ArrayList<String>();
						  		while(rs2.next()) {
						  			showXML.copiesget = Integer.parseInt(rs2.getString(1));
						 		showXML.isbnget = rs2.getString(2);
						 		showXML.nameget = rs2.getString(3);
						 		showXML.shelfget = rs2.getString(4);	
						 		showXML.bookget = rs2.getString(5);
						 		showXML.al.add(showXML.bookget);
						 	//	System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
						 	//	System.out.println(" Searching Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget);		 		
						  		}
													  		if(showXML.al.size() > 1) {
														  		Iterator it = showXML.al.iterator();
														  		int num = 1;
														  		int temp = 0;
														        System.out.println("Choose number with title: ");
															        while (it.hasNext()) {
															            System.out.println(num + ": " + it.next());
															            num +=1;
															        }
															        Scanner book3 = new Scanner(System.in);
															  	    temp = book3.nextInt();
															  	  ResultSet rs3;
															  	    String checkcopy3 = "SELECT so.copies, so.isbn, so.name, s.shelf_num, b.title "
																			+ "FROM Storedon so, Shelf s, Book b "
																			+ "WHERE b.title = '" + showXML.al.get(temp-1) + "'  AND so.storein_id = s.shelf_id AND b.isbn = so.isbn";
															  		//System.out.println(checkcopy3);
															  		rs3 = showXML.stmt.executeQuery(checkcopy3);
															  		int count2 = 0;
															  		while(rs3.next()) {
															 		int copiesget = Integer.parseInt(rs3.getString(1));
															 		String isbnget = rs3.getString(2);
															 		String nameget = rs3.getString(3);
															 		String shelfget = rs3.getString(4);	
															 		String bookget = rs3.getString(5);		
															 		if(copiesget >= 1) {
															 //		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
															 		System.out.println(" Library: " + nameget + ", Shelf: " + shelfget);
															 		}
															 		else {
															 	//		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
															 			System.out.println(" Library: " + nameget + ", Shelf: " + shelfget + ", no copies available");
															 			count2 = count2 + 1;
															 		}
															 			if(count2 == 2) {
															 				System.out.println("neither library seems to have your book");
															 			}
														  	    
													  		}
														}
										  		else {
											 		if(showXML.copiesget >= 1) {
											 	//	System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
											 		System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget);
											 		}
											 		else {
											 	//		System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
											 			System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget + ", no copies available");
											 			count = count + 1;
											 		}
											 			if(count == 2) {
											 				System.out.println("neither library seems to have your book");
											 		}
										  		}
							  		}
							else
							{
								System.out.println("enter author firstname:");
								    ResultSet rs2;
									Scanner book = new Scanner(System.in);
							  		String authorfirstname = book.nextLine();
							  		System.out.println("enter author lastname:");
							  		Scanner book2 = new Scanner(System.in);
							  		String authorlastname = book2.nextLine();
							  		String checkcopy2 = "SELECT b.title, a.first_name, a.last_name "
											+ "FROM Book b, Writtenby w, Author a "
											+ "WHERE a.first_name LIKE '%" + authorfirstname + "%' AND b.isbn = w.isbn AND w.author = a.author";
							  	//	System.out.println(checkcopy2);
							  		rs2 = showXML.stmt.executeQuery(checkcopy2);
							  		int count = 0;
							  		showXML.al = new ArrayList<String>();
							  		while(rs2.next()) {
							  		showXML.title = rs2.getString(1);
							 		showXML.fname = rs2.getString(2);
							 		showXML.lname = rs2.getString(3);
							 		showXML.al.add(showXML.title);
							 //		System.out.println("title: " + showXML.title + " fisrt name: " + showXML.fname + " last name: " + showXML.lname);	 		
							  		}
														  		if(showXML.al.size() > 1) {
															  		Iterator it = showXML.al.iterator();
															  		int num = 1;
															  		int temp = 0;
															        System.out.println("Choose a title: ");
																        while (it.hasNext()) {
																            System.out.println(num + ": " + it.next());
																            num +=1;
																        }
																        Scanner book3 = new Scanner(System.in);
																  	    temp = book3.nextInt();
																  	  ResultSet rs3;
																  	    String checkcopy3 = "SELECT so.copies, so.isbn, so.name, s.shelf_num, b.title "
																				+ "FROM Storedon so, Shelf s, Book b "
																				+ "WHERE b.title = '" + showXML.al.get(temp-1) + "'  AND so.storein_id = s.shelf_id AND b.isbn = so.isbn";
																  	//	System.out.println(checkcopy3);
																  		rs3 = showXML.stmt.executeQuery(checkcopy3);
																  		int count2 = 0;
																  		showXML.al = new ArrayList<String>();
																  		while(rs3.next()) {
																 		int copiesget = Integer.parseInt(rs3.getString(1));
																 		String isbnget = rs3.getString(2);
																 		String nameget = rs3.getString(3);
																 		String shelfget = rs3.getString(4);	
																 		String bookget = rs3.getString(5);		
																 		if(copiesget >= 1) {
															//	 		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
																 		System.out.println(" Library: " + nameget + ", Shelf: " + shelfget);
																 		}
																 		else {
															//	 			System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
																 			System.out.println(" Library: " + nameget + ", Shelf: " + shelfget + ", no copies available");
																 			count2 = count2 + 1;
																 		}
																 			if(count2 == 2) {
																 				System.out.println("neither library seems to have your book");
																 			}
															  	    
														  		}
															}
														  		else {
															 		if(showXML.copiesget >= 1) {
															 	//	System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
															 		System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget);
															 		}
															 		else {
															 	//		System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
															 			System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget + ", no copies available");
															 			count = count + 1;
															 		}
															 			if(count == 2) {
															 				System.out.println("neither library seems to have your book");
															 		}
														  		}
							
								}
													
							}
						else {System.out.println("failed to add new member:");	}	
			  		}
			  		else // exit
			  		{
			 		System.out.println("exiting: ");	
			 		menuloop = false;
					System.out.println("");
			  		}
	
	 
			}
  			else { // existing member
  				
  				System.out.println("member found: " + showXML.rs.getString(1));
  				
  			// existing member pick a book
				System.out.println("existing member, " + memberid + " choose number from below: ");
				System.out.println("1 ISBN:");
				System.out.println("2 name:");
				System.out.println("3 author:");
				Scanner booksel = new Scanner(System.in);
		  		int bs = booksel.nextInt();
				if(bs == 1) {
					System.out.println("enter isbn:");
					Scanner isbnsel = new Scanner(System.in);
			  		String isbnname = isbnsel.next();
			  		String checkcopy = "SELECT so.copies, so.isbn, so.name, s.shelf_num "
							+ "FROM Storedon so, Shelf s "
							+ "WHERE so.isbn = '" + isbnname + "' AND so.storein_id = s.shelf_id";
			  	//	System.out.println(checkcopy);
			  		showXML.rs = showXML.stmt.executeQuery(checkcopy);
			  		int count = 0;
				  		while(showXML.rs.next()) {
				 		int copiesget = Integer.parseInt(showXML.rs.getString(1));
				 		String isbnget = showXML.rs.getString(2);
				 		String nameget = showXML.rs.getString(3);
				 		String shelfget = showXML.rs.getString(4);						 		
					 		if(copiesget >= 1) {
					 	//	System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
					 		System.out.println(" Library: " + nameget + ", Shelf: " + shelfget);
					 		}
					 		else {
					 	//		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
					 			System.out.println(" Library: " + nameget + ", Shelf: " + shelfget + ", no copies available");
					 			count = count + 1;
					 		}
					 			if(count == 2) {
					 				System.out.println("neither library seems to have your book in stock");
					 			}
				  		}
			  		
				}
				else if(bs == 2) {
				    ResultSet rs2;
					System.out.println("enter name: ");
					Scanner book = new Scanner(System.in);
			  		String bookname2 = book.nextLine();
			  		String checkcopy2 = "SELECT so.copies, so.isbn, so.name, s.shelf_num, b.title "
							+ "FROM Storedon so, Shelf s, Book b "
							+ "WHERE b.title LIKE '%" + bookname2 + "%' AND so.storein_id = s.shelf_id AND b.isbn = so.isbn";
			  		//System.out.println(checkcopy2);
			  		rs2 = showXML.stmt.executeQuery(checkcopy2);
			  		int count = 0;
			  		showXML.al = new ArrayList<String>();
			  		while(rs2.next()) {
			  			showXML.copiesget = Integer.parseInt(rs2.getString(1));
			 		showXML.isbnget = rs2.getString(2);
			 		showXML.nameget = rs2.getString(3);
			 		showXML.shelfget = rs2.getString(4);	
			 		showXML.bookget = rs2.getString(5);
			 		showXML.al.add(showXML.bookget);
			 		//System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
			 		//System.out.println(" Searching Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget);		 		
			  		}
										  		if(showXML.al.size() > 1) {
											  		Iterator it = showXML.al.iterator();
											  		int num = 1;
											  		int temp = 0;
											        System.out.println("Choose a number with title: ");
												        while (it.hasNext()) {
												            System.out.println(num + ": " + it.next());
												            num +=1;
												        }
												        Scanner book3 = new Scanner(System.in);
												  	    temp = book3.nextInt();
												  	  ResultSet rs3;
												  	    String checkcopy3 = "SELECT so.copies, so.isbn, so.name, s.shelf_num, b.title "
																+ "FROM Storedon so, Shelf s, Book b "
																+ "WHERE b.title = '" + showXML.al.get(temp-1) + "'  AND so.storein_id = s.shelf_id AND b.isbn = so.isbn";
												  //		System.out.println(checkcopy3);
												  		rs3 = showXML.stmt.executeQuery(checkcopy3);
												  		int count2 = 0;
												  		while(rs3.next()) {
												 		int copiesget = Integer.parseInt(rs3.getString(1));
												 		String isbnget = rs3.getString(2);
												 		String nameget = rs3.getString(3);
												 		String shelfget = rs3.getString(4);	
												 		String bookget = rs3.getString(5);		
												 		if(copiesget >= 1) {
												 //		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
												 		System.out.println(" Library: " + nameget + ", Shelf: " + shelfget);
												 		}
												 		else {
												 	//		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
												 			System.out.println(" Library: " + nameget + ", Shelf: " + shelfget + ", no copies available");
												 			count2 = count2 + 1;
												 		}
												 			if(count2 == 2) {
												 				System.out.println("neither library seems to have your book");
												 			}
											  	    
										  		}
											}
										  		else {
											 		if(showXML.copiesget >= 1) {
											 		//System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
											 		System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget);
											 		}
											 		else {
											 		//	System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
											 			System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget + ", no copies available");
											 			count = count + 1;
											 		}
											 			if(count == 2) {
											 				System.out.println("neither library seems to have your book");
											 		}
										  		}
				  		}
				else {
					System.out.println("enter author firstname:");
					    ResultSet rs2;
						Scanner book = new Scanner(System.in);
				  		String authorfirstname = book.nextLine();
				  		System.out.println("enter author lastname:");
				  		Scanner book2 = new Scanner(System.in);
				  		String authorlastname = book2.nextLine();
				  		String checkcopy2 = "SELECT b.title, a.first_name, a.last_name "
								+ "FROM Book b, Writtenby w, Author a "
								+ "WHERE a.first_name LIKE '%" + authorfirstname + "%' AND b.isbn = w.isbn AND w.author = a.author";
				  	//	System.out.println(checkcopy2);
				  		rs2 = showXML.stmt.executeQuery(checkcopy2);
				  		int count = 0;
				  		showXML.al = new ArrayList<String>();
				  		while(rs2.next()) {
				  		showXML.title = rs2.getString(1);
				 		showXML.fname = rs2.getString(2);
				 		showXML.lname = rs2.getString(3);
				 		showXML.al.add(showXML.title);
				 	//	System.out.println("title: " + showXML.title + " fisrt name: " + showXML.fname + " last name: " + showXML.lname);	 		
				  		}
											  		if(showXML.al.size() > 1) {
												  		Iterator it = showXML.al.iterator();
												  		int num = 1;
												  		int temp = 0;
												        System.out.println("Choose a number with title: ");
													        while (it.hasNext()) {
													            System.out.println(num + ": " + it.next());
													            num +=1;
													        }
													        Scanner book3 = new Scanner(System.in);
													  	    temp = book3.nextInt();
													  	  ResultSet rs3;
													  	    String checkcopy3 = "SELECT so.copies, so.isbn, so.name, s.shelf_num, b.title "
																	+ "FROM Storedon so, Shelf s, Book b "
																	+ "WHERE b.title = '" + showXML.al.get(temp-1) + "'  AND so.storein_id = s.shelf_id AND b.isbn = so.isbn";
													  	//	System.out.println(checkcopy3);
													  		rs3 = showXML.stmt.executeQuery(checkcopy3);
													  		int count2 = 0;
													  		showXML.al = new ArrayList<String>();
													  		while(rs3.next()) {
													 		int copiesget = Integer.parseInt(rs3.getString(1));
													 		String isbnget = rs3.getString(2);
													 		String nameget = rs3.getString(3);
													 		String shelfget = rs3.getString(4);	
													 		String bookget = rs3.getString(5);		
													 		if(copiesget >= 1) {
													 	//	System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
													 		System.out.println(" Library: " + nameget + ", Shelf: " + shelfget);
													 		}
													 		else {
													 	//		System.out.println("copies: " + copiesget + " isbn: " + isbnget + " name: " + nameget + " shelf: " + shelfget);
													 			System.out.println(" Library: " + nameget + ", Shelf: " + shelfget + ", no copies available");
													 			count2 = count2 + 1;
													 		}
													 			if(count2 == 2) {
													 				System.out.println("neither library seems to have your book");
													 			}
												  	    
											  		}
												}
								  		else {
									 		if(showXML.copiesget >= 1) {
									 	//	System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
									 		System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget);
									 		}
									 		else {
									 	//		System.out.println("copies: " + showXML.copiesget + " isbn: " + showXML.isbnget + " name: " + showXML.nameget + " shelf: " + showXML.shelfget);
									 			System.out.println(" Library: " + showXML.nameget + ", Shelf: " + showXML.shelfget + ", no copies available");
									 			count = count + 1;
									 		}
									 			if(count == 2) {
									 				System.out.println("neither library seems to have your book");
									 		}
								  		}
				
					}
										
  			}
  
  		}
  		
  	}catch( Exception e ) {
  		e.printStackTrace();

  	}//end catch
  	
	showXML.con.close();
    }catch( Exception e ) {
      e.printStackTrace();

    }//end catch

  }//end main

}//end class Lab4A_ex