package ia;

import java.util.ArrayList;

import house.Data_item;
import house.code.InputCode;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * Classe responsável por aprender a programar itens adicionados pelo usuário.
 *  - Parâmetros para criação de formulários:
 * @author JJSantos
 *
 */
public class programmer {
	
	//ATENÇÃO!
	/*CRIAR UM SISTEMA DE SENSORES CRIADOS PELO USUÁRIO, SENDO QUE QUANDO ELE FOR ADICIONAR O ITEM AO PROJETO, O FORMULÁRIO PERGUNTE QUEM
	 * SERÁ O ITEM ACIONADO POR AQUELE SENDOR. A FORMA MAIS FÁCIL DE FAZER ISSO É ADICIONANDO BUTTONPIN COMO INPUTCODE OBRIGATÓRIO.*/
	
	//ATENÇÃO
	/*ANÁLISAR COM CUIDADO A FORMA COM A QUAL A INPUTCODE PIN DEVE SE COMPORTAR QUANDO O ITEM TIVER MAIS DE UM PINO DE FUNCIONAMENTO.*/
	
	
	public programmer(){
		
	}
	
	/***
	 * Retorna todos os nodes que serão utilizados como entradas de valores
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
