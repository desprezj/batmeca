package servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sgbd.SGBDfactory;
import utilisateur.User;


/**
 * Servlet implementation class LoginServlet
 * Servlet de Login pour vérifier les ID et créer la session
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("test");
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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

		SGBDfactory sgbd = SGBDfactory.getInstance();
		String login = request.getParameter("inputLogin");
		String password = request.getParameter("inputPassword");
		//On tente la connexion avec les ID entrés
		User user = null;
		user = sgbd.getSqlAccess().login(login,password);

		if(user == null){//si la connexion echoue
			logger.warning("ID incorrect");
			request.setAttribute("error", "Incorrect ID, please try again!");
			getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
		}
		else{//authentification réussie
			//Ouverture d'une session de travail
			logger.info("Connexion à la BDD");
			
			HttpSession session = request.getSession();
			session.setAttribute("login", user.getLogin());
			session.setAttribute("nom", user.getLastName());
			session.setAttribute("prenom", user.getFirstName());
			session.setAttribute("admin", user.isAdmin());

			session.setAttribute("email", user.getEmail());
			session.setAttribute("labId", user.getLabId());

			//Seul PATH écrit en dur et à modifier pour la portabilité
			ServletContext context = getServletContext();
			
			session.setAttribute("PATH", context.getInitParameter("ressourcePath"));

			getServletContext().getRequestDispatcher("/accueil.jsp").forward(request, response);
		}
	}

}
