package ExceptionAdapter;

import java.io.PrintWriter;
import java.io.StringWriter;

import controllers.telaprincipal_controller;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ShowErro {

	public ShowErro() {
		// TODO Auto-generated constructor stub
	}
	
	//Janela do erro
	public void ThereIsAException(Throwable exception){
		Alert showErro= new Alert(AlertType.ERROR);
		StringWriter sw= new StringWriter(); 
		PrintWriter pw= new PrintWriter(sw);
		exception.printStackTrace(pw);
		TextArea textArea= new TextArea(sw.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxHeight(Double.MAX_VALUE);
		textArea.setMaxWidth(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		showErro.getDialogPane().setExpandableContent(textArea);
		showErro.setTitle("ERROR");
		showErro.setHeaderText(exception.getLocalizedMessage());
		//showErro.setContentText(exception.);
		showErro.showAndWait();
		telaprincipal_controller.guard.cancel();
		Platform.exit();
	}

}
