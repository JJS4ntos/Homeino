/*Essa classe será responsável por gerenciar o processo de aprendizado do software*/
package ia;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import database.Sqlite;
import house.Data_item;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class Header {

	public Header(){
		
	}
	
	
	public static ArrayList<ToggleButton> loadDataItemsToggles() throws SQLException{
		//File root= new File("items/");
		//File[] items=root.listFiles();
		ArrayList<String> objectArray=Sqlite.getObjectUsers();
		ArrayList<ToggleButton> toggles= new ArrayList<>();
		objectArray.forEach(i->{
			try{String[] object= i.split("~~");
				//object[2].replace('/', '\"');
				FileInputStream readFile= new FileInputStream(object[2]);
				ObjectInputStream input= new ObjectInputStream(readFile);
				Data_item item=(Data_item)input.readObject();
				item.loadImage();
				ToggleButton button = new ToggleButton();
				button.getStyleClass().add("button-item");
				button.setPrefSize(40, 40);
				ImageView img_view=new ImageView(item.getImage());
				img_view.setFitHeight(40);
				img_view.setFitWidth(40);
				button.setGraphic(img_view);
				button.setTooltip(new Tooltip(item.getDescription()));
				button.setId(item.getName());
				toggles.add(button);
				readFile.close();
				input.close();
			}catch(ClassNotFoundException | IOException e){e.printStackTrace();}
		});
		return toggles;
	}

	
}
