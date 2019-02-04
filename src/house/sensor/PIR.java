package house.sensor;

import javafx.scene.image.Image;
import house.Item;
import house.Room;

public class PIR extends Item {

	transient private Image image = new Image(getClass().getResource("/images/PIR_icon.png").toExternalForm());
	//private Label lbl_move_detected= new Label("NO MOVIMENT DETECTED");
	
	public PIR(int id, String name, int onoff, int parent, String pin,
			int button, double x, double y, Room room, int type) {
		super(id, name, onoff, parent, pin, button, x, y, room, type,"", true);
		img_view.setImage(image);
	
	}

	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}

}
