package house.digital;

import house.Item;


/*Este é o atuador geral, com ele será possivel acionar um atuador mesmo o programa não disponibilizando ele para a integração no software.
 *Vale ressaltar que todos os atuadores gerais tem o mesmo código de acionamento, ou seja, não poderão ser acionados atuadores que necessitem
 *de acionamentos especiais ou com variações de tensão.*/
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
