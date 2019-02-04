package controllers;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import comunication.HSendBox;
import comunication.dataBtnSend;
import database.Sqlite;
import dialogs.main_screen;
import house.Data_item;
import house.code.InputCode;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/***
 * Tela de criação de item do usuário
 * @author JJSantos
 *
 */
public class new_item_controller implements Initializable {

	@FXML private TextArea txt_coderead, txt_coderun, txt_setupCode, txt_method;
    @FXML private TextField tf_name, tf_description, tf_library, tf_inputname, tf_vglobal_name,
    tf_vglobal_content, tf_name_send, tf_content_send;
    @FXML private ComboBox<String> cb_type, cb_inputtype, cb_vglobal_type;
	@FXML private ImageView icon_view;
		  private SimpleBooleanProperty createItemProperty= new SimpleBooleanProperty(false);
	@FXML private CheckBox chk_analog;	  
	@FXML private AnchorPane newmethod_pane;
	@FXML private Button btn_createInput, btn_addSend, btn_removeSend, btn_addvglobal, btn_addvglobal_person, btn_addMethod, btn_newMethod;
	@FXML private ListView<InputCode> list_inputs;
	@FXML private ListView<HSendBox> list_sends;
	@FXML private ListView<String> list_vglobals;
	@FXML private VBox box_methods;
	@FXML private StackPane pane_methods;
	@FXML private CheckBox chk_vglobal_array;
	@FXML private Label lbl_closeAddMethod;
    	  private Data_item edit=null;
    	  
    public new_item_controller(Data_item edit){
    	this.edit=edit;
    }
    	  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		init();
		if(edit!=null)loadEdit();
	}
	
	public Data_item getEdit(){
		return edit;
	}
	
	private void loadEdit(){
		tf_name.setText(edit.getName());
		tf_description.setText(edit.getDescription());
		tf_library.setText(edit.getLibrary());
		txt_coderead.setText(edit.getCodeRead());
		txt_coderun.setText(edit.getCodeRun());
		txt_setupCode.setText(edit.getCodeSetup());
		list_inputs.getItems().setAll(edit.getFields());
		for(dataBtnSend data:edit.getSends()){
			HSendBox box= new HSendBox("Nome do botão", "Bytes para envio");
			box.getTf_name().setText(data.getName());
			box.getTf_bytes().setText(data.getSendContent());
			list_sends.getItems().add(box);
		}
	}
	
	//17/01/2017 ADICIONAR A OBRIGATORIEDADE DO BUTTONPIN PARA OS SENSORES, ASSIM COMO O USO DELE E DO PIN NO CÓDIGO
	private void init(){
		//CRIANDO A ÚNICA ENTRADA PADRÃO - PIN
		InputCode pin= new InputCode("PIN", 0);
		list_inputs.getItems().add(pin);
		lbl_closeAddMethod.setOnMouseClicked(e->newmethod_pane.setVisible(false));
		createItemProperty.bind(tf_name.textProperty().isEmpty().or(tf_description.textProperty().isEmpty()).or(txt_coderun.textProperty().isEmpty())
				.or(txt_coderead.textProperty().isEmpty().or(cb_type.selectionModelProperty().get().selectedItemProperty().isNull())
						.or(chk_analog.selectedProperty())));
		cb_type.getItems().addAll("Atuador","Sensor");
		cb_type.selectionModelProperty().get().selectedIndexProperty().addListener(e->{
			if(cb_type.selectionModelProperty().get().selectedIndexProperty().get()==1)
				list_inputs.getItems().add(new InputCode("ACTIVEPIN", 0));
			else list_inputs.getItems().removeIf(i->i.getName().equals("ACTIVEPIN"));
		});
		cb_inputtype.getItems().addAll("int","char","String","float","double");
		btn_createInput.disableProperty().bind(tf_inputname.textProperty().isEmpty().or(cb_inputtype.selectionModelProperty().get().selectedItemProperty().isNull()));
		btn_createInput.setOnAction(e->{
			InputCode input= new InputCode(tf_inputname.getText(), cb_inputtype.getSelectionModel().getSelectedIndex());
			if(!list_inputs.getItems().stream().filter(i->i.getName().equals(input.getName())).findFirst().isPresent()){
				list_inputs.getItems().add(input);
				tf_inputname.clear();
				cb_inputtype.getSelectionModel().clearSelection();
			}else{
				Alert alert= new Alert(AlertType.WARNING);
				alert.setTitle("Entrada já existente");
				alert.setHeaderText("A entrada que você está tentando adicionar já existe");
				alert.setContentText("Se deseja criar uma nova entrada, altere o nome e tente novamente");
				alert.show();
			}
		});
		icon_view.setOnMouseClicked(e->{
			FileChooser chooser=new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("Arquivo de imagem png", "*.png"));
			File file=chooser.showOpenDialog(main_screen.stage);
			if(file!=null)icon_view.setImage(new Image("file:///"+file.getPath()));
			
		});
		btn_addSend.setOnAction(e->{
			HSendBox hSend= new HSendBox(tf_name_send.getText(), tf_content_send.getText());
			list_sends.getItems().add(hSend);
		});
		btn_addvglobal.setOnAction(e->{
			String vglobal=null;
			//Se for um array
			if(chk_vglobal_array.isSelected()){
				vglobal= cb_vglobal_type.getSelectionModel().getSelectedItem()+"[] "+tf_vglobal_name.getText()+"= {"+tf_vglobal_content.getText()+"};";
			}else{//Se não for um array
				vglobal= cb_vglobal_type.getSelectionModel().getSelectedItem()+" "+tf_vglobal_name.getText()+"= "+tf_vglobal_content.getText()+";";
			}
			//Verifico se já existe
			if(!list_vglobals.getItems().stream().filter(i->i.contains(tf_vglobal_name.getText()+"=")).findFirst().isPresent()){
				list_vglobals.getItems().add(vglobal);
			}
			
		});
		btn_addvglobal.disableProperty().bind(cb_vglobal_type.selectionModelProperty().get().selectedItemProperty().isNull().and(
				tf_vglobal_name.textProperty().isEmpty()).and(tf_vglobal_content.textProperty().isEmpty()));
		newmethod_pane.setVisible(false);
		btn_newMethod.setOnAction(e->newmethod_pane.setVisible(true));//Torna o pane de adição de métodos visivel.
		btn_addMethod.setOnAction(e->{
			Label method= new Label(txt_method.getText());
			method.setWrapText(true);
			method.setStyle("-fx-text-fill: white; -fx-background-color: lightskyblue; -fx-background-radius:20;");
			txt_method.clear();
			box_methods.getChildren().add(method);
		});
		btn_addMethod.disableProperty().bind(txt_method.textProperty().isEmpty());
	}
	
	/**
	 * Método responsável por criar um Dataitem e registrá-lo no banco de dados.
	 * TRATAR PARA QUE AS INFORMAÇÕES INSERIDAS NAS VARIÁVEIS GLOBAIS E OS MÉTODOS TENHAM UM PADRÃO (MATCH)
	 * @return
	 * @throws IOException
	 */
	public Data_item createItem() throws IOException{
		String root="items"+System.getProperty("file.separator")+tf_name.getText()+System.getProperty("file.separator");
		File file= new File(root);
		if(file.exists())file.delete();
		Data_item item=null;
		//Esse método "regObjectUser" registra o nome, id e o local do arquivo do item criado. Em caso de nome já existente ele retorna o valor -1
		if(Sqlite.regObjectUser(tf_name.getText(), root)!=-1){
			dataBtnSend[] sends = null;
			list_sends.getItems().toArray(sends);
			item= new Data_item(tf_name.getText(), tf_description.getText(), txt_coderead.getText(), txt_coderun.getText(),
					getImageItem(), tf_library.getText(), isSensor(), chk_analog.isSelected(), txt_setupCode.getText(), sends, getMethods(),
					getVglobals());
			item.setFields(getInputs());			
			item.setMethods(getMethods());
			item.setVglobals(getVglobals());
			item.setSends(sends);
			FileOutputStream out= new FileOutputStream(root);
			ObjectOutputStream objOut= new ObjectOutputStream(out);
			objOut.writeObject(item);
			objOut.flush();
			objOut.close();
			out.flush();
			out.close();
		}
		return item;
	}
	
	/***
	 * Retorna os métodos incluindos neste item
	 * @return
	 */
	public String[] getMethods(){
		String[] methods= new String[box_methods.getChildren().size()];
		for(int i=0; i<box_methods.getChildren().size();i++){
			Label MethodLabel=(Label)box_methods.getChildren().get(i);
			methods[i]= MethodLabel.getText();
		}
		return methods;
	}
	
	
	public String[] getVglobals(){
		String[] VGlobals= (String[])list_vglobals.getItems().toArray();
		return VGlobals;
	}
	
	public SimpleBooleanProperty createItemProperty(){
		return createItemProperty;
	}
	
	private boolean isSensor(){
		return cb_type.getSelectionModel().getSelectedIndex()!=0;
	}
	
	private Image getImageItem(){
		return icon_view.getImage();
	}
		
	private InputCode[] getInputs(){
		InputCode[] inputs= new InputCode[list_inputs.getItems().size()];
		for(int i=0; i<list_inputs.getItems().size();i++){
			inputs[i]=list_inputs.getItems().get(i);
		}
		
		return inputs;
	}
}
