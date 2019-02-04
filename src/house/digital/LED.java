package house.digital;

import house.Item;
import house.Room;

import java.io.IOException;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import comunication.PopOvers;

import controllers.telaprincipal_controller;
import animation.runanim;
import anothers.personal_filters;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/*Esta classe é filha da classe item - A classe item é responsável por padronizar todos os objetos que podem ser colocados na planta e depois
 * utilizados como controladores.*/
public class LED extends Item{

	transient public Image light_on= new Image(getClass().getResource("/images/L1.png").toExternalForm()),
			light_off= new Image(getClass().getResource("/images/L0.png").toExternalForm());
	
	public LED(int id, String name, int timeon, int onoff, int parent, String pin, int button, double x, double y, Room room, int type) {
		super(id, name, timeon, onoff, parent, pin, button, x, y, room, type,"", false);
		setTimeon(timeon);
		initPopOver();
		//lbl_name.setText(name);
		
		if(onoff==1){img_view.setImage(light_on);
		turn.setSelected(true);
		}else{img_view.setImage(light_off);
		turn.setSelected(false);
		}
		//init();
	}
	
	public void init(){
		loadObjectFromItem();
	}

	public void loadObjectFromItem(){
		
		//img_view.setOnMouseEntered(e->runanim.mouseOn(img_view, 50));
		//img_view.setOnMouseExited(e->runanim.mouseOff(img_view, 50));
		turn.setAlignment(Pos.TOP_CENTER);
		turn.selectedProperty().addListener((e,e1,e2)->{
			if(e1.booleanValue()){
				img_view.setImage(light_off);
				setOnoff(0);
				sendsinal();
				
				//lbl_name.setTextFill(Color.RED);
				//turn.setText("Desligada");
			}
			else{
				img_view.setImage(light_on);
				setOnoff(1);
				sendsinal();
				//lbl_name.setTextFill(Color.GREEN);
				//turn.setText("Ligada");
			}
		});
    	//this.minWidthProperty().bindBidirectional(img_view.fitWidthProperty());
		this.setOnMouseClicked(e->{
			if(e.getClickCount()==2){
				if(!pop_changes.isShowing()){
					pop_changes.show(this);
				}else{
					pop_changes.hide(Duration.ZERO);
				}
			}
		});
		
		this.getChildren().setAll(lbl_name, img_view, turn);
		this.setAlignment(Pos.CENTER);
		this.setLayoutX(x);
		this.setLayoutY(y);
	}

	public void setTimeon(int timeon) {
		this.timeon = timeon;
	}

	public void initPopOver(){
		pop_changes=PopOvers.getDigitalPopOver(this, telaprincipal_controller.getLanguage());
	}

	public Label getLabel(){
		return lbl_name;
	}

	public void setOnoff(Integer onoff) {
		attStatusLight(onoff);
		this.onoff = onoff;
		//if(onoff==1)turn.setSelected(true);//img_view.setImage(light_on);
		//else turn.setSelected(false);//img_view.setImage(light_off);
	}
	
	public void sendsinal(){
	 try {getRoom().sendSinal(Integer.valueOf(this.getPin()).byteValue(), onoff.byteValue());}
	 catch (IOException io) {io.printStackTrace();}
	}
	
}
