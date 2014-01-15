package servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import sgbd.SGBDfactory;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("test");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
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
		SGBDfactory sgbd = SGBDfactory.getInstance();
		StringBuilder path = new StringBuilder();
		path.append(request.getSession().getAttribute("PATH"));
		//fileseparator pour économiser des strings
		String fileSeparator="file.separator";
		path.append(System.getProperty(fileSeparator)).append(request.getParameter("nomMat")).append(System.getProperty(fileSeparator));
		path.append(request.getParameter("nomEssai")).append(System.getProperty(fileSeparator)).append(request.getParameter("nomDoss"));
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
 
            try {
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
 
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        File uploadedFile = new File(path.toString() + System.getProperty(fileSeparator) + fileName);
                        logger.info(uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);
                        sgbd.getSqlAccess().insertFile(request.getParameter("nomEssai"), fileName);
                    }
                }
            } catch (FileUploadException e) {
            	logger.log(Level.SEVERE,"echec de l'upload",e);
            } catch (Exception e) {
            	logger.log(Level.SEVERE,"liste vide",e);
            }
        }
		getServletContext().getRequestDispatcher("/pageEssai.jsp?ID="+request.getParameter("ID")+"").forward(request, response);
	}

}
