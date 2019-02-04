package house;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import comunication.dataBtnSend;
import house.code.InputCode;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Data_item implements Serializable{
	
	/**
	 * Essa classe foi feita para armazenar as informações dos sensores e atuadores criados pelo próprio usuário
	 */
	public static final long serialVersionUID = 4070533013759610877L;
	private String name, description, codeRead, codeRun, library, codeSetup;
	private boolean sensor, analog;
	private transient Image image;//Icone do item
	private byte[] serialized_image;
	private InputCode[] fields;//Campos de entrada no código, são as informações que deverão ser postas pelo usuário.
	private final double UID; //identificação do item 
	private dataBtnSend[] sends;
	private String[] methods;
	private String[] vglobals;
	
	//CRIAR AS NOVAS PROPRIEDADOS DO DATA_ITEM
	public Data_item(String name, String description, String codeRead, String codeRun, Image image, String library, boolean sensor, boolean analog,
			String codeSetup, dataBtnSend[] sends, String[] methods, String[] vglobals){
		UID=Double.valueOf(LocalDateTime.now().getDayOfMonth()+""+LocalDateTime.now().getMonthValue()+""+LocalDateTime.now().getYear()+""+
			LocalDateTime.now().getHour()+""+LocalDateTime.now().getMinute()+""+LocalDateTime.now().getSecond()+""+LocalDateTime.now().getNano());
		setName(name);
		setDescription(description);
		setCodeRead(codeRead);
		setCodeRun(codeRun);
		setLibrary(library);
		setSensor(sensor);
		setAnalog(analog);
		setCodeSetup(codeSetup);
		this.setSends(sends);
		try{saveImage(image);}catch(IOException e){e.printStackTrace();}
		this.setMethods(methods);
		this.setVglobals(vglobals);
		//System.out.println("Tamanho do array: "+serialized_image.length);
	}
	
	public void setFields(InputCode[] inputs){
		fields=inputs;
	}
	
	public InputCode[] getFields(){
		return fields;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCodeRead() {
		return codeRead;
	}

	public void setCodeRead(String codeRead) {
		this.codeRead = codeRead;
	}

	public String getCodeRun() {
		return codeRun;
	}

	public void setCodeRun(String codeRun) {
		this.codeRun = codeRun;
	}

	public boolean isSensor() {
		return sensor;
	}

	public void setSensor(boolean sensor) {
		this.sensor = sensor;
	}

	public boolean isAnalog() {
		return analog;
	}

	public void setAnalog(boolean analog) {
		this.analog = analog;
	}

	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image){
		this.image=image;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getCodeSetup() {
		return codeSetup;
	}

	public void setCodeSetup(String codeSetup) {
		this.codeSetup = codeSetup;
	}
	
	public void saveImage(Image image) throws IOException{
		BufferedImage buf=SwingFXUtils.fromFXImage(image, null);
		ByteArrayOutputStream s= new ByteArrayOutputStream();
		ImageIO.write(buf, "png", s);
		serialized_image= s.toByteArray();
		//System.out.println("Tamanho do array: "+serialized_image.length);
		setImage(image);
		
		s.close();
	}
	
	public Image loadImage() throws IOException{
		ByteArrayInputStream byte_in= new ByteArrayInputStream(serialized_image);
		BufferedImage buf=ImageIO.read(byte_in);
		return image= SwingFXUtils.toFXImage(buf, null);
	}
	
	//RESOLVER PROBLEMA COM O ARMAZENAMENTO DE IMAGEM
	public void writeObject(ObjectOutputStream stream) throws IOException{
		//ImageOutputStream image_out=ImageIO.createImageOutputStream(image);
		//image_out.read(serialized_image);
		//System.out.println("Tamanho do array: "+serialized_image.length);
		stream.defaultWriteObject();
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		//image= SwingFXUtils.toFXImage(ImageIO.read(stream), null);
		ByteArrayInputStream byte_in= new ByteArrayInputStream(serialized_image);
		BufferedImage buf=ImageIO.read(byte_in);
		image= SwingFXUtils.toFXImage(buf, null);
		//System.out.println("Tamanho do array: "+serialized_image.length);
		stream.defaultReadObject();
	}
	
	public byte[] getByteImage(){
		return serialized_image;
	}

	public double getUID() {
		return UID;
	}

	public dataBtnSend[] getSends() {
		return sends;
	}

	public void setSends(dataBtnSend[] sends) {
		this.sends = sends;
	}

	public String[] getMethods() {
		return methods;
	}

	public void setMethods(String[] methods) {
		this.methods = methods;
	}

	public String[] getVglobals() {
		return vglobals;
	}

	public void setVglobals(String[] vglobals) {
		this.vglobals = vglobals;
	}
	
}
