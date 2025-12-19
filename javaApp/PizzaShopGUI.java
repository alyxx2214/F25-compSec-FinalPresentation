import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

public class PizzaShopGUI 
{
	private JFrame mainFrame;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameInput;
	private JTextField passwordInput;
	private JButton submitButton;

	private String URL;    
    // // Database connection string

	public PizzaShopGUI()
	{
		this.URL = PizzaShopGUI.getURLFromCredentials("../../DBCredentials.txt");
		prepareGUI();
	}
	
	public static void main(String[] args)
	{
		PizzaShopGUI pizzaShop = new PizzaShopGUI();
	}
		
	public static String getURLFromCredentials(String filename){
		//catch error
        try(BufferedReader contents = new BufferedReader(new FileReader(filename))){ //open my file
            String Username = contents.readLine();
            String Password = contents.readLine();
            String Hostname = contents.readLine();
            String DbName = contents.readLine();

            String URL = String.format(
                "jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;",
                Hostname,
                DbName,
                Username,
                Password
            );

			System.out.println(String.format("HN:\n %s\nDbName:\n %s\nUname:\n %s\nPw:\n %s\nJava DB URL:\n %s", Hostname, DbName, Username, Password, URL));

            return URL;
        }
        catch(IOException e){
            System.out.println("Error, broh... got no cred!: " + e.getMessage());

            return "Error Credentials";
        }
	}

	private void prepareGUI()
	{
		mainFrame = new JFrame("SELECT * FROM PIZZA;");
	    mainFrame.setSize(400,400);
	    mainFrame.setLayout(new GridLayout(3, 1));
	    
	    mainFrame.addWindowListener(new WindowAdapter() {
			@Override
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
	    		boolean queryResultProt = ProtectedQuery(inputs);
	    		boolean queryResultVuln = UnprotectedQuery(inputs);
	    		
				String conclusion = "";

				if (queryResultProt && queryResultVuln) conclusion = "Valid, correct login";
				else if (!queryResultVuln && !queryResultProt) conclusion = "Valid, incorrect login";
				else if (queryResultVuln && !queryResultProt) conclusion = "SQL Injection attack";

				JOptionPane.showMessageDialog(mainFrame, String.format("Protected results:\n  > Logged in successfully?  -  %b\n\nUnprotected results:\n  > Logged in successfully?  -  %b\n\n--  Input was a: %s  --", queryResultProt, queryResultVuln, conclusion));

				/*//
	    		if(queryResult)
	    		{
	    			JOptionPane.showMessageDialog(mainFrame, "Congratulations! You have signed into SELECT * FROM PIZZA;");
	    		}
	    		else
	    		{
	    			JOptionPane.showMessageDialog(mainFrame, "Good try! We were not fooled.");
	    		}*/
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
	
	public boolean UnprotectedQuery(String[] inputs){
        try(Connection connection = DriverManager.getConnection(this.URL);)
        {
			System.out.println("Dispatching the query...");
        	try (
        			final Statement statement = connection.createStatement();
        			final ResultSet resultSet = statement.executeQuery(String.format(
						"select name " + 
						"from Person " + 
						"where username = '%s' and passwd = '%s';", 
						inputs[0], 
						inputs[1]));
        			) 
        			
        	{
        		System.out.println("Query dispatched successfully.");
        		//actually run query

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
	
	public boolean ProtectedQuery(String[] inputs){
		try(Connection connection = DriverManager.getConnection(this.URL);)
		{
			try (
				final PreparedStatement statement = connection.prepareStatement(
					"select name " + 
					"from Person " + 
					"where username = ? and passwd = ?;"
					//Query where you set a 'fill-in' value as a ? mark
				)
			) 
			{
				statement.setString(1,inputs[0]);//set the Username
				statement.setString(2,inputs[1]);//set the Password

				System.out.println("Dispatching the query...");
				//actually run query
				ResultSet result = statement.executeQuery();   
				System.out.println("Query dispatched successfully.");

				// return the boolean
				return result.next();
			}
		}
		//trying to execute query
		catch (Exception e){
			System.out.println(String.format("Error executing query: Protected Query\n > %s", e.getMessage()));
		}

        //default return
        return false;
    }
}