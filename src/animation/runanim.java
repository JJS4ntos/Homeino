package animation;

import house.Item;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

public class runanim {

	public static void mouseOff(Item btn, int time){
		ScaleTransition transition = new ScaleTransition(
				Duration.millis(time), btn);
				transition.setToX(1.0);
				transition.setToY(1.0);
				transition.play();
	}
	
	public static void mouseOn(Item btn, int time){
		ScaleTransition transition = new ScaleTransition(
				Duration.millis(time), btn);
				transition.setToX(1.05);
				transition.setToY(1.05);
				transition.play();
	}
	
	public static void toVisible(Node node, int time, boolean reverse, Node node2){
		ScaleTransition transition= new ScaleTransition(Duration.seconds(time), node);
				transition.setToX(1.5);
				transition.setToY(1.5);
				transition.play();
				if(reverse)transition.setOnFinished(e->toInvisible(node, time, node2));
				
	}
	
	public static void toInvisible(Node node, int time, Node node2){
		ScaleTransition transition= new ScaleTransition(Duration.seconds(time), node);
				transition.setToX(0.2);
				transition.setToY(0.2);
				transition.play();
				transition.setOnFinished(e->{
					node.setVisible(false);
					node2.setVisible(true);
				});
	}
	
	
}
