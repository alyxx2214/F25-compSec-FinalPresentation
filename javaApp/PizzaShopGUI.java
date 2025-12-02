import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PizzaShopGUI 
{
	private JFrame mainFrame;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameInput;
	private JTextField passwordInput;
	private JButton submitButton;
	
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
	    
	    usernameInput = new JTextField();
	    passwordInput = new JTextField();
	    
	    mainFrame.add(usernameLabel);
	    mainFrame.add(passwordLabel);
	    mainFrame.add(usernameInput);
	    mainFrame.add(passwordInput);
	    mainFrame.add(submitButton);
	    mainFrame.setVisible(true);
	}
}