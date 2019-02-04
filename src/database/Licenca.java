package database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Licenca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4815263623154702810L;
	/**
	 * 
	 */
	public transient final static String LICENCE_DIR="/configuration_house/security.jj";  
	        
	public transient static final int LICENCE_TRIAL=83732052, LICENCE_FULL=81179535;
	private Integer contador_max_trial, contador;//Essa variável será incrementada sempre que a data for maior de que a inicial
	private final Integer LICENCE_TYPE;
	private byte[] mac=null;
	
	public Licenca(int LICENCE_TYPE, byte[] mac) {
		this.mac=mac;
		this.LICENCE_TYPE=LICENCE_TYPE;
		switch(this.LICENCE_TYPE){
			case Licenca.LICENCE_TRIAL:{contador=0;contador_max_trial=86400;}break;
			case Licenca.LICENCE_FULL: {}break;
			default: {contador=0;contador_max_trial=86400;}break;
		}
	}
	
	public Licenca(int LICENCE_TYPE) {
		//this.mac=mac;
		this.LICENCE_TYPE=LICENCE_TYPE;
		switch(this.LICENCE_TYPE){
			case Licenca.LICENCE_TRIAL:{contador=0;contador_max_trial=86400;}break;
			case Licenca.LICENCE_FULL: {}break;
			default: {contador=0;contador_max_trial=86400;}break;
		}
	}
	
	public boolean IsTrial(){
		return LICENCE_TYPE==Licenca.LICENCE_TRIAL;
	}
	
	public boolean IsFull(){
		return LICENCE_TYPE==Licenca.LICENCE_FULL;
	}
	
	public Integer getCount(){
		return contador; 
	}
	
	public Integer getMaxCount(){
		return contador_max_trial;
	}
	
	public void incrementSecond(){
		if(!IsFull())
			contador=contador+1;
	}

	//É chamado na desserialização
	public void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException{
		//obsfucator contador
		contador=(contador*15)/18;
		contador=contador*100/Integer.bitCount(LICENCE_TYPE);
		contador=contador>>2;
		if(IsActivedMAC()){
			for(int i=0; i<mac.length;i++){
				mac[i]=(byte) (mac[i]>>2);
			}
		}
		stream.defaultReadObject();
	}
	
	/*CRIAR UM MÉTODO ABSTRATO E ESCREVE-LO SOMENTO NA LICENCA COMPLETA*/
	
	//É chamado na serialização
	public void writeObject(ObjectOutputStream stream) throws IOException{
		//obsfucator contador
		contador=(contador*18)/15;
		contador=contador*Integer.bitCount(LICENCE_TYPE)/100;
		contador=contador<<2;
		if(IsActivedMAC()){
			for(int i=0; i<mac.length;i++){
				mac[i]=(byte) (mac[i]<<2);
			}
		}
		//obsfucator licence
		stream.defaultWriteObject();
	}
	
	public byte[] getMac() {
		return mac;
	}
	
	public void setMac(byte[] mac){
		this.mac=mac;
	}
	
	public boolean IsActivedMAC(){
		return mac!=null;
	}
	
}
