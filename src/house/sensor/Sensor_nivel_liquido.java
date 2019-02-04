package house.sensor;

import javafx.scene.image.Image;
import house.Item;
import house.Room;

public class Sensor_nivel_liquido extends Item {

	transient private Image image= new Image(getClass().getResource("/images/SensorNivel_icon.png").toExternalForm());
	
	public Sensor_nivel_liquido(int id, String name, int onoff, int parent,
			String pin, int button, double x, double y, Room room, int type) {
		super(id, name, onoff, parent, pin, button, x, y, room, type,"", true);
		// TODO Auto-generated constructor stub
		img_view.setImage(image);

	}
	
	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}

}
