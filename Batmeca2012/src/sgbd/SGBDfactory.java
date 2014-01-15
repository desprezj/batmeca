package sgbd;


/**
 * 
 * @author Julien
 * Classe permettant la création d'une SGQB du type MySQL
 * Factory qui met à disposition la BDD via la class MySQLAccess
 */
public class SGBDfactory {
	private static SGBDfactory instance = null;
	private MySQLAccess sqlAccess = null;
	
	/**
	 * constructor pour initialiser la sqlAccess
	 */
	private SGBDfactory(){
		this.sqlAccess = new MySQLAccess();
	}
	
	/**
	 * Renvoie l'instance de la bdd pour avoir acces à la connexion
	 * @return L'instance de la bdd
	 */
	public static SGBDfactory getInstance() {
		if(instance == null) {
			instance = new SGBDfactory();
		}
		return instance;
	}

	/**
	 * Renvoie l'acces à la BDD
	 * @return sqlaccess
	 */
	public MySQLAccess getSqlAccess() {
		return sqlAccess;
	}
	
	
	
}
