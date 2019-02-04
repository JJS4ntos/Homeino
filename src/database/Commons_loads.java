package database;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import ExceptionAdapter.ShowErro;
import comunication.Translate;
import house.Item;
import house.Room;
import house.code.ParseToCode;
import controllers.telaprincipal_controller;
import controllers.telaprincipal_controller.serialArduino;
import dialogs.dialog_additem;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class Commons_loads{

	//private ArrayList<Item> all_items;
	private final TabPane tab_root;
	private final User usuario;
	private final serialArduino arduino;
	private final ParseToCode code;
	private final ToggleGroup group_items;
	private final StackPane main;
	private final TextArea out; 
	private LocalDateTime now= LocalDateTime.now();
	public ArrayList<Room> rooms;
	
	public Commons_loads(TabPane tab_root, User usuario, serialArduino arduino, ParseToCode code, ToggleGroup group_items, TextArea out) {
		this.tab_root=tab_root;
		this.out=out;
		this.usuario=usuario;
		this.arduino=arduino;
		this.code=code;
		this.group_items=group_items;
		main=(StackPane)tab_root.getParent().getParent().getParent();
	}
	
	public Task<Void> getLoad(){
		//TASK QUE CARREGA TODAS AS ROOMS E OS SEUS CÓDIGOS
				Task<Void> load_rooms= new Task<Void>(){
					protected Void call(){
						Platform.runLater(()->tab_root.getTabs().clear());
						code.clear();
						//all_items= new ArrayList<>();
						ArrayList<Data_room> data_rooms=Sqlite.getDatarooms(usuario.getId());
						ArrayList<Room> tab_room= new ArrayList<>();
						rooms= new ArrayList<>();
						data_rooms.forEach(i->{
							Room room=new Room(i, code, arduino, Commons_loads.this);
							rooms.add(room);
							room.getContent().setOnMouseClicked(e->{
								Platform.runLater(()->{
								//Se o item selecionado for uma luz
								if(group_items.getSelectedToggle()!=null){
									ToggleButton button=(ToggleButton)group_items.getSelectedToggle();
									switch(button.idProperty().getValue()){
										case "btn_light":{
											createItem(room, e.getX(), e.getY(), 0, 0);
										}break;
										case "btn_servo":{
											createItem(room, e.getX(), e.getY(), ParseToCode.SERVO_MOTOR, false);
										}break;
										case "btn_ldr":{
											createItem(room, e.getX(), e.getY(), ParseToCode.LDR, true);
										}break;
										case "btn_buzzer":{
											createItem(room, e.getX(), e.getY(), ParseToCode.BUZZER, 0);
										}break;
										case "btn_ntc_temp":{
											createItem(room, e.getX(), e.getY(), ParseToCode.NTC_TEMP, true);
										}break;
										case "btn_lm35":{
											createItem(room, e.getX(), e.getY(), ParseToCode.LM35, true);
										}break;
										case "btn_LCD":{
											createfxmlItem(room, e.getX(), e.getY(), ParseToCode.LCD);
										}break;
										case "btn_PIR":{
											createItem(room, e.getX(), e.getY(), ParseToCode.PIR, 0);
										}break;
										case "btn_ultrasonic":{
											createfxmlItem(room, e.getX(), e.getY(), ParseToCode.ULTRA_SONIC);
										}break;
										case "btn_DS1302":{
											createfxmlItem(room, e.getX(), e.getY(), ParseToCode.DS1302);
										}break;
										case "btn_snivel":{
											createItem(room, e.getX(), e.getY(), ParseToCode.SENSOR_NIVEL, 0);
										}break;
										case "btn_IR":{
											createfxmlItem(room, e.getX(), e.getY(), ParseToCode.INFRA_RED);
										}break;
										default:{
											dialog_additem dialog= new dialog_additem();
											dialog.addUserItem(telaprincipal_controller.data_items.stream().filter(dataitem-> dataitem.getName().equals(button.getId())).findFirst().get()
													, e.getX(), e.getY(), usuario, room).show();
										}break;
								}
								/*//Se o botão light for pressionado
								if(button.idProperty().getValue().equals("btn_light")){
									createItem(room, e.getX(), e.getY(), 0, 0);
								}
								//Se o botão servo for pressionado
								else if(button.idProperty().getValue().equals("btn_servo")){
									createItem(room, e.getX(), e.getY(), ParseToCode.SERVO_MOTOR, false);
								}
								//Se o botão do LDR for pressionado
								else if(button.idProperty().getValue().equals("btn_ldr")){
									createItem(room, e.getX(), e.getY(), ParseToCode.LDR, true);
								}
								//Se o botão do buzzer for pressionado
								else if(button.idProperty().getValue().equals("btn_buzzer")){
									createItem(room, e.getX(), e.getY(), ParseToCode.BUZZER, 0);
								}
								else if(button.idProperty().getValue().equals("btn_ntc_temp")){
									createItem(room, e.getX(), e.getY(), ParseToCode.NTC_TEMP, true);
								}
								else if(button.idProperty().getValue().equals("btn_lm35")){
									createItem(room, e.getX(), e.getY(), ParseToCode.LM35, true);
								}
								else if(button.idProperty().getValue().equals("btn_PIR")){
									createItem(room, e.getX(), e.getY(), ParseToCode.PIR, 0);
								} 
								else if(button.idProperty().getValue().equals("btn_LCD")){
									createfxmlItem(room, e.getX(), e.getY(), ParseToCode.LCD);
								}
								else if(button.idProperty().getValue().equals("btn_ultrasonic")){
									createfxmlItem(room, e.getX(), e.getY(), ParseToCode.ULTRA_SONIC);
								}
								else if(button.idProperty().getValue().equals("btn_DS1302")){
									createfxmlItem(room, e.getX(), e.getY(), ParseToCode.DS1302);
								}
								else if(button.idProperty().getValue().equals("btn_snivel")){
									createItem(room, e.getX(), e.getY(), ParseToCode.SENSOR_NIVEL, 0);
								}
								else if(button.idProperty().getValue().equals("btn_IR")){//terminar aqui!!!!
									createfxmlItem(room, e.getX(), e.getY(), ParseToCode.INFRA_RED);
								}*/
									
								button.setSelected(false);
								//room.loadLights();
							}//Fim do if group
							});});
							tab_room.add(room);
							//all_items.addAll(room.getLoadedLight());
							print("Carregado o cômodo "+room.getRoom().getName());
						});
						
						Platform.runLater(()->{
							code.getCode();
							tab_root.getTabs().setAll(tab_room);
							print("O código foi gerado de acordo com os itens adicionados");
						});
						telaprincipal_controller.data_rooms= new Data_room[rooms.size()];
						for(int i=0; i<rooms.size();i++)telaprincipal_controller.data_rooms[i]=rooms.get(i).getRoom();
						return null;
					}
					
				};
				
				return load_rooms;
	}
	
	public void refreshBackgroundRoom(String name){
		ShowErro erro= new ShowErro();
		tab_root.getTabs().forEach(i->{
			Room room= (Room)i;
			if(room.getRoom().getName().equals(name)){
				Platform.runLater(()->{
					try{room.refreshBackground();print("Alterado a imagem do cômodo "+room.getRoom().getName());}
					catch(SQLException e){erro.ThereIsAException(e);}
					catch(IOException e){erro.ThereIsAException(e);}
				});
			}
		});
	}
	
	public void print(String string){
		out.appendText("["+now+"]"+string+"\n");
	}
	
	public void loadRooms(){
		HBox hbox=(HBox)main.getChildren().get(1);
		Task<Void> load_rooms= getLoad();
		hbox.visibleProperty().bind(load_rooms.runningProperty());
		//int index=tab_root.getSelectionModel().getSelectedIndex();
		Platform.runLater(()->new Thread(load_rooms).start());
	}
	
	public void createfxmlItem(Room room, double x, double y, int type){
		dialog_additem dialog= new dialog_additem();
		Optional<Item> result=null;
		//Verifico qual o tipo do item e crio o seu dialog
		switch(type){
			case ParseToCode.DS1302: result=dialog.fxmlDialogDS1302(room.getlights(), room, x, y, usuario).showAndWait();
			break;
			case ParseToCode.INFRA_RED: result=dialog.fxmlDialogIR(room.getlights(), room, x, y, usuario).showAndWait();
			break;
			case ParseToCode.LCD: result=dialog.fxmlDialogLCD(room.getlights(), room, x, y, usuario).showAndWait();
			break;
			case ParseToCode.ULTRA_SONIC: result=dialog.fxmlDialogUltraSonic(room.getlights(), room, x, y, usuario).showAndWait();
			break;
		}
		if(result.isPresent()){
			Item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}
	
	/*public void createEspecialDS1302(Room room, double x, double y){
		dialog_additem dialog= new dialog_additem();
		Optional<Item> result=dialog.fxmlDialogDS1302(room.getlights(), room, x, y, usuario).showAndWait();
		if(result.isPresent()){
			Item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}
	
	public void createEspecialLCD(Room room, double x, double y){
		dialog_additem dialog= new dialog_additem();
		Optional<Item> result=dialog.fxmlDialogLCD(room.getlights(), room, x, y, usuario).showAndWait();
		if(result.isPresent()){
			Item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}
	
	public void createEspecialUltraSonic(Room room, double x, double y){
		dialog_additem dialog= new dialog_additem();
		Optional<Item> result=dialog.fxmlDialogUltraSonic(room.getlights(), room, x, y, usuario).showAndWait();
		if(result.isPresent()){
			Item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}*/
	
	//Para acionamentos digitais comuns
	private void createItem(Room room, double x, double y, int type, int timeon){
		dialog_additem dialogs= new dialog_additem();
		Optional<Item> result=dialogs.initDialog(room, x, y, type, timeon, usuario).showAndWait();
		if(result.isPresent()){
			Item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}
	
	//Para analógicos e atuadores especiais
	private void createItem(Room room, double x, double y, int type, boolean analog){
		dialog_additem dialogs= new dialog_additem();
		Optional<Item> result=null;
		if(!analog)result=dialogs.initDialog(room, x, y, type, usuario).showAndWait();
		else result=dialogs.initDialogAnalog(room, x, y, type, usuario).showAndWait();
		if(result.isPresent()){
			Item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}

	public void changeMenuLanguage(final int LANGUAGE, Menu main, Menu home, Menu configuration, Menu help, Tab tab_comand, Tab tab_bytes, Tab tab_reg,
			Tab tab_myhouse, Tab tab_mycode, TextArea txt_comand, TextField tf_comand){
		switch(LANGUAGE){
			case Translate.GERMANY:{
				//MENUBAR
				main.setText("Zuerst");
				home.setText("Haus");
				configuration.setText("Einstellungen");
				help.setText("Hilfe");
				main.getItems().get(0).setText("Raum erstellen");
				main.getItems().get(1).setText("Einstellungen speichern");
				main.getItems().get(2).setText("Carregar projeto...");
				main.getItems().get(3).setText("Verlassen");
				//home.getItems().get(0).setText("Fügen sie eine neue lampe");
				//home.getItems().get(1).setText("Fügen sie einen neuen LDR");
				home.getItems().get(0).setText("Ändern sie hintergrundbild eines raumes");
				home.getItems().get(1).setText("Anzeige generierten code");
					Menu languages=(Menu) configuration.getItems().get(0);
					languages.setText("Sprachen");
					languages.getItems().get(0).setText("Deutsch");
					languages.getItems().get(1).setText("Spanisch");
					languages.getItems().get(2).setText("Französisch");
					languages.getItems().get(3).setText("Englisch");
					languages.getItems().get(4).setText("Italienisch");
					languages.getItems().get(5).setText("Portugiesisch");
				configuration.getItems().get(1).setText("Ändern sie lhr konto");
				help.getItems().get(0).setText("Über das projekt");
				//FIM DA TRADUÇÃO DO MENUBAR
				//TABELAS SUPERIORES
				tab_myhouse.setText("Mein haus");
				tab_mycode.setText("Generierten code");
				//TABELAS INFERIORES
				tab_comand.setText("Command line");
				tab_bytes.setText("Antwort Byte");
				tab_reg.setText("Berichte");
				//COMPONENTES INFERIORES
				txt_comand.setPromptText("Kein Befehl eingegeben noch");
				tf_comand.setPromptText("Geben sie den befehl und drücken sie Enter");
			}break;
			case Translate.SPANISH:{
				main.setText("Primero");
				home.setText("Casa");
				configuration.setText("Configuración");
				help.setText("Apoyo");
				main.getItems().get(0).setText("Crear una habitación");
				main.getItems().get(1).setText("Guardar los ajustes");
				main.getItems().get(2).setText("Carregar projeto...");
				main.getItems().get(3).setText("Dejar");
				//home.getItems().get(0).setText("Crear una nueva lámpara");
				//home.getItems().get(1).setText("Crear un nuevo LDR");
				home.getItems().get(0).setText("Ellos cambiar imagen de fondo de una habitación");
				home.getItems().get(1).setText("Visualización del código generado");
					Menu languages=(Menu) configuration.getItems().get(0);
					languages.setText("Idiomas");
					languages.getItems().get(0).setText("Alemán");
					languages.getItems().get(1).setText("Español");
					languages.getItems().get(2).setText("Francés");
					languages.getItems().get(3).setText("Englisch");
					languages.getItems().get(4).setText("Inglés");
					languages.getItems().get(5).setText("Portugués");
				configuration.getItems().get(1).setText("Cambiar datos de la cuenta");
				help.getItems().get(0).setText("Sobre el proyecto");
				//FIM DA TRADUÇÃO DO MENUBAR
				//TABELAS SUPERIORES
				tab_myhouse.setText("Mi casa");
				tab_mycode.setText("Código generado");
				//TABELAS INFERIORES
				tab_comand.setText("Línea de comandos");
				tab_bytes.setText("Byte de respuesta");
				tab_reg.setText("Informes");
				//COMPONENTES INFERIORES
				txt_comand.setPromptText("Sin embargo comando entró");
				tf_comand.setPromptText("Escriba un comando y presione ENTER");
			}break;
			case Translate.FRANCE:{
				main.setText("Principal");
				home.setText("Maison");
				configuration.setText("Configuration");
				help.setText("Support");
				main.getItems().get(0).setText("Créer une chambre");
				main.getItems().get(1).setText("Enregistrer les paramètres");
				main.getItems().get(2).setText("Carregar projeto...");
				main.getItems().get(3).setText("Laisser");
				//home.getItems().get(0).setText("Créer une nouvelle lampe");
				//home.getItems().get(1).setText("Créer un nouveau LDR");
				home.getItems().get(0).setText("Image de fond d'une chambre Ils changent");
				home.getItems().get(1).setText("Affichage du code généré");
					Menu languages=(Menu) configuration.getItems().get(0);
					languages.setText("Langues");
					languages.getItems().get(0).setText("Allemand");
					languages.getItems().get(1).setText("Espagnol");
					languages.getItems().get(2).setText("Français");
					languages.getItems().get(3).setText("Anglais");
					languages.getItems().get(4).setText("Italien");
					languages.getItems().get(5).setText("Portugais");
				configuration.getItems().get(1).setText("Modifier les données de compte");
				help.getItems().get(0).setText("A propos du projet");
				//FIM DA TRADUÇÃO DO MENUBAR
				//TABELAS SUPERIORES
				tab_myhouse.setText("Ma maison");
				tab_mycode.setText("Code généré");
				//TABELAS INFERIORES
				tab_comand.setText("Ligne de commande");
				tab_bytes.setText("Octet de réponse");
				tab_reg.setText("Information");
				//COMPONENTES INFERIORES
				txt_comand.setPromptText("Cependant commande entrée");
				tf_comand.setPromptText("Tapez une commande et appuyez sur ENTRER");
			}break;
			case Translate.ITALY:{
				main.setText("Principale");
				home.setText("Casa");
				configuration.setText("Impostazioni");
				help.setText("Aiuto");
				main.getItems().get(0).setText("Creare lo spazio");
				main.getItems().get(1).setText("Salva impostazioni");
				main.getItems().get(2).setText("Carregar projeto...");
				main.getItems().get(3).setText("Abbandono");
				//home.getItems().get(0).setText("Si prega di aggiungere una nuova lampada");
				//home.getItems().get(1).setText("Si prega di aggiungere un nuovo LDR");
				home.getItems().get(0).setText("Cambiare loro immagine di sfondo di una stanza");
				home.getItems().get(1).setText("Visualizzazione codice generato");
					Menu languages=(Menu) configuration.getItems().get(0);
					languages.setText("Lingue");
					languages.getItems().get(0).setText("Tedesco");
					languages.getItems().get(1).setText("Spagnolo");
					languages.getItems().get(2).setText("Francese");
					languages.getItems().get(3).setText("Inglese");
					languages.getItems().get(4).setText("Italiano");
					languages.getItems().get(5).setText("Portoghese");
				configuration.getItems().get(1).setText("Cambiare a lei conto");
				help.getItems().get(0).setText("Il Progetto");
				//FIM DA TRADUÇÃO DO MENUBAR
				//TABELAS SUPERIORES
				tab_myhouse.setText("La mia casa");
				tab_mycode.setText("Codice generato");
				//TABELAS INFERIORES
				tab_comand.setText("Riga di comando");
				tab_bytes.setText("Risposta byte");
				tab_reg.setText("Rapporti");
				//COMPONENTES INFERIORES
				txt_comand.setPromptText("Nessun comando entrato ancora");
				tf_comand.setPromptText("Inserire il comando e premere Invio");
			}break;
			case Translate.PORTUGUESE:{
				main.setText("Principal");
				home.setText("Casa");
				configuration.setText("Configurações");
				help.setText("Ajuda");
				main.getItems().get(0).setText("Criar um novo cômodo");
				main.getItems().get(1).setText("Salvar projeto...");
				main.getItems().get(2).setText("Carregar projeto...");
				main.getItems().get(3).setText("Sair");
				//home.getItems().get(0).setText("Si prega di aggiungere una nuova lampada");
				//home.getItems().get(1).setText("Si prega di aggiungere un nuovo LDR");
				home.getItems().get(0).setText("Alterar a imagem de fundo de um cômodo");
				home.getItems().get(1).setText("Visualizar código gerado");
					Menu languages=(Menu) configuration.getItems().get(0);
					languages.setText("Idiomas");
					languages.getItems().get(0).setText("Alemão");
					languages.getItems().get(1).setText("SPANISH");
					languages.getItems().get(2).setText("Francês");
					languages.getItems().get(3).setText("Inglês");
					languages.getItems().get(4).setText("Italiano");
					languages.getItems().get(5).setText("Português");
				configuration.getItems().get(1).setText("Alterar conta");
				help.getItems().get(0).setText("Sobre o projeto");
				//FIM DA TRADUÇÃO DO MENUBAR
				//TABELAS SUPERIORES
				tab_myhouse.setText("Minha casa");
				tab_mycode.setText("Código gerado");
				//TABELAS INFERIORES
				tab_comand.setText("Linha de comando");
				tab_bytes.setText("Bytes de resposta");
				tab_reg.setText("Registros");
				//COMPONENTES INFERIORES
				txt_comand.setPromptText("Nenhum comando digitado");
				tf_comand.setPromptText("Escreva um comando e aperte enter");
			}break;
		}
	}
	
	public void initToolTip(ToggleButton node, String text){
		Tooltip tooltip= new Tooltip(text);
		tooltip.setStyle("-fx-font-size: 11px;");
		node.setTooltip(tooltip);
	}

	public serialArduino getArduino() {
		return arduino;
	}
	
	public ParseToCode getParseCode(){
		return code;
	}
}
