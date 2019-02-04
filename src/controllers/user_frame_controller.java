package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import database.Sqlite;
import database.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;


public class user_frame_controller implements Initializable {

	@FXML private Label lbl_hello, lbl_myname, lbl_mypassword, lbl_confirmpassword, lbl_secret, my_language;
    @FXML private PasswordField tf_password, tf_passwordcheck;
    @FXML private TextField tf_name, tf_login, tf_secret;
    @FXML private ComboBox<String> cb_language;
    @FXML private Button btn_save, btn_exit, btn_clear_rooms, btn_clear_myitems;
	private User user;
	private SimpleBooleanProperty passwordProperty= new SimpleBooleanProperty(false);
	@FXML private TitledPane root;
	
    public user_frame_controller(User user){
    	this.user=user;
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		init();
	}

	private void init(){
		lbl_hello.setText("Olá, "+user.getName()+"!");
		tf_login.setText(user.getLogin());
		tf_name.setText(user.getName());
		passwordProperty.bind(tf_password.textProperty().isEqualTo(tf_passwordcheck.getText()));
		btn_save.disableProperty().bind(passwordProperty);
		btn_save.setOnAction(e->{
			Sqlite.updateUser(user, tf_passwordcheck.getText(), tf_secret.getText());
		});
		
	}	
}
