package servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sgbd.SGBDfactory;

/**
 * Servlet implementation class DeleteBdd
 * Suppression d'un essai de la BDD : visibilité deviens 0 (plus visible mais tj en bdd)
 */
@WebServlet("/DeleteBdd")
public class DeleteBdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("test");     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBdd() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String iD = request.getParameter("ID");
		int id = Integer.parseInt(iD);
		
		SGBDfactory sgbd = SGBDfactory.getInstance();
		if(!sgbd.getSqlAccess().removeEssai(id)){
			logger.warning("Echec de la supression");
		}
		getServletContext().getRequestDispatcher("/indexEssai.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
