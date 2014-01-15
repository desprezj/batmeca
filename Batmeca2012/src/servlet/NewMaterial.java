package servlet;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sgbd.SGBDfactory;

/**
 * Servlet implementation class NewMaterial
 * 
 * Gère la création d'un nouveau materiel dans l'arborescence :
 * si il n'existe pas déjà et si le nom est correcte.
 */
@WebServlet("/NewMaterial")
public class NewMaterial extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("test");   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewMaterial() {
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
		// Création d'un nouveau dossier dans l'arborescence de matérial
		// Check si le nom est vide ou différent d'un existant
		String path = (String) request.getSession().getAttribute("PATH");
		String newMat = request.getParameter("nomMat");
		Boolean okToCreate = true;
		
		if(newMat.isEmpty()){
			logger.warning("new material : ERROR, empty");
			getServletContext().getRequestDispatcher("/indexMaterial.jsp").forward(request, response);
		}
		else
		{
			File repertoire = new File(path);
			String [] listFile;
			listFile = repertoire.list();
			if(listFile == null){
				logger.info("Liste Vide");
			}
			else{
				for(int i=0;i<listFile.length;i++){
					if(listFile[i].equalsIgnoreCase(newMat)){
						okToCreate = false;
						
					}
				}
			}
			
			if(okToCreate)
			{
				
				SGBDfactory sgbd = SGBDfactory.getInstance();
				if(!sgbd.getSqlAccess().insertMat(newMat)){
					logger.warning("Echec de l'insertion");
				}
				File file = new File(path+System.getProperty("file.separator")+newMat);
				file.mkdir();
				getServletContext().getRequestDispatcher("/indexMaterial.jsp").forward(request, response);
			}
			else
			{
				logger.warning("new material : ERROR, nom déjà utilisé");
				getServletContext().getRequestDispatcher("/indexMaterial.jsp").forward(request, response);
			}
		}

		
	}

}
