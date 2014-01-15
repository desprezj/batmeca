<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="Design/style.css" />
<title>Login</title>
</head>
<body>
<h1>Welcome on BatMeca</h1>
<div id="connexion">

		<form action="LoginServlet" method="POST">
				<p>Login: <input name="inputLogin" type="text" width="200" /></p>
				<p>Password: <input name="inputPassword" type="password" width="200" /></p>
				<p><input type="submit" value="Connection !"/></p>				

		</form>
		<% out.println("<p style='color:red'>"+request.getAttribute("error")+"</p>"); %>
		<!--  <a href="#" onclick="document.getElementById('mdp_perdu').style.visibility='visible';">Forgot your password ?</a>
			
			<div id="mdp_perdu" style="visibility:hidden">
			<form action="" method="POST">
				<p>Login: <input name="inputLogin" type="text" width="200" /></p>
				<p>Email: <input name="inputEmail" type="text" width="200" /></p>
				 <p><input type="submit" value="Send Password"/></p
			</form>
	
			</div>-->
		</div>
</body>
</html>