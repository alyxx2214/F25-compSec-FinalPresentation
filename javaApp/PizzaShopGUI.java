import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PizzaShopGUI 
{
	private JFrame mainFrame;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameInput;
	private JTextField passwordInput;
	private JButton submitButton;
	
	// Database credentials
    final static String HOSTNAME = "egel0003-sql-server.database.windows.net";
    final static String DBNAME = "cs-dsa-4513-sql-db";
    final static String USERNAME = "egel0003";
    final static String PASSWORD = "iamMola55e5!";
    
    // Database connection string
    final static String URL =
        String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            HOSTNAME, DBNAME, USERNAME, PASSWORD);
	
	public static String getLoginQuery_Unprotected = 
	        "select name " + 
	        "from Person " + 
	        "where username = '%s' and passwd = '%s';";
	        
	public static String getLoginQuery_Protected = 
	        "select name " + 
	        "from Person " + 
	        "where username = ? and passwd = ?;";
	
	public PizzaShopGUI()
	{
		prepareGUI();
	}
	
	public static void main(String[] args)
	{
		PizzaShopGUI pizzaShop = new PizzaShopGUI();
	}
	
	private void prepareGUI()
	{
		mainFrame = new JFrame("SELECT * FROM PIZZA;");
	    mainFrame.setSize(400,400);
	    mainFrame.setLayout(new GridLayout(3, 1));
	    
	    mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	    });
	    
	    usernameLabel = new JLabel("Username: ", JLabel.LEFT);
	    passwordLabel = new JLabel("Password: ", JLabel.LEFT);
	    
	    submitButton = new JButton("Submit");
	    
	    submitButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		String username = usernameInput.getText();
	    		String password = passwordInput.getText();
	    		String[] inputs = {username, password};
	    		boolean queryResult = UnprotectedQuery(getLoginQuery_Unprotected, inputs);
	    		
	    		if(queryResult)
	    		{
	    			JOptionPane.showMessageDialog(mainFrame, "Congratulations! You have signed into SELECT * FROM PIZZA;");
	    		}
	    		else
	    		{
	    			JOptionPane.showMessageDialog(mainFrame, "Good try! We were not fooled.");
	    		}
	    	}
	    });
	    
	    usernameInput = new JTextField();
	    passwordInput = new JTextField();
	    
	    mainFrame.add(usernameLabel);
	    mainFrame.add(passwordLabel);
	    mainFrame.add(usernameInput);
	    mainFrame.add(passwordInput);
	    mainFrame.add(submitButton);
	    mainFrame.setVisible(true);
	}
	
	public static boolean UnprotectedQuery(String query, String[] inputs){
        try(Connection connection = DriverManager.getConnection(URL);)
        {
        	try (
        			final Statement statement = connection.createStatement();
        			final ResultSet resultSet = statement.executeQuery(String.format(query, inputs[0], inputs[1]));
        			) 
        			
        	{
        		System.out.println(String.format(query, inputs[0], inputs[1]));
        		//actually run query
        		System.out.println("Dispatching the query...");

        		//return the ResultSet
        		return resultSet.next();
        	}

        	//trying to execute query
        }
        catch (Exception e){
        	System.out.println(String.format("Error executing query: Unprotected Query\n Message: %s",e.getMessage()));
        }

        //default return
        return false;
    }
	
	public static boolean ProtectedQuery(String query, String[] inputs){
		try(Connection connection = DriverManager.getConnection(URL);)
		{
			try (
					final PreparedStatement statement = connection.prepareStatement(
							query//Query where you set a 'fill-in' value as a ? mark
							)
			) {
				statement.setString(1,inputs[0]);//set the Username
				statement.setString(2,inputs[1]);//set the Password

				System.out.println("Dispatching the query...");
				//actually run query
				ResultSet result = statement.executeQuery();   
            
				// return the boolean
				return result.next();
			}
		}
		//trying to execute query
		catch (Exception e){
			System.out.println("Error executing query: Protected Query");
		}

        //default return
        return false;
    }
}