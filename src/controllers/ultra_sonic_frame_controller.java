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

public class ultra_sonic_frame_controller implements Initializable {

 	@FXML private Label lbl_cm, lbl_active, lbl_title, lbl_name;
    @FXML private ComboBox<String> cb_item, cb_onoff, cb_trigger, cb_echo;
    @FXML private ComboBox<Integer> cb_cm;
    @FXML private TextField tf_name;
    private final ArrayList<Item> items;
    private SimpleBooleanProperty disable_property= new SimpleBooleanProperty(true);
    
	public ultra_sonic_frame_controller(ArrayList<Item> items) {
		this.items=items;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		init();
	}
	
	private void init(){
		ArrayList<String> pins=Sqlite.getPinsEmpty(items);
			cb_trigger.getItems().addAll(pins);
			cb_echo.getItems().addAll(pins);
		
		cb_onoff.getItems().setAll(">","<","!=");
		for(int i=0; i<400;i++)cb_cm.getItems().add(i);
		
		items.forEach(i->{
			if(!i.isSensor())
			cb_item.getItems().addAll(i.getName()+"::"+i.getPin());	
		});
		
		tf_name.textProperty().addListener(e->refreshProperty());
		cb_item.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_trigger.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_echo.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_cm.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
		cb_onoff.selectionModelProperty().get().selectedItemProperty().addListener(e->refreshProperty());
	}
	
	public void refreshProperty(){
		disable_property.set(cb_trigger.selectionModelProperty().get().selectedItemProperty().isNull().get()||
				cb_echo.selectionModelProperty().get().selectedItemProperty().isNull().get()||
				cb_cm.selectionModelProperty().get().selectedItemProperty().isNull().get()||
				cb_item.selectionModelProperty().get().selectedItemProperty().isNull().get()||
				cb_onoff.selectionModelProperty().get().selectedItemProperty().isNull().get()||
				tf_name.getText().isEmpty()||personal_filters.filter(tf_name.getText()));
	}
	
	public SimpleBooleanProperty disableCreateproperty(){
		return disable_property;
	}
	
	public String getName(){
		return tf_name.getText();
	}
	
	public String getPin(){
		return cb_trigger.getSelectionModel().getSelectedItem()+":"+cb_echo.getSelectionModel().getSelectedItem();
	}
	
	public int getOnOff(){
		return cb_onoff.getSelectionModel().getSelectedIndex();
	}
	
	public int getTimeon(){
		return cb_cm.getSelectionModel().getSelectedItem();
	}
	
	public int getPinButton(){
		return Integer.valueOf(cb_item.getSelectionModel().getSelectedItem().split("::")[1]);
	}
	
	

}
