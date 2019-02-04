package controllers;

import house.Item;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Sqlite;
import anothers.personal_filters;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class lcd_frame_controller implements Initializable {
  
	@FXML private Label lbl_message_line2, lbl_message_line1, lbl_message, lbl_title, lbl_item_read, lbl_pins, lbl_name;
    @FXML private TextField tf_message_line2, tf_name, tf_message_line1;
    @FXML private ComboBox<String> cb_message, cb_pin1, cb_pin2, cb_pin3, cb_pin4, cb_pin5, cb_pin6, cb_item_read;
    private final ArrayList<Item> items;
	private SimpleBooleanProperty disable_property = new SimpleBooleanProperty(true);
    
	public lcd_frame_controller(ArrayList<Item> items) {
		this.items=items;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		tf_message_line2.setDisable(true);
		init();
		refreshProperty();
	}
	
	private void init(){
		cb_message.getItems().addAll("Leitura de sensor/atuador", "Mensagem personalizada por mim");
		ArrayList<String> pins=Sqlite.getPinsEmpty(items);
		cb_pin1.getItems().addAll(pins);
		cb_pin2.getItems().addAll(pins);
		cb_pin3.getItems().addAll(pins);
		cb_pin4.getItems().addAll(pins);
		cb_pin5.getItems().addAll(pins);
		cb_pin6.getItems().addAll(pins);
		items.forEach(i->cb_item_read.getItems().add(i.getName()+"::"+i.getPin()));
		cb_pin1.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_pin2.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_pin3.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_pin4.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_pin5.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_pin6.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_message.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_item_read.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_message.getEditor().textProperty().addListener(e->refreshProperty());
		tf_name.textProperty().addListener(e->refreshProperty());
		tf_message_line2.disableProperty().bind(cb_message.selectionModelProperty().get().selectedIndexProperty().isEqualTo(0));
		tf_message_line1.disableProperty().bind(cb_message.selectionModelProperty().get().selectedIndexProperty().isEqualTo(0));
		cb_item_read.disableProperty().bind(tf_message_line2.disableProperty().not());
		//TESTAR FORMAULÁRIO
		
	}
	
	private void refreshProperty(){
		disable_property.set(cb_message.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_pin1.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_pin2.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_pin3.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_pin4.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_pin5.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_pin6.selectionModelProperty().get().selectedItemProperty().isNull().get()||
		cb_item_read.selectionModelProperty().get().selectedItemProperty().isNull().get()||tf_name.getText().isEmpty()
		||personal_filters.filter(tf_name.getText()));
	}
	
	
	public SimpleBooleanProperty disableCreateproperty(){
		return disable_property;
	}
	
	public int getMessageType(){
		return cb_message.getSelectionModel().getSelectedIndex();
	}
	
	public String getName(){
		return tf_name.getText();
	}
	
	public String getPinSelected(){
		String pin= cb_pin1.getSelectionModel().getSelectedItem()+":"+cb_pin2.getSelectionModel().getSelectedItem()+":"+
					cb_pin3.getSelectionModel().getSelectedItem()+":"+cb_pin4.getSelectionModel().getSelectedItem()+":"+
					cb_pin5.getSelectionModel().getSelectedItem()+":"+cb_pin6.getSelectionModel().getSelectedItem();
		return pin;
	}
	
	public String getSelectedItem(){
		return cb_item_read.getSelectionModel().getSelectedItem().split("::")[1];
	}
	
	public String getMessageLine1(){
		return tf_message_line1.getText().substring(0, 16);
	}
	
	public String getMessageLine2(){
		return tf_message_line2.getText().substring(0, 16);
	}

}
