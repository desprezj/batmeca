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
 * Servlet permettant l'accès à la BDD pour modification
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
		{//Mise à jour des données utilisateurs
			logger.info("Modification du USER en BDD");
			//création d'un nouveau User grâce aux données des champs
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
			//Ouverture de la session et récupération de la connexion à la BDD
			SGBDfactory sgbd = SGBDfactory.getInstance();
			if(!sgbd.getSqlAccess().modifyUser(user.getLogin(), user)){
				logger.warning("Echec de la modification");
			}
			getServletContext().getRequestDispatcher("/indexUser.jsp").forward(request, response);
		}
		
		if(action.equals("INSERT_ESSAI"))
		{//Insertion d'un nouvelle essai en BDD + création des dossiers physique associé
			logger.info("Insertion d'un nouvelle essai en Bdd");
			//création d'un nouvelle essai
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
			//création du dossier physique de l'essai
			StringBuilder path = new StringBuilder();
			path.append( request.getSession().getAttribute("PATH")).append(fileSeparator).append(essai.getIdMateriau()).append(fileSeparator);
			path.append(essai.getType()).append("_").append(essai.getIdAttribute());
			File file = new File(path.toString());
			file.mkdir();
			
			path.append(fileSeparator);
			
			//création des sous dossiers de l'essai : Historique
			file = new File(path.toString()+"history");
			file.mkdir();
			//données brute
			file = new File(path.toString()+"rawdata");
			file.mkdir();
			//configuration
			file = new File(path.toString()+"config");
			file.mkdir();
			//resultat
			file = new File(path.toString()+"result");
			file.mkdir();
			
			//Début de l'historique de l'essai avec la création
			FileWriter writer = null;
			//On récupère la date pour l'historique
			Date aujourdhui = new Date();
			DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
			HttpSession session = request.getSession();
			//texte de création avec l'heure et la date
			String texte = shortDateFormat.format(aujourdhui)+"-- création par "+session.getAttribute("nom");
			try{//écriture en mode "append" dans le fichier d'historique
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
