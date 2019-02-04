package house.code.conditions;

public class ir_children {
	
	private final int id, parent, item_active_id, onoff, userid;
	private final String hexValue;
	
	public ir_children(int id, int parent, int item_active_id, int onoff, int userid, String hexValue){
		this.id=id;
		this.parent=parent;
		this.item_active_id=item_active_id;
		this.onoff=onoff;
		this.userid=userid;
		this.hexValue=hexValue;
	}

	public int getId() {
		return id;
	}

	public int getParent() {
		return parent;
	}

	public int getItem_active_id() {
		return item_active_id;
	}

	public int getOnoff() {
		return onoff;
	}

	public int getUserid() {
		return userid;
	}

	public String getHexValue() {
		return hexValue;
	}
	
	
}
