package servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sgbd.SGBDfactory;
import utilisateur.User;
import essai.EssaiModel;

/**
 * Servlet implementation class BddServlet
 * Servlet permettant l'acc�s � la BDD pour modification
 */
@WebServlet("/BddServlet")
public class BddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("test");   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = (String) request.getParameter("ACTION");
		String fileSeparator = System.getProperty("file.separator");
		
		if(action.equals("UPDATE_USER"))
		{//Mise � jour des donn�es utilisateurs
			logger.info("Modification du USER en BDD");
			//cr�ation d'un nouveau User gr�ce aux donn�es des champs
			User user = new User();
			user.setLogin(request.getParameter("login"));
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			user.setPassword(request.getParameter("password"));
			user.setEmail(request.getParameter("email"));
			user.setLabId(request.getParameter("labId"));
			if((request.getParameter("admin")).equalsIgnoreCase("true")){
				user.setAdmin(true);//on traite le cas d'un utilisateur/administrateur
			}else{
				user.setAdmin(false);
			}
			//Ouverture de la session et r�cup�ration de la connexion � la BDD
			SGBDfactory sgbd = SGBDfactory.getInstance();
			if(!sgbd.getSqlAccess().modifyUser(user.getLogin(), user)){
				logger.warning("Echec de la modification");
			}
			getServletContext().getRequestDispatcher("/indexUser.jsp").forward(request, response);
		}
		
		if(action.equals("INSERT_ESSAI"))
		{//Insertion d'un nouvelle essai en BDD + cr�ation des dossiers physique associ�
			logger.info("Insertion d'un nouvelle essai en Bdd");
			//cr�ation d'un nouvelle essai
			EssaiModel essai = new EssaiModel();
			essai.setType(request.getParameter("type"));
			essai.setIdAttribute(request.getParameter("id_essai"));
			essai.setIdMateriau(request.getParameter("id_materiau"));
			essai.setVisible(true);
			//insertion de l'essai en bdd
			SGBDfactory sgbd = SGBDfactory.getInstance();
			if(!sgbd.getSqlAccess().insertEssai(essai)){
				logger.warning("Echec de l'insertion");
			}
			//cr�ation du dossier physique de l'essai
			StringBuilder path = new StringBuilder();
			path.append( request.getSession().getAttribute("PATH")).append(fileSeparator).append(essai.getIdMateriau()).append(fileSeparator);
			path.append(essai.getType()).append("_").append(essai.getIdAttribute());
			File file = new File(path.toString());
			file.mkdir();
			
			path.append(fileSeparator);
			
			//cr�ation des sous dossiers de l'essai : Historique
			file = new File(path.toString()+"history");
			file.mkdir();
			//donn�es brute
			file = new File(path.toString()+"rawdata");
			file.mkdir();
			//configuration
			file = new File(path.toString()+"config");
			file.mkdir();
			//resultat
			file = new File(path.toString()+"result");
			file.mkdir();
			
			//D�but de l'historique de l'essai avec la cr�ation
			FileWriter writer = null;
			//On r�cup�re la date pour l'historique
			Date aujourdhui = new Date();
			DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
			HttpSession session = request.getSession();
			//texte de cr�ation avec l'heure et la date
			String texte = shortDateFormat.format(aujourdhui)+"-- cr�ation par "+session.getAttribute("nom");
			try{//�criture en mode "append" dans le fichier d'historique
			     writer = new FileWriter(path.toString()+"history"+fileSeparator+"history.txt", true);
			     writer.write(texte,0,texte.length());
			}catch(IOException ex){
				logger.log(Level.WARNING,"echec de l'ecriture",ex);
			}finally{
			  if(writer != null){
			     writer.close();
			  }
			}
			//redirection vers la liste des essais du materiau
			getServletContext().getRequestDispatcher("/indexEssai.jsp?material="+essai.getIdMateriau()+"").forward(request, response);
		}
	}

}
