package dialogs;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import ExceptionAdapter.ShowErro;
import comunication.Translate;
import controllers.new_item_controller;
import database.Sqlite;
import database.User;
import house.Data_item;
import house.Item;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
/**
 * Essa classe fornecerá os principais dialogs(janelas de alerta ou de inserção de informações);
 * @author JJuniorSantos
 *
 */
public class dialogs {

	public static Alert showmessage(String title, String headertext, String message, AlertType type){
		Alert dialog= new Alert(type);
		dialog.setTitle(title);
		dialog.setHeaderText(headertext);
		dialog.setContentText(message);
		return dialog;
	}
	
	public static Alert createConfirmationDialog(String title, String headertext, String message){
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		dialog.setTitle(title);
		dialog.setContentText(message);
		dialog.setHeaderText(headertext);
		ButtonType btn_yes= new ButtonType("Sim", ButtonData.OK_DONE);
		ButtonType btn_no= new ButtonType("Não", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().setAll(btn_yes, btn_no);
		return dialog;
	}
	
	//Método que cria a janela para adição de um novo item ao menu dos sensores e atuadores.
	public Dialog<Data_item> createNewItem(Data_item item) throws IOException{
		Dialog<Data_item> dialog= new Dialog<>();
		FXMLLoader fxmlLoad= new FXMLLoader(getClass().getResource("/fxmls/new_item.fxml"));
		new_item_controller Controller= new new_item_controller(item);
		fxmlLoad.setController(Controller);
		TabPane tabPane=fxmlLoad.load();
		dialog.getDialogPane().setContent(tabPane);
		ButtonType createItem;
		if(Controller.getEdit()==null)createItem= new ButtonType("Criar item",ButtonData.OK_DONE);
		else createItem= new ButtonType("Editar item",ButtonData.OK_DONE);
		ButtonType cancel= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().setAll(createItem, cancel);
		Button btn_createItem=(Button)dialog.getDialogPane().lookupButton(createItem);
		btn_createItem.disableProperty().bind(Controller.createItemProperty());
		btn_createItem.setOnAction(e->{
			//if(dialog.getResultConverter().call(createItem)!=null)
			
		});
		dialog.setResultConverter(v->{
			//Se o resultado não for o botão cancelar, então retorne o item criado
			if(!v.getButtonData().isCancelButton()){
				Data_item data=null;
				try{data=Controller.createItem();
				}catch(IOException ex){ex.printStackTrace();}
				return data;
			}
			return null;
		});
		return dialog;
	}
	
	public static Dialog<Void> changeUser(User usuario, final int LANGUAGE){
		String[] texts=Translate.getChangeUserTexts(LANGUAGE);
		Dialog<Void> dialog= new Dialog<>();
		dialog.setHeaderText(texts[3]);
		GridPane gridpane= new GridPane();
		Hyperlink name= new Hyperlink(texts[0]), password= new Hyperlink(texts[1]), secret=new Hyperlink(texts[2]);
			//Janela para alterar nome
			name.setOnAction(e->{
				Dialog<Void> change_name= new Dialog<>();
				change_name.setHeaderText(texts[4]);
				// Set the button types.
				ButtonType changeButtonType = new ButtonType("OK", ButtonData.OK_DONE);
				change_name.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);
				Button btnChange=(Button)change_name.getDialogPane().lookupButton(changeButtonType);
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));
				TextField username = new TextField(usuario.getName());
				PasswordField password_confirm = new PasswordField();
				btnChange.setOnAction(changeEvent->{
					//Verificar se a senha está correto e alterar o nome do usuário.
					try{
						ResultSet rs=Sqlite.getResultSet("select * from users where id='"+usuario.getId()+"' and password='"+Sqlite.toMD5(password_confirm.getText())+"'");
						if(rs.next()){
							Sqlite.insert("update users set name='"+username.getText()+"' where id='"+usuario.getId()+"'");
							dialogs.showmessage("Concluído", "Nome alterado com sucesso", "O seu nome de usuário foi alterado para "+username.getText(), AlertType.INFORMATION);
							usuario.setName(username.getText());
						}
						else dialogs.showmessage("Erro", "A senha informada está incorreta", "Verifique se não houve erro de digitação e tente novamente.",
								AlertType.WARNING).show();
					}catch(SQLException | NoSuchAlgorithmException e1){
						ShowErro error= new ShowErro();
						error.ThereIsAException(e1);
					}
				});
				grid.add(new Label(texts[7]), 0, 0);
				grid.add(username, 1, 0);
				grid.add(new Label(texts[6]), 0, 1);
				grid.add(password_confirm, 1, 1);
				change_name.getDialogPane().setContent(grid);
				change_name.show();
			});
			password.setOnAction(e->{
				
			});
			secret.setOnAction(e->{
				
			});
		gridpane.add(name, 0, 0);
		gridpane.add(password, 1, 0);
		gridpane.add(secret, 2, 0);
		dialog.getDialogPane().setContent(gridpane);
		ButtonType cancel= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().setAll(cancel);
		return dialog;
	}

}
