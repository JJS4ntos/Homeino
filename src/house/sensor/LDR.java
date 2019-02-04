package house.sensor;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import house.Item;

public class LDR extends Item {
	/*FALTA AS IMAGENS DOS ESTADOS E TAMB�M A JANELA DE ADI��O, ASSIM COMO O BOT�O L� NO MENU*/
	transient private Label analog_value=new Label("Aguardando sinal...");
	transient private Image img_LDR=new Image(getClass().getResource("/images/LDR_icon.png").toExternalForm());
	
	
	public LDR(int id, String name, int timeon, int onoff, int parent, String pin, int button,
			double x, double y, house.Room room, int type) {
		super(id, name, timeon, onoff, parent, pin, button, x, y, room, type,"", true);
		analog=true;
		sensor=true;
		init();
	}

	private void init(){
		img_view.setImage(img_LDR);
		analog_value.setTooltip(new Tooltip("Este � o valor enviado na comunica��o serial e corresponde ao sinal anal�gico deste sensor"));
		analog_value.setStyle("-fx-text-fill: red; -fx-font-size: 9px;");
		analog_value.textProperty().addListener(e->analog_value.setStyle("-fx-text-fill: green; -fx-font-size: 9px;"));
		//getLabel().setText(name+"("+getPin()+")");
		//this.getChildren().setAll(lbl_name, analog_value, img_view, getActive());
	}

	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}
	
	public void setAnalogValue(Integer value){
		Platform.runLater(()->analog_value.setText("AnalogRead::"+value));
	}
	
	//Esse TimeOn vai ser o valor que ele ir� disparar o atuador. Se esse valor for atingido ou ultrapassado aciono o atuador
	public void setTimeon(Integer timeon){
		this.timeon=timeon;

	}

	//O Onoff nos items anal�gicos s�o os valores anal�gicos destes. (AnalogRead());
	public void setOnOff(Integer analog){
		onoff=analog;
	}

}
