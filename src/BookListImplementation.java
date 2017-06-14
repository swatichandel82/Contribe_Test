import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookListImplementation implements BookList {

	
	@Override
	public Book[] list(String searchString) {
		// TODO Auto-generated method stub
		Book [] booksList = new Book [20] ;
		String bookTitle;
		String bookAuthor;
		  //int ArraySize = -1;
		if(searchString.length() > 1)  {
		String[] parts = searchString.split("#");
		 	bookTitle = parts[0]; 
		 	bookAuthor = parts[1]; 
		}
		else{
			 bookTitle = "";
		     bookAuthor = ""; 
	}
	
		Connection connection = null;
	      try
	      {
	         // create a database connection
	         connection = DriverManager.getConnection("jdbc:sqlite:bookstore.db");

	         Statement statement = connection.createStatement();
	         statement.setQueryTimeout(30);  // set timeout to 30 sec.
	         
	         String searchQuery;
	          if(bookTitle.isEmpty() && bookAuthor.isEmpty()){
	        	  searchQuery = "SELECT * from Books";
	          }
	          else if (bookTitle.isEmpty()){
	        	  
	        	  searchQuery = "SELECT id, title, author, price, instock FROM Books WHERE author LIKE '%" + bookAuthor + "%'";
	          }
	          else if (bookAuthor.isEmpty()){
	        	  
	        	  searchQuery = "SELECT id, title, author, price, instock FROM Books WHERE title LIKE '%" + bookTitle + "%'";
	          }
	          else {
	        	  
	        	  searchQuery = "SELECT id, title, author, price, instock FROM Books WHERE title LIKE '%" + bookTitle + "%' AND author LIKE '%" + bookAuthor  + "%'";;
	          }	  

	           ResultSet resultSet = statement.executeQuery(searchQuery);
	           int i = 0;
	           while(resultSet.next())
	           {
	              // iterate & read the result set
	              System.out.println("title = " + resultSet.getString("title"));
	              System.out.println("author = " + resultSet.getString("author"));
	              System.out.println("price = " + resultSet.getInt("price"));
	              System.out.println("instock = " + resultSet.getInt("instock"));
	              
	              booksList[i] = new Book();
	              booksList[i].SetTitle(resultSet.getString("title"));
	              booksList[i].SetAuthor(resultSet.getString("author"));
	              booksList[i].SetPrice(resultSet.getDouble("price"));
	              i++;
	              
	           }
	           
	          }

	     catch(SQLException e){  System.err.println(e.getMessage()); }       
	      finally {         
	            try {
	                  if(connection != null)
	                     connection.close();
	                  }
	            catch(SQLException e) {  // Use SQLException class instead.          
	               System.err.println(e); 
	             }
	      }
		return booksList;
	}

	@Override
	public boolean add(Book book, int quantity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] buy(Book [] books, int [] stock) {
		// TODO Auto-generated method stub
		int [] resultset = new int [20] ;
		
		
		return stock;
	}

}
