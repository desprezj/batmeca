<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="essai.*"%>
<%@ page import="sgbd.*"%>
<%@ page import="java.io.File"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel = "stylesheet" href = "Design/style.css" />
		<%
			String idEssai = (String) request.getParameter("ID");
			int ID = Integer.parseInt(idEssai);
			SGBDfactory sgbd = SGBDfactory.getInstance();
			EssaiModel essai= (EssaiModel) sgbd.getSqlAccess().getEssai(ID);
			String NomEssai = essai.getType()+"_"+essai.getIdAttribute();
			List<String> listeFile = sgbd.getSqlAccess().getAllFile(NomEssai);
		%>
		<title>Essai: <%out.println(NomEssai); %></title>
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
		<div class="ongletUnselected">
			<a href="accueil.jsp">Home</a>
		</div>
		<div class="ongletUnselected">
			<a href="indexMaterial.jsp">Index of all materials</a>
		</div>
		<div class="ongletUnselected">
			<a href='indexEssai.jsp?material=<%out.print(essai.getIdMateriau());%>'>Material name</a>
		</div>
		<div class="onglet">
			<% out.println(NomEssai); %>
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<table class="tableMaterialList">
		
		
		<tr>
			<th colspan="2">
				<b>List of raw data files</b>
			</th>
		</tr>	
			<%
			//création du path de base pour l'arborescence
			StringBuilder basePath = new StringBuilder();
			basePath.append(request.getSession().getAttribute("PATH")).append(System.getProperty("file.separator"));
			basePath.append(essai.getIdMateriau()).append(System.getProperty("file.separator")).append(NomEssai).append(System.getProperty("file.separator"));
			
			String tr;
			for(int i=0;i<listeFile.size();i++){
				 if(listeFile.get(i).contains(".dat")){
						if(i%2 == 0)
							tr = "<tr class=\"trMaterialList1\">";
						else
							tr = "<tr class=\"trMaterialList2\">";	

						out.print(tr+"<td><a href='traitement.jsp?nomDoss=rawdata&nomMat="+essai.getIdMateriau()+"&nomEssai="+NomEssai+"&ID="+essai.getId()+"&file="+listeFile.get(i)+"'>"+listeFile.get(i)+"</td>");
						out.print("<td class=\"tdDelete\"><a href='DeleteFile?ID="+essai.getId()+"&file="+listeFile.get(i)+"&nomEssai="+NomEssai+"'><img src='./Design/img/supprimer.png' alt='DELETE'/></a></td></tr>");
				}
			}
			
		
		%>
			<tr>
				<th>
					<b>List of results files</b>
				</th>
			</tr>	
				<%
				
				for(int i=0;i<listeFile.size();i++){
					if(listeFile.get(i).contains(".res")){
						if(i%2 == 0)
							tr = "<tr class=\"trMaterialList1\">";
						else
							tr = "<tr class=\"trMaterialList2\">";
							
						out.println(tr+"<td><a href='OuvertureSimple.jsp?nomDoss=result&nomMat="+essai.getIdMateriau()+"&nomEssai="+NomEssai+"&ID="+essai.getId()+"&file="+listeFile.get(i)+"'  onclick='window.open(this.href); return false;'>"+listeFile.get(i)+"</a></td>");
						out.print("<td class=\"tdDelete\"><a href='DeleteFile?ID="+essai.getId()+"&file="+listeFile.get(i)+"&nomEssai="+NomEssai+"'><img src='./Design/img/supprimer.png' alt='DELETE'/></a></td></tr>");
					}
				}
				
			
				%>		
				<tr>
					<th>
						<b>List of configuration files</b>
					</th>
				</tr>
				<%
				for(int i=0;i<listeFile.size();i++){
					if(listeFile.get(i).contains(".par")){
						if(i%2 == 0)
							tr = "<tr class=\"trMaterialList1\">";
						else
							tr = "<tr class=\"trMaterialList2\">";
							
						out.println(tr+"<td><a href='OuvertureSimple.jsp?nomDoss=config&nomMat="+essai.getIdMateriau()+"&nomEssai="+NomEssai+"&ID="+essai.getId()+"&file="+listeFile.get(i)+"' onclick='window.open(this.href); return false;'>"+listeFile.get(i)+"</a></td>");
						out.print("<td class=\"tdDelete\"><a href='DeleteFile?ID="+essai.getId()+"&file="+listeFile.get(i)+"&nomEssai="+NomEssai+"'><img src='./Design/img/supprimer.png' alt='DELETE'/></a></td></tr>");
					}
				}
			
				%>		
				<tr>
					<th>	
						<b>History</b>
					</th>
				</tr>
			</table>
			</div>
		</div>
		<div class="onglet">
			Add a raw data file
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<form action="UploadFile?nomMat=<%out.print(essai.getIdMateriau()); %>&nomEssai=<%out.print(NomEssai); %>&ID=<%out.print(essai.getId()); %>&nomDoss=rawdata" method="POST" enctype="multipart/form-data">
					<p>data file:(.dat) <input type="file" name="file1" size="50"/></p>
		  			<input type="submit" value="Upload this raw file" />
				</form>
			</div>
		</div>
		<div class="onglet">
			Add a config file
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<form action="UploadFile?nomMat=<%out.print(essai.getIdMateriau()); %>&nomEssai=<%out.print(NomEssai); %>&ID=<%out.print(essai.getId()); %>&nomDoss=config" method="POST" enctype="multipart/form-data">
					<p>config file:(.par) <input type="file" name="file1" size="50"/></p>
		  			<input type="submit" value="Upload this config file" />
				</form>
			</div>
		</div>
	</body>
</html>