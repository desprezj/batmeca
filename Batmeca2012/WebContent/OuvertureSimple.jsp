<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%out.print(request.getParameter("file")); %></title>
</head>
<body>
	<%
	//Récupération des variables dans l'URL
	String fileName = request.getParameter("file");
	String ID = request.getParameter("ID");
		
	StringBuilder path = new StringBuilder();
	
	path.append(request.getSession().getAttribute("PATH"));
	path.append(System.getProperty("file.separator")).append(request.getParameter("nomMat"));
	path.append(System.getProperty("file.separator")).append(request.getParameter("nomEssai"));
	//création du chemin vers le dossier
	path.append(System.getProperty("file.separator")).append(request.getParameter("nomDoss"));
	path.append(System.getProperty("file.separator")).append(fileName);
	
	
	BufferedReader br = new BufferedReader(new FileReader(path.toString()));
	String line;
	while ((line = br.readLine()) != null) {
	   	out.println(line.replace("\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") + "<br>");
	}
	
	br.close();

	
	%>

</body>
</html>