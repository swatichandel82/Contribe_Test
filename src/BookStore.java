

import javax.swing.*;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
public class BookStore extends JFrame {

	  private JTextField titleText;
	  private JTextField authorText;
	  private JScrollPane scroll;
	  //private JScrollPane scroll = new JScrollPane();
	  
	  Book [] bookList = new Book [20] ;
	  int ArraySize = -1;
	  
	  private JTable tableA = new JTable();
	  private DefaultTableModel dtm1 = new DefaultTableModel(0, 0);
	  
	  private JTable tableB = new JTable();
	  private DefaultTableModel dtm2 = new DefaultTableModel(0, 0);
	  
	  private JPanel panelTable = new JPanel();
	
  public static void main(String[] args) {
	  new BookStore();
	  
	  BookList interfaceList = new BookListImplementation();
    
}
  
  public BookStore() {
		

	    
      // Creating instance of JFrame
      JFrame frame = new JFrame("Book Store");
      // Setting the width and height of frame
      frame.setSize(700, 700);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      /* Creating panel. This is same as a div tag in HTML
       * We can create several panels and add them to specific 
       * positions in a JFrame. Inside panels we can add text 
       * fields, buttons and other components.
       */
      JPanel panel = new JPanel();   
      
      panel.setLayout(null);

      // Creating JLabel
      JLabel titleLabel = new JLabel("Title");
      /* This method specifies the location and size
       * of component. setBounds(x, y, width, height)
       * here (x,y) are cordinates from the top left 
       * corner and remaining two arguments are the width
       * and height of the component.
       */
      titleLabel.setBounds(10,20,80,25);
      panel.add(titleLabel);

      /* Creating text field where user is supposed to
       * enter user name.
       */
      titleText = new JTextField(25);
      titleText.setBounds(100,20,165,25);
      panel.add(titleText);

      
      JLabel authorLabel = new JLabel("Author");
      authorLabel.setBounds(10,50,80,25);
      panel.add(authorLabel);

      
      authorText = new JTextField(25);
      authorText.setBounds(100,50,165,25);
      panel.add(authorText);

      // Creating login button
      JButton searchButton = new JButton("Search");
      searchButton.setBounds(10, 80, 80, 25);
      panel.add(searchButton);

      
      searchButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        	if(scroll != null)
        		scroll.setVisible(false);
        	//GetSearchResult(titleText.getText(), authorText.getText());
        	BookList interfaceList = new BookListImplementation();
        	bookList = interfaceList.list(titleText.getText() + "#" + authorText.getText() );
        	
        	int counter = 0;
        	for (int i = 0; i < bookList.length; i ++)
        	    if (bookList[i] != null)
        	        counter ++;
        	
        	ArraySize = counter;
            
        	//displaySearchResult();
        	//display();
        	displaySearchResults();
        	display();
        	
        }
      });
      
      // adding panel to frame
      frame.add(panel);
      /* calling user defined method for adding components
       * to the panel.
       */
      TitledBorder border = new TitledBorder("Search Result");
      border.setTitleJustification(TitledBorder.CENTER);
      border.setTitlePosition(TitledBorder.TOP);
      
      panelTable = new JPanel();
      panelTable.setBorder(border);
      panelTable.setBounds(100,200,500,400);
      
      String header[] = new String[] { "Title", "Author", "Price", "Add"};

      // add header in table model     
       dtm1.setColumnIdentifiers(header);
      
      tableA.setModel(dtm1);
      tableA.getColumn("Add").setCellRenderer(new ButtonRendererAdd());
      
      JScrollPane scrollPane = new JScrollPane(tableA);
      panelTable.add(scrollPane, BorderLayout.CENTER);
      
      //panelTable.setSize(150, 150);
      panelTable.add(tableA);
      //panelTable.setLocationRelativeTo(null);
      panelTable.setVisible(true);
      
      panel.add(panelTable);

      // Setting the frame visibility to true
      frame.setVisible(true);

		
	  }
  
  public void GetSearchResult(String bookTitle, String bookAuthor){

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
              
              bookList[i] = new Book();
              bookList[i].SetTitle(resultSet.getString("title"));
              bookList[i].SetAuthor(resultSet.getString("author"));
              bookList[i].SetPrice(resultSet.getDouble("price"));
              i++;
              
           }
           
           ArraySize = i;
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
  }
  
  public void displaySearchResults(){

       // add row dynamically into the table      
  for (int count = 1; count <= ArraySize; count++) {
          dtm1.addRow(new Object[] { bookList[count-1].GetTitle(), bookList[count-1].GetAuthor(),bookList[count-1].GetPrice(), "Add" });
   }
  
  tableA.setModel(dtm1);
  tableA.getColumn("Add").setCellRenderer(new ButtonRendererAdd());
  tableA.getColumn("Add").setCellEditor( new ButtonEditor(new JCheckBox()));
  
  dtm1.fireTableDataChanged();
}

  
  
  public void display(){


  // add header of the table
  String header2[] = new String[] { "Title", "Author", "Quantity", "Price", "Remove"};

  // add header in table model     
  dtm2.setColumnIdentifiers(header2);
         
   

       // add row dynamically into the table      
/*  for (int count = 1; count <= ArraySize; count++) {
	  dtm2.addRow(new Object[] { bookList[count-1].GetTitle(), bookList[count-1].GetAuthor(),bookList[count-1].GetPrice(), "Remove" });
   }*/
  
  tableB.setModel(dtm2);
  tableB.getColumn("Remove").setCellRenderer(new ButtonRendererRemove());
  tableB.getColumn("Remove").setCellEditor( new ButtonEditor(new JCheckBox()));
  //------------------

  //JLabel labelA = new JLabel("Search Result");
  JLabel labelB = new JLabel("Shopping Basket");
  
  
  JButton btnBuy = new JButton("Buy");

  Container c = getContentPane();

  c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));


  c.add(labelB);

  c.add(tableB);


  c.add(btnBuy);
  
  btnBuy.addActionListener(new ActionListener()
  {
    public void actionPerformed(ActionEvent e)
    {
    	Book [] bookListBuy = new Book [20] ;
    	Book [] tempbookList = new Book [20] ;

    	int index = 0;
    	
    	int rowCount = tableB.getRowCount();
  	  if(rowCount>1){
  	
  		  for (int i = 0;i< rowCount-1; i++){
  			bookListBuy[i] = new Book();

  			bookListBuy[i].SetTitle((String)tableB.getValueAt(i, 0));
  			bookListBuy[i].SetAuthor((String)tableB.getValueAt(i, 1));
  			bookListBuy[i].SetPrice((Double)tableB.getValueAt(i, 3));
  		  }
  	  }
  	
  	  Book tempBook = new Book();
  	tempBook = bookListBuy[0];
  	boolean found = false;
  	int[] stock = new int[20];
  	int counter = 1;
  	int couterTemp = 0;
  	int counterStock = 0;

  	for (int j = 0; j < rowCount-1; j++) {
  	    if (tempBook.GetAuthor() == bookListBuy[j].GetAuthor() && tempBook.GetTitle() == bookListBuy[j].GetTitle() && !found) {
  	        found = true;
  	        
  	        if(counter == 1){
  	        	tempbookList[couterTemp] = tempBook;
  	        	couterTemp = couterTemp +1;
  	        }
  	        
  	      counter = counter + 1;
  	        	
  	    } else if (tempBook.GetAuthor() != bookListBuy[j].GetAuthor() || tempBook.GetTitle() != bookListBuy[j].GetTitle() ) {
  	        System.out.print(" " + tempBook);
  	      tempBook = bookListBuy[j];
  	    tempbookList[couterTemp] = tempBook;
  	        found = false;
  	        stock[counterStock] = counter;
  	      counterStock = counterStock + 1;
  	      counter = 1;
  	      
  	    }
  	}
  	
  	  
  	  	int[] output = new int[20];
    	BookList interfaceList = new BookListImplementation();
    	output = interfaceList.buy(tempbookList, stock);
    	
    }
  });
  
  setSize(600, 600);
  setVisible(true);
  setLocation(800,10);
  }
  
 
  public void UpdateBasketAdd(int selRow){
	  int rowCount = tableB.getRowCount();
	  if(rowCount>1)
		  dtm2.removeRow(rowCount-1);
	  
	  dtm2.addRow(new Object[] { bookList[selRow].GetTitle(), bookList[selRow].GetAuthor(),1, bookList[selRow].GetPrice(), "Remove" }); 
	  double totalPrice = 0;
	  
	  if(rowCount==0)
		  totalPrice = bookList[selRow].GetPrice();
	
	for (int i = 0;i< rowCount; i++){
		totalPrice = totalPrice + (double)tableB.getValueAt(i, 3);
	}

	  dtm2.addRow(new Object[] { "", "", "Total", totalPrice, ""});
	  tableB.setModel(dtm2);
	  
	  dtm2.fireTableDataChanged();
  }
  
  public void UpdateBasketRemove(int selRow){
	  int rowCount = tableB.getRowCount();
	  double totalPrice = 0;
	  
	  totalPrice = (double)tableB.getValueAt(rowCount-1, 3) - (double)tableB.getValueAt(selRow, 3);
	  tableB.setValueAt(totalPrice, rowCount-1, 3);
	  
	  
	  
	  if(rowCount == 2){
		  dtm2.removeRow(1);
		  dtm2.removeRow(selRow);
	  }
	  else
		  dtm2.removeRow(selRow);
	  
	  
	  tableB.setModel(dtm2);
	  
	  dtm2.fireTableDataChanged();
  }
  
public void displaySearchResult(){

    JTable tbl = new JTable();
    DefaultTableModel dtm = new DefaultTableModel(0, 0);

   // add header of the table
   String header[] = new String[] { "Title", "Author", "Price", "Add"};

   // add header in table model     
    dtm.setColumnIdentifiers(header);
          
    

        // add row dynamically into the table      
   for (int count = 1; count <= ArraySize; count++) {
           dtm.addRow(new Object[] { bookList[count-1].GetTitle(), bookList[count-1].GetAuthor(),bookList[count-1].GetPrice(), "Add" });
    }
   
   tbl.setModel(dtm);
   tbl.getColumn("Add").setCellRenderer(new ButtonRendererAdd());
   //tbl.getColumn("Add").setCellEditor( new ButtonEditor(new JCheckBox()));
   scroll = new JScrollPane(tbl);
   //scroll.add(tbl);
   getContentPane().add(scroll);
   setSize(800, 800);
   setVisible(true);

    
	}

/*class ButtonEditor extends DefaultCellEditor {
	  protected JButton button;

	  private String label;

	  private boolean isPushed;

	  public ButtonEditor(JCheckBox checkBox) {
	    super(checkBox);
	    button = new JButton();
	    button.setOpaque(true);
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        fireEditingStopped();
	      }
	    });
	  }
}*/

class ButtonRendererAdd extends JButton implements TableCellRenderer {

	  public ButtonRendererAdd() {
	    setOpaque(true);
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value,
	      boolean isSelected, boolean hasFocus, int row, int column) {
	    if (isSelected) {
	    	System.out.println("Pressed Add");
	    	System.out.println(row);
	    	//int selRow = table.getSelectedRow();
	    	//int selColumn = table.getSelectedColumn();
	    	/*String valueCell = (String)table.getValueAt(row, column);
	    	
	    	if(valueCell == "Add"){
	    		UpdateBasketAdd(row);
	    	}*/
	    	
	      setForeground(table.getSelectionForeground());
	      setBackground(table.getSelectionBackground());
	    } else {
	      setForeground(table.getForeground());
	      setBackground(UIManager.getColor("Button.background"));
	    }
	    setText((value == null) ? "" : value.toString());
	    return this;
	  }
	}

class ButtonRendererRemove extends JButton implements TableCellRenderer {

	  public ButtonRendererRemove() {
	    setOpaque(true);
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value,
	      boolean isSelected, boolean hasFocus, int row, int column) {
	    if (isSelected) {
	    	System.out.println("Pressed Remove");
	    	System.out.println(row);
	    	//UpdateBasketRemove(row);
	    	/*int selRow = tableA.getSelectedRow();
	    	int selColumn = tableA.getSelectedColumn();
	    	String valueCell = (String)tableA.getValueAt(row, column);
	    	
	    	if(valueCell == "Add"){
	    		UpdateBasketAdd(row);
	    	}
	    	else
	    		UpdateBasketRemove(row);*/
	    	
	      setForeground(table.getSelectionForeground());
	      setBackground(table.getSelectionBackground());
	    } else {
	      setForeground(table.getForeground());
	      setBackground(UIManager.getColor("Button.background"));
	    }
	    setText((value == null) ? "" : value.toString());
	    return this;
	  }
	}


class ButtonEditor extends DefaultCellEditor {
	  protected JButton button;

	  private String label;

	  private boolean isPushed;

	  
  	
	  public ButtonEditor(JCheckBox checkBox) {
	    super(checkBox);
	    button = new JButton();
	    button.setOpaque(true);
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  int selRow = tableA.getSelectedRow();
		    	int selColumn = tableA.getSelectedColumn();
		    	String valueCell = (String)tableA.getValueAt(selRow, selColumn);
		    	
		    	if(valueCell == "Add"){
		    		
		    		//UpdateBasketAdd(selRow);
		    	}

	          
	        fireEditingStopped();
	        
	      }
	    });
	  }

	  public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column) {
	    if (isSelected) {
	      button.setForeground(table.getSelectionForeground());
	      button.setBackground(table.getSelectionBackground());
	    } else {
	      button.setForeground(table.getForeground());
	      button.setBackground(table.getBackground());
	    }
	    
	    label = (value == null) ? "" : value.toString();
	    button.setText(label);
	    isPushed = true;
	    return button;
	  }

	  public Object getCellEditorValue() {
	    if (isPushed) {
	    	System.out.println("getCellEditorValue");
	    	if(label.equals("Add")){
	    		int selRow = tableA.getSelectedRow();
		    	int selColumn = tableA.getSelectedColumn();
	    		UpdateBasketAdd(selRow);
	    	}
	    	else{
	    		int selRow = tableB.getSelectedRow();
		    	int selColumn = tableB.getSelectedColumn();
	    		UpdateBasketRemove(selRow);
	    	}
	    		
	    }
	    
	    isPushed = false;
	    return new String(label);
	  }

	  public boolean stopCellEditing() {
	    isPushed = false;
	    return super.stopCellEditing();
	  }

	  protected void fireEditingStopped() {
	    super.fireEditingStopped();
	  }
	}

}
  