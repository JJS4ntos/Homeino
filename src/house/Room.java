package house;

import house.code.ParseToCode;
import house.digital.Buzzer;
import house.digital.LCD;
import house.digital.LED;
import house.digital.ServoMotor;
import house.sensor.DS1302;
import house.sensor.IR;
import house.sensor.LDR;
import house.sensor.LM35;
import house.sensor.NTCTemp;
import house.sensor.PIR;
import house.sensor.Sensor_nivel_liquido;
import house.sensor.Ultra_sonic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import controllers.telaprincipal_controller;
import controllers.telaprincipal_controller.serialArduino;
import database.Commons_loads;
import database.Data_room;
import database.Sqlite;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class Room extends Tab implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2979037161795436863L;
	private ScrollPane root= new ScrollPane();
	private StackPane stack_pane=new StackPane();
	private Pane pane_room;
	private ImageView img_view;
	private static ArrayList<Item> my_lights;
	private final Integer id;
	//final private String name, info;
	private final Data_room room;
	private final ParseToCode code;
	transient private serialArduino arduino;
	private final Commons_loads commons_loads;
	
	
	//O construtor deve receber um objeto room.
	public Room(Data_room room, ParseToCode code, serialArduino arduino, Commons_loads commons_loads) {
		this.commons_loads=commons_loads;
		this.arduino=arduino;
		this.room=room;
		this.code=code;
		this.id=room.getId();
		try{init();}catch(IOException e){e.printStackTrace();}
	}
	
	public void refreshBackground() throws SQLException, IOException{
		ResultSet rs=Sqlite.getResultSet("SELECT * FROM room WHERE id='"+room.getId()+"'");
		if(rs.next())
		img_view.setImage(loadImage());
		rs.getStatement().getConnection().close();
		rs.close();
	}
	
	private void loadLines(){
		my_lights.forEach(item->{
			if(item.isSensor()){
				Item found=my_lights.stream().filter(i-> i.getPin().equals(String.valueOf(item.getPinButton()))).findFirst().get();
				if(found!=null){
				Line line = new Line();
				line.setStrokeWidth(3);
				line.setStrokeLineCap(StrokeLineCap.ROUND);
				line.setStroke(Color.LIGHTSEAGREEN);
				line.setStrokeLineJoin(StrokeLineJoin.ROUND);
				line.startXProperty().bind(item.layoutXProperty().add(50));
				line.startYProperty().bind(item.layoutYProperty().add(2));
				line.endXProperty().bind(found.layoutXProperty().add(50));
				line.endYProperty().bind(found.layoutYProperty().add(2));
				pane_room.getChildren().add(line);
				}
			}
		});
		
	}
	
	/***
	 * Carrega a estrutura visual do cômodo
	 * @throws IOException
	 */
	private void init() throws IOException{
		img_view= new ImageView(loadImage());
		pane_room= new Pane();
		pane_room.prefHeightProperty().bindBidirectional(stack_pane.prefHeightProperty());
		pane_room.prefWidthProperty().bindBidirectional(stack_pane.prefWidthProperty());
		img_view.fitHeightProperty().bindBidirectional(stack_pane.prefHeightProperty());
		img_view.fitWidthProperty().bindBidirectional(stack_pane.prefWidthProperty());
		stack_pane.getChildren().setAll(img_view, pane_room);
		root.contentProperty().setValue(stack_pane);
		this.contentProperty().setValue(root);
		this.textProperty().setValue(room.getName());
		this.setOnCloseRequest(e->{
			if(dialogs.dialogs.createConfirmationDialog("Excluir "+this.getRoom().getName(), "Você realmente deseja exluir este cômodo?",
			"Quando excluído, os cômodos jamais serão restaurados.").showAndWait().get().getButtonData()==ButtonData.CANCEL_CLOSE)
				e.consume();
			else{
				Sqlite.insert("DELETE FROM room WHERE id='"+this.getRoomId()+"'");
				Platform.runLater(()->Sqlite.insert("DELETE FROM light_children WHERE parent='"+this.getRoomId()+"'"));
				commons_loads.print(this.getRoom().getName()+" acaba de ser excluído");
				commons_loads.loadRooms();
				
			}
		});
		
		loadLights();
		loadLines(); //Carrega as linhas de ligação
	}
	
	/***
	 * Carrega os itens a partir do banco de dados
	 * @return ArrayList contendo os itens
	 */
	public ArrayList<Item> getlights(){
		/*EM SENSORES O PINBUTTON SERÁ O ATUADOR. EM SENSORES ANALÓGICOS O VALOR ANALÓGICO SERÁ O ONOFF*/
		my_lights= new ArrayList<Item>();
	try{
		ResultSet resultset= Sqlite.getResultSet("SELECT * FROM light_children WHERE parent='"+id+"'");
		while(resultset.next()){
			switch(resultset.getInt("TYPE")){
			case ParseToCode.LED:{
			LED light= new LED(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
					resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
			my_lights.add(light);
			
			code.addButtonOfLight(light);
			}break;
			case ParseToCode.SERVO_MOTOR:{
				ServoMotor servo= new ServoMotor(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(servo);
				code.addServoInCode(servo.getName(), servo.getPin(), servo.getTimeon(), servo.getPinButton());
			}break;
			case ParseToCode.LDR:{
				LDR ldr= new LDR(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(ldr);
			}break;
			case ParseToCode.BUZZER:{
				Buzzer buzzer= new Buzzer(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(buzzer);
				code.addAlarm(buzzer);
			}break;
			case ParseToCode.NTC_TEMP:{
				NTCTemp temp= new NTCTemp(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(temp);
			}break;
			case ParseToCode.LM35:{
				LM35 lm35= new LM35(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(lm35);
			}break;
			case ParseToCode.PIR:{
				PIR pir= new PIR(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(pir);
			}break;
			case ParseToCode.LCD:{
				LCD lcd = new LCD(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"), resultset.getString("STRING_USE"));
				my_lights.add(lcd);
				if(lcd.getOnoff()==1)code.addLCD(lcd, lcd.string_use.split("\n")[0], lcd.string_use.split("\n")[1]);
			}break;
			case ParseToCode.ULTRA_SONIC:{
				Ultra_sonic ultra_sonic= new Ultra_sonic(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(ultra_sonic);
			}break;
			case ParseToCode.DS1302:{
				DS1302 DS1302= new DS1302(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("TIMEON"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"), resultset.getString("STRING_USE"));
				my_lights.add(DS1302);
				if(DS1302.getOnoff()==3)code.addDS1302ReadOnly(DS1302);
			}break;
			case ParseToCode.SENSOR_NIVEL:{
				Sensor_nivel_liquido nivel= new Sensor_nivel_liquido(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(nivel);
			}break;
			case ParseToCode.INFRA_RED:{
				IR ir= new IR(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("ONOFF"), resultset.getInt("PARENT"), resultset.getString("PIN"),
						resultset.getInt("BUTTONPIN"),	resultset.getDouble("X"), resultset.getDouble("Y"), this, resultset.getInt("TYPE"));
				my_lights.add(ir);
			}break;
			//SE FOR UM ITEM CRIADO PELO USUÁRIO O STRING_USE DELE SERÁ O UID DO 
			case ParseToCode.PERSON_ITEM:{
				final String STRING_USE=resultset.getString("string_use"); //Pego o string_use do item
				final double UID=Double.valueOf(STRING_USE);//Converto para double para compará-lo
				telaprincipal_controller.data_items.forEach(i->{
					try {
							//Verifico se o UID é o mesmo do item
							if(i.getUID()==UID){
								person_Item item= new person_Item(resultset.getString("name"), i, resultset.getInt("id"), resultset.getDouble("x"),
										resultset.getDouble("y"), this, resultset.getString("pin")); //Crio um "person_item" com base nas informações
								code.addPersonItem(item);
								my_lights.add(item);
							}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}break;
		  }
		}
		resultset.getStatement().getConnection().close();
		resultset.close();
		}catch(SQLException e){e.printStackTrace();}
		return my_lights;
	}

	/***
	 * Retorna o data_room do cômodo
	 * @return
	 */
	public Data_room getRoom() {return room;}
	
	public Integer getRoomId(){return id;}
	
	public Pane getContentItems(){return pane_room;}
	
	/***
	 * Carrega e exibe os itens do cômodo
	 */
	public void loadLights(){
		pane_room.getChildren().clear();
		getlights().forEach(i->pane_room.getChildren().add(i));
		//Carregamento dos items analógicos-------------------------------------------------------------------------------------------------------------
		pane_room.getChildren().forEach(i->{
			Item sensor=(Item)i; //Crio um item
			if(sensor.isSensor()){//Verifico se ele é analógico
				switch(sensor.getType()){//Estudo de casos
				case 2:{//Se o tipo for 2
					LDR ldr = (LDR)i;//Crio o LDR
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital cujo pin equivale ao pinButton do LDR
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(ldr.getPinButton()));}).findFirst().get();
					code.addAnalogItem(ldr, item);
				}break;
				case 4:{
					NTCTemp temp = (NTCTemp)i;//Crio o LDR
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital cujo pin equivale ao pinButton do NTCTemp
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(temp.getPinButton()));}).findFirst().get();
					code.addNTCTemp(temp, item.getName(), temp.getOnoff());
				}break;
				case 5:{
					LM35 lm35 = (LM35)i;//Crio o LDR
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital cujo pin equivale ao pinButton do LM35
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(lm35.getPinButton()));}).findFirst().get();
					code.addLM35(lm35, item.getName(), lm35.getOnoff());
				}break;
				case 6:{
					PIR pir=(PIR)i;
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital cujo pin equivale ao pinButton do PIR
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(pir.getPinButton()));}).findFirst().get();
					code.addPIR(pir, item);
				}break;
				case 7:{
					LCD lcd= (LCD)i;
					if(lcd.getOnoff().intValue()==0){
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital ou analógico cujo pin equivale ao pinButton do LCD
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(lcd.string_use));}).findFirst().get();
					code.addLCD(lcd, item);
					}
				}break;
				case 8:{
					Ultra_sonic ultra_sonic= (Ultra_sonic)i;
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital ou analógico cujo pin equivale ao pinButton do ULTRA SÔNICO
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(ultra_sonic.getPinButton()));}).findFirst().get();
					code.addUltraSonic(ultra_sonic, item, ultra_sonic.getOnoff());
					
				}break;
				case 9:{
					DS1302 DS1302= (DS1302)i;
					if(DS1302.getOnoff().intValue()!=3){
						Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital ou analógico cujo pin equivale ao pinButton do DS1302
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(DS1302.getPinButton()));}).findFirst().get();
					code.addDS1302(DS1302, item);
				}
				}break;
				case ParseToCode.SENSOR_NIVEL:{
					Sensor_nivel_liquido nivel=(Sensor_nivel_liquido)i;
					Item item=(Item)pane_room.getChildren().stream().filter(//Filtro para encontrar o item digital ou analógico cujo pin equivale ao pinButton do SN
							active->{Item search=(Item)active;
							return search.getPin().equals(String.valueOf(nivel.getPinButton()));}).findFirst().get();
					code.addSensorNivel(nivel, item);
				}break;
			  }
				
			}
		});
		//Carregamento dos items analógicos-------------------------------------------------------------------------------------------------------------
	}
	
	/***
	 * Envia um sinal para o arduino se houver conexão estabelecida
	 * @param item
	 * @param onoff
	 * @throws IOException
	 */
	public void sendSinal(byte item, byte onoff) throws IOException{
		arduino.sendSinal(item, onoff);
	}

	public Commons_loads getCommons_loads() {
		return commons_loads;
	}
	
	public static ArrayList<Item> getLoadedLight(){
		return my_lights;
	}

	public Image loadImage() throws IOException{
		String[] strings=room.getImageurl().split(",");
		byte[] bytes= new byte[strings.length];
		for(int i=0; i<bytes.length;i++)bytes[i]=new Byte(strings[i]);
		ByteArrayInputStream byte_in= new ByteArrayInputStream(bytes);
		BufferedImage buf=ImageIO.read(byte_in);
		return SwingFXUtils.toFXImage(buf, null);
	}
}
