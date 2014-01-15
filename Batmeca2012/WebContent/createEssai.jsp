<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="java.util.*"%>
    <%@ page import="essai.*"%>
    <%@ page import="sgbd.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel = "stylesheet" href = "Design/style.css" />
<title>Create a new essai</title>
</head>
<body>
		<div class="barreMenu">
			<table class="tableBarreMenu">
				<tr>
					<td>
						<a href="accueil.jsp">
							<img src="Design/img/home.png" alt=""/>
						</a>	
					</td>
					<td>
						<a href="indexMaterial.jsp">
							<img src="Design/img/material.png" alt=""/>
						</a>	
						<img class="barreMenuImgUnclickable"src="Design/img/add.png" alt=""/>	
					</td>
					<td>
						<form action="Deconnection" method="POST">
							<input class="submitImage" value="" type="submit"/>
						</form>
					</td>
					<td>
						<a href="" title="Help">
							<img src="Design/img/help.png" alt=""/>
						</a>	
					</td>
				</tr>
			</table>			
		</div>
		<div class="ongletUnselected">
			<a href="accueil.jsp">Home</a>
		</div>
		<div class="ongletUnselected">
			<a href="indexMaterial.jsp">Index of all materials</a>
		</div>
		<div class="ongletUnselected">
			<a href="indexEssai.jsp?material=<%out.print(request.getParameter("material"));%>">Material name</a>
		</div>
		<div class="onglet">
			Insert a new essai into the database
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<form action="BddServlet" method="POST">
					<table class="tableFormLogin">
						<tr>
							<td>
								Type :
							</td>
							<td>
								<input type="text" name="type" size="50">
							</td>
						</tr>
						<tr>
							<td>
								Id_attribut_essai :
							</td>
							<td>
								<input type="text" name="id_essai" size="50">
							</td>
						</tr>	
						<tr>
							<td>
								<input class="buton" type="submit" value="create essai"/>
							</td>
						</tr>	
				 	</table>
				 	<%
						out.println("<input type='hidden' name='id_materiau' value="+request.getParameter("material")+">");
					%>
				 	<input type="hidden" value="INSERT_ESSAI" name="ACTION" />
				 	
				</form>
			</div>
		</div>		
	</body>
</html>