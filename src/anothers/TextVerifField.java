package anothers;

import comunication.Translate;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TextVerifField extends VBox{

	private Label status_label=new Label();
	private TextField tf_=new TextField();
	final public String ERROR_SPECIAL_CHAR_ENGLISH= "Invalid character.", ERROR_SPECIAL_CHAR_PORTUGUESE="Caractere inválido",
			ERROR_INSUFFICIENT_CHAR_ENGLISH="Insufficient characters", ERROR_INSUFFICIENT_CHAR_PORTUGUESE="Caracteres insuficientes",
			ERROR_INSUFFICIENT_CHAR_DEUTSCH="Unzureichende Zeichen", ERROR_SPECIAL_CHAR_DEUTSCH="Ungültiges Zeichen",
			ERROR_INSUFFICIENT_CHAR_ITALIAN="Caratteri insufficienti", ERROR_SPECIAL_CHAR_ITALIAN= "Carattere non valido", 
			ERROR_INSUFFICIENT_CHAR_FRENCH="Caractères insuffisants", ERROR_SPECIAL_CHAR_FRENCH="Caractère invalide", 
					ERROR_INSUFFICIENT_CHAR_SPANISH="Suficientes caracteres", ERROR_SPECIAL_CHAR_SPANISH="Carácter no válido";
			
	private int min;
	boolean confirm=false;
	SimpleIntegerProperty language= new SimpleIntegerProperty();
	
	public TextVerifField(ReadOnlyIntegerProperty language, int min) {
		// TODO Auto-generated constructor stub
		this.setPrefWidth(200);
		this.language.bind(language);
		this.min=min;
		init();
	}
	
	public TextVerifField(SimpleIntegerProperty language, int min, String promptText) {
		// TODO Auto-generated constructor stub
		this.language.bind(language);
		tf_.setPromptText(promptText);
		
		init();
	}
	
	public void init(){
		this.getChildren().addAll(tf_, status_label);
		tf_.textProperty().addListener((e,e1,e2)->{
			//Verificação de caracteres especiais.
			if(personal_filters.filter(tf_.textProperty().get()))
			refresh_status(personal_filters.filter(tf_.textProperty().get()), select_error(0, language.get()));
			else if(tf_.textProperty().get().length()<min) refresh_status(true, select_error(1, language.get()));
			else refresh_status(false, select_error(1, language.get()));
			
		});
	}
	
	private void refresh_status(boolean error, String name){
		if(!error){
			status_label.setTextFill(Color.GREEN);
			if(tf_.getText().length()>=min){
				switch(language.get()){
				case Translate.PORTUGUESE:status_label.setText("Confirmado. (Aguardando a checagem)");break;
				case Translate.ENGLISH:status_label.setText("Confirmed. (Waiting for sign up)");break;
				case Translate.SPANISH:status_label.setText("Confirmado. (A la espera de verificación)");break;
				case Translate.FRANCE:status_label.setText("Confirmé. (En attente de vérification)");break;
				case Translate.ITALY:status_label.setText("Confermato. (In attesa di verifica)");break;
				case Translate.GERMANY:status_label.setText("Bestätigt. (Waiting for check)");break;
				}				
				confirm=true;
			}
		}else{
			status_label.setText(name);
			status_label.setTextFill(Color.RED);
			confirm=false;
		}	
	}
	
	private String select_error(int errortype, int language){
		String message="";
		switch(errortype){
		case 0:	switch(language){
				case Translate.PORTUGUESE: message=ERROR_SPECIAL_CHAR_PORTUGUESE;break;
				case Translate.ENGLISH: message=ERROR_SPECIAL_CHAR_ENGLISH;break;
				case Translate.FRANCE: message=ERROR_SPECIAL_CHAR_FRENCH;break;
				case Translate.SPANISH: message=ERROR_SPECIAL_CHAR_SPANISH;break;
				case Translate.GERMANY: message=ERROR_SPECIAL_CHAR_DEUTSCH;break;
				case Translate.ITALY: message=ERROR_SPECIAL_CHAR_ITALIAN;break;
				}
		
			break;
		case 1: switch(language){
				case Translate.PORTUGUESE: message=ERROR_INSUFFICIENT_CHAR_PORTUGUESE;break;
				case Translate.ENGLISH: message=ERROR_INSUFFICIENT_CHAR_ENGLISH;break;
				case Translate.FRANCE: message=ERROR_INSUFFICIENT_CHAR_FRENCH;break;
				case Translate.ITALY: message=ERROR_INSUFFICIENT_CHAR_ITALIAN;break;
				case Translate.GERMANY: message=ERROR_INSUFFICIENT_CHAR_DEUTSCH;break;
				case Translate.SPANISH: message=ERROR_INSUFFICIENT_CHAR_SPANISH;break;
			}

			break;
		}	
		return message;
	}
	
	public boolean isConfirmed(){
		return confirm;
	}
	
	public TextField getTextField(){
		return tf_;
	}

}
