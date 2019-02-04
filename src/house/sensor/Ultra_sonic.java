package house.sensor;

import javafx.scene.image.Image;
import house.Item;
import house.Room;

public class Ultra_sonic extends Item {

	transient private Image image= new Image(getClass().getResource("/images/ULTRA_SONIC.png").toExternalForm());
	
	public Ultra_sonic(int id, String name, int timeon, int onoff, int parent,
			String pin, int button, double x, double y, Room room, int type) {
		super(id, name, timeon, onoff, parent, pin, button, x, y, room, type,"", true);
		// TODO Auto-generated constructor stub
		img_view.setImage(image);
		
	}
	
	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}

}
