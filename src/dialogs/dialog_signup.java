package dialogs;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

import comunication.Translate;

import database.Commons_loads;
import database.Sqlite;
import anothers.TextVerifField;
import anothers.personal_filters;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class dialog_signup extends Dialog<Boolean>{

	//final public int LANGUAGE_PORTUGUES=0, LANGUAGE_ENGLISH=1;
	private TextVerifField tf_name,
			tf_login, tf_secret; 
	private PasswordField tf_password= new PasswordField(), tf_confirmpass= new PasswordField();
	private Label lbl_name=new Label(), lbl_login=new Label(),
			lbl_password=new Label(), lbl_confirmpass=new Label(),
			lbl_secret= new Label(), lbl_language= new Label(), lbl_password_status= new Label();
	private ComboBox<String> cb_language= new ComboBox<String>();
	private ButtonType btn_cancel= new ButtonType("", ButtonData.CANCEL_CLOSE), btn_signup= new ButtonType("", ButtonData.OK_DONE);
	private Button signupButton, cancelButton;
	
	public dialog_signup() {
		tf_name= new TextVerifField(cb_language.selectionModelProperty().get().selectedIndexProperty(), 8);
		tf_login= new TextVerifField(cb_language.selectionModelProperty().get().selectedIndexProperty(), 6);
		tf_secret= new TextVerifField(cb_language.selectionModelProperty().get().selectedIndexProperty(), 20); 
		GridPane gridpane= new GridPane();
		gridpane.setPrefSize(500, 200);
		gridpane.setHgap(2);
		gridpane.setVgap(2);
		//
		gridpane.add(lbl_name, 0, 0);
		gridpane.add(tf_name, 1, 0);
		//
		gridpane.add(lbl_login, 0, 1);
		gridpane.add(tf_login, 1, 1);
		//
		gridpane.add(lbl_password, 0, 2);
		gridpane.add(tf_password, 1, 2);
		//
		gridpane.add(lbl_confirmpass, 0, 3);
		gridpane.add(new VBox(tf_confirmpass, lbl_password_status) , 1, 3);
		//
		gridpane.add(lbl_secret, 0, 4);
		gridpane.add(tf_secret, 1, 4);
		//
		gridpane.add(lbl_language, 0, 5);
		gridpane.add(cb_language, 1, 5);
		initBox();
		this.getDialogPane().getButtonTypes().addAll(btn_signup,btn_cancel);
		this.getDialogPane().setContent(gridpane);
		signupButton = (Button) this.getDialogPane().lookupButton(btn_signup);
		cancelButton= (Button) this.getDialogPane().lookupButton(btn_cancel); 
		tf_name.getTextField().textProperty().addListener(e->check(signupButton));
		tf_login.getTextField().textProperty().addListener(e->check(signupButton));
		tf_password.textProperty().addListener(e->check(signupButton));
		tf_confirmpass.textProperty().addListener(e->check(signupButton));
		cb_language.selectionModelProperty().get().selectedItemProperty().addListener(e->{
			setLanguage(cb_language.getSelectionModel().getSelectedIndex());
		check(signupButton);});
		tf_secret.getTextField().textProperty().addListener(e->check(signupButton));
		Platform.runLater(() ->{ tf_name.requestFocus(); signupButton.disableProperty().set(true);});
		this.setResultConverter(dialogButton -> {
		if (dialogButton == btn_signup) {
			return true;} return null;
		});
		cb_language.getSelectionModel().select(0);
	}
	
	public void setLanguage(int language){
		switch(language){
		case  Translate.PORTUGUESE:{
				lbl_name.setText("Qual o seu nome?");
				lbl_login.setText("Digite o seu login:");
				lbl_password.setText("Digite a sua senha:");
				lbl_confirmpass.setText("Confirme a sua senha:");
				lbl_secret.setText("Digite sua frase secreta:");
				lbl_language.setText("Selecione uma linguagem:");
				signupButton.setText("Cadastrar");
				cancelButton.setText("Cancelar");
				this.setTitle("Criar um novo usuário");
				this.setHeaderText("Preencha as informações abaixo para criar um novo usuário");
			}break;
		case  Translate.ENGLISH:{
			lbl_name.setText("What's your name?");
			lbl_login.setText("Write your login:");
			lbl_password.setText("Write your password:");
			lbl_confirmpass.setText("Confirm your password:");
			lbl_secret.setText("Write your secret phrase:");
			lbl_language.setText("Select a language:");
			signupButton.setText("Sign up");
			cancelButton.setText("Cancel");
			this.setTitle("Create new user");
			this.setHeaderText("Fill in the information to create a new user");
		}break;
		case Translate.GERMANY:{
			lbl_name.setText("Was ist lhr name?");
			lbl_login.setText("Geben sie einfach lhre login:");
			lbl_password.setText("Geben sie lhr passwort:");
			lbl_confirmpass.setText("Bestätigen sie lhr passwort:");
			lbl_secret.setText("Geben sie einen begriff:");
			lbl_language.setText("Wählen sie eine sprache:");
			signupButton.setText("Registrieren");
			cancelButton.setText("Stornieren");
			this.setTitle("Erstellen sie einen neuen benutzer");
			this.setHeaderText("Füllen sie die informationen einen neuen benutzer zu erstellen");
		}break;
		case Translate.SPANISH:{
			lbl_name.setText("Escribe tu nombre:");
			lbl_login.setText("Introduzca su nombre de usuario:");
			lbl_password.setText("Escriba su contraseña:");
			lbl_confirmpass.setText("Escriba su contraseña:");
			lbl_secret.setText("Escribe una frase:");
			lbl_language.setText("Seleccione un idioma:");
			signupButton.setText("Registrar");
			cancelButton.setText("Cancelar");
			this.setTitle("Crear un nuevo usuario");
			this.setHeaderText("Llene la información para crear un nuevo usuario");
		}break;
		case Translate.FRANCE:{
			lbl_name.setText("Quel est votre nom?");
			lbl_login.setText("Entrez votre login:");
			lbl_password.setText("Entrez votre mot de passe:");
			lbl_confirmpass.setText("Confirmea votre mot de passe:");
			lbl_secret.setText("Tapez une phrase:");
			lbl_language.setText("Sélectionnez une langue:");
			signupButton.setText("Enregistrer");
			cancelButton.setText("Annuler");
			this.setTitle("Créer un nouvel utilisateur");
			this.setHeaderText("Remplissez les informations ci-dessous pour créer un nouvel utilisateur");
		}break;
		case Translate.ITALY:{
			lbl_name.setText("Qual è il tuo nome?");
			lbl_login.setText("Inserisci il tuo login:");
			lbl_password.setText("Inserisci la tua password:");
			lbl_confirmpass.setText("Conferma la tua password:");
			lbl_secret.setText("Digitare una frase:");
			lbl_language.setText("Scegli una lingua:");
			signupButton.setText("Registro");
			cancelButton.setText("Annullare");
			this.setTitle("Criar um novo usuário");
			this.setHeaderText("Inserire le informazioni per creare un nuovo utente");
		}break;
		}
	}
	
	private void initBox(){
		cb_language.getSelectionModel().select(1);
		cb_language.selectionModelProperty().get().selectedItemProperty().addListener(e->{
			setLanguage(cb_language.getSelectionModel().getSelectedIndex());
		});
		cb_language.getItems().addAll("Deutsch", "Español", "Français", "English", "Italiano", "Português");
		this.tf_confirmpass.textProperty().addListener(e->verifPassword());
		this.tf_password.textProperty().addListener(e->verifPassword());
	}
	
	public void StartSignup(){
		Optional<Boolean> result = this.showAndWait();
		result.ifPresent(rs->{
			if(rs)
				try {
				Sqlite.signup(tf_name.getTextField().getText(), tf_login.getTextField().getText(), Sqlite.toMD5(tf_confirmpass.getText())
				, Sqlite.toMD5(tf_secret.getTextField().getText()), cb_language.getSelectionModel().getSelectedIndex());
				if(cb_language.getSelectionModel().getSelectedIndex()==Translate.PORTUGUESE)
				dialogs.showmessage("Usuário cadastrado", "Você foi cadastrado com sucesso!", "Você voltará para a página de login para efetuar o login.", AlertType.INFORMATION).show();
				if(cb_language.getSelectionModel().getSelectedIndex()==Translate.ENGLISH)
				dialogs.showmessage("User registred", "You were successfully registred", "You'll be directed to the login page.", AlertType.INFORMATION).show();
			} catch (NoSuchAlgorithmException | SQLException e) {e.printStackTrace();}
				
			});
	}
	
	private void check(Node signupButton){
		Platform.runLater(()->signupButton.setDisable(!tf_name.isConfirmed()||!tf_login.isConfirmed()||!tf_secret.isConfirmed()||tf_password.getText().length()<8
				||!tf_confirmpass.getText().equals(tf_password.getText())||cb_language.selectionModelProperty().get().selectedItemProperty().isNull().get()));
	}
	
	private void verifPassword(){
		if(tf_password.getText().length()<8){
			switch(cb_language.getSelectionModel().getSelectedIndex()){
			case Translate.PORTUGUESE:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("A senha precisa de 8 caracteres.");
			}
				break;
			case Translate.ENGLISH:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("The password need 8 characters.");
			}
				break;
			case Translate.GERMANY:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("Das passwort muss acht zeichen");
			}
			    break;
			case Translate.ITALY:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("La password deve otto caratteri.");
			}
				break;
			case Translate.FRANCE:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("Le mot de passe a besoin de huit caractères.");
			}
				break;
			}
		}
		if(personal_filters.filter(tf_password.getText())){
			lbl_password_status.setTextFill(Color.RED);
			lbl_password_status.setText("A senha precisa de 8 caracteres.");
		}
		if(tf_confirmpass.getText().equals(tf_password.getText()) && tf_password.getText().length()>=8)
			switch(cb_language.getSelectionModel().getSelectedIndex()){
			case 0:{
				lbl_password_status.setTextFill(Color.GREEN);
				lbl_password_status.setText("Password confirmed.");
			}
				break;
			case 1:{
				lbl_password_status.setTextFill(Color.GREEN);
				lbl_password_status.setText("Senha confirmada.");
			}
				break;
			default:{
			lbl_password_status.setTextFill(Color.GREEN);
			lbl_password_status.setText("Password confirmed.");
		}
		}else{
			switch(cb_language.getSelectionModel().getSelectedIndex()){
			case 0:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("As senhas não são iguais.");
			}
				break;
			case 1:{
				lbl_password_status.setTextFill(Color.RED);
				lbl_password_status.setText("Password are not equal.");
			}
				break;
				default:{
					lbl_password_status.setTextFill(Color.RED);
					lbl_password_status.setText("Password are not equal.");
				}
			}
		}

	}
	
	
}
