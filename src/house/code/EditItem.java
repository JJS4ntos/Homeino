package house.code;

import java.sql.SQLException;

import database.Sqlite;
import house.Item;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditItem extends Dialog<Void> {

	private TextField tf_name, tf_pin, tf_buttonpin;
	private final Item item;
	private VBox mainBox;
	
	public EditItem(Item item){
		this.item=item;
		init();
	}
	
	private void init(){
		mainBox= new VBox();
		mainBox.setPrefSize(200, 100);
		//mainBox.setStyle("-fx-background-color: LightSeaGreen; -fx-background-radius: 100;");
		mainBox.setSpacing(3);
		tf_name = new TextField(item.getName());
		tf_pin= new TextField(item.getPin());
		tf_buttonpin= new TextField(String.valueOf(item.getPinButton()));
		mainBox.getChildren().addAll(tf_name, tf_pin, tf_buttonpin);
		this.getDialogPane().setContent(mainBox);
		ButtonType OK= new ButtonType("OK", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().add(OK);
		Button btnOK=(Button)this.getDialogPane().lookupButton(OK);
		btnOK.setOnAction(e->{
			try {
				if(!Sqlite.wasFound("SELECT * FROM light_children WHERE userid='"+item.getRoom().getRoom().getUserid()+"' and name='"+tf_name.getText()+"' and "
						+ "id!= '"+item.getItemId()+"'")){
					Sqlite.updateItem(item);
					item.setName(tf_name.getText());
					Platform.runLater(new Thread(item.getRoom().getCommons_loads().getLoad()));
				}} catch (SQLException e1) {e1.printStackTrace();}
		});
	}
	
}
