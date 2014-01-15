<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*"%>
    <%@ page import="utilisateur.*"%>
    <%@ page import="sgbd.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Index of Users</title>
</head>
<body>
<h1>List of all the User in database</h1>
<TABLE>
<TR>
  <TD>login</TD>
  <TD>password</TD>
  <TD>last name</TD>
  <TD>first name</TD>
  <TD>email</TD>
  <TD>lab ID</TD>
  <TD>IsAdmin</TD>
  <TD>Update</TD>
  <TD>Delete</TD>
</TR>
<%

SGBDfactory sgbd = SGBDfactory.getInstance();
List<User> liste = (List<User>)sgbd.getSqlAccess().getAllUser();
Iterator<User> it = liste.iterator();
while(it.hasNext()) {
		User user = (User) it.next();
		out.println("<TR>");
		out.println("<TD>" + user.getLogin() + "</TD>");
		out.println("<TD>" + user.getPassword() + "</TD>");
		out.println("<TD>" + user.getLastName() + "</TD>");
		out.println("<TD>" + user.getFirstName() + "</TD>");
		out.println("<TD>" + user.getEmail() +"</TD>");
		out.println("<TD>" + user.getLabId() +"</TD>");
		out.println("<TD>" + user.isAdmin() + "</TD>");
		out.println("<TD> <a href='editerUser.jsp?login="+user.getLogin()+"'> <img src='Design"+System.getProperty("file.separator")+"editer.png'/></a> </TD>");
		out.println("<TD> <img src='Design"+System.getProperty("file.separator")+"supprimer.png'/></TD>");
		out.println("</TR>");
	
}

%>
</TABLE>
<a href='accueil.jsp'>Return to Main page</a>
</body>
</html>