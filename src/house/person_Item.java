package house;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comunication.dataBtnSend;
import database.Sqlite;
import house.code.InputCode;
import javafx.scene.control.Button;

/**
 * Esta classe corresponde ao item criado pelo usuário o tipo sempre será 99
 * @author JJSantos
 *
 */
public class person_Item extends Item{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*ATENÇÃO, ADAPTAR ESSA CLASSE PARA QUE ELA SEJA UM ITEM INSERIDO NO PROJETO, COM BASE NO DATA_ITEM QUE CONTERÁ AS INFORMAÇÕES DELE*/
	private final double UID;
	private final Data_item data;
	private ArrayList<InputCode> inputs;
	//super(id, data.getName(), timeon, onoff, room.getRoom().getId(), pin, button, x, y, room, Data_item.serialVersionUID, string_use, sensor);
	public person_Item(String name, Data_item data, int id, double x, double y, Room room, String pin){
		super(id, data.getName(), 0, 0, room.getRoom().getId(), pin, 0, x, y, room, 99, "", data.isSensor());
		try {loadInputCodes();} catch (SQLException e) {e.printStackTrace();}
		this.UID=data.getUID();
		this.img_view.setImage(data.getImage());
		this.img_view.setFitHeight(100);
		this.img_view.setFitWidth(100);
		this.data=data;
		loadSends();
	}
	
	public Data_item getData(){
		return data;
	}
	
	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}
	
	public double getUID(){
		return UID;
	}
	
	public void loadInputCodes() throws SQLException{
		inputs= new ArrayList<>();
		ResultSet rs=Sqlite.getResultSet("SELECT * FROM INPUT_CODES WHERE ITEM_ID='"+this.getItemId()+"'");
		while(rs.next()){
			InputCode input= new InputCode(rs.getString("NAME"), rs.getInt("type"));
			input.setInput(rs.getString("CONTENT"));
			input.setId(String.valueOf(rs.getInt("ID")));
			inputs.add(input);
		}
		rs.getStatement().getConnection().close();
		rs.getStatement().close();
		rs.close();
		
	}

	public ArrayList<InputCode> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<InputCode> inputs) {
		this.inputs = inputs;
	}
	
	public void loadSends(){
		if(data.getSends()!=null)
		for(dataBtnSend dataSend:data.getSends()){
			Button btnSend= new Button(dataSend.getName());
			btnSend.setOnAction(e->{
				getRoom().getCommons_loads().getArduino().write(dataSend.getSendContent().getBytes());
			});
			this.getChildren().add(btnSend);
		}
	}
	/*private String getPin(String pins[]){
		StringBuffer sb= new StringBuffer();
		//Verifico se o comprimento dos pinos são iguais tanto no data_item quanto no item criado.
		if(data.getPins().length==pins.length){
			for(int i=0; i<data.getPins().length; i++){
				//Verifica se nos pinos analógicos o array recebido segue o padrão "An", exemplo: "A0", "A5", "A2";
				if(data.getPins()[i].startsWith("A")){
					sb.append(arg0)
				}
			}
		}
	}*/

}
