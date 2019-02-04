package controllers;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import house.Data_item;
import house.Item;
import house.Room;
import house.person_Item;
import house.code.InputCode;
import house.code.ParseToCode;
import house.digital.LED;
import house.digital.ServoMotor;
import house.sensor.LDR;
import house.sensor.LM35;
import house.sensor.NTCTemp;
import ia.Header;
import java.util.Map;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;
import ExceptionAdapter.ShowErro;
import anothers.ItemFound;
import anothers.projectSaved;
import comunication.Translate;
import controllers.little.menu_controller_methods;
import database.Commons_loads;
import database.Data_room;
import database.Fiscal;
import database.LoadVersion;
import database.Sqlite;
import database.User;
import dialogs.dialog_signup;
import dialogs.dialogs;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;


public class telaprincipal_controller implements Initializable{
	
	@FXML private Label lbl_expired_title, lbl_expired_subtitlelbl, lbl_count;						
    @FXML private MenuBar menu_top;
    @FXML private FlowPane flow_items;
    @FXML private BorderPane bordermain, borderlogin;
    @FXML private Menu menu_main, menu_home, menu_configuration, menu_help, menu_languages;
    @FXML private MenuItem item_newroom, item_changeimg, comserial, closeshow, project_save, item_close, item_newlight, item_newldr, item_showcode,
    				item_account, item_about, menu_create_item, menu_itemuser, load_project;
    @FXML private TextArea txt_comand;
    @FXML private TextField send_comand, tf_login, tf_name, tf_codet, tf_city, tf_state, tf_country, tf_email, tf_finditem;
    @FXML private PasswordField tf_password;
    @FXML private Button btn_signin, btn_signup, btn_exit, copyButton, btn_itemuserview_exit;
    @FXML private ToggleButton btn_light, btn_servo, btn_ldr, btn_buzzer, btn_ntc_temp, btn_lm35;
    @FXML private WebView wb_teste;
    @FXML private StackPane root;
    @FXML private ComboBox<String> cb_portas;
    @FXML private ComboBox<Integer> cb_baunds;
    @FXML private Circle color_status;
    @FXML private Hyperlink h_conectar, h_alterar;
    @FXML private GridPane items_grid;
	@FXML private ListView<String> list_logs, listOfCode;
	@FXML private ListView<Label> list_itemuser; 
	@FXML private ListView<ItemFound> list_myitems;
	@FXML private ListView<Hyperlink>  list_reg;
	@FXML private Slider slide_janela;
		  public serialArduino arduino=new serialArduino();
		  public ihm IHM;
		  private ShowErro show_erro;
		  public static Data_room[] data_rooms;
		  private static Integer language;
		  protected int[] ultimosbytes;
	@FXML private HBox h_center, load_room_layer;	  
	@FXML private VBox expired_screen, item_user_screen;
		  private ParseToCode code;
		  private LocalDateTime now= LocalDateTime.now();
    	  //private int time=0, coluna=0, temperatura=0;
    @FXML private ToggleGroup group_light, group_items, languages, payment;
    	  ArrayList<Number> arraybytes= new ArrayList<Number>();
    	  private Commons_loads loads;
    	  private User usuario=null;
    @FXML private TabPane tab_root;
    @FXML private Tab tab_comand, tab_bytes, tab_reg, tab_myhouse, tab_mycode;
	      private database.logs logs;
    	  public static Fiscal guard;
	      public static ArrayList<Data_item> data_items;
    	  
    private void login() throws NoSuchAlgorithmException, SQLException{
    	usuario=Sqlite.login(tf_login.getText(), Sqlite.toMD5(tf_password.getText()));
    	if(usuario==null){
    		dialogs.showmessage("Falha no login", "Login ou senha incorretos", "Por favor, verifique se as informações "
    					+ "inseridas estão corretas e tente novamente.", AlertType.WARNING).show();
    	}else{
    		loadLogin();
    		logs= new database.logs(list_reg, usuario);
    	}
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		show_erro= new ShowErro();
		try{Sqlite.checkDatabase();
		//initGuard();
		loadDatas();
		 tf_login.setText(Sqlite.getValueRegJVM("last_enter")==null?"":Sqlite.getValueRegJVM("last_enter"));
		 borderlogin.setVisible(true);
		 //Header.loadDataItemsToggles();
		 initComponents();
		}catch( IOException | SQLException e){show_erro.ThereIsAException(e);}
		 Platform.runLater(()->splash());
		 
	}

	private void close(){
		if(dialogs.createConfirmationDialog("Atenção", "Você realmente deseja sair?", "").showAndWait()
				.get().getButtonData()==ButtonData.CANCEL_CLOSE);
		else if(telaprincipal_controller.guard!=null){
			telaprincipal_controller.guard.cancel();
			Platform.exit();
		}
	}
	
	private void initComponents() throws SQLException{
		loadDataitem();
		//Procura por item na lista dos "meus itens"
		tf_finditem.textProperty().addListener(ls->{
			if(!list_myitems.getItems().isEmpty()){
				list_myitems.getItems().stream().filter(i-> i.getName().contains(tf_name.getText()));
			}
		});
		menu_create_item.setOnAction(e->{
			dialogs dialog = new dialogs();
			try{Dialog<Void> Dialog_newitem=dialog.createNewItem();
				Dialog_newitem.show();
				Platform.runLater(()->{try{loadDataitem();}catch(SQLException ex){ex.printStackTrace();}});
			}catch(IOException ex){ex.printStackTrace();}
		});
		//Sai da tela de exibição dos itens criados pelo usuário
		btn_itemuserview_exit.setOnAction(e->item_user_screen.setVisible(false));
		//Mostra a lista de itens adicionadas pelo usuário
		menu_itemuser.setOnAction(e->item_user_screen.setVisible(true));
		btn_exit.setOnAction(e->close());
		//Salva um projeto em um arquivo externo
		project_save.setOnAction(e->{try {
			Map<Data_room, Item[]> map= new HashMap<Data_room, Item[]>();
			loads.rooms.forEach(r->{
				Item[] items= new Item[r.getlights().size()];
				for(int i=0; i<r.getlights().size();i++)items[i]=r.getlights().get(i);
				map.put(r.getRoom(), items);	
			});
			menu_controller_methods.saveProject(new projectSaved(usuario.getName(), map));
			
		}catch (IOException e3) {e3.printStackTrace();}});
		//Carrega o projeto salvo
		load_project.setOnAction(e->{try{menu_controller_methods.loadProject(usuario, loads);}
		catch(IOException | SQLException | ClassNotFoundException e1){e1.printStackTrace();}
		finally{Platform.runLater(()->{try{loadDataitem();}catch(SQLException ex){ex.printStackTrace();}});}});
		//Copia o código gerado
		copyButton.setOnAction(e->{
			Toolkit toolkit = Toolkit.getDefaultToolkit();
    		Clipboard clipboard= toolkit.getSystemClipboard();
    		StringBuffer stringBuf= new StringBuffer();
    		for(String string: listOfCode.getItems())
    		stringBuf.append(string);
    		clipboard.setContents(new StringSelection(stringBuf.toString()), null);
		});
		
		languages.selectedToggleProperty().addListener((e, e1, e2)->{
			if(languages.selectedToggleProperty().isNotNull().get()){
			RadioMenuItem selected=(RadioMenuItem) e2;
			StringProperty id=selected.idProperty();
			usuario.setLanguage(Integer.valueOf(id.get()));
			setFrameLanguage(Integer.valueOf(id.get()));
			setLanguage(usuario.getLanguage());
			}
		});
		
		//Botão para cadastrar usuário
		btn_signup.setOnAction(e->{
			dialog_signup dialog= new dialog_signup();
			Platform.runLater(()->dialog.StartSignup());
		});
		//Botão para logar
		btn_signin.setOnAction(e->{try{login();}catch(NoSuchAlgorithmException | SQLException ex){ex.printStackTrace();}});
		//Cria uma cômodo
		item_newroom.setOnAction(e->{
			menu_controller_methods.create_new_room(usuario, tab_root, loads).showAndWait();
			Platform.runLater(()->loads.loadRooms());
		});
		//Menu item para alterar imagem
		item_changeimg.setOnAction(e->{
			menu_controller_methods.changeBackgroundRoom(Sqlite.getDatarooms(usuario.getId()), usuario, loads).showAndWait();
			loads.loadRooms();
		});
		
		item_close.setOnAction(e->close());

		cb_baunds.getItems().addAll(50,110,300,1200,2400,4800,9600,19200,38400,57600);
		cb_portas.setOnMouseClicked(event->{
			cb_portas.getItems().clear();
			Enumeration<?> enumeracao= CommPortIdentifier.getPortIdentifiers();
			while(enumeracao.hasMoreElements()){
				CommPortIdentifier porta= (CommPortIdentifier) enumeracao.nextElement();
				if(porta.getPortType()==CommPortIdentifier.PORT_SERIAL){
					cb_portas.getItems().add(porta.getName());
				}
			}
		});

		h_conectar.disableProperty().bind(cb_baunds.selectionModelProperty().get().selectedItemProperty().isNull().or(
				cb_portas.selectionModelProperty().get().selectedItemProperty().isNull()));
		cb_portas.disableProperty().bind(h_conectar.textProperty().isEqualTo("Terminar conexão"));
		cb_baunds.disableProperty().bind(h_conectar.textProperty().isEqualTo("Terminar conexão"));
		h_conectar.setOnAction(event->requestConnection());
		list_reg.selectionModelProperty().get().selectedItemProperty().addListener(event->{
			Platform.runLater(()->showreg(list_reg.selectionModelProperty().get().selectedItemProperty().get().textProperty().get()));
		});
		
		//Alterar conta
		item_account.setOnAction(e->dialogs.changeUser(usuario, usuario.getLanguage()).show());
		
	}
	
	/***
	 * Carrega os dataitens criados pelo usuário
	 * @throws SQLException
	 */
	public void loadDatas() throws SQLException{
		ArrayList<String> objectArray=Sqlite.getObjectUsers();
		telaprincipal_controller.data_items= new ArrayList<>();
		objectArray.forEach(i->{
			try{String[] object= i.split("~~");
			FileInputStream readFile= new FileInputStream(object[2]);
			ObjectInputStream input= new ObjectInputStream(readFile);
			Data_item item=(Data_item)input.readObject();
			telaprincipal_controller.data_items.add(item);
			input.close();
			}catch(IOException | ClassNotFoundException e){e.printStackTrace();}
		});		
	}

	
	//Carregamentos que só podem ser feitos após o login. Aqui ficará todo o carregamento do usuário
	private void loadLogin(){
		borderlogin.setVisible(false);
		bordermain.setEffect(null);
		code=new ParseToCode(listOfCode);
		IHM=new ihm(txt_comand, send_comand);
		btn_light.setToggleGroup(group_items);
		loads=new Commons_loads(tab_root, usuario, arduino, code, group_items, txt_comand, list_myitems);
		loads.loadRooms();
		setFrameLanguage(usuario.getLanguage());
		setLanguage(usuario.getLanguage());
		Platform.runLater(()->Sqlite.putValueRegJVM("last_enter", tf_login.getText()));
		//code.getCode();
	}
	
	/***
	 * Carrega os botões dos itens criados pelo usuário e adiciona ao flow_pane dos botões à direita e carrega a tela de items do usuário
	 * @throws SQLException
	 */
	private void loadDataitem() throws SQLException{
		loadDatas();
		for(ToggleButton toggle: Header.loadDataItemsToggles()){
			flow_items.getChildren().add(toggle); //Trabalhar nas informações deste toggle recebido
			toggle.setToggleGroup(group_items);
		}
		list_itemuser.getItems().clear();
		data_items.forEach(item->{
			String inputs="";
			for(InputCode input: item.getFields())
				inputs=inputs+input.getName()+" - ";
			try{
			Label data_view= new Label(item.getName()+"\n"+item.getDescription()+"\n"+inputs);
			data_view.setStyle("-fx-background-color: lightskyblue; -fx-background-radius: 15;");
			data_view.setAlignment(Pos.CENTER_LEFT);
			data_view.setTextAlignment(TextAlignment.LEFT);
			ImageView image=new ImageView(item.loadImage());
			image.setFitHeight(70);
			image.setFitWidth(70);
			data_view.setGraphic(image);
			list_itemuser.getItems().add(data_view);
			}catch(IOException e){e.printStackTrace();}
		});
		
	}
	//Para acionamentos digitais comuns
	/*private void createItem(room room, double x, double y, int type, int timeon){
		dialog_additem dialogs= new dialog_additem();
		Optional<item> result=dialogs.initDialog(room, x, y, type, timeon, usuario).showAndWait();
		if(result.isPresent()){
			item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}
	
	//Para analógicos e atuadores especiais
	private void createItem(room room, double x, double y, int type){
		dialog_additem dialogs= new dialog_additem();
		Optional<item> result=dialogs.initDialog(room, x, y, type, usuario).showAndWait();
		if(result.isPresent()){
			item item= result.get();
			room.getContentItems().getChildren().add(item);
			code.clear();
			int index=tab_root.getSelectionModel().getSelectedIndex();
			loadRooms();
			tab_root.getSelectionModel().select(index);
		}
	}*/
	
	public void initGuard() throws FileNotFoundException, ClassNotFoundException, IOException{
		 LoadVersion load= new LoadVersion();
		 guard= new Fiscal(load.loadLicenca());
		 lbl_count.textProperty().bind(guard.licencecountproperty().asString());
		 Thread thread_guard= new Thread(guard);
		 thread_guard.start();
		 bordermain.disableProperty().bind(guard.getLicenceExpired());
		 expired_screen.visibleProperty().bind(guard.getLicenceExpired());
		// main_screen.stage.titleProperty().bind(guard.getLicence().contadorproperty().asString());
	}
	
	public void setFrameLanguage(final int id){
		switch(id){
		case Translate.GERMANY:{loads.changeMenuLanguage(0, menu_main, menu_home, menu_configuration, menu_help, tab_comand, tab_bytes, tab_reg,
				tab_myhouse, tab_mycode, txt_comand, send_comand);
		}break;
		case Translate.SPANISH:{loads.changeMenuLanguage(1, menu_main, menu_home, menu_configuration, menu_help, tab_comand, tab_bytes, tab_reg,
				tab_myhouse, tab_mycode, txt_comand, send_comand);
		}break;
		case Translate.FRANCE:{loads.changeMenuLanguage(2, menu_main, menu_home, menu_configuration, menu_help, tab_comand, tab_bytes, tab_reg,
				tab_myhouse, tab_mycode, txt_comand, send_comand);
		}break;
		case Translate.ENGLISH:{loads.changeMenuLanguage(3, menu_main, menu_home, menu_configuration, menu_help, tab_comand, tab_bytes, tab_reg,
				tab_myhouse, tab_mycode, txt_comand, send_comand);
		}break;
		case Translate.ITALY:{loads.changeMenuLanguage(4, menu_main, menu_home, menu_configuration, menu_help, tab_comand, tab_bytes, tab_reg,
				tab_myhouse, tab_mycode, txt_comand, send_comand);
		}break;
		case Translate.PORTUGUESE:{loads.changeMenuLanguage(5, menu_main, menu_home, menu_configuration, menu_help, tab_comand, tab_bytes, tab_reg,
				tab_myhouse, tab_mycode, txt_comand, send_comand);
				loads.initToolTip(btn_light, "Selecione esse item e depois clique no cômodo para adicionar uma lâmpada ou led");
				loads.initToolTip(btn_ldr, "Selecione esse item e depois clique no cômodo para adicionar um sensor LDR");
				loads.initToolTip(btn_buzzer, "Selecione esse item e depois clique no cômodo para adicionar um buzzer/piezo");
				loads.initToolTip(btn_servo, "Selecione esse item e depois clique no cômodo para adicionar um servo motor");
				loads.initToolTip(btn_lm35, "Selecione esse item e depois clique no cômodo para adicionar um sensor de temperatura LM35");
				loads.initToolTip(btn_ntc_temp, "Selecione esse item e depois clique no cômodo para adicionar um sensor de temperatura NTC");
		}break;
	    }
	}
	
	//Solicita a conexão
	public void requestConnection(){
		if(!arduino.isConnected()){
			arduino.opencomunication(cb_portas.getSelectionModel().getSelectedItem(),
					cb_baunds.getSelectionModel().getSelectedItem());
				color_status.setStyle("-fx-fill: lightgreen;");
				h_conectar.setText("Terminar conexão");
				items_grid.setDisable(true);
		}else{
		StringBuffer sb= new StringBuffer();
		for(String line:list_logs.getItems())
			sb.append(line+"\n");
		logs.registrar(txt_comand.getText(), sb.toString());
		arduino.closecomunication();
		color_status.setStyle("-fx-fill: red;");
		h_conectar.setText("Iniciar conexão");
		items_grid.setDisable(false);
		list_logs.getItems().clear();
		
		}
	}
		
	public void showreg(String name){
		try{		
			Dialog<Void> dialog = new Dialog<>();
	    	dialog.setTitle("Visualização de relatório");
	    	dialog.setHeaderText("Relatório");
	    	dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
	    	TextArea txt= new TextArea();
	    	txt.setPrefSize(800, 600);
	    	txt.setEditable(false);
	    	txt.setStyle("-fx-font-size: 13px");
	    	ResultSet result=Sqlite.getResultSet("SELECT * FROM logs WHERE date='"+name+"' and userid='"+usuario.getId()+"'");
	    	if(result.next()){
	    		String log=result.getString("log").isEmpty()?"Não há registro de log":result.getString("log");
	    		String logbyte=result.getString("logbyte").isEmpty()?"Não há registro de logbyte":result.getString("logbyte");
	    		txt.appendText(log+"\n\n logbyte-\n"+logbyte);
	    	}
	    		
	    	result.getStatement().getConnection().close();
	    	result.getStatement().close();
	    	result.close();
	    	dialog.getDialogPane().setContent(txt);
	    	dialog.show();}catch(SQLException e){show_erro.ThereIsAException(e);}
	}
	
	public void splash(){
		Alert dialog = new Alert(AlertType.NONE);
    	dialog.setTitle("Bem vindo ao HouseOfCodes");
    	dialog.setHeaderText("Informações sobre esta versão");
    	//ImageView splash= new ImageView(new Image(getClass().getResource("/images/splash.png").toExternalForm()));
    	dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
    	VBox box= new VBox();
    	Label label = new Label();
    	label.setWrapText(true);
    	label.setWrapText(true);
    	label.setPrefSize(400, 300);
    	label.setText("Olá! Este é um projeto independente de automação residencial utilizando o arduino como microcontrolador."
    				+ "\nInformações sobre esta versão:\n-Geração de código automaticamente.\n-Adição virtual de componentes(limitados).\n"
    				+ "-Conexão Serial com o arduino.\n-Controle dos componentes por meio da comunicação serial.\n"
    				+ "-Sistema de comandos por texto(Prompt de comando).\n-Armazenamento de logs após termino de comunicação serial.\n"
    				+ "-Visualização de bytes recebidos pelo software.\n\nEsta é uma versão demonstrativa e não contém todos os sensores "
    				+ "e atuadores disponíveis para o uso. Para saber mais informações sobre a versão completa e como adiquirir visite "
    				+ "o nosso site.");
    	box.getChildren().addAll(label);
    	dialog.getDialogPane().setContent(box);
    	dialog.show();
	}

	public static Integer getLanguage() {
		return language;
	}

	public static void setLanguage(Integer language) {
		telaprincipal_controller.language = language;
	}

	public class serialArduino implements SerialPortEventListener{
			private InputStream serialInput;
			private OutputStream serialOut;
			private SerialPort port;
			private boolean connected=false;
			
			public boolean isConnected(){
				return connected;
			}
			
			public void opencomunication(String porta, int taxa){
				try{CommPortIdentifier IDport=CommPortIdentifier.getPortIdentifier(porta);
				port= (SerialPort) IDport.open("Comunicação Serial", taxa);
				port.setSerialPortParams(taxa, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
				port.setFlowControlMode(SerialPort.DATABITS_8);
				port.addEventListener(this);
				port.notifyOnDataAvailable(true);
				serialOut= port.getOutputStream();
				serialInput= port.getInputStream();
				connected=true;
				}
			catch(NoSuchPortException e){show_erro.ThereIsAException(e);closecomunication();}
			catch(UnsupportedCommOperationException e){show_erro.ThereIsAException(e);closecomunication();}
			catch(PortInUseException e){show_erro.ThereIsAException(e);closecomunication();}
			catch(IOException e){show_erro.ThereIsAException(e);closecomunication();}
			catch (TooManyListenersException e){show_erro.ThereIsAException(e);closecomunication();}
			}
			
			public void closecomunication(){
				try {serialOut.close();
				serialInput.close();
				port.removeEventListener();
				port.close();connected=false;} catch (IOException e) {show_erro.ThereIsAException(e);}
			}
			
			public void write(byte[] comunication){
				try {serialOut.write(comunication);}
				catch (IOException e) {show_erro.ThereIsAException(e);closecomunication();}
			}

			@Override
			public void serialEvent(SerialPortEvent event){
				switch(event.getEventType()){
				case SerialPortEvent.BI: txt_comand.appendText("Event::BI\n");break;
				case SerialPortEvent.CD: txt_comand.appendText("Event::CD\n");break;
				case SerialPortEvent.DSR:  txt_comand.appendText("Event::DSR\n");break;
				case SerialPortEvent.FE:  txt_comand.appendText("Event::FE\n");break;
				case SerialPortEvent.OE:  txt_comand.appendText("Event::OE\n");break;
				case SerialPortEvent.OUTPUT_BUFFER_EMPTY: txt_comand.appendText("Event::OUTPUT_BUFFER_EMPTY\n");break;
				case SerialPortEvent.PE: txt_comand.appendText("Event::PE\n");break;
				case SerialPortEvent.RI:  txt_comand.appendText("Event::RI\n");break;
				case SerialPortEvent.DATA_AVAILABLE:{
					byte[] bytes=new byte[20];
				try {
					serialInput.read(bytes);
					StringBuffer sb= new StringBuffer();
					for(byte b: bytes)
						sb.append((b&0xff));
					Platform.runLater(()->list_logs.itemsProperty().get().add("["+now+"]"+" Sinal recebido do arduino:: "+sb.toString()));
					checkItem(bytes);
				} catch (IOException e1) {show_erro.ThereIsAException(e1);}

				 }break;
			  }			
			}

			public void checkItem(byte[] bytes){
			for(int i=0; i<bytes.length; i++){
				if(bytes.length-i>=3){
						// && bytes[i+1]!=0
						//if(bytes[i+1]<0)
					final int data= bytes[i], onoff=(bytes[i+1]&0xff), analogvalue=(bytes[i+2]&0xff);//
					//txt_comand.appendText("\n["+now+"]"+"Pino::"+data+" - Status::"+onoff);
					tab_root.getTabs().forEach(r->{
						Room room=(Room)r;
						room.getContentItems().getChildren().forEach(l->{
							Item item=(Item)l;
							if(item.getType()==0&&data==Integer.valueOf(item.getPin())){
								LED light=(LED)item;
								//light.setOnoff(onoff);
								if(onoff==1)light.turn.setSelected(true);
								else if(onoff==0)light.turn.setSelected(false);
							}else if(item.getType()==1&&data==Integer.valueOf(item.getPin())){
								ServoMotor servo=(ServoMotor)item;
								//light.setOnoff(onoff);
								if(onoff==1)servo.turn.setSelected(true);
								else if(onoff==0)servo.turn.setSelected(false);
							}else if(item.getType()==2&&data=='A'){
								if(item.getPin().length()==2){
									String id="A"+(char)onoff;
									if(item.getPin().equals(id)&&analogvalue!=0){
									LDR ldr=(LDR)l;
									ldr.setAnalogValue(analogvalue);
								}
								}else if(item.getPin().length()==3){
									String id="A"+(char)onoff+(char)analogvalue;
									if(item.getPin().equals(id)&&bytes[+3]!=0){
									LDR ldr=(LDR)l;
									ldr.setAnalogValue(Integer.valueOf(bytes[+3]));
								}
							  }
							}else if(item.getType()==4){
								if(item.getPin().length()==2){
									String id="A"+(char)onoff;
									if(item.getPin().equals(id)&&analogvalue!=0){
									NTCTemp temp=(NTCTemp)l;
									temp.setAnalogValue(analogvalue);
								}
								}else if(item.getPin().length()==3){
									String id="A"+(char)onoff+(char)analogvalue;
									if(item.getPin().equals(id)&&bytes[+3]!=0){
									NTCTemp temp=(NTCTemp)l;
									temp.setAnalogValue(Integer.valueOf(bytes[+3]));
								}
							  }
							}else if(item.getType()==5){
								if(item.getPin().length()==2){
									String id="A"+(char)onoff;
									if(item.getPin().equals(id)&&analogvalue!=0){
									LM35 lm35=(LM35)l;
									lm35.setAnalogValue(analogvalue);
								}
								}else if(item.getPin().length()==3){
									String id="A"+(char)onoff+(char)analogvalue;
									if(item.getPin().equals(id)&&bytes[+3]!=0){
									LM35 lm35=(LM35)l;
									lm35.setAnalogValue(Integer.valueOf(bytes[+3]));
								}
							  }
							}
							});
						});
					}	
				}
			}

			public OutputStream getserialOut(){
				return serialOut;
			}
			
			public InputStream getInputStream(){
				return serialInput;
			}
			
			public void sendSinal(byte item, byte status) throws IOException{
				if(isConnected()){
					byte[] bytes={item, status};
					write(bytes);
					txt_comand.appendText("["+now+"]Enviando bytes:: "+item+status+"\n");
				}
			}
			
	}

	public class ihm{
		
		private final TextField tf_comand;
		private final TextArea txt_ihm; 

		public ihm(TextArea txt_ihm, TextField comand){
			this.tf_comand=comand;
			this.txt_ihm=txt_ihm;
			tf_comand.setOnKeyPressed(event->{
				if(event.getCode().equals(KeyCode.ENTER) && !tf_comand.getText().isEmpty()){
					//System.out.println("Olá mundo!");
					//txt_comand.appendText("Comando: \""+send_comand.getText()+"\"\n");
					//scada.comunication(send_comand.getText());
					execIHM(tf_comand.getText());
					tf_comand.clear();
				}
			});
		}
		
		private void execIHM(String chars){
			String[] comand=chars.split(" "); //Tokenizo a string que chegou com os espaços.
			switch(comand[0]){
				case "set":{
					if(comand[1].equals("item")&&comand.length==4)
						set(comand[2], Integer.valueOf(comand[3]));
					else txt_ihm.appendText("["+now+"]comando inválido\n");
				}break;
				default: txt_ihm.appendText("["+now+"]comando inválido\n");
			}
		}
		
		private void set(String pin, Integer onoff){
			if(!(pin==null||onoff==null)&&onoff<=1){
			byte[] bytes={Integer.valueOf(pin).byteValue(), onoff.byteValue(), 0,};
			txt_ihm.appendText("["+now+"]>mudança de estado realizada\n");
			arduino.checkItem(bytes);
			}else txt_ihm.appendText("["+now+"]>mudança inválida\n"); 
		}
		
		
		
		
	}
}


