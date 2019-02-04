package house.digital;

import comunication.PopOvers;
import controllers.telaprincipal_controller;
import javafx.scene.image.Image;
import house.Item;

public class Buzzer extends Item {

	transient private Image img_buzzer= new Image(getClass().getResource("/images/BUZZER_icon.png").toExternalForm());
	
	public Buzzer(int id, String name, int onoff, int parent, String pin,
			int button, double x, double y, house.Room room, int type) {
		super(id, name, onoff, parent, pin, button, x, y, room, type,"", false);
		img_view.setImage(img_buzzer);
	}

	@Override
	public void initPopOver() {
		pop_changes=PopOvers.getDigitalPopOver(this, telaprincipal_controller.getLanguage());
	}

}
