package internet;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/***
 * Essa classe verificar� o status da compra do usu�rio e se estiver paga alterar� a licen�a para definitiva.
 * ATEN��O- A LICEN�A SEMPRE SER� POR TEMPO DETERMINADO, ENTRETANTO, AOS USU�RIOS QUE PAGAM PELO SOFTWARE, ENQUANTO UTILIZAM O SOFTWARE
 * ESTA CLASSE IR� AGIR DE FORMA QUE O TEMPO N�O DECORRA PARA ELES.
 * @author Learning or Develop
 *
 */
public class HomeLoading extends BorderPane {

	private Button btn_credentials, btn_trial;
	private VBox vbox_buttons;
	
	public HomeLoading(){
		initScreen();
	}
	
	/***
	 * M�todo que carrega a tela de carregamento
	 */
	public void initScreen(){
		this.setPrefSize(100, 200);
		vbox_buttons.setSpacing(5);
			btn_credentials.setText("Registrar licen�a comprada");
			btn_trial.setText("Continuar em modo demonstrativo");
		vbox_buttons.getChildren().addAll(btn_credentials, btn_trial);
		this.setCenter(vbox_buttons);
	}

	
	
	

}
