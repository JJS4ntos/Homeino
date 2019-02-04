package dialogs;

import house.Data_item;
import house.Item;
import house.Room;
import house.person_Item;
import house.code.ParseToCode;
import ia.programmer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import ExceptionAdapter.ShowErro;
import anothers.personal_filters;
import controllers.DS1302_frame_controller;
import controllers.ir_controller;
import controllers.lcd_frame_controller;
import controllers.ultra_sonic_frame_controller;
import controllers.little.item_controller;
import database.Sqlite;
import database.User;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class dialog_additem {

	private TextField tf_name= new TextField();
	private ComboBox<String> cb_pin= new ComboBox<>(), cb_button= new ComboBox<>();
	private ComboBox<Integer> cb_timeon= new ComboBox<>();
	
	public dialog_additem() {
		// TODO Auto-generated constructor stub
	}
	
	/*public Dialog<light> initDialog(room room, double x, double y){
		//Criando o dialog e adicionando as informações deste.
		dialog_light= new Dialog<light>();
		dialog_light.setHeaderText("Insira as informações da nova lâmpada");
		dialog_light.setTitle("Nova lâmpada");
		//Criando o gridpane e adicionando os objetos pertencentes a este
		GridPane gridpane= new GridPane();
		gridpane.add(new Label("Nome da lâmpada:"), 0, 0);
		gridpane.add(tf_name, 1, 0);
		tf_name.setPromptText("Ex: Lampada_da_Sala");
		ButtonType createButton= new ButtonType("Criar lâmpada", ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog_light.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		Button btn_newlight=(Button)dialog_light.getDialogPane().lookupButton(createButton);
		btn_newlight.setDisable(true);
		
		tf_name.textProperty().addListener(e->
			btn_newlight.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
			.selectedItemProperty().isNull().get())
		);
		
		cb_pin.selectionModelProperty().get()
		.selectedItemProperty().addListener(e->btn_newlight.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
			.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		cb_button.selectionModelProperty().get()
		.selectedItemProperty().addListener(e->btn_newlight.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
				.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		
		gridpane.add(new Label("Pino da lâmpada"), 0, 1);
		gridpane.add(cb_pin, 1, 1);
		gridpane.add(new Label("Pino do botão"), 0, 2);
		gridpane.add(cb_button, 1, 2);
		for(int i=1; i<53; i++){
			cb_pin.getItems().add(i);
			cb_button.getItems().add(i);
		}
		
		btn_newlight.setOnAction(e->{try{dialog_light.setResult(item_controller.insertItem(0, room.getRoomId(), x, y, tf_name.getText(), 0, cb_pin.getSelectionModel().getSelectedItem(), room));}
		catch(SQLException e1){e1.printStackTrace();}});
		dialog_light.getDialogPane().setContent(gridpane);
		dialog_light.setResultConverter(e->{
			if(e.getButtonData()==ButtonData.OK_DONE) return dialog_light.getResult();
			return null;
		});
		return dialog_light;
		
	}*/
	
	public Dialog<Item> initDialog(Room room, double x, double y, int type, User usuario){
		//Criando o dialog e adicionando as informações deste.
		Dialog<Item> dialog_= new Dialog<Item>();
		final String ITEMNAME= getItemTypeName(type);
		dialog_.setHeaderText("Insira as informações do(a) nova "+ITEMNAME);
		dialog_.setTitle("Novo(a) "+ITEMNAME);
		//Criando o gridpane e adicionando os objetos pertencentes a este
		GridPane gridpane= new GridPane();
		gridpane.add(new Label("Nome do(a) "+ITEMNAME+":"), 0, 0);
		gridpane.add(tf_name, 1, 0);
		tf_name.setPromptText("Ex: Lampada_da_Sala");
		ButtonType createButton= new ButtonType("Criar "+ITEMNAME, ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog_.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		Button btn_new=(Button)dialog_.getDialogPane().lookupButton(createButton);
		btn_new.setDisable(true);

		tf_name.textProperty().addListener(e->
		btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
			.selectedItemProperty().isNull().get()||cb_timeon.selectionModelProperty().get().selectedItemProperty().isNull().get())
		);
		
		cb_pin.selectionModelProperty().get()
		.selectedItemProperty().addListener(e->btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
			.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()
			||cb_timeon.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		
		cb_button.selectionModelProperty().get()
		.selectedItemProperty().addListener(e->btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
				.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()
				||cb_timeon.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		
		cb_timeon.selectionModelProperty().get().selectedItemProperty().addListener(e->btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
				.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()
				||cb_timeon.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		
		gridpane.add(new Label("Pino do(a) "+ITEMNAME), 0, 1);
		gridpane.add(cb_pin, 1, 1);
		gridpane.add(new Label("Pino do botão do(a) "+ITEMNAME), 0, 2);
		gridpane.add(cb_button, 1, 2);
		gridpane.add(new Label("Selecione o angulo: "), 0, 3);
		gridpane.add(cb_timeon, 1, 3);
		ArrayList<String> pins=Sqlite.getPinsEmpty(room.getlights());
		cb_button.getItems().setAll(pins);
		cb_pin.getItems().setAll(pins);
		for(int i=1; i<=180; i++)
			cb_timeon.getItems().add(i);
		for(int i=2; i<13; i++)
		btn_new.setOnAction(e->{
			try{
				dialog_.setResult(item_controller.insertItem(type, room.getRoomId(), x, y, tf_name.getText(), 0,
						cb_pin.getSelectionModel().getSelectedItem(), room,
				cb_timeon.getSelectionModel().getSelectedItem(), Integer.valueOf(cb_button.getSelectionModel().getSelectedItem()),usuario,""));
				
			}
		catch(SQLException e1){e1.printStackTrace();}});
		dialog_.getDialogPane().setContent(gridpane);
		dialog_.setResultConverter(e->{
			if(e.getButtonData()==ButtonData.OK_DONE) return dialog_.getResult();
			return null;
		});
		return dialog_;
		
	}
	
	public Dialog<Item> initDialog(Room room, double x, double y, int type, int timeon, User usuario){
		//Criando o dialog e adicionando as informações deste.
		Dialog<Item> dialog_= new Dialog<Item>();
		final String ITEMNAME= getItemTypeName(type);
		dialog_.setHeaderText("Insira as informações do(a) "+ITEMNAME);
		dialog_.setTitle("Novo(a) "+ITEMNAME);
		//Criando o gridpane e adicionando os objetos pertencentes a este
		GridPane gridpane= new GridPane();
		gridpane.add(new Label("Nome do(a) "+ITEMNAME+":"), 0, 0);
		gridpane.add(tf_name, 1, 0);
		tf_name.setPromptText("Ex: Lampada_da_Sala");
		ButtonType createButton= new ButtonType("Criar "+ITEMNAME, ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog_.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		Button btn_new=(Button)dialog_.getDialogPane().lookupButton(createButton);
		btn_new.setDisable(true);
		
		tf_name.textProperty().addListener(e->
		btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
			.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get())
		);
		
		cb_pin.selectionModelProperty().get()
		.selectedItemProperty().addListener(e->btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
			.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		cb_button.selectionModelProperty().get()
		.selectedItemProperty().addListener(e->btn_new.setDisable(personal_filters.filter(tf_name.getText())||tf_name.getText().isEmpty()||cb_pin.selectionModelProperty().get()
				.selectedItemProperty().isNull().get()||cb_button.selectionModelProperty().get().selectedItemProperty().isNull().get()));
		Label lbl_pinButton= new Label();
		lbl_pinButton.setText(type==6||type==11?"Acionar ao detectar movimento":"Pino do botão do(a) "+ITEMNAME);
		gridpane.add(new Label("Pino do(a) "+ITEMNAME), 0, 1);
		gridpane.add(cb_pin, 1, 1);
		gridpane.add(lbl_pinButton, 0, 2);
		gridpane.add(cb_button, 1, 2);
		
		ArrayList<String> pins=Sqlite.getPinsEmpty(room.getlights());
		cb_pin.getItems().setAll(pins);
		if(type==6||type==11){
			room.getlights().stream().filter(i->!i.isSensor()).forEach(i->cb_button.getItems().add(i.getName()+"::"+i.getPin()));
		}//PIR
		else if(type!=6||type!=11){
			cb_button.getItems().setAll(pins);
		}
		
		btn_new.setOnAction(e->{try{
			int buttonpin=type==6||type==11?getIdOfSelection(cb_button.getSelectionModel().getSelectedItem()):Integer.valueOf(cb_button.getSelectionModel().getSelectedItem());
			dialog_.setResult(item_controller.insertItem(type, room.getRoomId(), x, y, tf_name.getText(), 0, String.valueOf(cb_pin.getSelectionModel().getSelectedItem()), room,
				timeon, buttonpin, usuario, ""));}
		catch(SQLException e1){e1.printStackTrace();}});
		dialog_.getDialogPane().setContent(gridpane);
		dialog_.setResultConverter(e->{
			if(e.getButtonData()==ButtonData.OK_DONE) return dialog_.getResult();
			return null;
		});
		return dialog_;
		
	}

	
	//Nos items analógicos o BUTTONPIN equivale ao atuador que será acionado quando o valor do TIMEON(Valor analógico) for igual ao
	//determinado como limite na criação do item.
	public Dialog<Item> initDialogAnalog(Room room, double x, double y, int type, User usuario){
		Dialog<Item> dialog=new Dialog<>();
		dialog.setTitle("Novo item analógico");
		dialog.setHeaderText("Adicionar novo "+getItemTypeName(type));
		GridPane analogGrid= new GridPane();
		TextField tf_name= new TextField();
		ComboBox<String> cb_analog_pin= new ComboBox<>();
		ComboBox<Integer> cb_analog_value= new ComboBox<>();
		cb_analog_pin.getItems().addAll("A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","A10","A11","A12","A13","A14","A15");
		for(int i=0; i<1023;i++)cb_analog_value.getItems().add(i);
		ComboBox<String> cb_actives= new ComboBox<>(), cb_control=new ComboBox<>();
		cb_control.getItems().addAll("Maior que o disparo", "Menor que o disparo");
		room.getlights().forEach(i->{if(!i.isSensor())cb_actives.getItems().add(i.getName()+"::"+i.getPin());});
		analogGrid.add(new Label("Nome do item: "), 0, 0);
		analogGrid.add(tf_name, 1, 0);
		analogGrid.add(new Label("Nome do atuador: "), 0, 1);
		analogGrid.add(cb_actives, 1, 1);
		analogGrid.add(new Label("Pino: "), 0, 2);
		analogGrid.add(cb_analog_pin, 1, 2);
		analogGrid.add(new Label("Valor de disparo: "), 0, 3);
		analogGrid.add(cb_analog_value, 1, 3);
		analogGrid.add(new Label("Acionar quando o valor analógico: "), 0, 4);
		analogGrid.add(cb_control, 1, 4);
		dialog.getDialogPane().setContent(analogGrid);
		ButtonType createButton= new ButtonType("Criar", ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		Button button= (Button) dialog.getDialogPane().lookupButton(createButton);
		button.setDisable(true);
		tf_name.textProperty().addListener(e->{
			button.setDisable(tf_name.getText().length()<3||cb_analog_pin.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_analog_value.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_actives.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_control.selectionModelProperty().get().selectedItemProperty().isNull().get());
		});
		cb_analog_pin.selectionModelProperty().get().selectedItemProperty().addListener(e->{
			button.setDisable(tf_name.getText().length()<3||cb_analog_pin.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_analog_value.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_actives.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_control.selectionModelProperty().get().selectedItemProperty().isNull().get());
		});
		cb_analog_value.selectionModelProperty().get().selectedItemProperty().addListener(e->{
			button.setDisable(tf_name.getText().length()<3||cb_analog_pin.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_analog_value.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_actives.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_control.selectionModelProperty().get().selectedItemProperty().isNull().get());
		});
		cb_actives.selectionModelProperty().get().selectedItemProperty().addListener(e->{
			button.setDisable(tf_name.getText().length()<3||cb_analog_pin.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_analog_value.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_actives.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_control.selectionModelProperty().get().selectedItemProperty().isNull().get());
		});
		cb_control.selectionModelProperty().get().selectedItemProperty().addListener(e->{
			button.setDisable(tf_name.getText().length()<3||cb_analog_pin.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_analog_value.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_actives.selectionModelProperty().get().selectedItemProperty().isNull().get()||
					cb_control.selectionModelProperty().get().selectedItemProperty().isNull().get());
		});
		button.setOnAction(e->{
			try{dialog.setResult(item_controller.insertItem(type, room.getRoomId(), x, y, tf_name.getText(), cb_control.getSelectionModel().getSelectedIndex()
					, cb_analog_pin.getSelectionModel().getSelectedItem()
					, room, cb_analog_value.getSelectionModel().getSelectedItem(),
					getIdOfSelection(cb_actives.getSelectionModel().getSelectedItem()), usuario, ""));
			}catch(SQLException io){io.printStackTrace();}
		});
		
		dialog.setResultConverter(e->{if(e.getButtonData()==ButtonData.OK_DONE) return dialog.getResult();return null;});
		return dialog;
	}
	
	private String getItemTypeName(Integer type){
		switch(type){
		case ParseToCode.LED: return "LED/LIGHT";
		case ParseToCode.SERVO_MOTOR: return "SERVO";
		case ParseToCode.LDR: return "LDR";
		case ParseToCode.BUZZER: return "BUZZER";
		case ParseToCode.NTC_TEMP: return "NTC_TEMP";
		case ParseToCode.LM35: return "LM35";
		case ParseToCode.PIR: return "PIR";
		case ParseToCode.LCD: return "LCD";
		case ParseToCode.ULTRA_SONIC: return "ULTRA_SONIC";
		case ParseToCode.DS1302: return "DS1302";
		case ParseToCode.SENSOR_NIVEL:return "SENSOR DE NÍVEL";
		case ParseToCode.INFRA_RED: return "INFRA RED";
		default: return "";
		}
	}
	
	public Integer getIdOfSelection(String selected){
		return Integer.valueOf(selected.split("::")[1]);
	}
	
	public Dialog<Item> fxmlDialogLCD(ArrayList<Item> items, Room room, double x, double y, User usuario){
		ShowErro erro= new ShowErro();
		Dialog<Item> dialog= new Dialog<>();
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxmls/lcd_frame.fxml"));
		lcd_frame_controller Controller= new lcd_frame_controller(items);
		loader.setController(Controller);
		BorderPane bpane=null;
		try{bpane= (BorderPane)loader.load();}catch(IOException e){erro.ThereIsAException(e);}
		dialog.getDialogPane().setContent(bpane);
		ButtonType createButton= new ButtonType("Criar", ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton); 
		Button createbutton= (Button) dialog.getDialogPane().lookupButton(createButton);
		createbutton.disableProperty().bind(Controller.disableCreateproperty());
		createbutton.setOnAction(e->{
			try{
				//Quando o tipo da mensagem for igual a 0, o ONOFF do lCD será 0 para identificar o tipo da mensagem(Leitura analógica/digital)
				if(Controller.getMessageType()==0){
					dialog.setResult(item_controller.insertItem(7, room.getRoomId(), x, y, Controller.getName(), 0, Controller.getPinSelected()
								, room, 0, 0, usuario, Controller.getSelectedItem()));
				}else if(Controller.getMessageType()==1){
					dialog.setResult(item_controller.insertItem(7, room.getRoomId(), x, y, Controller.getName(), 1, Controller.getPinSelected()
							, room, 0, 0, usuario, Controller.getMessageLine1()+"\n"+Controller.getMessageLine2()));
				}
			}catch(SQLException e1){erro.ThereIsAException(e1);}
			//item_controller.insertItem(7, room.getRoomId(), x, y, name, onoff, pin, room, timeon, btnpin, usuario);
		});
		dialog.setResultConverter(e->{if(e.getButtonData()==ButtonData.OK_DONE) return dialog.getResult();return null;});
		return dialog;
	}

	public Dialog<Item> fxmlDialogUltraSonic(ArrayList<Item> items, Room room, double x, double y, User usuario){
		ShowErro erro= new ShowErro();
		Dialog<Item> dialog= new Dialog<>();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/ultra_sonic_frame.fxml"));
		ultra_sonic_frame_controller Controller= new ultra_sonic_frame_controller(items);
		loader.setController(Controller);
		BorderPane bpane=null;
		try{bpane= loader.load();}catch(IOException e){erro.ThereIsAException(e);}
		dialog.getDialogPane().setContent(bpane);
		ButtonType createButton= new ButtonType("Criar Ultrasônico", ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton); 
		Button createbutton= (Button) dialog.getDialogPane().lookupButton(createButton);
		createbutton.disableProperty().bind(Controller.disableCreateproperty());
		createbutton.setOnAction(e->{
			try{
				dialog.setResult(item_controller.insertItem(8, room.getRoomId(), x, y, Controller.getName(),
					         Controller.getOnOff(), Controller.getPin(), room, Controller.getTimeon(), Controller.getPinButton()
					         , usuario, ""));
			}catch(SQLException e1){erro.ThereIsAException(e1);}
			//item_controller.insertItem(7, room.getRoomId(), x, y, name, onoff, pin, room, timeon, btnpin, usuario);
		});
		dialog.setResultConverter(e->{if(e.getButtonData()==ButtonData.OK_DONE) return dialog.getResult();return null;});
		return dialog;
	}
	
	
	//ONOFF:0 acionamento com base na hora
	//ONOFF:1 acionamento com base na data
	//ONOFF:2 acionamento com base na hora/data
	//ONOFF:3 somente para leitura(sem nenhum acionamento condicional)
	public Dialog<Item> fxmlDialogDS1302(ArrayList<Item> items, Room room, double x, double y, User usuario){
		ShowErro erro= new ShowErro();
		Dialog<Item> dialog= new Dialog<>();
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxmls/DS1302_frame.fxml"));
		DS1302_frame_controller Controller= new DS1302_frame_controller(items);
		loader.setController(Controller);
		BorderPane bpane=null;
		try{bpane= (BorderPane)loader.load();}catch(IOException e){//erro.ThereIsAException(e);
			e.printStackTrace();
		}
		dialog.getDialogPane().setContent(bpane);
		ButtonType createButton= new ButtonType("Criar", ButtonData.OK_DONE);
		ButtonType cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton); 
		Button createbutton= (Button) dialog.getDialogPane().lookupButton(createButton);
		createbutton.disableProperty().bind(Controller.disableCreateproperty());
		createbutton.setOnAction(e->{
			try{
			dialog.setResult(item_controller.insertItem(9, room.getRoomId(), x, y, Controller.getName(),
					Controller.getTypeSelected(), Controller.getPin(), room, Controller.getCondition(), getIdOfSelection(Controller.getItemSelected()),
					usuario, Controller.getStringUse()));
			}catch(SQLException ex){erro.ThereIsAException(ex);}
		});
		dialog.setResultConverter(e->{if(e.getButtonData()==ButtonData.OK_DONE) return dialog.getResult();return null;});
		return dialog;
	}
	
	//Terminar este método
	public Dialog<Item> fxmlDialogIR(ArrayList<Item> items, Room room, double x, double y, User usuario){
		ShowErro erro= new ShowErro();
		Dialog<Item> dialog= new Dialog<>();
		FXMLLoader loader= new FXMLLoader(getClass().getResource("/fxmls/ir_frame.fxml"));
		ir_controller controller= new ir_controller(items, usuario);
		loader.setController(controller);
		GridPane gridPane=null;
		try{gridPane= (GridPane)loader.load();}catch(IOException e){e.printStackTrace();}
		dialog.getDialogPane().setContent(gridPane);
		ButtonType createButton= new ButtonType("Criar", ButtonData.OK_DONE), cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		Button btn_create= (Button)dialog.getDialogPane().lookupButton(createButton);
		btn_create.disableProperty().bind(controller.disableproperty());
		btn_create.setOnAction(e->{
			try{Item item=item_controller.insertItem(ParseToCode.INFRA_RED, room.getRoomId(), x, y, controller.getName(), 0, controller.getPin(), room, 0, 0, usuario, "");
				dialog.setResult(item);
				controller.insertChildren();
			}catch(SQLException ex){erro.ThereIsAException(ex);}
		});
		dialog.setResultConverter(e->{if(e.getButtonData()==ButtonData.OK_DONE) return dialog.getResult(); return null;});
		return dialog;
	}

	/***
	 * Adiciona ao projeto um item que foi criado pelo usuário
	 * @return
	 */
	public Dialog<Item> addUserItem(Data_item data, double x, double y, User usuario, Room room){
		//Estou me perguntando como armazenar as informações dos inputCodes inseridas no formulário
		Dialog<Item> dialog= new Dialog<>();
		ArrayList<Node> nodes=programmer.getNodes(data);
		ShowErro erro= new ShowErro();
		VBox vbox= new VBox();
		vbox.setSpacing(3);
		vbox.setPrefWidth(300);
		dialog.getDialogPane().getStyleClass().add("H-Box");
		dialog.getDialogPane().getStylesheets().add(getClass().getResource("/fxmls/softstyle.css").toExternalForm());
		ButtonType createButton= new ButtonType("Criar", ButtonData.OK_DONE),
				cancelButton= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		TextField name= new TextField();
		Label label = new Label(data.getDescription());
		label.setAlignment(Pos.CENTER);
		//label.setGraphic(new ImageView(data.getImage()));
		label.setGraphicTextGap(3);
		label.setWrapText(true);
		name.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(label, name);
			vbox.getChildren().addAll(nodes);
			dialog.getDialogPane().setContent(vbox);
			ImageView image= new ImageView(data.getImage());
			image.setFitHeight(100);
			image.setFitWidth(100);
			dialog.getDialogPane().setHeader(image);
			
		dialog.setTitle(data.getName());
		dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
		Button create=(Button)dialog.getDialogPane().lookupButton(createButton);
			   create.setOnAction(e->{
				   TextField tf_pin=(TextField)nodes.stream().filter(node->node.getId().equals("PIN")).findFirst().get();
				   //Crio o item e pego o id dele para poder criar os inputs no banco de dados
				   try {int item_id=item_controller.insertUserItem(name.getText(), data, x, y, room.getRoomId(), tf_pin.getText(), usuario);
				   //Adicionando os input_codes ao banco de dados...
				   if(item_id!=1){
				   		nodes.forEach(node->{
				   			TextField tf= (TextField)node;
				   			item_controller.insertInputCode(item_id, node.getId(), tf.getText(), 0);
				   		});
				   }else{dialogs.showmessage("Erro ao adicionar item", "Ops! Temos um problema por aqui",
						   "Parece que algum outro item já está usando este mesmo nome ou pino", AlertType.ERROR).show();;}
				   	} catch (SQLException e1) {erro.ThereIsAException(e1);}
			   });
		return dialog;
		}
	
}
