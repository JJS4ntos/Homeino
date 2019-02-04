package database;

import java.io.Serializable;

public class Data_room implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5597865405246084296L;
	private String name, info, imageurl;
	final private Integer id, userid;
	
	public Data_room(int id, String[] data, int userid) {
		this.id=id;
		this.userid=userid;
		setName(data[0]);
		setInfo(data[1]);
		setImageurl(data[2]);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Integer getUserid() {
		return userid;
	}

}
