package controllers.little;

import house.Data_item;
import house.Item;
import house.Room;
import house.person_Item;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Tab;
import anothers.personal_filters;
import anothers.projectSaved;
import controllers.telaprincipal_controller;
import database.Commons_loads;
import database.Data_room;
import database.Sqlite;
import database.User;
import dialogs.main_screen;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class menu_controller_methods {

	private static FileChooser file_search= new FileChooser();
	public static ExtensionFilter filter_project=new ExtensionFilter("Homeino project", "*.hproj");
	private static TextField tf_name= new TextField(), tf_imageurl= new TextField();
	private static TextArea txt_info= new TextArea();
	private static Label lbl_name=new Label("Nome do cômodo:"), lbl_info=new Label("Informações do cômodo:"),
			lbl_imageurl= new Label("Imagem de fundo");
	private static ButtonType btn_cancel= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE),
			btn_create= new ButtonType("Criar cômodo", ButtonData.OK_DONE);

	/***
	 * Tela para criação de um novo cômodo
	 * @param usuario
	 * @param root
	 * @param loads
	 * @return
	 */
	public static Dialog<Void> create_new_room(User usuario, TabPane root, Commons_loads loads){
		Dialog<Void> dialog= new Dialog<Void>();
		GridPane gridpane= new GridPane();
		gridpane.add(lbl_name, 0, 0);
		gridpane.add(tf_name, 1, 0);
		gridpane.add(lbl_info, 0, 1);
		gridpane.add(txt_info, 1, 1);
		gridpane.add(lbl_imageurl, 0, 2);
		gridpane.add(tf_imageurl, 1, 2);
		FileChooser filter=search_init();
		loadNewroomfields();
		tf_imageurl.setEditable(false);
		tf_imageurl.setOnMouseClicked(e->{
			File file= filter.showOpenDialog(main_screen.stage);
			if(file!=null)
			tf_imageurl.setText(file.getAbsolutePath());
		});
		dialog.getDialogPane().getButtonTypes().addAll(btn_create,btn_cancel);
		Button btn_createroom= (Button) dialog.getDialogPane().lookupButton(btn_create);
		btn_createroom.setDisable(true);
		//Crio o cômodo e mostro a mensagem
		btn_createroom.setOnAction(e->{
			Stream<Tab> stream=root.getTabs().stream().filter(i->{Room room=(Room)i;return room.getRoom().getName().equals(tf_name.getText());});
			Image image_house= new Image("file:"+tf_imageurl.getText());
			byte[] imagebytes=null;
			try{imagebytes=getBytesFromImage(image_house);}catch(IOException e1){e1.printStackTrace();}
			StringBuffer sb= new StringBuffer();
			for(byte b: imagebytes)sb.append(b+",");
			if(!stream.findFirst().isPresent()){
			Sqlite.insert("INSERT INTO room(NAME, INFO, IMAGEURL, USERID) VALUES('"+tf_name.getText()+"','"+
							txt_info.getText()+"','"+sb.toString()+"','"+usuario.getId()+"')");
				dialogs.dialogs.showmessage("Novo cômodo adicionado!", "O cômodo "+tf_name.getText()+" foi criado com sucesso.", "Agora você já pode"
				+ "manipular o seu novo cômodo", AlertType.INFORMATION).show();
			//Data_room data=Sqlite.getDatarooms(usuario.getId()).stream().filter(i->i.getName().equals(tf_name.getText())).findFirst().get();
			//Room room= new Room(data, loads.getParseCode(), loads.getArduino(), loads);
			//root.getTabs().add(room);
			
			}else dialogs.dialogs.showmessage("Erro", "O cômodo "+tf_name.getText()+" já existe.", "Não é possível criar cômodos com nomes iguais", AlertType.WARNING).show();
		});
		tf_name.textProperty().addListener(e->{
			btn_createroom.setDisable(personal_filters.filter(tf_name.getText())||personal_filters.filter(txt_info.getText())||
					tf_imageurl.getText().length()<=3||tf_name.getText().isEmpty()||txt_info.getText().isEmpty());
		});
		txt_info.textProperty().addListener(e->{
			btn_createroom.setDisable(personal_filters.filter(tf_name.getText())||personal_filters.filter(txt_info.getText())||
					tf_imageurl.getText().length()<=3||tf_name.getText().isEmpty()||txt_info.getText().isEmpty());
		});
		tf_imageurl.textProperty().addListener(e->{
			btn_createroom.setDisable(personal_filters.filter(tf_name.getText())||personal_filters.filter(txt_info.getText())||
					tf_imageurl.getText().length()<=3||tf_name.getText().isEmpty()||txt_info.getText().isEmpty());
		});
		dialog.getDialogPane().setContent(gridpane);
		return dialog;
	}
	
	private static FileChooser search_init(){
		//Crio o filechooser que irá carregar a imagem de fundo do cômodo
		ExtensionFilter filter=new ExtensionFilter("Image Files", "*.png");
		file_search.setTitle("Selecione um plano de fundo para o seu projeto");
		file_search.getExtensionFilters().add(filter);
		file_search.setSelectedExtensionFilter(filter);
		return file_search;
	}
	
	/***
	 * Carrega o texto da janela que adiciona um novo cômodo
	 */
	private static void loadNewroomfields(){
		tf_name.setPromptText("Somente letras e números");
		txt_info.setPromptText("Fale um pouco sobre o cômodo (Somente letras e números)");
		tf_imageurl.setPromptText("Selecionar imagem...");
		txt_info.textProperty().addListener((e, e1, e2)->{
			if(txt_info.getText().length()==200)txt_info.setText(e1);
		});
	}
	
	/***
	 * Método que altera o plano de fundo do cômodo selecionado
	 * @param data_rooms
	 * @param user
	 * @param commons_loads
	 * @return
	 */
	public static Dialog<Void> changeBackgroundRoom(ArrayList<Data_room> data_rooms, User user, Commons_loads commons_loads){
		Dialog<Void> dialog_change_image= new Dialog<>();
		dialog_change_image.setTitle("Alterar imagem de fundo de cômodo");
		dialog_change_image.setHeaderText("Selecione o cômodo e adicione a imagem");
		GridPane gridpane= new GridPane();
		ComboBox<String> cb_room= new ComboBox<>();
		cb_room.setPromptText("Selecione o cômodo");
		for(Data_room room: data_rooms)
			cb_room.getItems().add(room.getName());
		TextField tf_image_url= new TextField();
		tf_image_url.setPromptText("Clique para selecionar a imagem...");
		gridpane.add(new Label("Selecione o cômodo: "), 0, 0);
		gridpane.add(cb_room, 1, 0);
		gridpane.add(new Label("Selecione a imagem: "), 0, 1);
		gridpane.add(tf_image_url, 1, 1);
		gridpane.setHgap(3);
		gridpane.setVgap(3);
		dialog_change_image.getDialogPane().setContent(gridpane);
		ButtonType changeImage= new ButtonType("Atualizar imagem de fundo", ButtonData.OK_DONE);
		ButtonType changeExit= new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog_change_image.getDialogPane().getButtonTypes().addAll(changeImage, changeExit);
		Button btn_change_image= (Button) dialog_change_image.getDialogPane().lookupButton(changeImage);
		btn_change_image.setDisable(true);
		btn_change_image.setOnAction(e->{
			try{
			Image img=new Image(tf_image_url.getText());
			byte[] bytes_img=getBytesFromImage(img);
			StringBuffer sb= new StringBuffer();
				for(byte b: bytes_img)sb.append(b+",");
			Sqlite.insert("UPDATE room SET imageurl='"+sb.toString()+"' WHERE userid='"+user.getId()+"' and name='"+cb_room.getSelectionModel().getSelectedItem()+"'");
			Platform.runLater(()->commons_loads.refreshBackgroundRoom(cb_room.getSelectionModel().getSelectedItem()));
			dialogs.dialogs.showmessage("Processo concluído", "A imagem foi alterada com sucesso", "Seus cômodos serão recarregados para a aplicação da nova imagem", AlertType.INFORMATION).show();
			}catch(IOException e1){e1.printStackTrace();}
		});
		tf_image_url.setEditable(false);
		tf_image_url.setOnMouseClicked(e->{
			FileChooser filter=search_init();
			File file= filter.showOpenDialog(main_screen.stage);
			if(file!=null)tf_image_url.setText(file.getAbsolutePath());
		});
		
		cb_room.selectionModelProperty().get().selectedItemProperty().addListener(e->btn_change_image.
				setDisable(cb_room.selectionModelProperty().get().selectedItemProperty().isNull().get()
						||tf_image_url.getText().isEmpty()));
		
		tf_image_url.textProperty().addListener(e->btn_change_image.
			setDisable(cb_room.selectionModelProperty().get().selectedItemProperty().isNull().get()||tf_image_url.getText().isEmpty())
		);
		return dialog_change_image;
	}
	
	/***
	 * Salva o projeto em um arquivo externo
	 * @param proj
	 * @throws IOException
	 */
	public static void saveProject(projectSaved proj) throws IOException{
		FileChooser chooser= new FileChooser();
		chooser.getExtensionFilters().add(filter_project);
		chooser.setSelectedExtensionFilter(filter_project);
		File file= chooser.showSaveDialog(main_screen.getStage());
		if(file!=null){
		FileOutputStream out= new FileOutputStream(file.getPath());
		ObjectOutputStream objOut= new ObjectOutputStream(out);
		objOut.writeObject(proj);
		objOut.flush();
		objOut.close();
		out.flush();
		out.close();
		dialogs.dialogs.showmessage("Projeto Salvo", "Processo concluído", "O seu projeto foi salvo com sucesso!", AlertType.INFORMATION);
		}
	}
	
	/***
	 * Carrega o projeto
	 * @param proj
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void loadProject(User usuario, Commons_loads load) throws SQLException, ClassNotFoundException, IOException{
		projectSaved proj=loadProjectFromFile();
		if(proj!=null)
		proj.getRooms().forEach((data ,items)->{
			try{
			ResultSet rs=Sqlite.getResultSet("SELECT * FROM room WHERE name='"+data.getName()+"' and userid='"+usuario.getId()+"'");
				String room_name= data.getName();
				if(rs.next())room_name=room_name+"_";
			rs.getStatement().getConnection().close();
			rs.close();
			Sqlite.insert("INSERT INTO room (NAME, INFO, IMAGEURL, USERID) VALUES("
					+ "'"+room_name+"','"+data.getInfo()+"','"+data.getImageurl()+"','"+usuario.getId()+"')");
			rs=Sqlite.getResultSet("SELECT * FROM room WHERE name='"+room_name+"' and userid='"+usuario.getId()+"'");
			int room_id=-1;
				if(rs.next())room_id=rs.getInt("Id");
			rs.getStatement().getConnection().close();
			rs.close();
			for(Item item: items){
				rs=Sqlite.getResultSet("SELECT * FROM light_children WHERE name='"+item.getName()+"' and userid='"+usuario.getId()+"'");
				String item_name=item.getName();
					if(rs.next())item_name=item_name+"_";
					rs.getStatement().getConnection().close();
					rs.close();
					//Se for um item que não foi criado pelo usuário
				if(item.getType()!=99){
					Sqlite.insert("INSERT INTO light_children(TYPE, PARENT, X, Y, NAME, ONOFF, PIN, TIMEON, BUTTONPIN, USERID, STRING_USE) VALUES('"+
							item.getType()+"','"+room_id+"','"+item.getX()+"',"
									+ "'"+item.getY()+"','"+item_name+"','"+item.getOnoff()+"','"+item.getPin()+"',"
											+ "'"+item.getTimeon()+"','"+item.getPinButton()+"','"+usuario.getId()+"','"+item.getStringUSE()+"')");
				}else{//Se for um item criado pelo usuário
					person_Item uitem=(person_Item)item;
					if(telaprincipal_controller.data_items.stream().filter(data_item-> data_item.equals(uitem.getData())).findFirst().isPresent());
					else{
						createItem(uitem.getData());
						dialogs.dialogs.showmessage("Essa foi por pouco!", "Nós adicionamos um novo item para você", "Pelo visto, este projeto que você está tentando "
								+ "adicionar continha um item que você ainda não havia adicionado ao Homeino. Graças a nossa atenção verificamos isso e já adicionamos o item"
								+ "que faltava.\nNão precisa agradecer ;)", AlertType.INFORMATION);
					}
					item_controller.insertUserItem(item_name, uitem.getData(), uitem.getX(), uitem.getY(), room_id, uitem.getPin(), usuario);
				}
				dialogs.dialogs.showmessage("Projeto carregado", "O projeto foi carregado com sucesso!", "O sistema já identificou o projeto e "
						+ "gerou o código a partir dele", AlertType.INFORMATION);
				load.loadRooms();
			}
			}catch(IOException | SQLException e){e.printStackTrace();}
		});
	}
	
	/***
	 * Converte a imagem em bytes
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromImage(Image image) throws IOException{
		BufferedImage buf=SwingFXUtils.fromFXImage(image, null);
		ByteArrayOutputStream s= new ByteArrayOutputStream();
		ImageIO.write(buf, "png", s);
		byte[] imagebytes= s.toByteArray();
		System.out.println(imagebytes);
		s.close();
		return imagebytes;
	}
	
	/***
	 * Carrega o arquivo do projeto e retorna o projeto como objeto
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static projectSaved loadProjectFromFile() throws IOException, ClassNotFoundException{
		FileChooser chooser= new FileChooser();
		chooser.getExtensionFilters().add(filter_project);
		chooser.setSelectedExtensionFilter(filter_project);
		File file= chooser.showOpenDialog(main_screen.getStage());
		projectSaved proj=null;
		if(file!=null){
		FileInputStream in= new FileInputStream(file.getPath());
		ObjectInputStream objIn= new ObjectInputStream(in);
		//objIn.writeObject(proj);
		//objIn.flush();
		proj=(projectSaved)objIn.readObject();
		objIn.close();
		//in.flush();
		in.close();
		
		}
		return proj;
	}
	
	/***
	 * Cria o data_item e registra-o em nosso banco de dados local
	 * @param item
	 * @throws IOException
	 */
	public static void createItem(Data_item item) throws IOException{
		String root="items"+System.getProperty("file.separator")+tf_name.getText()+System.getProperty("file.separator");
		//Esse método "regObjectUser" registra o nome, id e o local do arquivo do item criado. Em caso de nome já existente ele retorna o valor -1
		if(Sqlite.regObjectUser(item.getName(), root)!=-1){		
			FileOutputStream out= new FileOutputStream(root);
			ObjectOutputStream objOut= new ObjectOutputStream(out);
			objOut.writeObject(item);
			objOut.flush();
			objOut.close();
			out.flush();
			out.close();
		}else dialogs.dialogs.showmessage("Ops!", "Um erro ocorreu", "Já existe um item igual a esse nos seus registros, é impossível criar um igual", AlertType.WARNING);
		System.out.println(item.getUID());
		//return item;
	}
	
	
	
	
	
	
	
	
}
