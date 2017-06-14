import java.math.BigDecimal;

public class Book {
	   private String title;
	   private String author;
	   private BigDecimal price;
	   
	   public void SetTitle (String bookTitle){
		   title = bookTitle;
	   }
	   
	   public String GetTitle (){
		   return title;
	   }
	
	   public void SetAuthor (String bookAuthor){
		   author = bookAuthor;
	   }
	   
	   public String GetAuthor (){
		   return author;
	   }
	   
	   public void SetPrice (double bookPrice){
		   price = BigDecimal.valueOf(bookPrice);;
	   }
	   
	   public double GetPrice (){
		   return price.doubleValue();
	   }
}