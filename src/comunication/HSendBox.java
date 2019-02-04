package comunication;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class HSendBox extends HBox {
	
	private TextField tf_name, tf_bytes;
	
	public HSendBox(String prompt_name, String prompt_bytes){
		tf_name=new TextField();
		tf_name.setPromptText(prompt_name);
		tf_bytes=new TextField();
		tf_name.setPromptText(prompt_bytes);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
		this.getChildren().addAll(tf_name, tf_bytes);
	}

	public TextField getTf_name() {
		return tf_name;
	}

	public void setTf_name(TextField tf_name) {
		this.tf_name = tf_name;
	}

	public TextField getTf_bytes() {
		return tf_bytes;
	}

	public void setTf_bytes(TextField tf_bytes) {
		this.tf_bytes = tf_bytes;
	}
	
}
