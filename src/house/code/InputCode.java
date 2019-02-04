package house.code;

import java.io.Serializable;

import javafx.scene.control.Label;

/**
 * Essa classe é responsável por armazenar o dado informado no formulário de inserção do item ao projeto,
 * apresentar identificação que será inserida no código original, e depois substituida pelo valor indicado no formulário.
 * @author JJSantos
 *
 */
public class InputCode extends Label implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5236654609152763783L;
	private String name, input;
	private int type;
	public static final int INTEGER=0, CHAR=1, STRING=2, FLOAT=3, DOUBLE=4;
	
	
	public InputCode(String name, int type){
		setName(name);
		setType(type);
		this.setText(name+" - "+type);
		switch(type){
			case INTEGER:{
				this.setStyle("-fx-font-size: 11px; -fx-background-color: red; -fx-background-radius: 10; -fx-padding: 2 2 2 2; -fx-text-fill: white;");
			}break;
			case CHAR:{
				this.setStyle("-fx-font-size: 11px; -fx-background-color: orange; -fx-background-radius: 10; -fx-padding: 2 2 2 2; -fx-text-fill: white;");
			}break;
			case STRING:{
				this.setStyle("-fx-font-size: 11px; -fx-background-color: lightblue; -fx-background-radius: 10; -fx-padding: 2 2 2 2; -fx-text-fill: white;");
			}break;
			case FLOAT:{
				this.setStyle("-fx-font-size: 11px; -fx-background-color: lightgreen; -fx-background-radius: 10; -fx-padding: 2 2 2 2; -fx-text-fill: white;");
			}break;
			case DOUBLE:{
				this.setStyle("-fx-font-size: 11px; -fx-background-color: yellow; -fx-background-radius: 10; -fx-padding: 2 2 2 2; -fx-text-fill: white;");
			}break;
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setInput(String input){
		this.input=input;
	}
	
	public String getInput(){
		return input;
	}
	
	
	
}
