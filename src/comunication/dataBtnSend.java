package comunication;

/***
 * Dados do botão de envio de sinal para a porta serial onde o Arduino está conectado.
 * @author JJ
 *
 */
public class dataBtnSend{
  
  private String name, content_send;
  private final int id, item_id;
  
  public dataBtnSend(int id, int item_id, String name, String content_send){
    this.id=id;
    this.item_id=item_id;
    this.name=name;
    this.content_send=content_send;
  }
  
  public void setName(String name){
    this.name=name;
  }
  
  public String getName(){
    return name;
  }
  
  public int getId(){
    return id;
  }
  
  public int getItemId(){
    return item_id;
  }
  
  public void setSendContent(String content){
    this.content_send=content;
  }
  
  public String getSendContent(){
    return content_send;
  }
  
}