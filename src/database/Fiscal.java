package database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

public class Fiscal extends Task<Void> {

	//private final Label label;
	private SimpleBooleanProperty booleanproperty;
	private SimpleIntegerProperty licencecountproperty;
	private final Licenca licence;

	public Fiscal(Licenca licence){
		//this.label=label;
		booleanproperty= new SimpleBooleanProperty(false);
		this.licence=licence;
		licencecountproperty=new SimpleIntegerProperty(getLicence().getCount());
		this.setOnCancelled(e->save());
		this.setOnFailed(e->save());
		this.setOnRunning(e->save());
	}
	
	@Override
	protected Void call() throws InterruptedException {
		// TODO Auto-generated method stub
		do{
			Thread.sleep(1000);
			licence.incrementSecond();
			//Somar +1 ou setar para valor atual
			Platform.runLater(()->licencecountproperty.set(licence.getCount()));
		}while(licence.getCount()<licence.getMaxCount());
			booleanproperty.setValue(true);
		return null;
	}
	
	public SimpleBooleanProperty getLicenceExpired(){
		return booleanproperty;
	}
	
	public Licenca getLicence(){
		return licence;
	}
	
	private void save(){
		try{
			FileOutputStream fileoutput= new FileOutputStream(Licenca.LICENCE_DIR);
			ObjectOutputStream output= new ObjectOutputStream(fileoutput);
				output.writeObject(licence);
				output.flush();
				output.close();
				fileoutput.flush();
				fileoutput.close();
		}catch(IOException e1){Platform.exit();}
	}

	public SimpleIntegerProperty licencecountproperty() {
		return licencecountproperty;
	}
	
	
	

}
