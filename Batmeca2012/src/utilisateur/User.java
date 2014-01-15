package utilisateur;

/**
 * 
 * @author Julien Desprez
 * Class représentant un utilisateur
 */
public class User {
	private String login;
	private String password;
	private String lastName;
	private String firstName;
	private String labId;
	private String email;
	private boolean admin;
	/**
	 * renvoie le loggin ACCESSEUR
	 * @return login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * change le login de l'utilisateur
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * renvoie le password de l'utilisateur
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * change le mot de passe de l'utilisateur
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLabId() {
		return labId;
	}
	public void setLabId(String labId) {
		this.labId = labId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	
	
}
