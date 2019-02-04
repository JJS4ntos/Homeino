package internet;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/***
 * Essa classe verificará o status da compra do usuário e se estiver paga alterará a licença para definitiva.
 * ATENÇÃO- A LICENÇA SEMPRE SERÁ POR TEMPO DETERMINADO, ENTRETANTO, AOS USUÁRIOS QUE PAGAM PELO SOFTWARE, ENQUANTO UTILIZAM O SOFTWARE
 * ESTA CLASSE IRÁ AGIR DE FORMA QUE O TEMPO NÃO DECORRA PARA ELES.
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
	 * Método que carrega a tela de carregamento
	 */
	public void initScreen(){
		this.setPrefSize(100, 200);
		vbox_buttons.setSpacing(5);
			btn_credentials.setText("Registrar licença comprada");
			btn_trial.setText("Continuar em modo demonstrativo");
		vbox_buttons.getChildren().addAll(btn_credentials, btn_trial);
		this.setCenter(vbox_buttons);
	}

	
	
	

}
