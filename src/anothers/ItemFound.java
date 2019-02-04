package anothers;

import java.io.IOException;

import database.Sqlite;
import house.Item;
import house.code.InputCode;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import house.person_Item;

public class ItemFound extends HBox {

	private String name, pin, buttonpin;
	private InputCode[] inputs; //Apenas para itens criados pelo usuário
	private final Image imageb;
	private final int item_id;
	private SimpleBooleanProperty disable= new SimpleBooleanProperty(true);
	public Button save;
	private Item item;
	
	/***
	 * Construtor para itens criados pelo usuário
	 * @param inputs
	 * @param imageb
	 * @param name
	 * @param pin
	 * @param buttonpin
	 * @throws IOException 
	 */
	public ItemFound(Item item) throws IOException{
		if(item.getType()==99){
			person_Item uitem=(person_Item)item; 
			this.item_id=uitem.getItemId();
			this.name=uitem.getName();
			this.pin=uitem.getPin();
			this.buttonpin=String.valueOf(uitem.getPinButton());
			this.inputs=uitem.getInputs().toArray(inputs);
			this.imageb=uitem.getData().loadImage();
		}else{
		this.item_id=item.getItemId();
		this.name=item.getName();
		this.pin=item.getPin();
		this.buttonpin=String.valueOf(item.getPinButton());
		this.imageb=item.getImage();
		}
		this.item=item;
		init();
	}

	private void init(){
		TextField tf_name= new TextField(name), tf_pin=new TextField(pin), tf_buttonpin= new TextField(buttonpin);
		tf_name.disableProperty().bind(disable); tf_pin.disableProperty().bind(disable); tf_buttonpin.disableProperty().bind(disable);
		Label lbl_itemid= new Label(String.valueOf(item_id));
			lbl_itemid.setTextAlignment(TextAlignment.CENTER);
			lbl_itemid.setAlignment(Pos.CENTER);
		save= new Button("Editar");
		Button cancel=new Button("Cancelar");
		cancel.visibleProperty().bind(disable.not());
		this.getChildren().addAll(lbl_itemid, tf_name, tf_pin, tf_buttonpin, save, cancel);
		this.setSpacing(3);
		save.setOnAction(e->{
				if(disable.get()){
					disable.setValue(false);
					save.setText("Salvar");
				}else{disable.setValue(true);
					save.setText("Editar");
					Sqlite.insert("UPDATE light_children SET name='"+tf_name.getText()+"', pin='"+tf_pin.getText()+"', buttonpin='"+
							tf_buttonpin.getText()+"' WHERE Id='"+item_id+"'");
					item.setName(tf_name.getText());
					item.setPin(tf_pin.getText());
					item.setPinButton(Integer.valueOf(tf_buttonpin.getText()));
				}
		});
		cancel.setOnAction(e->{disable.setValue(true);save.setText("Editar");});
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getButtonpin() {
		return buttonpin;
	}

	public void setButtonpin(String buttonpin) {
		this.buttonpin = buttonpin;
	}

	public InputCode[] getInputs() {
		return inputs;
	}

	public void setInputs(InputCode[] inputs) {
		this.inputs = inputs;
	}

	public Image getImage() {
		return imageb;
	}

	public int getItemId() {
		return item_id;
	}
	
}
