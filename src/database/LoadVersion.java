package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import controllers.Load_version;

public class LoadVersion implements Load_version{
	
	public byte[] MAC;
	
	public LoadVersion() throws SocketException, UnknownHostException {
		MAC= NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		// TODO Auto-generated constructor stub
	}

	public byte[] getMAC() {
		return MAC;
	}
	
	public Licenca loadLicenca() throws IOException, ClassNotFoundException{
		FileInputStream readFile=new FileInputStream(Licenca.LICENCE_DIR);
		ObjectInputStream input= new ObjectInputStream(readFile);
		Licenca licence=null;
		//Se o registro de primeiro acesso não existir
		if(Sqlite.getValueRegJVM("WINDOWS_JAVA_ACCESS_ENABLED")==null&&this.HaveLicence()){
			licence=(Licenca) input.readObject();
			if(!licence.IsActivedMAC()){
				licence.setMac(MAC);
			}
			Sqlite.putValueRegJVM("WINDOWS_JAVA_ACCESS_ENABLED", "1");
			
		}else if(Sqlite.getValueRegJVM("WINDOWS_JAVA_ACCESS_ENABLED").equals("1")&&this.HaveLicence()){
			licence=(Licenca) input.readObject();
			//Se o mac da licenca estiver correto vai passar
			if(!Arrays.equals(licence.getMac(), MAC)){
				licence=null;
			}
		}
		
		//else if(licence.IsActivedMAC()&&licence.getMac()!=MAC)licence=null;
		input.close();
		readFile.close();
		return licence;
	}

}
