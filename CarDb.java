import java.sql.*;
import java.io.*;

public class CarDb {

    public static void setup() {     
        String setupFile = "setup.sql";
        String insertFile = "insert.sql";
        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {
            ScriptRunner runner = new ScriptRunner(connection, false, false);
            runner.runScript(new BufferedReader(new FileReader(setupFile)));
            runner.runScript(new BufferedReader(new FileReader(insertFile)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
          catch(IOException e){
            System.out.println("File not found");
        }
    }
    
    public static void tearDown(){
        String deleteCountries = "delete from countries;";
        String deleteSales = "delete from sales;";
        String deletePeople = "delete from people;";
        String deleteCars = "delete from cars";
        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {
            statement.execute(deleteSales);
            statement.execute(deletePeople);
            statement.execute(deleteCars);
            statement.execute(deleteCountries);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }        
    }

    public static String getPersonName() {
        String querySql = "select full_name from people where id = 1;";
        try (Connection connection = DriverManager.getConnection(connectionString);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(querySql);
            return resultSet.getString("full_name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getPersonName(int id) {
        String querySql = "select full_name from people where id = ?;";
        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement preparedStatement = connection.prepareStatement(querySql);) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getString("full_name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static int insertPerson(String name,String email,int countryId) {
        //Because of how SQL-lite works since our primary key is an integer it is automatically set if not specifed      
        String querySql = "insert into people(full_name,email,country_id) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(querySql);) {
            statement.setString(1,name);
            statement.setString(2,email);
            statement.setInt(3,countryId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    


    private static String connectionString =
        "jdbc:sqlite:/sandbox/hiker.db";
}
