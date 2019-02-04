package anothers;


public class personal_filters {

	public personal_filters() {
		// TODO Auto-generated constructor stub
	}

	public static Boolean filter(String text){
		char specialone;
		String specials="!@~;:<>%|/\'*&¨¨¨%#?+-=|\\/;:☺☻♥♦♣♠•◘○°$¢¹²³£¢¬()-§\"";
		for(int i=0; i<specials.length(); i++){
			specialone=specials.charAt(i);
			if(text.contains(String.valueOf(specialone)))
			return true;
		}
		return false;
	}
	
}
