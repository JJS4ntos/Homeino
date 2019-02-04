package house.digital;

import house.Item;


/*Este � o atuador geral, com ele ser� possivel acionar um atuador mesmo o programa n�o disponibilizando ele para a integra��o no software.
 *Vale ressaltar que todos os atuadores gerais tem o mesmo c�digo de acionamento, ou seja, n�o poder�o ser acionados atuadores que necessitem
 *de acionamentos especiais ou com varia��es de tens�o.*/
public class Atuador extends Item {

	public Atuador(int id, String name, int onoff, int parent, String pin,
			int button, double x, double y, house.Room room, int type) {
		super(id, name, onoff, parent, pin, button, x, y, room, type, "", false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initPopOver() {
		// TODO Auto-generated method stub

	}

}
