/*Criar sistema de logs e login para a segurança da automação*/
package database;

import house.Item;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Sqlite {
	private static final java.io.File DATABASE = new java.io.File("/configuration_house/bancodedados.jj");
	
    public static void checkDatabase() throws IOException {  
        if (!DATABASE.exists()) {
            createNewDatabase();  
        }  
    }  
      
    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {  
        Class.forName("org.sqlite.JDBC");  
        Connection conn =DriverManager.getConnection("jdbc:sqlite:" + DATABASE.getPath()); 
        return conn;  
    }  
      
    public static ArrayList<String> getPinsEmpty(ArrayList<Item> items){
    	ArrayList<String> pins= new ArrayList<>();
    	for(int i=0;i<54;i++){
    		String pin=String.valueOf(i);
    		if(!items.stream().filter(item->item.getPin().contains(pin)).findFirst().isPresent())
    			pins.add(pin);
    	}
    	return pins;
    }
    
    public static void deleteItem(Item item){
    	try{
    		Connection connection= Sqlite.getConnection();
    		Statement statement= connection.createStatement();
        	statement.executeUpdate("DELETE FROM light_children WHERE id='"+item.getItemId()+"'");
        	item.setVisible(false);
        	statement.close();
        	connection.close();
    	}catch(SQLException | IOException | ClassNotFoundException e){e.printStackTrace();}
    	
    }
    
    public static void updateItem(Item item){
    	try{
    		Connection connection= Sqlite.getConnection();
    		Statement statement= connection.createStatement();
        	statement.executeUpdate("UPDATE light_children SET name='"+item.getName()+"', pin='"+item.getPin()+"', buttonpin='"+item.getPinButton()+"'"
        			+ "WHERE id='"+item.getItemId()+"'");
        	statement.close();
        	connection.close();
    	}catch(SQLException | IOException | ClassNotFoundException e){e.printStackTrace();}
    }
    
    //Método que adiciona HashMd5 à String
  	public static String toMD5(String toMD5) throws NoSuchAlgorithmException{
  		MessageDigest md5 = MessageDigest.getInstance("MD5");
  		md5.update(toMD5.getBytes(),0,toMD5.length());
  		return new BigInteger(1, md5.digest()).toString(16);
  	}
  	
    //Método que retornará o objeto usuário.
    public static User login(String login, String password) throws SQLException{
    	User usuario= null;
    	ResultSet resultset= getResultSet("SELECT * FROM users WHERE login='"+login.trim()+"' and password='"+password.trim()+"'");
    	if(resultset.next()){
    		final Integer id= resultset.getInt("ID"), language= resultset.getInt("LANGUAGE");
    		final String[] data={resultset.getString("NAME"), resultset.getString("LOGIN"), resultset.getString("AUTH"), resultset.getString("SECRET")};
    		usuario= new User(id, data, language);
    	}
    	resultset.getStatement().getConnection().close();
		resultset.close();
    	return usuario;
    }
    
    public static void createNewDatabase() {  
        try {  
            DATABASE.getParentFile().mkdirs();   
            DATABASE.createNewFile();           
            if (!DATABASE.exists())  
                throw new IOException("Erro ao gravar o arquivo de banco de dados.");  
            Connection conn = getConnection();  
            Statement s = conn.createStatement();   
           //Tabela do usuário, registra todos eles.
            s.execute("CREATE TABLE IF NOT EXISTS users ("  
                    + "ID INTEGER PRIMARY KEY, "  
                    + "LOGIN TEXT, "  
                    + "PASSWORD TEXT,"
                    + "NAME TEXT,"
                    + "AUTH TEXT,"
                    + "LANGUAGE INTEGER,"
                    + "SECRET TEXT"
                    + ")");
            
            //Tabela dos conteúdos, registra todos eles.
            s.execute("CREATE TABLE IF NOT EXISTS logs ("  
                    + "ID INTEGER PRIMARY KEY, "  
                    + "DATE TEXT, "
                    + "LOG TEXT,"
                    + "USERID INTEGER, "
                    + "LOGbyte TEXT)");
            
           //Tabela das matérias, registra todas elas. 
           s.execute("CREATE TABLE IF NOT EXISTS room ("  
        		    + "ID INTEGER PRIMARY KEY, " 
        		    + "NAME TEXT, "  
                    + "IMAGEURL TEXT, "  
                    + "INFO TEXT,"
                    + "USERID INTEGER"
                    + ")");
           
           //Tabela responsável por armazenar as linguagens
           s.execute("CREATE TABLE IF NOT EXISTS languages ("  
        		    + "ID INTEGER PRIMARY KEY, " 
        		    + "NAME TEXT"  
                    + ")");
           
           //Tabela que armazena as lâmpadas
           s.execute("CREATE TABLE IF NOT EXISTS light_children ("  
       		    + "ID INTEGER PRIMARY KEY, " 
       		    + "TYPE INTEGER," //Tipo de ITEM
       		    + "PARENT INTEGER," // Qual o id do cômodo pai desse objeto
       		    + "X REAL," //Posição plano cartesiano (X)
       		    + "Y REAL," //Posição plano cartesiano (Y)
       		    + "NAME TEXT," //NOME
       		    + "ONOFF INTEGER," //SE está ligado ou delisgado
       		    + "TIMEON INTEGER," // Tempo máximo ligado ou angulo
       		    + "PIN INTEGER,"//Pino da lâmpada
       		    + "BUTTONPIN INTEGER,"//Pino do botão de acionamento
       		    + "USERID INTEGER,"
       		    + "STRING_USE TEXT"
                + ")");
           
           s.execute("CREATE TABLE IF NOT EXISTS condition_children ("  
       		    + "ID INTEGER PRIMARY KEY, " 
       		    + "ITEM_ID INTEGER,"
       		    + "ITEM_ACTIVE_ID INTEGER,"
       		    + "CONDITION_TYPE INTEGER," //>::0, <::1, ==::2,!=::3, >=::4, <=::5  <--TIPOS DE CONDIÇÕES SIMPLES
       		    + "ONOFF INTEGER,"//0::Desliga o item, 1::Liga o item
       		    + "CONTROLLER_TYPE INTEGER,"//Tipo da estrutura de controle
       		    + "READSTRING TEXT"//String contendo o código de leitura, digitalRead(pino)==HIGH ou analogRead(pino)==1023 
                + ")");
           
           s.execute("CREATE TABLE IF NOT EXISTS IR_object ("  
          		    + "ID INTEGER PRIMARY KEY, " 
          		    + "ITEM_ID INTEGER,"
          		    + "ITEM_ACTIVE_ID INTEGER,"
          		    + "ONOFF INTEGER,"
          		    + "USERID INTEGER,"
          		    + "HEXVALUE TEXT" 
                   + ")");
           
           s.execute("CREATE TABLE IF NOT EXISTS OBJECTS_USERS("
           			+ "ID INTEGER PRIMARY KEY,"
           			+ "NAME TEXT,"
           			+ "ROOT TEXT)");
           
           s.execute("CREATE TABLE IF NOT EXISTS INPUT_CODES("
           			+ "ID INTEGER PRIMARY KEY,"
           			+ "NAME TEXT,"
           			+ "ITEM_ID TEXT,"
           			+ "CONTENT TEXT,"
           			+ "TYPE INTEGER)");
           
         s.close();
         conn.close();
        } catch (SQLException | IOException | ClassNotFoundException ex) {ex.printStackTrace();}  

    }
      
    public static void insertIRObject(final String IR, Item item, final int SIGNAL, String HEXVALUE, User usuario){//SIGNAL é HIGH ou LOW
    	insert("INSERT INTO IR_object(PARENT, ITEM_ACTIVE_ID, ONOFF, USERID, HEXVALUE) VALUES("
    	+ "'"+IR+"','"+item.getItemId()+"','"+SIGNAL+"','"+usuario.getId()+"','"+HEXVALUE+"')");
    }
    
    //Método que cria o objeto base para um room: o data_room.
    public static ArrayList<Data_room> getDatarooms(int userid){
    	ArrayList<Data_room> datarooms= new ArrayList<Data_room>();
    	try{
    	Connection connection= Sqlite.getConnection();
    	Statement statement= connection.createStatement();
    	ResultSet resultset= statement.executeQuery("SELECT * FROM room WHERE userid='"+userid+"'");
    	while(resultset.next()){
    		String[] data={resultset.getString("NAME"), resultset.getString("INFO"), resultset.getString("IMAGEURL")};
    		Data_room data_room= new Data_room(resultset.getInt("id"), data, resultset.getInt("userid"));
    		datarooms.add(data_room);
    	}
    	statement.close();
    	connection.close();
    	resultset.close();
    }catch(IOException | SQLException | ClassNotFoundException e){e.printStackTrace();}
    return datarooms;
    }
    
    public static void signup(String name, String login, String password, String secret, int language) throws SQLException{
    	ResultSet resultset = getResultSet("SELECT * FROM users WHERE login='"+login+"'"); 
    	if(resultset.next()){ dialogs.dialogs.showmessage("Erro no cadastro", "Login já existente", "Esse login já está sendo utilizado por "
    			+ "outro usuário. Tente novamente com um novo login.", AlertType.WARNING).show();
    		resultset.getStatement().getConnection().close();
    		resultset.close();
    	}
    	else
    	insert("INSERT INTO users(LOGIN, PASSWORD, NAME, AUTH, LANGUAGE, SECRET) VALUES('"+login+"','"+password+"','"
    				+name+"',"+"'0','0'"+",'"+secret+"')");
    	
    }
    
    public static void insert(String query) {
    	try{
    		Connection connection = Sqlite.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(query);
    		connection.close();
    	}catch(SQLException | IOException | ClassNotFoundException e){e.printStackTrace();}
    	
    }
    
    public static ResultSet getResultSet(String query){
    	ResultSet resultset=null;
    	try{Connection connection = Sqlite.getConnection();
    	Statement statement = connection.createStatement();
    	resultset = statement.executeQuery(query);
    	}catch(SQLException | IOException | ClassNotFoundException e){e.printStackTrace();}
    	return resultset;
    }
    
    public static String getValueRegJVM(String key){
		Preferences p= Preferences.userRoot();
		return p.get(key, null);
	}
    
    public static ArrayList<String> getObjectUsers() throws SQLException{
    	ArrayList<String> arrayOfObjects= new ArrayList<>();
    	ResultSet rs=getResultSet("SELECT * FROM OBJECTS_USERS");
    	while(rs.next()){
    		String string=rs.getString("ID")+"~~"+rs.getString("NAME")+"~~"+rs.getString("ROOT");
    		arrayOfObjects.add(string);
    		System.out.println(string);
    		
    	}
    	rs.getStatement().getConnection().close();
    	rs.close();
    	return arrayOfObjects;
    }
    
    public static boolean wasFound(String query) throws SQLException{
    	ResultSet rs= getResultSet(query);
    	boolean next= rs.next();
    	rs.getStatement().getConnection().close();
    	rs.close();
    	return next;
    }
    
    public static int regObjectUser(String name, String root){
    	int id=-1;
    	try{
    		if(!wasFound("SELECT * FROM OBJECT_USERS WHERE NAME='"+name+"'")){
    		insert("INSERT INTO OBJECT_USERS (NAME, ROOT) VALUES('"+name+"', '"+root+"')");
    		ResultSet rs = getResultSet("SELECT * FROM OBJECT_USERS WHERE NAME='"+name+"'");
    		id=rs.getInt("ID");
    		rs.getStatement().getConnection().close();
    		rs.close();
    		}    		
    	}catch(SQLException e){e.printStackTrace();}
    	return id;
    }
	
	public static void putValueRegJVM(String key, String value){
		Preferences p= Preferences.userRoot();
		p.put(key, value);
	}
    
	public static void updateUser(User user, String password, String secret){
		try{
			Connection connection= Sqlite.getConnection();
			PreparedStatement pstatement= connection.prepareStatement("UPDATE user SET name=?, password=?, secret=?, language=? WHERE"
					+ "Id='"+user.getId()+"'");
			pstatement.setString(0, user.getName());
			pstatement.setString(1, Sqlite.toMD5(password));
			pstatement.setString(2, Sqlite.toMD5(secret));
			pstatement.setInt(3, user.getLanguage());
			pstatement.execute();
			connection.close();
		}catch(SQLException | ClassNotFoundException | NoSuchAlgorithmException | IOException e){e.printStackTrace();}
	}
}
