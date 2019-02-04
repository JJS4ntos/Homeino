package controllers;

import java.io.File;

import database.Licenca;
import database.Sqlite;

public interface Load_version {
	
	
	final String DIR=System.getProperty("file.separator")
	        + "configuration_house"  
	        + System.getProperty("file.separator")
	        +"security.jj";

	public byte[] getMAC();

	default boolean IsIniciante(){
		File security= new File(DIR);
		return !security.exists()&&Sqlite.getValueRegJVM("WINDOWS_JAVA_ACCESS_ENABLED")==null;
	}
	
	default boolean HaveLicence(){
		File security= new File(Licenca.LICENCE_DIR);
		return security.exists();
	}
	
	
}
