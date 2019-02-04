package house.digital;

import house.Item;
import house.Room;
import java.io.IOException;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import anothers.personal_filters;
import database.Sqlite;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ServoMotor extends Item {

	transient private Image servo= new Image(getClass().getResource("/images/SERVO_icon.png").toExternalForm());
	transient private Label lbl_status;
	
	
	public ServoMotor(int id, String name, int timeon, int onoff, int parent, String pin, int button, double x, double y, Room room, int type) {
		super(id, name, timeon, onoff, parent, pin, button, x, y, room, type,"", false);	
		this.timeon=timeon;
		//lbl_name.setText(name);
		lbl_status=new Label();
		img_view.setImage(servo);
		if(onoff==1){
			lbl_status.setStyle("-fx-text-fill: lightgreen;-fx-font-size: 9px;");
			lbl_status.setText("(Servo acionado)");
			turn.setSelected(true);
		}else{
		lbl_status.setStyle("-fx-text-fill: red;-fx-font-size: 9px;");
		lbl_status.setText("(Servo não acionado)");
		turn.setSelected(false);
		}
		init();
	}
	
	public void init(){
		//loadObjectFromItem();
	}
	
	
	public void setTimeon(int timeon){
		this.timeon=timeon;
	}

	@Override
	public void initPopOver() {
		pop_changes= new PopOver();
		pop_changes.setArrowLocation(ArrowLocation.LEFT_CENTER);
		
		GridPane gridpane=new GridPane();
		TextField tf_name=new TextField(getName());
		SimpleBooleanProperty binding= new SimpleBooleanProperty(personal_filters.filter(tf_name.getText()));
		ComboBox<String> cb_pin=new ComboBox<>();
		ComboBox<Integer> cb_pinButton=new ComboBox<>();
		for(int i=1; i<53; i++){
			cb_pin.getItems().add(i+"");
			cb_pinButton.getItems().add(i);
		}
		cb_pin.getSelectionModel().select(getPin());
		cb_pinButton.getSelectionModel().select(getPinButton());
		gridpane.setPrefSize(270, 150);
		gridpane.setHgap(5);
		gridpane.setVgap(5);
		gridpane.setAlignment(Pos.CENTER);
		gridpane.setStyle("-fx-padding: 5;");
		gridpane.add(new Label("Nome do servo: "), 0, 0);
		gridpane.add(tf_name, 1, 0);
		gridpane.add(new Label("Pino do servo: "), 0, 1);
		gridpane.add(cb_pin, 1, 1);
		gridpane.add(new Label("Pino do botão acionador: "), 0, 2);
		gridpane.add(cb_pinButton, 1, 2);
		Button buttonAtt= new Button("Aplicar mudanças"), cancelButton= new Button("Cancelar");
		Button buttonDel= new Button("Excluir");
		buttonDel.setOnAction(e->{
			if(!dialogs.dialogs.createConfirmationDialog("Atenção", "Realmente deseja excluir esse item?",
					"Após excluído ele não poderá ser recuperado.").showAndWait().get().getButtonData().isCancelButton()){
				Sqlite.insert("DELETE FROM light_children WHERE id='"+getId()+"'");
				dialogs.dialogs.showmessage("Terminado", "Item excluído com sucesso!", "Não é possível recupera-lo.", AlertType.INFORMATION);
				this.getChildren().clear();
				this.setVisible(false);
				pop_changes.hide();
				getRoom().loadLights();
			}
		});
		buttonAtt.setOnAction(e->{
			setPin(cb_pin.getSelectionModel().getSelectedItem());
			setPinButton(cb_pinButton.getSelectionModel().getSelectedItem());
			setName(tf_name.getText());
		pop_changes.hide();
		dialogs.dialogs.showmessage("Atualizado", "Informações alteradas com sucesso!",
				"As informações já foram atualizadas e prontas para uso.", AlertType.INFORMATION).show();
		getRoom().loadLights();
		});
		buttonAtt.disableProperty().bind(binding.or(cb_pin.selectionModelProperty().get().selectedItemProperty().isNull().or(
				cb_pinButton.selectionModelProperty().get().selectedItemProperty().isNull())));
		cancelButton.setOnAction(e->pop_changes.hide());
		gridpane.add(buttonAtt, 0, 3);
		gridpane.add(cancelButton, 1, 3);
		gridpane.add(buttonDel, 0, 4);
		pop_changes.setAutoFix(true);
		pop_changes.setContentNode(gridpane);
		
	}

	public void loadObjectFromItem() {
		turn.selectedProperty().addListener((e,e1,e2)->{
			if(e1.booleanValue()){
				lbl_status.setStyle("-fx-text-fill: red;-fx-font-size: 9px;");
				lbl_status.setText("(Servo não acionado)");
				setOnoff(0);
				try {getRoom().sendSinal(Integer.valueOf(this.getPin()).byteValue(), onoff.byteValue());} catch (IOException io) {io.printStackTrace();}
				//lbl_name.setTextFill(Color.RED);
				//turn.setText("Desligada");
			}
			else{
				lbl_status.setStyle("-fx-text-fill: green;-fx-font-size: 9px;");
				lbl_status.setText("(Servo acionado)");
				setOnoff(1);
				try {getRoom().sendSinal(Integer.valueOf(this.getPin()).byteValue(), onoff.byteValue());} catch (IOException io) {io.printStackTrace();}
				//lbl_name.setTextFill(Color.GREEN);
				//turn.setText("Ligada");
			}
		});
    	//this.minWidthProperty().bindBidirectional(img_view.fitWidthProperty());
		img_view.setOnMouseClicked(e->{
			if(e.getClickCount()==2){
				if(!pop_changes.isShowing()){
					pop_changes.show(img_view);
				}else{
					pop_changes.hide(Duration.ZERO);
				}
			}
		});
		this.setAlignment(Pos.CENTER);
		this.getChildren().setAll(getLabel(), img_view, lbl_status, turn);
		this.setLayoutX(x);
		this.setLayoutY(y);	
	}
	
	public void setOnoff(Integer onoff) {
		attStatusLight(onoff);
		this.onoff = onoff;
		if(onoff==1)turn.setSelected(true);//img_view.setImage(light_on);
		else turn.setSelected(false);//img_view.setImage(light_off);
	}


}
