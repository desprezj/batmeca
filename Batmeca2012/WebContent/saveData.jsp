<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="java.util.*"%>
      <%@ page import="java.io.*"%>
      <%@ page import="sgbd.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Saving data...</title>
</head>
<body>
	<%
		
		String header = request.getParameter("header");
		String histo = request.getParameter("historic");
		String data = request.getParameter("data");
		String repValue = request.getParameter("repValue");
		
		String tabHeader = header.replace("$$","\t");
		String[] tabData = data.replace("$$","\t").split("@@");
		String[] tabHisto = histo.split("@@");
		String[] tabValue = repValue.replace("$$","\t").split("\t");
	
		//on récupère le nom d'ID de l'essai
		String[] tabTemp = request.getParameter("savePath").replace(System.getProperty("file.separator"), "\t").split("\t");
		String idEssai = tabTemp[tabTemp.length-1];

		SGBDfactory sgbd = SGBDfactory.getInstance();
		sgbd.getSqlAccess().insertFile(idEssai, request.getParameter("fileName").replace(".dat", ".res"));
		
		StringBuilder basePath = new StringBuilder();
		basePath.append(request.getParameter("savePath")).append(System.getProperty("file.separator"));
		
		StringBuilder savePath = new StringBuilder();
		savePath.append(basePath.toString()).append("result").append(System.getProperty("file.separator"));
		
		//écriture du fichier de données + header
		Writer output = null;
		File file = new File(savePath.toString()+request.getParameter("fileName").replace(".dat", ".res"));
		output = new BufferedWriter(new FileWriter(file));
		int i=0;

		while(i<tabValue.length){
			output.write("# "+tabValue[i]+"\n");
			i++;
		}
		
		output.write(tabHeader+"\t");

		i =0;
		
		output.write("\n");
		while(i<tabData.length){
			output.write(tabData[i]+"\n");
			i++;
		}
		output.close();
		
		//écriture du fichier d'historique
		String histoPath = basePath.toString() + "history" + System.getProperty("file.separator");
		file = new File(histoPath+"historic_"+request.getParameter("fileName").replace(".dat",".txt"));
		output = new BufferedWriter(new FileWriter(file));
		i=0;
		while(i<tabHisto.length){
			output.write(tabHisto[i]+"\n");
			i++;
		}
		output.close();

		out.println("File is saved, you can close this page");
	%>
</body>
</html>