package anothers;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.TooManyListenersException;


public class comunicationserial implements SerialPortEventListener{
	
	private DataOutputStream serialOut;
	private final int taxa;
	private final String porta;
	private SerialPort port;
	
	
	public comunicationserial(String porta, int taxa){
		this.taxa=taxa;
		this.porta=porta;
		opencomunication();
		
	}
	
	public void opencomunication(){
		try{CommPortIdentifier IDport=CommPortIdentifier.getPortIdentifier(this.porta);
		port= (SerialPort) IDport.open("Comunicação Serial", this.taxa);
		port.addEventListener(this);
		port.notifyOnDataAvailable(true);
		serialOut= new DataOutputStream(port.getOutputStream());
		
		port.setSerialPortParams(taxa, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);}
	catch(NoSuchPortException e){e.printStackTrace();}
	catch(UnsupportedCommOperationException e) {e.printStackTrace();}
	catch(PortInUseException e){e.printStackTrace();}
	catch(IOException e){e.printStackTrace();} catch (TooManyListenersException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

	
	public void closecomunication(){
		try {serialOut.close();
		port.close();} catch (IOException e) {e.printStackTrace();}
	}
	
	public void write(int comunication){
		try {serialOut.write(comunication);
		serialOut.flush();}
		catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
