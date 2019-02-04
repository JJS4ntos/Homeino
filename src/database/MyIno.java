package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyIno {

	/***
	 * Estabelece conexão com o banco de dados MySQL
	 * @param serverName
	 * @param database
	 * @param username
	 * @param password
	 * @return
	 */
	public static Connection getConnection(String serverName, String database, String username, String password){
		Connection connection=null;
		try{
			String driver="com.mysql.jdbc.Driver";
			Class.forName(driver);
			String url= "jdbc:mysql://"+serverName+"/"+database;
			connection= DriverManager.getConnection(url, username, password);
			if(connection!=null)System.out.print("Conectado!");
		}catch(SQLException | ClassNotFoundException e){e.printStackTrace();}
		return connection;
	}
	
	
	/***
	 * Insere o usuário no banco de dados
	 * @param name
	 * @param email
	 * @param country
	 * @param state
	 * @param city
	 * @param codet
	 */
	public static void insertDefUserMySQL(String name, String email, String country, String state, String city, String codet){
    	try{
    		Connection connection = MyIno.getConnection("localhost", "dbino_home", "root", "81179535");
    		Statement statement = connection.createStatement();
    		statement.executeUpdate("INSERT INTO def_users(name, email, country, city, state, codet)VALUES("
    				+ "'"+name+"','"+email+"','"+country+"','"+city+"','"+state+"','"+codet+"')");
    		connection.close();
    		statement.close();
    	}catch(SQLException e){e.printStackTrace();}
    	
    }
	
	/***
	 * Verifica se a licenca foi comprada pelo usuário, através de registro no JVM
	 * @return
	 */
	public boolean checklicence(){
		boolean licence=false;
		try{
			String name=Sqlite.getValueRegJVM("name"), codet=Sqlite.getValueRegJVM("codet");
			Connection connection=getConnection("localhost", "dbino_home", "root", "81179535");
			Statement statement= connection.createStatement();
			ResultSet resultset= statement.executeQuery("SELECT * FROM def_users WHERE name='"+name+"' and codet='"+codet+"'");
			licence=resultset.next();
			connection.close();
			statement.close();
			resultset.close();
		}catch(SQLException e){e.printStackTrace();}
		return licence;
	}
	
}
