<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*"%>
    <%@ page import="utilisateur.*"%>
    <%@ page import="sgbd.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Index of Essais</title>
</head>
<body>
<%
	String login = request.getParameter("login");
	
	SGBDfactory sgbd = SGBDfactory.getInstance();
	User user = sgbd.getSqlAccess().getUser(login);
%>
<h1>Edit information</h1>
<form action="BddServlet" method="POST">
	<p>Login :<%out.print(user.getLogin()); %></p>
	<input type="hidden" name="login" size="50" value="<%out.print(user.getLogin()); %>">
	<p>password :<input type="text" name="password" size="50" value="<%out.print(user.getPassword()); %>"></p>
	<p>Last Name :<input type="text" name="lastName" size="50" value="<%out.print(user.getLastName()); %>"></p>
	<p>First Name :<input type="text" name="firstName" size="50" value="<%out.print(user.getFirstName()); %>"></p>
	<p>email address :<input type="text" name="email" size="50" value="<%out.print(user.getEmail()); %>"></p>
	<p>lab ID :<input type="text" name="labId" size="50" value="<%out.print(user.getLabId()); %>"></p>
	<input type="hidden" value="UPDATE_USER" name="ACTION" />
	<p>is Admin(true/false) :<input type="text" name="admin" size="5" value="<%out.print(user.isAdmin()); %>"></p>
 
 <p><input type="submit" value="Save Modifications"/> <a href='indexUser.jsp'>Return to User list</a></p>	
</form>



</body>
</html>