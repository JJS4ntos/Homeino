package database;

public class User {

	private String name, login, auth, secret;
	private Integer language;
	final protected Integer id;
	
	//Esse é será o objeto gerado quando o usuário efetuar o login.
	public User(int id, String[] data, int language){
		this.id=id;
		name=data[0];
		login=data[1];
		auth=data[2];
		setSecret(data[3]);
		this.language=language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setLanguage(Integer language) {
		Sqlite.insert("UPDATE users SET language='"+language+"' WHERE id='"+id+"'");
		this.language = language;
	}
	
	public Integer getId(){
		return id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
