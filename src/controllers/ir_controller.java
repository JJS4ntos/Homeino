package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import database.Sqlite;
import database.User;
import house.Item;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class ir_controller implements Initializable {
	
	@FXML private Button btn_addRead, btn_delRead;
    @FXML private ComboBox<String> cb_pin, cb_items;
    @FXML private ListView<String> list_read;
    @FXML private Label lbl_warning, lbl_name, lbl_pin;
    @FXML private TextField tf_name, tf_hexValue;
		  private final ArrayList<Item> items;
	@FXML private ToggleButton btn_onoff;
		  private final User usuario;
	
    public ir_controller(ArrayList<Item> items, User usuario) {
		this.items=items;
		this.usuario=usuario;
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadController();
	}
	
	private void loadController(){
		cb_pin.getItems().setAll(Sqlite.getPinsEmpty(items));
		disable_property.bind(cb_pin.selectionModelProperty().get().selectedItemProperty().isNull().or(tf_name.textProperty().isEmpty()));
		btn_addRead.disableProperty().bind(tf_hexValue.textProperty().isEmpty().or(
				cb_items.selectionModelProperty().get().selectedItemProperty().isNull()));
		items.forEach(i->{if(!i.isSensor())cb_items.getItems().add(i.getName()+"::"+i.getPin());});
		btn_addRead.setOnAction(e->{
			String acionamento=	btn_onoff.selectedProperty().get()?"ON":"OFF";
			Item item= items.stream().filter(i-> i.getPin().equals(cb_items.getSelectionModel().getSelectedItem().split("::")[1])).findFirst().get();
			list_read.getItems().add(tf_hexValue.getText()+"____"+item.getName()+"____"+acionamento);
			tf_hexValue.clear();
			cb_items.getSelectionModel().clearSelection();
		});
		btn_onoff.selectedProperty().addListener((e1, e2, e3)->{
			if(e3.booleanValue())btn_onoff.setText("On");
			else btn_onoff.setText("Off");
		});
		
	}
	
	private SimpleBooleanProperty disable_property = new SimpleBooleanProperty(true);
	
	public SimpleBooleanProperty disableproperty(){
		return disable_property;
	}
	
	public void insertChildren(){
		list_read.getItems().forEach(i->{
			String[] token=i.split("____");
					String hexValue=token[0], item_name=token[1], acionamento=token[2];
					Item item_found=items.stream().filter(item->item.getName().equals(item_name)).findFirst().get();
					final int STATUS= acionamento.equals("On")?1:0;
					Sqlite.insertIRObject(getPin(), item_found, STATUS, hexValue, usuario);
		});
	}
	
	public String getName(){
		return tf_name.getText();
	}
	
	public String getPin(){
		return cb_pin.getSelectionModel().getSelectedItem();
	}


}
