<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel = "stylesheet" href = "Design/style.css" />
<title>Welcome</title>
</head>
<body>
		<div class="barreMenu">
			<table class="tableBarreMenu">
				<tr>
					<td>
						<a href="accueil.jsp" title="Main page">
							<img src="Design/img/home.png" alt=""/>
						</a>	
					</td>
					<td>
						<a href="indexMaterial.jsp" title="See all materials">
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
		<div class="onglet">
			Home
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<p class="pAccueil">
					Hello <% out.print(session.getAttribute("prenom") + " "+session.getAttribute("nom"));%>
					, welcome on BatMeca !<br/><br/>
					<% 
				String condition = (session.getAttribute("admin")).toString();
				if(condition.equals("true")){
				  out.println("<a href='indexUser.jsp'> - Handle Users</a>");
				 
			   }
			
			%>
			<br/><br/>
					For any help <a href="">click here</a>
				</p>	
			</div>
		</div>	
			
	
	</body>
</html>