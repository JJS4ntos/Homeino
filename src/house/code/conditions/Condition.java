package house.code.conditions;

import house.Item;
import java.util.ArrayList;

public class Condition {

	private final int IF=0, WHILE=1, DOWHILE=2, FOR=3;
	private final int id, item_id, item_active_id, condition_type, onoff, controller_type;//Propriedades principais
	private final String readString;
	private ArrayList<Item> items= new ArrayList<>();
	
	public Condition(int id, int item_id, int item_active_id, int condition_type, int onoff, int controller_type, String readString){
		this.id=id;//Identifica��o �nica da condi��o
		this.readString=readString;
		this.item_id=item_id;//Identifica��o �nica do item sensor
		this.item_active_id=item_active_id;//Identifica��o �nica do item atuador
		this.condition_type=condition_type;//Tipo da condi��o
		this.controller_type=controller_type;//Tipo da estrutura de controle(IF=0, WHILE=1, DOWHILE=2, FOR=3)
		this.onoff=onoff;//Liga o item=1, desliga o item=0
		
	}
	
	//Esse m�todo ir� inserir um c�digo nesta estrutura de controle(Condition)
	public void addCode(Item code){
		this.items.add(code);
	}
	
	public void getConditionCode(){
		StringBuffer sb= new StringBuffer();
		switch(controller_type){
			case IF:{
				sb.append("if("+readString+"){\n");
			}break;
			case WHILE:{
				sb.append("while("+readString+"){\n");
			}break;
			case DOWHILE:{
				sb.append("do{\n");
			}break;
			case FOR:{
				sb.append("for("+readString+"){\n");
			}break;
		}
		//Preciso criar um m�todo em alguma classe para pesquisar o item adicionado nessa condi��o, caso o item seja inexistente a condi��o ser� exclu�da;
		/*if(isOutEnable())
			ParseToCode.getCodeOnThis(code);
		else ParseToCode.getCodeOffThis(code);*/
		//items.forEach(i->);
	}
	
	public char getCloseChar(){
		return '}';
	}
	
	//Faz com que a sa�da seja ativada ou desativada?(Liga ou desliga o item)
	public boolean isOutEnable(){
		return onoff==1;
	}
	
	

}
