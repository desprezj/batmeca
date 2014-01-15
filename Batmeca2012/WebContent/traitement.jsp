<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="parser.*"%>
  <%@ page import="donnees.*"%>
 <%@ page import="java.util.*"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Details of an Essai</title>
<script language="JavaScript">
        function transfertFunc(header, historic, data, savePath, fileName, repValue){
        	
          	//window.open("saveData.jsp?header="+header+"&histo="+historic+"&data="+data+"&savePath="+savePath+"&fileName="+fileName, "_blank");
        	var form = document.createElement("form");
        	form.setAttribute("method", "post");
        	form.setAttribute("action", "saveData.jsp");

        	// setting form target to a window named 'formresult'
        	form.setAttribute("target", "formresult");
			
        	//header complet
        	var hiddenField = document.createElement("input");     
        	hiddenField.setAttribute("type", "hidden");
        	hiddenField.setAttribute("name", "header");
        	hiddenField.setAttribute("value", header);
        	form.appendChild(hiddenField);
        	//historique
        	hiddenField = document.createElement("input");     
        	hiddenField.setAttribute("type", "hidden");
        	hiddenField.setAttribute("name", "historic");
        	hiddenField.setAttribute("value", historic);
        	form.appendChild(hiddenField);
        	//savePath ou sauvegarder
        	hiddenField = document.createElement("input");     
        	hiddenField.setAttribute("type", "hidden");
        	hiddenField.setAttribute("name", "savePath");
        	hiddenField.setAttribute("value", savePath);
        	form.appendChild(hiddenField);
        	//nom du fichier traité
        	hiddenField = document.createElement("input");     
        	hiddenField.setAttribute("type", "hidden");
        	hiddenField.setAttribute("name", "fileName");
        	hiddenField.setAttribute("value", fileName);
        	form.appendChild(hiddenField);
        	//données
        	hiddenField = document.createElement("input");     
        	hiddenField.setAttribute("type", "hidden");
        	hiddenField.setAttribute("name", "data");
        	hiddenField.setAttribute("value", data);
        	form.appendChild(hiddenField);
        	//valeurs représentative
        	hiddenField = document.createElement("input");     
        	hiddenField.setAttribute("type", "hidden");
        	hiddenField.setAttribute("name", "repValue");
        	hiddenField.setAttribute("value", repValue);
        	form.appendChild(hiddenField);
        	
        	document.body.appendChild(form);
        	
        	form.submit();
        }
        
</script>
</head>
<body>

<%
	//Récupération des variables dans l'URL
	String fileName = request.getParameter("file");
	String ID = request.getParameter("ID");
	
	//Création du chemin de sauvegarde (utilisation de stringbuilder pour l'efficacité)
	StringBuilder path = new StringBuilder();
	StringBuilder savePath = new StringBuilder();
	StringBuilder configPath = new StringBuilder();
	
	savePath.append(request.getSession().getAttribute("PATH"));
	savePath.append(System.getProperty("file.separator")).append(request.getParameter("nomMat"));
	savePath.append(System.getProperty("file.separator")).append(request.getParameter("nomEssai"));

	//création du chemin vers le dossier
	path.append(savePath.toString()).append(System.getProperty("file.separator")).append(request.getParameter("nomDoss"));
	configPath.append(savePath.toString()).append(System.getProperty("file.separator")).append("config").append(System.getProperty("file.separator"));
	configPath.append(fileName.replace(".dat",".par"));
	//Parsage du header du fichier brute
	ParserType1 parser = new ParserType1();
	
	//création du chemin vers le fichier
	StringBuilder pathToFile = new StringBuilder();
	pathToFile.append(path.toString()).append(System.getProperty("file.separator")).append(fileName);
	
	//on parse le fichier brute
	parser.loadFile(pathToFile.toString());
	parser.processLineByLine();
	parser.extractHeaderInformation();
	
	//Parsage du fichier de config
	ParserConfig pc = new ParserConfig();
	pc.loadFile(configPath.toString());
	pc.processLinebyLine();
	pc.completeHeader(parser.getmD());
	
	
%>
	<h1>details of <%out.print(fileName); %></h1>
	<a href='pageEssai.jsp?ID=<%out.print(ID);%>'>Return to folder</a>
	

<div id="app">

<applet code="AppletGraph.class" codebase="src"
archive="jfreechart-1.0.14.jar,jcommon-1.0.17.jar,graph.jar,expr.jar,TestApplet.jar" height="1200" width="1200">
<%
	out.println("<param name='length' value='"+parser.getmD().get(0).size()+"'/>");
	out.println("<param name='path' value='"+path.toString()+"'/>");
	out.println("<param name='nbColumn' value='"+parser.getmD().size()+"'/>");
	out.println("<param name='fileName' value='"+fileName+"'/>");
	out.println("<param name='savePath' value='"+savePath.toString()+"'/>");
	out.println("<param name='configuration' value='"+pc.toString()+"'/>");
	
	for(int i =0; i<parser.getNbColonne();i++){
		out.println("<param name='col"+i+"' value='"+parser.getmD().get(i).getName()+"'/>");
	}
	
	for(int i =0; i<parser.getNbColonne();i++){
		out.println("<param name='DetCol"+i+"' value='"+parser.getmD().get(i).getUnit()+"'/>");
	}
	
	for(int i =0; i<parser.getmD().get(0).size();i++){
		out.print("<param name='XY"+i+"' value='");
		for(int j=0;j<parser.getmD().size();j++){
			out.print(parser.getmD().get(j).get(i)+"#");
		}
		out.println("'/>");
	}
%>
</applet>

</div>
<br/>
<hr>

</body>
</html>