package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;

public class logs {

	private final User usuario;
	
	private final ListView<Hyperlink> list_reg;
	
	public logs(ListView<Hyperlink> list_reg, User usuario) {
		this.usuario=usuario;
		this.list_reg=list_reg;
		loadingLogs();
		// TODO Auto-generated constructor stub
	}

	public void loadingLogs(){
		list_reg.getItems().clear();
		ResultSet result=Sqlite.getResultSet("SELECT * FROM logs WHERE userid='"+usuario.getId()+"'");
		try{
			while(result.next())
				list_reg.getItems().add(new Hyperlink(result.getString("DATE")));
		result.getStatement().getConnection().close();
		result.close();
		}catch(SQLException e){e.printStackTrace();}
	}
	
	
	public void registrar(String log, String logbyte){
		String date=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm-ss"));
		try{Sqlite.insert("INSERT INTO logs (DATE, USERID, LOG, LOGBYTE) values('"+date+"','"+usuario.getId()+"','"+log+"','"+logbyte+"')");
		}catch(Exception e){e.printStackTrace();}
	}
}
