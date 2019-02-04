package anothers;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class graphics {

	private XYChart.Series temp_grafic;
	private AreaChart<?,?> grafic_temp;
	private XYChart.Data<Number, Number>[] charts;
	public graphics() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init(){
		final NumberAxis xAxis = new NumberAxis(1, 9, 1);
		final NumberAxis yAxis = new NumberAxis();
		charts= new XYChart.Data[10];
		grafic_temp = new AreaChart<>(xAxis,yAxis);
		grafic_temp.setPrefWidth(900);
		grafic_temp.setStyle("-fx-background-color: white");
		grafic_temp.setTitle("Temperatura °C");
		temp_grafic= new XYChart.Series<Number, Number>();
		temp_grafic.setName("Temperatura");
		for(int i=0; i<10; i++){
			charts[i]= new XYChart.Data<Number, Number>();
			charts[i].setXValue(0);
			charts[i].setYValue(0);
			temp_grafic.getData().add(charts[i]);	
		}
		grafic_temp.getData().add(temp_grafic);
	}

}
