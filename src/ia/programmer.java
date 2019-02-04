package ia;

import java.util.ArrayList;

import house.Data_item;
import house.code.InputCode;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * Classe respons�vel por aprender a programar itens adicionados pelo usu�rio.
 *  - Par�metros para cria��o de formul�rios:
 * @author JJSantos
 *
 */
public class programmer {
	
	//ATEN��O!
	/*CRIAR UM SISTEMA DE SENSORES CRIADOS PELO USU�RIO, SENDO QUE QUANDO ELE FOR ADICIONAR O ITEM AO PROJETO, O FORMUL�RIO PERGUNTE QUEM
	 * SER� O ITEM ACIONADO POR AQUELE SENDOR. A FORMA MAIS F�CIL DE FAZER ISSO � ADICIONANDO BUTTONPIN COMO INPUTCODE OBRIGAT�RIO.*/
	
	//ATEN��O
	/*AN�LISAR COM CUIDADO A FORMA COM A QUAL A INPUTCODE PIN DEVE SE COMPORTAR QUANDO O ITEM TIVER MAIS DE UM PINO DE FUNCIONAMENTO.*/
	
	
	public programmer(){
		
	}
	
	/***
	 * Retorna todos os nodes que ser�o utilizados como entradas de valores
	 * @return
	 */
	public static ArrayList<Node> getNodes(Data_item data){
		ArrayList<Node> Nodes= new ArrayList<>();
		for(InputCode input:data.getFields()){
			TextField tf_input= new TextField();
			tf_input.setId(input.getName());
			tf_input.setPromptText(input.getName());
			tf_input.setAlignment(Pos.CENTER);
			Nodes.add(tf_input);
		}
		return Nodes;
	}

	
}
