package house.code;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import house.Item;
import house.person_Item;
import house.digital.LCD;
import house.digital.LED;
import house.digital.ServoMotor;
import house.sensor.DS1302;
import house.sensor.IR;
import house.sensor.PIR;
import house.sensor.Sensor_nivel_liquido;
import house.sensor.Ultra_sonic;

public class ParseToCode{
	

	public static final int LED=0, SERVO_MOTOR=1, LDR=2, BUZZER=3, NTC_TEMP=4, LM35=5, PIR=6, LCD=7, ULTRA_SONIC=8, DS1302=9,
							SENSOR_NIVEL=11, INFRA_RED=12, HIGH=1, LOW=0, PERSON_ITEM=99;
	
	private ArrayList<String> variables=new ArrayList<String>(), voidsetup=new ArrayList<String>(), voidloop=new ArrayList<String>(),
			serialEvent0=new ArrayList<String>(), serialEvent1=new ArrayList<String>(), serialEvent2= new ArrayList<String>(),
	methodComunication=new ArrayList<String>(), librarys= new ArrayList<String>();
	
	
	private final ListView<String>codes;
	
	//Gera o c�digo dos itens criados pelos usu�rios do software.
	public void addPersonItem(person_Item item){
		String code_setup=finallyCode(item, item.getData().getCodeSetup()),
				code_loop=finallyCode(item, item.getData().getCodeRun());
		voidsetup.add(code_setup);
		voidloop.add(code_loop);
		librarys.add(item.getData().getLibrary());
	}
	
	//M�todo que substitui as entradas por seus respectivos valores inseridos pelo usu�rio e retorna o c�digo pronto.
	public String finallyCode(person_Item item, String code){
		//System.out.println(code);
		
		for(InputCode input:item.getInputs()){
			if(!input.getName().equals("PIN")){
				String variable= input.getName();
				code=code.replaceAll("#"+input.getName()+"#", variable);
				variables.removeIf(i->i.contains(variable));
				String variable_type= input.getType()==InputCode.INTEGER?"int ": input.getType()==InputCode.CHAR?"char ":
					input.getType()==InputCode.DOUBLE?"double ":input.getType()==InputCode.FLOAT?"float ":"";
				variables.add(variable_type+variable+"= "+input.getInput()+";\n");
			}
			else{
				String variable= item.getName()+"_pin";
				variables.removeIf(i->i.contains(variable));
				variables.add("int "+variable+"= "+input.getInput()+";\n");
				code=code.replaceAll("#"+input.getName()+"#", variable);
			}
		}
		return code;
	}
	
	public void addLibrary(String library){
		String libraryLine= "#include <"+library+">";
		if(!librarys.stream().filter(i->i.contains(libraryLine)).findAny().isPresent())
		librarys.add(libraryLine+"\n");
	}
	
	public ParseToCode(ListView<String>codes) {
		this.codes=codes;
	}

	public void addIR(IR IR){
		addLibrary("IRremote.h");
		StringBuffer sb= new StringBuffer();
		String variableName=IR.getName();//Nome da vari�vel do pino
		String variableReceive=variableName+"_receive";//Nome do objeto IR
		String variableResult=variableName+"_result";//Nome do objeto result
		variables.add(variableName);
		variables.add("IRrecv "+variableReceive+"("+variableName+");\n");
		variables.add("decode_results "+variableResult+";\n");
		voidsetup.add(variableReceive+".enableIRIn();\n");
		voidloop.add("if ("+variableReceive+".decode(&"+variableResult+")){\n");
		try{IR.getIRChildren().forEach(i->{
			sb.append("");
		});
		}catch(SQLException e){e.printStackTrace();}
		voidloop.add(variableReceive+".resume();}\n}");
	}
	
	/*ADICIONA UMA LEITURA PARA O CONTROLE REMOTO- NECESSITA DO SINAL EM HEXADECIMAL, DO IR E DO */
	public void addReadIR(IR IR, String sinal, Item item){
		String variableName=IR.getName();//Nome da vari�vel do pino
		String variableResult=variableName+"_result";//Nome do objeto result
		String code="case 0x"+sinal+":"+"digitalWrite("+item.getName()+", digitalRead("+item.getName()+"));break;";
		voidloop.add(voidloop.indexOf(variableResult)+1, code);
	}
	
	public void addReadIR(IR IR, String sinal, ServoMotor servo, int angulo){
		String variableName=IR.getName();//Nome da vari�vel do pino
		String variableResult=variableName+"_result";//Nome do objeto result
		String code="case 0x"+sinal+":"+"{"
				+ "if("+servo.getName()+"<"+angulo+"){\n"
				+"for (int i = "+servo+".read(); i < "+angulo+"; i++) {\n"
				+servo.getName()+".write(i);\n"
				+"delay(50);\n}\n"
				+"byte data[]={"+servo.getPin()+", 1};\nSerial.write(data, 2);\n}"
				+"else{\n"
				+"for (int i = "+servo.getName()+".read(); i > 0; i--) {\n"
				+servo.getName()+".write(i);\n"
		        +"delay(50);\n}"
		        +"byte data[]={"+servo.getPin()+", 0};\nSerial.write(data, 2);"
		        + "\n}\n}"
				+"break;";
		voidloop.add(voidloop.indexOf(variableResult)+1, code);
	}

	public void addDS1302ReadOnly(DS1302 CI){
		addLibrary("virtuabotixRTC.h");
		String variableName= CI.getName();
		variables.add("virtuabotixRTC "+variableName+"("+CI.getPin().split(":")[0]+", "+CI.getPin().split(":")[1]+", "+CI.getPin().split(":")[2]+");");
		int hour=LocalDateTime.now().getHour(), minute=LocalDateTime.now().getMinute(), second=LocalDateTime.now().getSecond(),
				day_of_week=LocalDateTime.now().getDayOfWeek().getValue(), day_of_month=LocalDateTime.now().getDayOfMonth(),
				month=LocalDateTime.now().getMonthValue(), year= LocalDateTime.now().getYear();
		voidsetup.add("// (seconds, minutes, hour, day_of_week, day_of_month, month, year)"+
					  variableName+".setDS1302Time("+second+", "+minute+", "+hour+", "+day_of_week+", "+day_of_month+", "
				+month+", "+year+");");
	}
	
	//TIMEON:0==">", TIMEON:1=="<", TIMEON:2=="=="
	//ONOFF:0 acionamento com base na hora
	//ONOFF:1 acionamento com base na data
	//ONOFF:2 acionamento com base na hora/data
	//ONOFF:3 somente para leitura(sem nenhum acionamento condicional)
	/*				Par�metros para a execu��o do m�todo: DS1302, item que ser� acionado, tipo de acionamento(atrav�s de hora, data ou 
	 * date e hora, condi��o (0 para quando for maior, 1 para quando for menor e 2 para quando for igual*/
	public void addDS1302(DS1302 CI, Item item){
		addLibrary("virtuabotixRTC.h");
		String variableName= CI.getName();
		int condition= CI.getTimeon();
		
		String symbol= condition==0?">":condition==1?"<":condition==2?"==":"!=";
		
		//String symbol_not= symbol.equals(">")?"<":symbol.equals("<")?">":"!=";
		
		int hour=LocalDateTime.now().getHour(), minute=LocalDateTime.now().getMinute(), second=LocalDateTime.now().getSecond(),
				day_of_week=LocalDateTime.now().getDayOfWeek().getValue(), day_of_month=LocalDateTime.now().getDayOfMonth(),
				month=LocalDateTime.now().getMonthValue(), year= LocalDateTime.now().getYear();
		
		voidsetup.add("// (seconds, minutes, hour, day_of_week, day_of_month, month, year)\n"+
				  variableName+".setDS1302Time("+second+", "+minute+", "+hour+", "+day_of_week+", "+day_of_month+", "
			+month+", "+year+");");
		final int TIME=0, DATE=1, DATETIME=2;
		switch(CI.getOnoff()){
			case TIME:{
				voidloop.add("if("+variableName+".hours"+symbol+CI.getStringUSE().split(":")[0]+" && "+variableName+".minutes"+symbol+CI.getStringUSE().split(":")[1]
							+" && "+variableName+".seconds"+symbol+CI.getStringUSE().split(":")[2]+"){\n"
							+ getCodeOnThis(item) //Esse m�todo me fornece o c�digo para acionar o item
							+ "}else{\n"
							+ getCodeOffThis(item) //Esse m�todo me fornece o c�digo para desacionar o item
							+"}\n"
						);
			}break;
			case DATE:{
				voidloop.add("if("+variableName+".year"+symbol+CI.getStringUSE().split(":")[2]+" && "+variableName+".month"+symbol+CI.getStringUSE().split(":")[1]+"\n"
							+" && "+variableName+".dayofmonth"+symbol+CI.getStringUSE().split(":")[0]+"){\n"
							+ getCodeOnThis(item) //Esse m�todo me fornece o c�digo para acionar o item
							+ "}else{\n"
							+ getCodeOffThis(item) //Esse m�todo me fornece o c�digo para desacionar o item
							+"}\n"
				);
			}break;
			case DATETIME:{
				voidloop.add("if("+variableName+".year"+symbol+CI.getStringUSE().split(":")[2]+" && "+variableName+".month"+symbol+CI.getStringUSE().split(":")[1]+"\n"
							+" && "+variableName+".dayofmonth"+symbol+CI.getStringUSE().split(":")[0]+" && "+variableName+".hours"+symbol+CI.getStringUSE().split(":")[3]
							+" && "+variableName+".minutes"+symbol+CI.getStringUSE().split(":")[4]+"){\n"
							+ getCodeOnThis(item) //Esse m�todo me fornece o c�digo para acionar o item
							+ "}else{\n"
							+ getCodeOffThis(item) //Esse m�todo me fornece o c�digo para desacionar o item
							+"}\n"
				);
			}break;
		}
	}

	public void addPIR(PIR pir, Item item){
		String variableName="int "+pir.getName()+"="+pir.getPin()+";";
		variables.add(variableName);
		voidsetup.add("pinMode("+pir.getName()+", INPUT);\n");
		if(item.getType()!=1)
		voidloop.add("if(digitalRead("+pir.getName()+")==HIGH && !"+item.getName()+"Button_status)\n"
				+ "digitalWrite("+item.getName()+", HIGH);\n"
				+ "else (digitalRead("+pir.getName()+")==LOW && !"+item.getName()+"Button_status)\n"
				+ "digitalWrite("+item.getName()+", LOW);\n");
		else{
			String code="if(digitalRead("+pir.getName()+")==HIGH && !"+item.getName()+"Button_status){\n"
					+"for (int i = "+item.getName()+".read(); i < "+item.getTimeon()+"; i++) {\n"
					+item.getName()+".write(i);\n"
					+"delay(50);\n}\n"
					+"byte data[]={"+item.getPin()+", 1};\nSerial.write(data, 2);\n}"
					+"else if(digitalRead("+pir.getName()+")==LOW && !"+item.getName()+"Button_status){\n"
					+"for (int i = "+item.getName()+".read(); i > 0; i--) {\n"
					+item.getName()+".write(i);\n"
			        +"delay(50);\n}"
			        +"byte data[]={"+item.getPin()+", 0};\nSerial.write(data, 2);"
			        + "\n}"
			        ;
			voidloop.add(code);
		}
	}
	
	//LCD que l� estado de sensores e atuadores
	public void addLCD(LCD lcd, Item item){
		addLibrary("LiquidCrystal.h");
		variables.add("LiquidCrystal "+lcd.getName()+"("+lcd.getPin().split(":")[0]+", "+lcd.getPin().split(":")[1]+
				", "+lcd.getPin().split(":")[2]+", "+lcd.getPin().split(":")[3]+", "+lcd.getPin().split(":")[4]
						+", "+lcd.getPin().split(":")[5]+");\n");
		voidsetup.add(lcd.getName()+".begin(16, 2);\n");
		//Se este item for um sensor anal�gico
		if(item.isSensor()&&item.isAnalog()){
			voidloop.add(lcd.getName()+".clear();\n");
			voidloop.add(lcd.getName()+".setCursor(0, 0);\n");
			voidloop.add(lcd.getName()+".print(\"Analog value:\");\n");
			voidloop.add(lcd.getName()+".setCursor(0, 1);\n");
			voidloop.add(lcd.getName()+".print(String(analogRead("+item.getName()+")));\n");
		//Se esse item for um sensor digital e n�o for um ultra s�nico, um servo motor ou um DS1302;
		}else if(!item.isAnalog()&&item.getType()!=8&&item.getType()!=1&&item.getType()!=9){
			voidloop.add(lcd.getName()+".clear();\n");
			voidloop.add(lcd.getName()+".setCursor(0, 0);\n");
			voidloop.add(lcd.getName()+".print(\"Digital value:\");\n");
			voidloop.add(lcd.getName()+".setCursor(0, 1);\n");
			voidloop.add("String valueConvert= digitalRead("+item.getName()+")==HIGH?\"HIGH\":\"LOW\";\n");
			voidloop.add(lcd.getName()+".print(valueConvert);\n");
		}else if(item.getType()==8){//LER ULTRASONICO
			voidloop.add(lcd.getName()+".clear();\n");
			voidloop.add(lcd.getName()+".setCursor(0, 0);\n");
			voidloop.add(lcd.getName()+".print(\"CM:\");\n");
			voidloop.add(lcd.getName()+".setCursor(0, 1);\n");
			voidloop.add("String valueConvert= ultrasonic.convert("+item.getName()+".timing(), Ultrasonic::CM);\n");
			voidloop.add(lcd.getName()+".print(valueConvert);\n");
		}else if(item.getType()==1){//SERVO MOTOR
			voidloop.add(lcd.getName()+".clear();\n");
			voidloop.add(lcd.getName()+".setCursor(0, 0);\n");
			voidloop.add(lcd.getName()+".print(\"Servo read:\");\n");
			voidloop.add(lcd.getName()+".setCursor(0, 1);\n");
			voidloop.add("String valueConvert="+item.getName()+".read();\n");
			voidloop.add(lcd.getName()+".print(valueConvert);\n");
		}
		else if(item.getType()==9){
			voidloop.add(lcd.getName()+".clear();\n");
			voidloop.add(lcd.getName()+".setCursor(0, 0);\n");
			voidloop.add("int hour="+item.getName()+".hours, minutes="+item.getName()+".minutes, seconds="+item.getName()+".seconds;");
			voidloop.add("int day="+item.getName()+".dayofmonth, month="+item.getName()+".month, year="+item.getName()+".year;");
			voidloop.add(lcd.getName()+".print(hour+\":\"+minutes+\":\"+seconds);\n");
			voidloop.add(lcd.getName()+".setCursor(0, 1);\n");
			voidloop.add(lcd.getName()+".print(day+\"/\"+month+\"/\"+year);\n");
		}
		
	}
	
	//LCD que apenas exibe uma mensagem personalizada
	public void addLCD(LCD lcd, String line1, String line2){
		addLibrary("LiquidCrystal.h");
		variables.add("LiquidCrystal "+lcd.getName()+"("+lcd.getPin().split(":")[0]+", "+lcd.getPin().split(":")[1]+
				", "+lcd.getPin().split(":")[2]+", "+lcd.getPin().split(":")[3]+", "+lcd.getPin().split(":")[4]
						+", "+lcd.getPin().split(":")[5]+");\n");
		voidsetup.add(lcd.getName()+".begin(16, 2);\n");
		voidsetup.add(lcd.getName()+".clear();\n");
		voidsetup.add(lcd.getName()+".setCursor(0, 0);\n");
		voidsetup.add(lcd.getName()+".print(\""+line1+"\");\n");
		voidsetup.add(lcd.getName()+".setCursor(0, 1);\n");
		voidsetup.add(lcd.getName()+".print(\""+line2+"\");\n");
	}
	
	public void addSensorNivel(Sensor_nivel_liquido sensor, Item item){
		String variableName= sensor.getName();
		String variableStatusItem= item.getName()+"Button_status";
		variables.add("int "+variableName+"= "+sensor.getPin()+";\n");
		voidsetup.add("pinMode("+variableName+", INPUT);\n");
		voidloop.add("if(digitalRead("+variableName+")==HIGH && !"+variableStatusItem+"){\n"
						+ getCodeOnThis(item) //O erro � aqui, pois o item que eu estou acionando ainda n�o existe
					    +"byte data[]={"+sensor.getPin()+", 1};\nSerial.write(data, 2);\n}"
						+ "}"
						+ "else if(digitalRead("+variableName+")==LOW && !"+variableStatusItem+"){\n"
						+ getCodeOffThis(item)
						+"byte data[]={"+sensor.getPin()+", 0};\nSerial.write(data, 2);\n}"
						+ "}");	
	}
	
	/*ESTE M�TODO RECEBE O SENSOR ULTRA_SONICO, O ITEM QUE SER� ACIONADO E UMA VARI�VEL QUE SER� USADA PARA DETERMINAR SE O ITEM DE ACIONAMENTO
	 * SER� ACIONADO QUANDO O VALOR DE CENTIMETROS FOR MAIOR (0) OU MENOR(1) OU DIFERENTE(2) QUE O VALOR TIMEON;*/
	public void addUltraSonic(Ultra_sonic ultra_sonic, Item item, final int MORE_OR_LESS){
		String variableName= ultra_sonic.getName();
		String variableButtonStatus= item.getName()+"Button_status";
		addLibrary("Ultrasonic.h");
		variables.add("Ultrasonic "+variableName+"("+ultra_sonic.getPin().split(":")[0]+", "+ultra_sonic.getPin().split(":")[1]+");");
		voidloop.add("float "+variableName+"_cm = ultrasonic.convert("+variableName+".timing(), Ultrasonic::CM);");
		String symbol= MORE_OR_LESS==0?">=":MORE_OR_LESS==1?"<":"!=";
		String neg_symbol=symbol.equals(">=")?"<":symbol.equals("<")?">=":"==";
		if(item.getType()!=1){
			voidloop.add( "if("+variableName+"_cm "+symbol+" "+ultra_sonic.getTimeon()+" && !"+variableButtonStatus+"){\n"
						+ "digitalWrite("+item.getName()+", HIGH);\n"
						+ "byte data[]={"+item.getPin()+", 1};\nSerial.write(data, 2);\n}\n}"
						+ "else if("+variableName+"_cm "+neg_symbol+" "+ultra_sonic.getTimeon()+" && !"+variableButtonStatus+"){\n"
						+ "digitalWrite("+item.getName()+", LOW);\n"
						+ "byte data[]={"+item.getPin()+", 1};\nSerial.write(data, 2);\n}\n}");
		}else{
			voidloop.add("if("+variableName+"_cm "+symbol+" "+ultra_sonic.getTimeon()+" && !"+variableButtonStatus+"){\n"
						+"for (int i = "+item.getName()+".read(); i < "+item.getTimeon()+"; i++) {\n"
						+item.getName()+".write(i);\n"
						+"delay(50);\n}\n"
						+"byte data[]={"+item.getPin()+", 1};\nSerial.write(data, 2);\n}\n"
						+ "else if("+variableName+"_cm "+neg_symbol+" "+ultra_sonic.getTimeon()+" && !"+variableButtonStatus+"){\n"
						+"for (int i = "+item.getName()+".read(); i > 0; i--) {\n"
						+item.getName()+".write(i);\n"
				        +"delay(50);\n}"
				        +"byte data[]={"+item.getPin()+", 0};\nSerial.write(data, 2);"
				        + "\n}");
		}
	}
	
	public void addServoInCode(String name, String pin, int ang, int pinButton){
		String variableStatus=name+"Button_status", variableButtonServo= name+"Button";
		addLibrary("Servo.h");
		voidsetup.add(name+".attach("+pin+");\n");
		voidsetup.add("pinMode("+variableButtonServo+", INPUT);\n");
		variables.add("Servo "+name+";");
		variables.add("boolean "+variableStatus+"= false;\nint "+variableButtonServo+"="+pinButton+";");
		methodComunication.add("if(sinal[0]=="+pin+" && sinal[1]==1){\n"+variableStatus+"=true;\n");
		methodComunication.add("for (int i = "+name+".read(); i < "+ang+"; i++) {\n");
		methodComunication.add(name+".write(i);\n");
		methodComunication.add("delay(50);\n}");
		methodComunication.add("byte data[]={"+pin+", 1};\nSerial.write(data, 2);\n}");
		methodComunication.add("else if(sinal[0]=="+pin+" && sinal[1]==0){\n"+variableStatus+"=false;\n");
		methodComunication.add("for (int i = "+name+".read(); i > 0; i--) {\n");
		methodComunication.add(name+".write(i);\n");
		methodComunication.add("delay(50);\n}");
		methodComunication.add("byte data[]={"+pin+", 0};\nSerial.write(data, 2);\n}");
		voidloop.add("if(digitalRead("+variableButtonServo+")==HIGH && !"+variableStatus+"){\n");
		voidloop.add(variableStatus+"=true;\n");
		voidloop.add("for (int i = "+name+".read(); i < "+ang+"; i++) {\n");
		voidloop.add(name+".write(i);\n");
		voidloop.add("delay(30);\n}\n");
		voidloop.add("byte data[]={"+pin+", 0};\nSerial.write(data, 2);\ndelay(300);}");
		voidloop.add("else if(digitalRead("+variableButtonServo+")==HIGH && "+variableStatus+"){\n");
		voidloop.add(variableStatus+"=false;\n");
		voidloop.add("for (int i = "+name+".read(); i > 0; i--) {\n");
		voidloop.add(name+".write(i);\n");
		voidloop.add("delay(50);\n}\n");
		voidloop.add("byte data[]={"+pin+", 0};\nSerial.write(data, 2);\ndelay(300);}");
	}
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BLOCO DE M�TODOS PARA A FACILITA��O DO ACIONAMENTO E DO DESACIONAMENTO 
	/*				M�todo que retorna o c�digo de ACIONAMENTO (ON) para os atuadores apresentados */
	public static String getCodeOnThis(Item item){
		final int TYPE= item.getType();
		switch(TYPE){
			case LED: return "digitalWrite("+item.getName()+", HIGH);\n"
								+ "byte data[]={"+item.getPin()+", 1};\n"
								+ "Serial.write(data, 2);\n";
			case SERVO_MOTOR: return "for (int i = "+item.getName()+".read(); i < "+item.getTimeon()+"; i++) {\n"
									+item.getName()+".write(i);\n"
									+"delay(50);\n}\n"
									+"byte data[]={"+item.getPin()+", 1};\nSerial.write(data, 2);\n";
			
			
			default: return null;
		}
		
	}
	
	/*				M�todo que retorna o c�digo de DESACIONAMENTO (OFF) para os atuadores apresentados */
	public static String getCodeOffThis(Item item){
		final int TYPE= item.getType();
		switch(TYPE){
			case LED: return "digitalWrite("+item.getName()+", LOW);\n"
								+ "byte data[]={"+item.getPin()+", 0};\n"
								+ "Serial.write(data, 2);\n";
			case SERVO_MOTOR: return "for (int i = "+item.getName()+".read(); i > 0; i--) {\n"
									+item.getName()+".write(i);\n"
							        +"delay(50);\n}\n"
							        +"byte data[]={"+item.getPin()+", 0};\nSerial.write(data, 2);\n";
			default: return null;
		}
	}
	
	//ATEN��O ----- CRIAR UM NOVO M�TODO QUE IR� INUTILIZAR ESSES DOIS M�TODOS QUE AQUI EST�O!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BLOCO DE M�TODOS PARA A FACILITA��O DO ACIONAMENTO E DO DESACIONAMENTO
	//---------------------------------------------------------------------------------------------------
	//Se o controlType(ONOFF) for 0: Maior que, se for 1 : Menor que
	//Esse m�todo deve retornar uma estrutura de controle (if) completa para uma determinada leitura digital e j� determinando o acionamento
	//do atuador;
	@Deprecated
	public void InitAnalogIfLess(Item analogItem, Item active){
		addAnalogItem(analogItem, active);
	}
	//
	@Deprecated
	public void InitAnalogIfMore(Item analogItem, Item active){
		addAnalogItem(analogItem, active);
	}
	//----------------------------------------------------------------------------------------------------
	
	
	public void addAnalogItem(Item analogItem, Item active){
		/*	Este m�todo ser� repons�vel por adicionar ao c�digo os itens anal�gicos.
		 * 	Os itens anal�gicos agoram ser�o acionado de acordo com o ONOFF do item anal�gico. O ONOFF ser� verificado nesse m�todo, que por sua vez determinar�
		 *  Qual simbolo utilizar� para acionar o item(active). Dessa forma, n�o ser� preciso um m�todo para cada condi��o. Este m�todo tamb�m usar� o m�todo 
		 *  getOnThis e getOffThis para melhor legibilidade.
		 * 																																						*/
		String variableStatus=active.getName()+"_status";//Vari�vel que cont�m a vari�vel no c�digo do arduino que controlar� o estado do item
		String variableLoop= analogItem.getName()+"temploop";//Vari�vel que cont�m a vari�vel no c�digo do arduino que controlar� o tempo de verifica��o e envio de informa��es.
		variables.add("unsigned long "+variableLoop+" = 0;"); // Adiciona ao c�digo do arduino a vari�vel que controla o tempo de execu��o e envio de informa��es.
		addVariableOfObject(analogItem);
		String dataline= analogItem.getPin().length()==2?"byte data_analog[]={'"+analogItem.getPin().charAt(0)+"','"+analogItem.getPin().charAt(1)+"'};"
				:"byte data_analog[]={'"+analogItem.getPin().charAt(0)+"','"+analogItem.getPin().charAt(1)+"','"+analogItem.getPin().charAt(2)+"'};";
		String symbol= analogItem.getOnoff()==0?">":analogItem.getOnoff()==1?"<":analogItem.getOnoff()==2?"!=":"=="; //Simbolo utilizado na condi��o principal
		String else_symbol= analogItem.getOnoff()==0?"<":analogItem.getOnoff()==1?">":analogItem.getOnoff()==2?"==":"!=";//Simbolo utilizado na condi��o contr�ria
		//Adiciona o c�digo de acionamento no void loop.
		voidloop.add("if ((unsigned long)(millis() - "+variableLoop+") >= 1000){"
		+ "if(analogRead("+analogItem.getName()+") "+symbol+" "+analogItem.getTimeon()+" && !"+variableStatus+"){\n"
		//+ "byte data[]={"+active+", 1};\n"
		+getCodeOnThis(active)
		+ "}else if(analogRead("+analogItem.getName()+") "+else_symbol+" "+analogItem.getTimeon()+" && !"+variableStatus+"){\n"
		+getCodeOffThis(active)
        + "\n}"
		+dataline+"\n"
		//+ "Serial.write(data, 2);\n}\n"
		+ variableLoop+" = millis();\n}");
	}
	
	public void addAlarm(Item buzzer){
		addVariableOfObject(buzzer);
		String buttonPiezo= buzzer.getName()+"Button";
		variables.add("int "+buttonPiezo+"="+buzzer.getPinButton()+";");
		voidloop.add("if (digitalRead("+buttonPiezo+")==HIGH){\n"
					+"tone("+buzzer.getName()+", 500);\n"
					+"delay(50);\n"
					+ "tone("+buzzer.getName()+", 750);\n"
			        +"delay(300);\n"
					+"noTone("+buzzer.getName()+");\n"
			        +"}\n");
	}
	
	//Faz um objeto do tipo item se tornar uma vari�vel DO TIPO INTEIRO!
	public void addVariableOfObject(Item object){
		String variableName=object.getName();
		String variableValue=object.getPin();
		variables.add("int "+variableName+"="+variableValue+";//Vari�vel do(a) "+object.getName()+"\n");
	}
	
	public void addButtonOfLight(LED object){
		String variableButtonName=object.getName()+"Button";
		Integer variableValue=object.getPinButton();
		String variableStatus= variableButtonName+"_status";
		variables.add("int "+variableButtonName+"="+variableValue+";//Bot�o de acionamento do(a) "+object.getName()+"\n");//Vari�vel do bot�o
		variables.add("boolean "+variableStatus+"=false;");//Vari�vel do estado do led
		addVariableOfObject(object); //Adiciono a var�vel do led
		initObjectSetup(variableButtonName, 1);//Inicializo o bot�o no voidsetup
		initObjectSetup(object.getName(), 0);//Initializo o led no voidsetup
		voidloop.add("if(digitalRead("+variableButtonName+")==HIGH && "+variableStatus+"==false){"
				+ "\ndigitalWrite("+object.getName()+", HIGH);"
				+ "\n"+variableStatus+"=true;\n"
				+ "byte data[]={"+object.getPin()+", 1};"
				+ "Serial.write(data, 2);\n"
				+ "Serial.flush();"
				+ "\ndelay(300);"
				+ "}\nelse if(digitalRead("+variableButtonName+")==HIGH && "+variableStatus+"==true){"
				+ "\n"+variableStatus+"=false;"
				+ "\ndigitalWrite("+object.getName()+", LOW);\n"
				+ "byte data[]={"+object.getPin()+", 0};\n"
				+ "Serial.write(data, 2);\n"
				+ "Serial.flush();\n"	
				+ "delay(300);\n"
				+ "}");
		methodComunication.add("if(sinal[0]=="+object.getPin()+" && sinal[1]==1 && !"+ variableStatus+"){\n");
		methodComunication.add("digitalWrite("+object.getName()+", HIGH);\n");
		methodComunication.add(variableStatus+"=true;\n"
				+ "byte data[]={"+object.getPin()+", 1};"
				+ "Serial.write(data, 2);\n"
				+ "Serial.flush();"
		+ "}\nelse if(sinal[0]=="+object.getPin()+" && sinal[1]==0 && "+variableStatus+"){"
		+ "\n"+variableStatus+"=false;"
		+ "\ndigitalWrite("+object.getName()+", LOW);\n"
		+ "byte data[]={"+object.getPin()+", 0};"
		+ "Serial.write(data, 2);\n"
		+ "Serial.flush();"
		+ "}");
		
	}
	
	//Faz de objetos do tipo item se tornem vari�veis
	public void addVariablesOfObjects(Item... object){
		for(Item item:object){
			String variableName=item.getName();
			String variableValue=item.getPin();
			variables.add("int "+variableName+"="+variableValue+";//Vari�vel do(a) "+item.getName()+"\n");
		}
	}
	
	//Inicia o objeto no void setup()
	public void initObjectSetup(String name, int type){
		final int INPUT=1, OUTPUT=0, INPUT_PULLUP=2;
		switch(type){
		case INPUT: voidsetup.add("pinMode("+name+", INPUT);\n");
			break;
		case OUTPUT: voidsetup.add("pinMode("+name+", OUTPUT);\n");
			break;
		case INPUT_PULLUP: voidsetup.add("pinMode("+name+", INPUT_PULLUP);\n");
			break;
		}
	}
	 
	//Inicia o objeto no void setup()
	public void initObjectWithSinalSetup(Item item, int type, int serial){
		final int INPUT=1, OUTPUT=0, INPUT_PULLUP=2;
		switch(type){
		case INPUT:{
			voidsetup.add("pinMode("+item.getName()+", INPUT);\n");
			//EnableSerialSignal(item,serial);
		}
			break;
		case OUTPUT:{
			voidsetup.add("pinMode("+item.getName()+", OUTPUT);\n");
			//EnableSerialSignal(item,serial);
		}
			break;
		case INPUT_PULLUP:{
			voidsetup.add("pinMode("+item.getName()+", INPUT_PULLUP);\n");
			//EnableSerialSignal(item,serial);
		}
			break;
		}
	}
	
	//Se a linha conter o nome do item e o pino do mesmo ele automaticamente deleta.
	public void deleteVariable(Item object){
		variables.removeIf(i->i.contains("int "+object.getName()+"="+object.getPin()+";"));
		voidsetup.removeIf(i->i.contains("pinMode("+object.getName()));
	}
	
	//Inicia a comunica��o serial0
	public void initSerial(int baunds){
		voidsetup.removeIf(i->i.contains("Serial.begin("));
		voidsetup.add("Serial.begin("+baunds+"); //Iniciando comunica��o Serial0\n");
		serialEvent0.add("//Envia o sinal que chega para o m�todo de comunica��o\n");
		serialEvent0.add("byte data[2];\nSerial.readBytes(data, 2);\nmethodComunication(data);");
	}
	
	
	public void initSerial1(int baunds){
		voidsetup.add("Serial1.begin("+baunds+"); //Iniciando comunica��o Serial1.");
		serialEvent1.add("if(Serial1.available())");
		serialEvent1.add("methodComunication(Serial1.read());");
	}
	
	public void initSerial2(int baunds){
		voidsetup.add("Serial2.begin("+baunds+"); //Iniciando comunica��o Serial2.");
		serialEvent2.add("if(Serial2.available())");
		serialEvent2.add("methodComunication(Serial2.read());");
	}
	
	public void addNTCTemp(Item analogNTC, String active, int MoreOrLess){
		String variableLoop= analogNTC.getName()+"temploop";
		String variableStatus=active+"Button_status";
		addLibrary("Thermistor.h");
		variables.add("Thermistor "+analogNTC.getName()+"("+analogNTC.getPin()+");");
		variables.add("unsigned long "+variableLoop+" = 0;");
		String moreorless= MoreOrLess==0?">":"<=";
		String elsecondition= MoreOrLess==0?"<=":">";
		String dataline= analogNTC.getPin().length()==2?"byte data_analog[]={'"+analogNTC.getPin().charAt(0)+"','"+analogNTC.getPin().charAt(1)+"',temperatura};"
				:"byte data_analog[]={'"+analogNTC.getPin().charAt(0)+"','"+analogNTC.getPin().charAt(1)+"','"+analogNTC.getPin().charAt(2)+"'temperatura};";
		voidloop.add("if ((unsigned long)(millis() - "+variableLoop+")>= 1050){\n"
						+ "if("+analogNTC.getName()+".getTemp()"+moreorless+analogNTC.getTimeon()+" && !"+variableStatus+"){\n"
						+ "digitalWrite("+active+", HIGH);\n}"
						+ "else if("+analogNTC.getName()+".getTemp()"+elsecondition+analogNTC.getTimeon()+" && !"+variableStatus+"){\n"
						+ "digitalWrite("+active+", LOW);\n}\n"
						+ dataline+"\n}"
						+ variableLoop+" = millis();\n}"
						);
	}

	public void addLM35(Item analogLM35, String active, int MoreOrLess){
		String variableLoop= analogLM35.getName()+"temploop";
		String variableStatus=active+"Button_status";
		variables.add("int "+analogLM35.getName()+"="+analogLM35.getPin()+"; //LM35");
		variables.add("unsigned long "+variableLoop+" = 0;");
		String moreorless= MoreOrLess==0?">":"<=";
		String elsecondition= MoreOrLess==0?"<=":">";
		String dataline= analogLM35.getPin().length()==2?"byte data_analog[]={'"+analogLM35.getPin().charAt(0)+"','"+analogLM35.getPin().charAt(1)+"',temperatura};"
				:"byte data_analog[]={'"+analogLM35.getPin().charAt(0)+"','"+analogLM35.getPin().charAt(1)+"','"+analogLM35.getPin().charAt(2)+"'temperatura};";
		voidloop.add("if ((unsigned long)(millis() - "+variableLoop+")>=2000){\n"
						+ "float temperatura = (float(analogRead(LM35))*5/(1023))/0.01;"
						+ "if(analogRead(temperatura)"+moreorless+analogLM35.getTimeon()+" && !"+variableStatus+"){\n"
						+ "digitalWrite("+active+", HIGH);\n}"
						+ "else if(analogRead(temperatura)"+elsecondition+analogLM35.getTimeon()+" && !"+variableStatus+"){\n"
						+ "digitalWrite("+active+", LOW);\n}\n"
						+ dataline+"\n}"
						+ variableLoop+" = millis();\n}"
						);
		
	}
	
	//Gera o c�digo final
	public void getCode(){
		if(!serialEvent0.stream().filter(i->i.equals("byte data[2];\nSerial.readBytes(data, 2);\nmethodComunication(data);")).findFirst().isPresent())
			initSerial(9600);
		ArrayList<String> codeBuffer= new ArrayList<String>();
		codeBuffer.add("/*Este software foi criado por Junior Santos - Para saber mais sobre o desenvolvimento e o autor "
				+ "v� at� as informa��es do software. */\n");
		codeBuffer.addAll(librarys);
		//As vari�veis s�o as segundas a serem adicionadas
		codeBuffer.addAll(variables);
		//Os m�todos principais - void setup()
		codeBuffer.add("\nvoid setup(){\n");
		codeBuffer.addAll(voidsetup);
		codeBuffer.add("\n}\n");
		codeBuffer.add("void loop(){\n");
		codeBuffer.addAll(voidloop);
		codeBuffer.add("}\n");
		codeBuffer.add("void methodComunication(byte sinal[]){\n");
		codeBuffer.addAll(methodComunication);
		codeBuffer.add("\n}\n");
		codeBuffer.add("void serialEvent(){\n");
		codeBuffer.addAll(serialEvent0);
		codeBuffer.add("}\n");
		codes.getItems().setAll(codeBuffer);
	}
	
	public void clear(){
		variables.clear();
		voidsetup.clear();
		voidloop.clear();
		serialEvent0.clear();
		methodComunication.clear();
		librarys.clear();
	}

}
