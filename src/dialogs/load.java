package dialogs;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class load{
	
	public load(){
		
	}
	
	public Pane getLoadScreen() throws IOException{
		return FXMLLoader.load(getClass().getResource("/fxmls/checking_frame.fxml"));
	}

}
