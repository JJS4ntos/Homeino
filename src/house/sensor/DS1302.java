package house.sensor;

import javafx.scene.image.Image;
import house.Item;
import house.Room;

public class DS1302 extends Item {
	/*ESTE PINO UTILIZA TRÊS PINOS, POR ISSO USAREMOS UM CHAR PARA TOKENIZAR A STRING ":" E ASSIM DIVIDIR OS TRÊS PINOS*/
	transient private Image image= new Image(getClass().getResource("/images/DS1302_ICON.png").toExternalForm());

	public DS1302(int id, String name, int timeon, int onoff, int parent, String pin,
			int button, double x, double y, Room room, int type, String string_use) {
		super(id, name, timeon, onoff, parent, pin, button, x, y, room, type, string_use, true);
		// TODO Auto-generated constructor stub
		img_view.setImage(image);
		
	}
	
	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}
	


}
