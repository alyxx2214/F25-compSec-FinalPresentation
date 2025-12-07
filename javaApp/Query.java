import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


//How to use:
// you have the queries:
//   getLoginQuery and getOrdersQuery
// each of them have a different:
//   _Unprotected and _Protected
// to get the url from the DBCredentials, use `getHTTPToConnectTo()`
// Once you have a url, feed it into `makeConnection(url)` to get the db Connection
// To execute a query, use UnprotectedQuery or ProtectedQuery
//   example query:
//   Query.UnprotectedQuery(query.getLoginQuery_Unprotected, ["Username", "password"], makeConnection(getHTTPToConnectTo))
//
public class Query{
    public static String getLoginQuery_Unprotected = "select name " + 
        "from Person " + 
        "where username = '%s' and passwd = '%s';";
        
    public static String getLoginQuery_Protected = "select name " + 
        "from Person " + 
        "where username = ? and passwd = ?;";

    public static String getOrdersQuery_Unprotected = "select location, pizza, price " +
        "from PizzaOrder, Person " + 
        "where PizzaOrder.id = Person.id and Person.username = '%s' and Person.passwd = '%s';";

    public static String getOrdersQuery_Protected = "select location, pizza, price " +
        "from PizzaOrder, Person " + 
        "where PizzaOrder.id = Person.id and Person.username = ? and Person.passwd = ?;";

    public static String getHTTPToConnectTo(){
        String relativePath = "../../DBCredentials.txt";
        
        //catch error
        try(BufferedReader contents = new BufferedReader(new FileReader(relativePath))){ //open my file
            String Username = contents.readLine();
            String Password = contents.readLine();
            String Hostname = contents.readLine();
            String DbName = contents.readLine();

            String URL = String.format(
                "jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
                Hostname,
                DbName,
                Username,
                Password
            );

            return URL;
        }
        catch(IOException e){
            System.out.println("Error, broh... got no cred!: " + e.getMessage());

            return "Error Credentials";
        }


    }

    public static Connection makeConnection(String URL){
        System.out.println("Connecting to the database...");
        //Get a database connection
        try (
            final Connection connection = DriverManager.getConnection(URL);
        ) 
        {
            return connection;
        }
        catch (Exception e){
            System.out.println(String.format("Could not create connection to db:\n error message: %s", e.getMessage()));
        }

        //default return
        return null;
    }

    public static ResultSet UnprotectedQuery(String query, String[] inputs, Connection connection){
        try (
            final PreparedStatement statement = connection.prepareStatement(
                String.format(query, inputs[0], inputs[1])//Query where you take straight string values (un and pw)
            )
        ) 
        {
            //actually run query
            System.out.println("Dispatching the query...");

            //return the ResultSet
            return statement.executeQuery();                
        }

        //trying to execute query
        catch (Exception e){
            System.out.println("Error executing query: Unprotected Query");
        }
        

        //default return
        return null;
    }

    public static ResultSet ProtectedQuery(String query, String[] inputs, Connection connection){
        try (
            final PreparedStatement statement = connection.prepareStatement(
                query//Query where you set a 'fill-in' value as a ? mark
            )
        ) {
            statement.setString(1,inputs[0]);
            statement.setString(2,inputs[1]);

            System.out.println("Dispatching the query...");
            //actually run query

            //return the ResultSet
            return statement.executeQuery();                
        }
        //trying to execute query
        catch (Exception e){
            System.out.println("Error executing query: Protected Query");
        }

        //default return
        return null;
    }
}
