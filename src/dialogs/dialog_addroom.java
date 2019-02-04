package dialogs;

import database.User;
import house.Room;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class dialog_addroom extends Dialog<Room> {

	private GridPane gridpane; 
	private Label name_question=new Label(), info_question=new Label(), image_question=new Label();
	final private TextField tf_name=new TextField(), tf_info=new TextField(), tf_imageurl=new TextField();
	final private User usuario;
	
	public dialog_addroom(User usuario){
		this.usuario=usuario;
		this.getDialogPane().setContent(getGridPane());
		loadLabels();
	}
	
	private void loadLabels(){
		switch(usuario.getLanguage()){
			case 0: //Linguagem português
				break;
			case 1: //Linguagem inglês
				break;
		}
	}
	
	private GridPane getGridPane(){
		gridpane= new GridPane();
		gridpane.setHgap(10);
		gridpane.setVgap(10);
		gridpane.add(name_question, 0, 0);
		gridpane.add(tf_name, 0, 1);
		gridpane.add(info_question, 1, 0);
		gridpane.add(tf_info, 1, 1);
		gridpane.add(image_question, 2, 0);
		gridpane.add(tf_imageurl, 2, 1);
		return gridpane;
	}
	

	
}
