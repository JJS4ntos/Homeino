package anothers;

import java.io.IOException;

import house.Data_item;
import house.code.InputCode;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/***
 * Painel que exibirá as informações do item na tela de itens do usuário
 * @author JJSantos
 *
 */
public class DtItemPane extends HBox {
	
	private Data_item data;
	private Label lbl_name, lbl_info, lbl_library, lbl_sensor, lbl_analog;
	private VBox Mainbox;
	private TreeView<Label> inputs;
	
	public DtItemPane(Data_item data) throws IOException{
		this.data=data;
		init();
	}
	
	private void init() throws IOException{
		Mainbox= new VBox();
		inputs= new TreeView<Label>();
		TreeItem<String> raiz= new TreeItem<>("InputCode");
		for(InputCode input:data.getFields()){
			TreeItem<String> item= new TreeItem<String>(input.getName()+":"+input.getInput());
			
			raiz.getChildren().add(item);
		}
		
		lbl_name= new Label(data.getName());
		lbl_info= new Label(data.getDescription());
		lbl_library= new Label(data.getLibrary());
		lbl_sensor= new Label(data.isSensor()?"É um sensor":"Não é um sensor");
		lbl_analog= new Label(data.isAnalog()?"É analógico":"Não é analógico");
		Mainbox.getChildren().addAll(lbl_name, lbl_info, lbl_library, lbl_sensor, lbl_analog);
		this.getChildren().addAll(new ImageView(data.loadImage()), Mainbox);
	}
	
}
