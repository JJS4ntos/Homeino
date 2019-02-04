package house.sensor;

import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Sqlite;
import house.Item;
import house.Room;
import house.code.conditions.ir_children;

public class IR extends Item {

	//private Label analog_value=new Label("Aguardando sinal...");
	transient private Image img_IR=new Image(getClass().getResource("/images/IR.png").toExternalForm());
	
	public IR(int id, String name, int onoff, int parent,
			String pin, int button, double x, double y, Room room, int type) {
		super(id, name, onoff, parent, pin, button, x, y, room, type,"", true);
		img_view.setImage(img_IR);
		init();
	}
	
	private void init(){
		StringBuffer sb= new StringBuffer();
		try{getIRChildren().forEach(i->sb.append(i.getItem_active_id()+"	"+i.getHexValue()+"		"+(i.getOnoff()==1?"ON":"OFF")+"\n"));}
		catch(SQLException e){e.printStackTrace();}
		//getActive().setText(sb.toString());
		//this.prefWidthProperty().bind(getActive().prefWidthProperty());
	}

	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}
	
	public ArrayList<ir_children> getIRChildren() throws SQLException{
		ArrayList<ir_children> ir_children_list= new ArrayList<>();
		ResultSet rs=Sqlite.getResultSet("SELECT * FROM IR_object WHERE parent='"+getPin()+"'");
		while(rs.next()){
			ir_children ir_children= new ir_children(rs.getInt("id"), rs.getInt("parent"), rs.getInt("item_active_id"), rs.getInt("onoff"), rs.getInt("userid"),
					rs.getString("hexvalue"));
			ir_children_list.add(ir_children);
		}
		rs.getStatement().getConnection().close();
		rs.getStatement().close();
		rs.close();
		return ir_children_list;
	}

}
