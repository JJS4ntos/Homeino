package house.digital;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import house.Item;
import house.Room;

public class LCD extends Item {

	transient private Image image = new Image(getClass().getResource("/images/LCD_icon.png").toExternalForm());
	
	public LCD(int id, String name, int onoff, int parent, String pin,
			int button, double x, double y, Room room, int type, String string_use) {
		super(id, name, onoff, parent, pin, button, x, y, room, type, string_use, false);
		// TODO Auto-generated constructor stub
		init();
		sensor=true;
	}

	private void init(){
		img_view.setImage(image);
		Label lbl_showing= new Label();
		lbl_showing.setText("Showing::"+string_use);
		lbl_showing.setWrapText(true);
		lbl_showing.setStyle("-fx-background-color: green; -fx-background-radius: 5; -fx-text-fill: white;");
		this.getChildren().setAll(lbl_name, img_view, lbl_showing);
	}
	
	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}

}
