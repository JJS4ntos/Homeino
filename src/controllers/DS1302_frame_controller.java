package controllers;

import house.Item;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.Sqlite;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class DS1302_frame_controller implements Initializable {

	 @FXML private ComboBox<Integer> cb_minute, cb_second, cb_month, cb_year, cb_hour,  cb_day;
     @FXML private Label lbl_date, lbl_active, lbl_time, lbl_name, lbl_pin;
	 @FXML private ComboBox<String> cb_item, cb_condition, cb_pin3, cb_pin2, cb_pin1;
     @FXML private TextField tf_name;
     private final ArrayList<Item> items;
     @FXML private RadioButton radio_date, radio_datetime, radio_time, radio_read;
     private SimpleBooleanProperty disable_property= new SimpleBooleanProperty(true);
	 @FXML private ToggleGroup options;
     
	public DS1302_frame_controller(ArrayList<Item> items) {
		// TODO Auto-generated constructor stub
		this.items=items;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		init();
	}
	
	private void init(){
		cb_condition.getItems().addAll(">", "<", "==", "!=");
		ArrayList<String> pins=Sqlite.getPinsEmpty(items);
		cb_pin1.getItems().setAll(pins);
		cb_pin2.getItems().setAll(pins);
		cb_pin3.getItems().setAll(pins);
		radio_time.selectedProperty().addListener(e->loadType());
		radio_date.selectedProperty().addListener(e->loadType());
		radio_datetime.selectedProperty().addListener(e->loadType());
		radio_read.selectedProperty().addListener(e->loadType());
		items.forEach(i->{if(!i.isSensor())cb_item.getItems().add(i.getName()+"::"+i.getPin());});
		for(int i=0; i<24; i++)cb_hour.getItems().add(i);//Para horas
		for(int i=0; i<60; i++){cb_minute.getItems().add(i);//Para minutos e segundos
			cb_second.getItems().add(i);
		}
		for(int i=0; i<32; i++)cb_day.getItems().add(i);//Para dias 
		for(int i=0; i<13; i++)cb_month.getItems().add(i);//Para meses
		for(int i=2016; i<3001; i++)cb_year.getItems().add(i);//Para anos
		
		disable_property.bind(cb_hour.selectionModelProperty().get().selectedItemProperty().isNull().or(
		cb_minute.selectionModelProperty().get().selectedItemProperty().isNull().or(
		cb_second.selectionModelProperty().get().selectedItemProperty().isNull().or(
			cb_day.selectionModelProperty().get().selectedItemProperty().isNull().or(
					cb_month.selectionModelProperty().get().selectedItemProperty().isNull().or(
							cb_year.selectionModelProperty().get().selectedItemProperty().isNull().or(
									cb_item.selectionModelProperty().get().selectedItemProperty().isNull().or(
											cb_pin1.selectionModelProperty().get().selectedItemProperty().isNull().or(
													cb_pin2.selectionModelProperty().get().selectedItemProperty().isNull().or(
														cb_pin3.selectionModelProperty().get().selectedItemProperty().isNull().or(
																tf_name.textProperty().isEmpty().or(
																		options.selectedToggleProperty().isNull().or(
																				cb_condition.selectionModelProperty().get().selectedItemProperty().isNull())))))))))))));
	}
	
	public SimpleBooleanProperty disableCreateproperty(){
		return disable_property;
	}
	
	public String getName(){
		return tf_name.getText();
	}
	
	public String getPin(){
		return cb_pin1.getSelectionModel().getSelectedItem()+":"+cb_pin2.getSelectionModel().getSelectedItem()+":"+
				cb_pin3.getSelectionModel().getSelectedItem();
	}
	
	public String getItemSelected(){
		return cb_item.getSelectionModel().getSelectedItem();
	}
	
	public String getStringUse(){
		final int TIME=0, DATE=1, DATE_TIME=2, READ=3;
		switch(getTypeSelected()){
		case TIME:return cb_hour.getSelectionModel().getSelectedItem()+":"+cb_minute.getSelectionModel().getSelectedItem()+":"+cb_second.getSelectionModel().getSelectedItem();
		case DATE:return cb_day.getSelectionModel().getSelectedItem()+":"+cb_month.getSelectionModel().getSelectedItem()+":"+cb_year.getSelectionModel().getSelectedItem();
		case DATE_TIME:return cb_day.getSelectionModel().getSelectedItem()+":"+cb_month.getSelectionModel().getSelectedItem()+":"+cb_year.getSelectionModel().getSelectedItem()+
						":"+cb_hour.getSelectionModel().getSelectedItem()+":"+cb_minute.getSelectionModel().getSelectedItem()+":"+cb_second.getSelectionModel().getSelectedItem();
		case READ:return "";
		default: return "";
		}
	}
	
	public int getTypeSelected(){
		RadioButton selected=(RadioButton) options.getSelectedToggle();
		if(selected.getId().equals("time"))return 0;
		else if(selected.getId().equals("date"))return 1;
		else if(selected.getId().equals("date_time"))return 2;
		else if(selected.getId().equals("read"))return 3;
		return -1;
	}
	
	public int getCondition(){
		return cb_condition.getSelectionModel().getSelectedIndex();
	}
	
	private void loadType(){
		if(options.getSelectedToggle().equals(radio_time)){
			disable_property.bind(cb_hour.selectionModelProperty().get().selectedItemProperty().isNull().or(
					cb_minute.selectionModelProperty().get().selectedItemProperty().isNull().or(
					cb_second.selectionModelProperty().get().selectedItemProperty().isNull().or(
					cb_item.selectionModelProperty().get().selectedItemProperty().isNull().or(
							cb_pin1.selectionModelProperty().get().selectedItemProperty().isNull().or(
									cb_pin2.selectionModelProperty().get().selectedItemProperty().isNull().or(
										cb_pin3.selectionModelProperty().get().selectedItemProperty().isNull().or(
												tf_name.textProperty().isEmpty().or(
														options.selectedToggleProperty().isNull().or(
																cb_condition.selectionModelProperty().get().selectedItemProperty().isNull()))))))))));
		}else if(options.getSelectedToggle().equals(radio_date)){
			disable_property.bind(cb_day.selectionModelProperty().get().selectedItemProperty().isNull().or(
				cb_month.selectionModelProperty().get().selectedItemProperty().isNull().or(
						cb_year.selectionModelProperty().get().selectedItemProperty().isNull().or(
								cb_item.selectionModelProperty().get().selectedItemProperty().isNull().or(
										cb_pin1.selectionModelProperty().get().selectedItemProperty().isNull().or(
												cb_pin2.selectionModelProperty().get().selectedItemProperty().isNull().or(
													cb_pin3.selectionModelProperty().get().selectedItemProperty().isNull().or(
															tf_name.textProperty().isEmpty().or(
																	options.selectedToggleProperty().isNull().or(
																			cb_condition.selectionModelProperty().get().selectedItemProperty().isNull()))))))))));
		}else if(options.getSelectedToggle().equals(radio_datetime)){
			disable_property.bind(cb_hour.selectionModelProperty().get().selectedItemProperty().isNull().or(
					cb_minute.selectionModelProperty().get().selectedItemProperty().isNull().or(
					cb_second.selectionModelProperty().get().selectedItemProperty().isNull().or(
						cb_day.selectionModelProperty().get().selectedItemProperty().isNull().or(
								cb_month.selectionModelProperty().get().selectedItemProperty().isNull().or(
										cb_year.selectionModelProperty().get().selectedItemProperty().isNull().or(
												cb_item.selectionModelProperty().get().selectedItemProperty().isNull().or(
														cb_pin1.selectionModelProperty().get().selectedItemProperty().isNull().or(
																cb_pin2.selectionModelProperty().get().selectedItemProperty().isNull().or(
																	cb_pin3.selectionModelProperty().get().selectedItemProperty().isNull().or(
																			tf_name.textProperty().isEmpty().or(
																					options.selectedToggleProperty().isNull().or(
																							cb_condition.selectionModelProperty().get().selectedItemProperty().isNull())))))))))))));
		}else if(options.getSelectedToggle().equals(radio_read)){
			disable_property.bind(cb_pin1.selectionModelProperty().get().selectedItemProperty().isNull().or(
			cb_pin2.selectionModelProperty().get().selectedItemProperty().isNull().or(
				cb_pin3.selectionModelProperty().get().selectedItemProperty().isNull().or(
						tf_name.textProperty().isEmpty().or(
								options.selectedToggleProperty().isNull())))));
		}
	}

}
