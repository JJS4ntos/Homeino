package comunication;

public class Translate {

	public static final int REQUEST_MESSAGE_EXIT=0, MESSAGE_ALERT=1, MESSAGE_ADD_ITEM_DIGITAL=2, MESSAGE_ADD_ITEM_ANALOG=3;
	public static final int GERMANY=0, SPANISH=1, FRANCE=2, ENGLISH=3, ITALY=4, PORTUGUESE=5; 
	
	public Translate() {
		// TODO Auto-generated constructor stub
	}
	
	public String getTitleTextLanguage(final int TEXT_TYPE, final int LANGUAGE){
		String text="";
		switch(TEXT_TYPE){
		case REQUEST_MESSAGE_EXIT:{
			switch(LANGUAGE){
				case GERMANY:{
					text="Wollen Sie wirklich die Homeino zu schlie�en?";
				}break;
				case FRANCE:{
					text="Voulez-vous vraiment fermer le Homeino?";
				}break;
				case SPANISH:{
					text="Realmente desea cerrar el Homeino?";
				}break;
				case ITALY:{
					text="Vuoi veramente chiudere la Homeino?";
				}break;
				case ENGLISH:{
					text="Do you really want close this application?";
				}break;
				case PORTUGUESE:{
					text="Voc� realmente deseja fechar a aplica��o?";
				}break;
			}
		}break; //fim das mensagens para sair
		case MESSAGE_ADD_ITEM_DIGITAL:{
			switch(LANGUAGE){
				case GERMANY:{
					text="Bilden Sie einen neuen digitalen Element zu erstellen";
				}break;
				case FRANCE:{
					text="Formulaire pour cr�er un nouvel �l�ment num�rique";
				}break;
				case SPANISH:{
					text="Formar para crear un nuevo art�culo digital";
				}break;
				case ITALY:{
					text="Maschera per creare un nuovo elemento digitale";
				}break;
				case ENGLISH:{
					text="Form to create a new digital item";
				}break;
				case PORTUGUESE:{
					text="Formul�rio para a cria��o de um novo item digital";
				}break;
			}
		}break;
		case MESSAGE_ADD_ITEM_ANALOG:{
			switch(LANGUAGE){
				case GERMANY:{
					text="Bilden Sie einen neuen analogen Element zu erstellen";
				}break;
				case FRANCE:{
					text="Formulaire pour cr�er un nouvel �l�ment analogique";
				}break;
				case SPANISH:{
					text="Formulario para crear un nuevo elemento anal�gico";
				}break;
				case ITALY:{
					text="Maschera per creare un nuovo elemento analogico";
				}break;
				case ENGLISH:{
					text="Form to create a new analog item";
				}break;
				case PORTUGUESE:{
					text="Formul�rio para a cria��o de um novo item anal�gico";
				}break;
			}
		}break;
		}
		return text;
	}
	
	public static String[] getOptions(final int LANGUAGE){
		String[] options=null;
		switch(LANGUAGE){
			case PORTUGUESE:{
				String[] pt={"Nome do item","Pino do item","Pino do bot�o de acionamento","Aplicar","Apagar","Cancelar"};
				options=pt;
			}break;
			case ENGLISH:{
				String[] eng={"Item name","Item pin","Item button pin","Apply","Delete","Cancel"};
				options=eng;
			}break;
			case SPANISH:{
				String[] esp={"Nombre del elemento","Pasador de art�culos","Pin bot�n accionador","Aplicar","Borrar","Cancelar"};
				options=esp;
			}break;
			case FRANCE:{
				String[] fra={"Nom de l'article","Broche item","Bouton de commande de Pin","Appliquer","Effacer","Annuler"};
				options=fra;
			}break;
			case ITALY:{
				String[] ita={"Nome dell'articolo","Articolo pin","Pulsante attuatore Pin","Applicare","Cancellare","Annullare"};
				options=ita;
			}break;
			case GERMANY:{
				String[] ger={"Artikelname","Artikel Stift","Pin-Bet�tigungsknopf","Anwenden","I�schen","Stornieren"};
				options=ger;
			}break;
		}
		return options;
	}
	
	//Retorna os textos da tela de altera��o da conta conforme a linguagem indicada.
	public static String[] getChangeUserTexts(final int LANGUAGE){
		String[] texts=null;
		switch(LANGUAGE){
			case PORTUGUESE:{
				String[] portuguese={"Alterar nome do usu�rio", "Alterar senha do usu�rio", "Alterar frase secreta", "Alterar sua senha, seu nome ou frase secreta",
						"Insira a sua senha", "Precisamos da sua senha", "Senha :", "Nome :"};
				texts=portuguese;
			}break;
			case SPANISH:{
				
			}break;
			case GERMANY:{
				
			}break;
			case FRANCE:{	
				
			}break;
			case ITALY:{
				
			}break;
			case ENGLISH:{
				
			}break;
		}
		
		return texts;
	}
}
