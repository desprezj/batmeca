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
<title>Index of Essais</title>
</head>
<%
		String idMat = (String) request.getParameter("material");
	%>
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
						<a href="createEssai.jsp?material=<% out.print(idMat);%>" title="Create material">
							<img src="Design/img/add.png" alt=""/>
						</a>	
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
		<div class="onglet">
			Material name
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<table class="tableMaterialList">
					<tr>
					  <th>ID</th>
					  <th>Type</th>
					  <th>Id_attribut_essai</th>
					  <th>id_materiau</th>
					  <th>Open Essai</th>
					  <th>Delete</th>
					</tr>
					<%
					
					
					SGBDfactory sgbd = SGBDfactory.getInstance();
					List<EssaiModel> liste = (List<EssaiModel>)sgbd.getSqlAccess().getEssai(idMat);
					Iterator<EssaiModel> it = liste.iterator();
					String materialName ="";
					String tr;
					Boolean b = true;
					while(it.hasNext()) {
						if(b){
							tr = "<tr class=\"trMaterialList1\">";
						}	
						else{
							tr = "<tr class=\"trMaterialList2\">";	
						}
						EssaiModel essai = (EssaiModel) it.next();
						materialName = essai.getIdMateriau();
						if(essai.isVisible()){
							out.println(tr);
							out.println("<td>" + essai.getId() + "</td>");
							out.println("<td>" + essai.getType() + "</td>");
							out.println("<td>" + essai.getIdAttribute() + "</td>");
							out.println("<td>" + essai.getIdMateriau() + "</td>");
							out.println("<td><a href='pageEssai.jsp?ID="+essai.getId()+"'><img src='./Design/img/editer.png' alt='EDIT'/></a></td>");
							out.println("<td><a href='DeleteBdd?ID="+essai.getId()+"'><img src='./Design/img/supprimer.png' alt='DELETE'/></a></td>");
							out.println("</tr>");
						}
						b = !b;
					}
		
					%>
				</table>
			</div>
		</div>	
		<% 
			/*out.println("<a href='createEssai.jsp?material="+idMat+"'>Create an Essai</a>");*/
		%>
		
	</body>
</html>