package comunication;

import house.Item;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import anothers.personal_filters;

public class PopOvers {

	public PopOvers() {
		// TODO Auto-generated constructor stub
	}
	
	public static PopOver getDigitalPopOver(Item item, final int LANGUAGE){
		PopOver pop_changes= new PopOver();
		pop_changes.setArrowLocation(ArrowLocation.LEFT_CENTER);
		GridPane gridpane=new GridPane();
		String[] ArrayOptions=Translate.getOptions(LANGUAGE);
		gridpane.setStyle("-fx-background-color: black;");
		TextField tf_name=new TextField(item.getName());
		SimpleBooleanProperty binding= new SimpleBooleanProperty(personal_filters.filter(tf_name.getText()));
		ComboBox<String> cb_pin=new ComboBox<>();
		ComboBox<Integer> cb_pinButton=new ComboBox<>();
		for(int i=1; i<53; i++){
			cb_pin.getItems().add(i+"");
			cb_pinButton.getItems().add(i);
		}
		cb_pin.getSelectionModel().select(item.getPin());
		cb_pinButton.getSelectionModel().select(item.getPinButton());
		gridpane.setPrefSize(270, 150);
		gridpane.setHgap(5);
		gridpane.setVgap(5);
		gridpane.setAlignment(Pos.CENTER);
		gridpane.setStyle("-fx-padding: 5;");
		gridpane.add(new Label(ArrayOptions[0]+":"), 0, 0);
		gridpane.add(tf_name, 1, 0);
		gridpane.add(new Label(ArrayOptions[1]+":"), 0, 1);
		gridpane.add(cb_pin, 1, 1);
		gridpane.add(new Label(ArrayOptions[2]+":"), 0, 2);
		gridpane.add(cb_pinButton, 1, 2);
		Button buttonAtt= new Button(ArrayOptions[3]), cancelButton= new Button(ArrayOptions[5]);
		Button buttonDel= new Button(ArrayOptions[4]);
		buttonDel.setOnAction(e->{item.delete();pop_changes.hide();});
		buttonAtt.setOnAction(e->{
			item.setPin(cb_pin.getSelectionModel().getSelectedItem());
			item.setPinButton(cb_pinButton.getSelectionModel().getSelectedItem());
			item.setName(tf_name.getText());
		pop_changes.hide();
		//dialogs.dialogs.showmessage("Atualizado", "Informações alteradas com sucesso!", "As informações já foram atualizadas e prontas para uso.").show();
		item.getRoom().getCommons_loads().loadRooms();
		});
		buttonAtt.disableProperty().bind(binding.or(cb_pin.selectionModelProperty().get().selectedItemProperty().isNull().or(
				cb_pinButton.selectionModelProperty().get().selectedItemProperty().isNull())));
		cancelButton.setOnAction(e->pop_changes.hide());
		gridpane.add(buttonAtt, 0, 3);
		gridpane.add(cancelButton, 1, 3);
		gridpane.add(buttonDel, 2, 3);
		pop_changes.setAutoFix(true);
		pop_changes.setContentNode(gridpane);
		return pop_changes;
	}
	
	
	

}
