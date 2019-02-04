package anothers;

import java.io.Serializable;
import java.util.Map;
import database.Data_room;
import house.Item;

public class projectSaved implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Data_room, Item[]> map_rooms;
	private final String author;
	
	public projectSaved(String author, Map<Data_room, Item[]> map_rooms){
		this.map_rooms=map_rooms;
		this.author=author;
	}
	
	/***
	 * Retorna o um map que contém  o data_room e os itens pertencentes a ele.
	 * @return
	 */
	public  Map<Data_room, Item[]> getRooms(){
		return map_rooms;
	}

	/***
	 * Retorna o nome do autor do projeto
	 * @return
	 */
	public String getAuthor() {
		return author;
	}
	
}
