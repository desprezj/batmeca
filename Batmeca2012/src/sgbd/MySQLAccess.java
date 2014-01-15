package sgbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.PreparedStatement;

import utilisateur.User;
import essai.EssaiModel;

/**
 * 
 * @author Julien
 * Class permettant les accès à la BDD
 */
public class MySQLAccess {
	//Paramètre permettant la connexion à la BDD
	private static final String USER = "bat";
	private static final String PASSWORD = "bat";
	private static final String SERVER = "localhost";
	private static final String PORT = "3306";
	private static final String DATABASE = "batmeca2012";
	
	private Connection connection = null;
	private static Logger logger = Logger.getLogger("test"); 
public MySQLAccess(){
		
	}
/**
 * Ouvre la connexion à la bdd
 * @throws ClassNotFoundException
 * @throws SQLException
 */
private void open() throws ClassNotFoundException, SQLException{
	//Ouverture de la connexion à la BDD
	if (this.connection != null){
		this.connection.close();
	}
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	this.connection = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PORT+"/"+DATABASE,USER,PASSWORD);
	
}

/**
 * ferme la connexion à la bdd
 */
private void close(){
	//Fermeture de la connexion à la BDD
	try {	
		if (this.connection != null){
			this.connection.close();
		}
	} catch (SQLException e) {
		logger.log(Level.WARNING,"echec fermeture",e);
	}
}

/**
 * 
 * @param login: entré par l'utilisateur pour s'identifier
 * @param pass :mot de passe entré par l'utilisateur
 * @return
 */
public User login(String login, String pass){
	//Si on trouve l'utilisateur en BDD la connexion va réussir, on renvoie donc toutes les infos
	if (login == null || pass == null){
		return null;
	}
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM user WHERE login = '"+login+"' AND mdp = '"+pass+"'";
		ResultSet rs = st.executeQuery(query);
		
		if (rs.next()){
			User user = new User();
			user.setFirstName(rs.getString("prenom"));
			user.setLastName(rs.getString("nom"));
			user.setLogin(rs.getString("login"));
			user.setEmail(rs.getString("email"));
			user.setLabId(rs.getString("login"));
			user.setAdmin(rs.getBoolean("admin"));
			user.setLabId(rs.getString("LabID"));
			rs.close();
			st.close();
			return user;
		}
		else{
			rs.close();
			st.close();
			return null;
		}
	}
	catch(Exception e){
		logger.log(Level.SEVERE,"echec login sql",e);
		return null;
	}
	finally{
		this.close();
	}	
}

/**
 * 
 * @param user utilisateur à ajouter à la BDD
 * @return true si la création est ok
 */
public boolean insertUser(User user){
	if (user == null){
		return false;
	}
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "INSERT INTO user(login,nom,prenom,mdp,email,admin,LabID) VALUES('"+user.getLogin()+"','"+user.getLastName()+"','"+user.getFirstName()+"','"+user.getPassword()+"','"+user.getEmail()+"','"+user.isAdmin()+"','"+user.getLabId()+"')";
		int rs = st.executeUpdate(query);
		st.close();
		return rs > 0 ? true : false ;
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec insert user sql",e);
		return false;
	}
	finally{
		this.close();
	}
}


/**
 * Supprimer un utilisateur de la bdd.
 * @param login identifie l'utilisateur à supprimer
 * @return true si la suppression a réussi, false sinon
 */
public boolean removeUser(String login){
	if (login == null){
		return false;
	}
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "DELETE FROM user WHERE login = '"+login+"'";
		int rs = st.executeUpdate(query);
		st.close();
		return rs > 0 ? true : false ;
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec remove user sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

/**
 * Modifier un utilisateur de la bdd.
 * @param login identifier l'utilisateur à modifier
 * @param user contient les nouvelles donnéesde l'utilisateur
 * @return true si la modification réussi, false sinon
 */
public boolean modifyUser(String login,User user){
	if (login == null || user == null){
		return false;
	}
	try{
		this.open();
		Statement st = this.connection.createStatement();
		int admin;
		if(user.isAdmin()){
			admin=1;
		}
		else{
			admin=0;
		}
		String query = "UPDATE user SET nom='"+user.getLastName()+"', prenom='"+user.getFirstName()+"', email='"+user.getEmail()+"', mdp='"+user.getPassword()+"', LabID='"+user.getLabId()+"', admin='"+admin+"' WHERE login='"+login+"'";
		int rs = st.executeUpdate(query);
		st.close();
		return rs > 0 ? true : false ;
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec modify user sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

/**
 * 
 * @param login: récupération d'un user en fonction de son login
 * @return
 */
public User getUser(String login){
	if (login == null){
		return null;
	}
	try{
		this.open();
		Statement st = this.connection.createStatement();//on récupère le user en cherchant son login
		String query = "SELECT * FROM user WHERE login = '"+login+"'";
		ResultSet rs = st.executeQuery(query);
		User user = null;
		if(rs.next()){//on crée un new user avec les infos
			user = new User();
			user.setLogin(rs.getString("login"));
			user.setFirstName(rs.getString("prenom"));
			user.setLastName(rs.getString("nom"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("mdp"));
			user.setAdmin(rs.getBoolean("admin"));
			user.setLabId(rs.getString("LabID"));
		}
		rs.close();
		st.close();
		return user;//renvoie le nouvel utilisateur
		
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get user sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * 
 * @return La liste de tous les utilisateur en BDD
 */
public List<User> getAllUser(){
	ArrayList<User> liste  = new ArrayList<User>();
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM user";
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){ // on renvoie tous les utilisateurs en BDD
			User user = new User();
			user.setLogin(rs.getString("login"));
			user.setFirstName(rs.getString("prenom"));
			user.setLastName(rs.getString("nom"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("mdp"));
			user.setAdmin(rs.getBoolean("admin"));
			user.setLabId(rs.getString("LabID"));
			liste.add(user);
		}
		
		st.close();
		rs.close();
		return liste;
		
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get all user sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * Insert un essai dans la bdd.
 * @param essai essai à insérer
 * @return true si fonctionne
 */
public boolean insertEssai(EssaiModel essai){
	if (essai == null){
		return false;
	}
	try{
		this.open();
		PreparedStatement pstm = (PreparedStatement) this.connection.prepareStatement("INSERT INTO essai(type,id_attribut_essai,id_materiau,visible) VALUES(?,?,?,?)");
		int visible;
		if(essai.isVisible()){
			visible = 1;
		}
		else{
			visible = 0;
		}
		pstm.setString(1, essai.getType());
		pstm.setString(2, essai.getIdAttribute());
		pstm.setString(3, essai.getIdMateriau());
		pstm.setInt(4, visible);
		int rs = pstm.executeUpdate();
		return rs > 0 ? true : false ;
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec insert essai sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

/**
 * Supprime un essai dans la bdd (de manière logique)
 * @param id id de l'essai à supprimer
 * @return true si la suppression a réussie, false sinon
 */
public boolean removeEssai(int id){
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "UPDATE essai SET visible = 0 WHERE id="+id+"";
		int rs = st.executeUpdate(query);
		return rs > 0 ? true : false ;
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec remove essai sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

/**
 * Renvoie l'essai associé à son ID
 * @param id
 * @return essai
 */
public EssaiModel getEssai(int id){
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM essai WHERE id = '"+id+"'";
		ResultSet rs = st.executeQuery(query);
		if(rs.next()){
			EssaiModel essai = new EssaiModel();
			essai.setId(rs.getInt("id"));
			essai.setType(rs.getString("type"));
			essai.setIdAttribute(rs.getString("id_attribut_essai"));
			essai.setIdMateriau(rs.getString("id_materiau"));
			essai.setVisible(rs.getBoolean("visible"));
			return essai;
		}
		else{
			return null;
		}
		
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get essai par ID sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * 
 * @return Renvoie tous les essai en BDD
 */
public List<EssaiModel> getAllEssai(){
	ArrayList<EssaiModel> liste  = new ArrayList<EssaiModel>();
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM essai";
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			EssaiModel essai = new EssaiModel();
			essai.setId(rs.getInt("id"));
			essai.setType(rs.getString("type"));
			essai.setIdAttribute(rs.getString("id_attribut_essai"));
			essai.setIdMateriau(rs.getString("id_materiau"));
			essai.setVisible(rs.getBoolean("visible"));
			liste.add(essai);
		}
		return liste;
		
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get all essai sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * Renvoie l'essai associé à son id de matériau
 * @param idMat
 * @return essai
 */
public List<EssaiModel> getEssai(String idMat){
	ArrayList<EssaiModel> liste  = new ArrayList<EssaiModel>();
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM essai WHERE id_materiau ='"+idMat+"'";
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			EssaiModel essai = new EssaiModel();
			essai.setId(rs.getInt("id"));
			essai.setType(rs.getString("type"));
			essai.setIdAttribute(rs.getString("id_attribut_essai"));
			essai.setIdMateriau(rs.getString("id_materiau"));
			essai.setVisible(rs.getBoolean("visible"));
			liste.add(essai);
		}
		return liste;
		
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get essai apr IdMAT sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * 
 * @return Renvoie tous les materiaux en BDD
 */
public List<String> getAllMat(){
	ArrayList<String> liste  = new ArrayList<String>();
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM material";
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			liste.add(rs.getString("id_materiau"));
		}
		rs.close();
		st.close();
		return liste;
		
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get all mat sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * Insert un materiel dans la bdd.
 * @param idMat mat à insérer
 * @return true si fonctionne
 */
public boolean insertMat(String idMat){
	if (idMat == null){
		return false;
	}
	try{
		this.open();
		PreparedStatement pstm = (PreparedStatement) this.connection.prepareStatement("INSERT INTO material(id_materiau) VALUES(?)");
		pstm.setString(1, idMat);

		int rs = pstm.executeUpdate();
		return rs > 0 ? true : false ;
	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec insert mat sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

/**
 * 
 * @return Renvoie tous les fichiers associé à un essai
 */
public List<String> getAllFile(String nomEssai){
	List<String> liste  = new ArrayList<String>();
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM fichier WHERE id_essai='"+nomEssai+"'";
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			liste.add(rs.getString("nom_fichier"));
		}
		rs.close();
		st.close();
		return liste;

	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get all file sql",e);
		return null;
	}
	finally{
		this.close();
	}
}

/**
 * Insert un fichier dans la bdd.
 * @param idMat mat à insérer
 * @return true si fonctionne
 */
public boolean insertFile(String nomEssai, String nomFichier){
	if(!fileAlreadyInDataBase(nomEssai, nomFichier)){
		try{
			this.open();
			PreparedStatement pstm = (PreparedStatement) this.connection.prepareStatement("INSERT INTO fichier(id_essai,nom_fichier) VALUES(?,?)");
			pstm.setString(1, nomEssai);
			pstm.setString(2, nomFichier);
	
			int rs = pstm.executeUpdate();
			return rs > 0 ? true : false ;
		}
		catch (Exception e){
			logger.log(Level.SEVERE,"echec insert file sql",e);
			return false;
		}
		finally{
			this.close();
		}
	}
	logger.info("Fichier déjà en BDD");
	return false;
}

/**
 * Check si le fichier est déjà en BDD auquelle cas on ne le réajoute pas
 * @param nomEssai
 * @param nomFichier
 * @return
 */
public boolean fileAlreadyInDataBase(String nomEssai, String nomFichier){

	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "SELECT * FROM fichier WHERE id_essai='"+nomEssai+"' AND nom_fichier='"+nomFichier+"'";
		ResultSet rs = st.executeQuery(query);
		if(rs.next()){
			rs.close();
			st.close();
			return true;
		}
		else{
			rs.close();
			st.close();
			return false;
		}

	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec get all file sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

/**
 * Delete a file from BDD (not from physical)
 * @param nomEssai
 * @param nomFichier
 * @return
 */
public boolean deleteFile(String nomEssai, String nomFichier){
	try{
		this.open();
		Statement st = this.connection.createStatement();
		String query = "DELETE FROM fichier WHERE id_essai='"+nomEssai+"' AND nom_fichier='"+nomFichier+"'";
		return st.execute(query);

	}
	catch (Exception e){
		logger.log(Level.SEVERE,"echec delete file in sql",e);
		return false;
	}
	finally{
		this.close();
	}
}

}
