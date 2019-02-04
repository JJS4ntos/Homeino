package house.sensor;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import house.Item;

public class LM35 extends Item {
	
	private Label analog_value= new Label();
	
	public LM35(int id, String name, int timeon, int onoff, int parent,
			String pin, int button, double x, double y, house.Room room,
			int type) {
		super(id, name, timeon, onoff, parent, pin, button, x, y, room, type,"", true);
		analog=true;
		init();
	}

	private void init(){
		img_view= new ImageView(new Image(getClass().getResource("/images/LM35_ICON.png").toExternalForm()));
		analog_value.setTooltip(new Tooltip("Temperatura medida"));
		analog_value.setStyle("-fx-text-fill: red; -fx-font-size: 9px;");
		analog_value.textProperty().addListener(e->analog_value.setStyle("-fx-text-fill: green; -fx-font-size: 9px;"));
		//getLabel().setText(name+"("+getPin()+")");
		//this.getChildren().setAll(lbl_name, analog_value, img_view, getActive());
	}

	public void setAnalogValue(Integer value){
		Platform.runLater(()->analog_value.setText(value+"ºC"));
	}
	
	//Esse TimeOn vai ser o valor que ele irá disparar o atuador. Se esse valor for atingido ou ultrapassado aciono o atuador
	public void setTimeon(Integer timeon){
		this.timeon=timeon;
	}

	//O Onoff nos items analógicos são os valores analógicos destes. (AnalogRead());
	public void setOnOff(Integer analog){
		onoff=analog;
	}
	
	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}

}
