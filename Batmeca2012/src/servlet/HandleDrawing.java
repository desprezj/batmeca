package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import parser.ParserType1;

/**
 * Servlet implementation class HandleDrawing
 */
@WebServlet("/HandleDrawing")
public class HandleDrawing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandleDrawing() {
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
		String nomMat = request.getParameter("nomMat");
		String nomEssai = request.getParameter("nomEssai") ;
		String fileName = request.getParameter("file");
		String nomDossier = request.getParameter("nomDoss");
		String iD = request.getParameter("ID");

		
		//Création du chemin vers le fichier brute
		String path = (String) request.getSession().getAttribute("PATH");
		
		String fileSeparator = "file.separator";//pour faire des économies de string on en déclare une
		path = path + System.getProperty(fileSeparator) + nomMat + System.getProperty(fileSeparator) + nomEssai + System.getProperty(fileSeparator) + nomDossier;

		//Parsage du header du fichier brute
		
		//on parse le header du fichier
		ParserType1 parser = new ParserType1();
		parser.loadFile(path+System.getProperty(fileSeparator)+fileName);
		parser.extractHeaderInformation();
		
		int x = -1;
		int y = -1;
		
		//On récupère les deux colonnes à traiter : checkbox renvoie null si non cochée
		ArrayList<String> listColomn = (ArrayList<String>) parser.getColomnHeader();
		for(int i = 0 ; i<listColomn.size();i++){
			if(request.getParameter("X")!=null){
				x = Integer.parseInt(request.getParameter("X"));
			}
			if(request.getParameter("Y")!=null){
				y = Integer.parseInt(request.getParameter("Y"));
			}
		}
	
		
		request.setAttribute("X", x);
		request.setAttribute("Y", y);
		//on repart sur la page d'affichage avc les numéros des colonnes à utiliser
		getServletContext().getRequestDispatcher("/traitement.jsp?nomDoss="+nomDossier+"&ID="+iD+"&nomMat"+nomMat+"&file="+fileName+"&nomEssai="+nomEssai).forward(request, response);
	
	}

}
