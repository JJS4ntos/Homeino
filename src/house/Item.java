package house;


import org.controlsfx.control.PopOver;

import animation.runanim;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import database.Sqlite;
import house.code.EditItem;

public abstract class Item extends VBox{
	/**
	 * 
	 */
	/*O MASTER_KEY SERÁ */
	protected String name, pin, string_use, icon_dir, library;
	protected Integer onoff, pinButton;
	protected double x,y, deltax, deltay;
	protected final Integer id, parent, type;
	transient private final Room room;
	protected int timeon=-1;
	protected ImageView img_view= new ImageView();
	protected PopOver pop_changes;
	protected Label lbl_name= new Label();
	public ToggleButton turn = new ToggleButton();
	protected boolean analog=false, sensor=false;
	
	public Item(int id, String name, int timeon, int onoff, int parent, String pin, int button, double x, double y, Room room, int type, String string_use, boolean sensor){
		this.room=room;
		this.string_use=string_use;
		this.id=id;
		this.parent=parent;
		this.pin=pin;
		this.pinButton=button;
		this.x=x;
		this.y=y;
		this.type=type;
		this.name=name;
		this.timeon=timeon;
		this.onoff=onoff;
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.sensor=sensor;
		init_vbox();
	}
	
	public Label getLabel(){
		return lbl_name;
	}
	
	public boolean isSensor(){
		return sensor;
	}
	
	public String getStringUSE(){
		return string_use;
	}
	
	public Item(int id, String name, int onoff, int parent, String pin, int button, double x, double y, Room room, int type, String string_use, boolean sensor){
		this.room=room;
		this.id=id;
		this.parent=parent;
		this.string_use=string_use;
		this.pin=pin;
		this.pinButton=button;
		this.x=x;
		this.y=y;
		this.type=type;
		this.name=name;
		this.onoff=onoff;
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.sensor=sensor;
		init_vbox();
	}
	
	private void init_vbox(){
		this.setOnMouseEntered(e->runanim.mouseOn(this, 100));
		this.setOnMouseExited(e->runanim.mouseOff(this, 100));
		final String style="-fx-background-color: LightSeaGreen; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 9;"
				+ "-fx-padding: 2 2 2 2;";
		img_view.setFitHeight(50);
		img_view.setFitWidth(50);
		lbl_name.setMaxHeight(10);
		lbl_name.setMaxWidth(100);
		//this.getChildren().add(editPane);
		//StackPane top= new StackPane();
		HBox topBox= new HBox();
		topBox.setSpacing(3);
		Label close=new Label("X"), edit= new Label("E");
		close.setStyle(style);edit.setStyle(style);
		topBox.setAlignment(Pos.CENTER_RIGHT);
		//topBox.getChildren().addAll(edit, close);
		close.setOnMouseClicked(e->{
			//Mensagem perguntando se o usuário realmente deseja excluir este item
			if(!dialogs.dialogs.createConfirmationDialog("Atenção", "Deseja excluir este item?", "Após a exclusão ele não poderá ser recuperado.").showAndWait().get()
			.getButtonData().isCancelButton()){
				Sqlite.deleteItem(this);
				Platform.runLater(()->this.getRoom().getCommons_loads().loadRooms());
			}
		});
		edit.setOnMouseClicked(e->{
			EditItem editPane= new EditItem(this);
			editPane.show();
		});
		lbl_name.setText(name+"("+getPin()+")");
		lbl_name.setStyle(style);
		lbl_name.setTooltip(new Tooltip(getName()+"("+getPin()+")"));
		//lbl_name.setPrefHeight(20);
		lbl_name.setWrapText(true);
		topBox.getChildren().addAll(lbl_name, edit, close);
		this.setPrefSize(90, 90);
		//this.setStyle("-fx-background-color: DarkCyan; -fx-opacity: 0.7; -fx-background-radius: 7;");
		this.setStyle("-fx-border-color: LightSeaGreen; -fx-opacity: 0.8; -fx-border-radius: 5; -fx-border-width: 3;");
		//img_view.setFitWidth(50);
		//img_view.setFitHeight(50);
		turn.setCursor(Cursor.HAND);
		turn.setTooltip(new Tooltip("Aperte para enviar sinal e ligar em comunicação "
				+ "ou aperte apenas para alterar o estado atual quando não estiver em comunicação"));
		turn.setText("On/Off");
		//turn.setStyle("-fx-background-color: black; -fx-text-fill:white; -fx-background-radius:3;");
		this.setAlignment(Pos.CENTER);
		//this.setOnDragDetected(e->);
		
		this.setOnMouseDragged((e)->{
		
		if(e.getSceneX()-100>this.getParent().getLayoutX()&&e.getSceneY()-110>this.getParent().getLayoutY()){
			this.setCursor(Cursor.MOVE);
			//Com o getScene não tem ruído, entretanto preciso diminuir a defasagem da movimentação
			this.setLayoutX(e.getSceneX()-85);
			this.setLayoutY(e.getSceneY()-220);
		}
		});
		this.setOnMouseReleased(e->attXY(e.getSceneX()-85, e.getSceneY()-220));
		//SimpleBooleanProperty property= new SimpleBooleanProperty(sensor);
		//ButtonAdd.visibleProperty().bind(property);
		
		this.getChildren().setAll(topBox, img_view);
	}
	
	public void setName(String name) {
		attName(name);
		this.name = name;
		getLabel().setText(name+"("+getPin()+")");
	}

	public void setPin(String pin) {
		attPin(pin);
		getLabel().setText(getName()+"("+pin+")");
		this.pin = pin;
	}
	
	public void setX(double x) {
		attX(x);
		this.x=x;
	}

	public void setY(double y) {
		attY(y);
		this.y=y;
	}
	
	public void setPinButton(Integer pinButton) {
		attPinButton(pinButton);
		this.pinButton = pinButton;
	}
	
	public Integer getLightParent(){
		return parent;
	}
	
	public int getTimeon(){
		return timeon;
	}

	public String getPin() {
		return pin;
	}

	public Integer getItemId(){
		return id;
	}
	
	public double getX() {return x;}

	public double getY() {return y;}

	public Integer getPinButton() {
		return pinButton;
	}
	
	public String getName() {
		return name;
	}

	public Integer getOnoff() {
		return onoff;
	}
	
	public Integer getType() {
		return type;
	}
	
	public Image getImage(){
		return img_view.getImage();
	}
	
	//abstract public Integer getId();
	//abstract public void setOnoff(Integer onoff);
	//abstract public String getName();
	//abstract public void setName(String name);
	//abstract public Integer getPin();
	//abstract public void setPin(Integer pin);
	//abstract public double getX();
	//abstract public double getY();
	//abstract public Integer getOnoff();
	//abstract public void setX(double x);
	//abstract public void setY(double y);
	abstract public void initPopOver();
	//abstract public Integer getType();
	
	public boolean isAnalog(){
		return analog;
	}
	
	public void delete(){
		if(!dialogs.dialogs.createConfirmationDialog("Atenção", "Realmente deseja excluir esse item?",
				"Após excluído ele não poderá ser recuperado.").showAndWait().get().getButtonData().isCancelButton()){
			Sqlite.insert("DELETE FROM light_children WHERE id='"+getItemId()+"'");
			dialogs.dialogs.showmessage("Terminado", "Item excluído com sucesso!", "Não é possível recupera-lo.", AlertType.INFORMATION);
			this.getChildren().clear();
			this.setVisible(false);
			pop_changes.hide();
			getRoom().getCommons_loads().loadRooms();
		}
	}
	
	protected void attName(String name){
		Sqlite.insert("UPDATE light_children SET name='"+name+"' WHERE id='"+getItemId()+"'");
	}
	
	protected void attPin(String pin){
		Sqlite.insert("UPDATE light_children SET pin='"+pin+"' WHERE id='"+getItemId()+"'");
	}

	protected void attX(double x){
		Sqlite.insert("UPDATE light_children SET X='"+x+"' WHERE id='"+getItemId()+"'");
	}
	
	protected void attY(double y){
		Sqlite.insert("UPDATE light_children SET Y='"+y+"' WHERE id='"+getItemId()+"'");
	}
	
	protected void attStatusLight(int status){
		Sqlite.insert("UPDATE light_children SET onoff='"+status+"' WHERE id='"+getItemId()+"'");
	}
	
	protected void attPinButton(int pin){
		Sqlite.insert("UPDATE light_children SET buttonpin='"+pin+"' WHERE id='"+getItemId()+"'");
	}
	
	protected void attTimeon(int timeon){
		Sqlite.insert("UPDATE light_children SET timeon='"+timeon+"' WHERE id='"+getItemId()+"'");
	}
	
	protected void attXY(double x, double y){
		Sqlite.insert("UPDATE light_children SET x='"+x+"', y='"+y+"' WHERE id='"+getItemId()+"'");
	}

	public Room getRoom() {
		return room;
	}
	
}
