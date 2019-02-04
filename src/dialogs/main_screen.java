package dialogs;

import java.io.IOException;

import controllers.telaprincipal_controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class main_screen extends Application{

	public static Stage stage;
	
	@Override
	public void start(Stage stage) throws IOException {
		//Se for iniciante e não tiver o registro de primeira entrada
		//if(load.IsIniciante()&&!load.HaveReg()){
			//load.loadApplication();
		//if(!load.HaveLicence()){
			//load.createtriallicence();
			playTheGame(stage);
		//}else playTheGame(stage);
		//CreateTrialLicence();
		//Se não for iniciante, mas já tiver registro de primeira entrada
		//}else if(!load.IsIniciante()&&load.HaveReg()){
			//playTheGame(stage);
		//}
		//Se não for inciante e não tiver registro de primeira entrada(HACK)
		//else{
			//dialogs.showmessage("Error", "Licence expired", "More information: www.homeino.blogspot.com.br\nERRO1").showAndWait();
			//Platform.exit();
		//}
	}
	
	public static void main(String params[]){
		
		//if(params[0].equals("launch_home&ino_adê_<<#@_mylove_get_started_please_sir")){
			launch(params);
		//
	}
	
	private void playTheGame(Stage stage) throws IOException{
		Parent parent = FXMLLoader.load(getClass().getResource("/fxmls/Trabalho.fxml"));
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.setTitle("Homeino -");
		stage.centerOnScreen();
		main_screen.stage=stage;
		stage.show();
		stage.setOnCloseRequest(e->close(e));
	}
	
	public static Stage getStage(){
		return stage;
	}
	
	public static void close(WindowEvent e){
		if(dialogs.createConfirmationDialog("Atenção", "Você realmente deseja sair?", "").showAndWait()
				.get().getButtonData()==ButtonData.CANCEL_CLOSE)e.consume();
		else if(telaprincipal_controller.guard!=null) telaprincipal_controller.guard.cancel();
	}
	
	

}
