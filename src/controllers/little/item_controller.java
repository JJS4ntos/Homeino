package controllers.little;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Sqlite;
import database.User;
import house.Data_item;
import house.Item;
import house.Room;
import house.code.ParseToCode;
import house.digital.Buzzer;
import house.digital.LCD;
import house.digital.LED;
import house.digital.ServoMotor;
import house.sensor.DS1302;
import house.sensor.IR;
import house.sensor.LDR;
import house.sensor.LM35;
import house.sensor.NTCTemp;
import house.sensor.PIR;
import house.sensor.Sensor_nivel_liquido;
import house.sensor.Ultra_sonic;
import javafx.scene.control.Alert.AlertType;

public class item_controller {

	public item_controller() {
		// TODO Auto-generated constructor stub
	}

	//Adiciona e retorna o item adicionada.
	public static Item insertItem(int type, int parent, double x, double y, String name, int onoff, String pin, Room room, int timeon, int btnpin, User usuario, String string_use) throws SQLException{
		Item item=null;
		ResultSet resultset=Sqlite.getResultSet("SELECT * FROM light_children WHERE userid='"+usuario.getId()+"' and name='"+name+"' or pin='"+pin+"'");
		if(!resultset.next()){
		Sqlite.insert("INSERT INTO light_children(TYPE, PARENT, X, Y, NAME, ONOFF, PIN, TIMEON, BUTTONPIN, USERID, STRING_USE) VALUES('"+
						type+"','"+parent+"','"+x+"','"+y+"','"+name+"','"+onoff+"','"+pin+"','"+timeon+"','"+btnpin+"','"+usuario.getId()+"','"+string_use+"')");
		ResultSet rs=Sqlite.getResultSet("SELECT * FROM light_children WHERE name='"+name+"'");
		if(rs.next()){
		  switch(type){
			  case ParseToCode.LED:{
				item= new LED(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
						rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.SERVO_MOTOR:{
			  item= new ServoMotor(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
						rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));  
			  }break;
			  case ParseToCode.LDR:{
			  item = new LDR(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
							rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.BUZZER:{
			  item = new Buzzer(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
							rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.NTC_TEMP:{
			  item = new NTCTemp(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
						rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));  
			  }break;
			  case ParseToCode.LM35:{
			  item = new LM35(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
						rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.PIR:{
			  item = new PIR(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
							rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.LCD:{
				  item = new LCD(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
								rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"), rs.getString("STRING_USE"));
			  }break;
			  case ParseToCode.ULTRA_SONIC:{
				  item = new Ultra_sonic(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
								rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.DS1302:{
				  item = new DS1302(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("TIMEON"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
							rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"), rs.getString("STRING_USE"));
			  }break;
			  case ParseToCode.SENSOR_NIVEL:{
				  item = new Sensor_nivel_liquido(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
							rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
			  case ParseToCode.INFRA_RED:{
				  item = new IR(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("ONOFF"), rs.getInt("PARENT"), rs.getString("PIN"), rs.getInt("BUTTONPIN"),
							rs.getDouble("X"), rs.getDouble("Y"), room, rs.getInt("TYPE"));
			  }break;
		   }
		}
		rs.getStatement().getConnection().close();
		rs.close();
		resultset.close();
		return item;
		}else dialogs.dialogs.showmessage("Nome ou pino já utilizado", "Existe um outro item com este mesmo nome ou pino", "Altere o nome ou pino do item e tente"
				+ " novamente.", AlertType.WARNING).show();
		resultset.close();
		return item;
	}
	
	/***
	 * Adiciona ao projeto um item criado pelo usuário
	 * @return
	 * @throws SQLException 
	 */
	public static int insertUserItem(String name, Data_item data, double x, double y, double parent, String pin, User usuario) throws SQLException{
		//Item item=null;
		int id=-1;
		ResultSet resultset=Sqlite.getResultSet("SELECT * FROM light_children WHERE userid='"+usuario.getId()+"' and name='"+name+"' or pin='"+pin+"'");
		if(!resultset.next()){
		Sqlite.insert("INSERT INTO light_children(TYPE, PARENT, X, Y, NAME, ONOFF, PIN, TIMEON, BUTTONPIN, USERID, STRING_USE) VALUES('"+"99"
						+"','"+parent+"','"+x+"','"+y+"','"+name+"','"+0+"','"+pin+"','"+0+"','"+0+"','"+usuario.getId()+"','"+data.getUID()+"')");
		ResultSet rs=Sqlite.getResultSet("SELECT * FROM light_children WHERE name='"+name+"'");
			id =rs.getInt("Id");
			rs.close();
			
		}
		resultset.close();
		return id;
	}
	
	public static void insertInputCode(int item_id, String name, String content, int type){
		Sqlite.insert("INSERT INTO INPUT_CODES(ITEM_ID, NAME, CONTENT, TYPE) VALUES("
				+ "'"+item_id+"','"+name+"','"+content+"','"+type+"')");
	}
	
	
}
