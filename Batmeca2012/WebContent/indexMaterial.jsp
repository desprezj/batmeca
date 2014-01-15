<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="sgbd.*"%>
     <%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel = "stylesheet" href = "Design/style.css" />
<title>Index of all material</title>
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
			<a href="accueil.jsp">
				Home
			</a>	
		</div>
		<div class="onglet">
			Index of all materials
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<table class="tableMaterialList">
			<%
			
			SGBDfactory sgbd = SGBDfactory.getInstance();
			List<String> liste = (List<String>)sgbd.getSqlAccess().getAllMat();
			Iterator<String> it = liste.iterator();
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
				materialName = it.next();
				out.println(tr+"<td><a href='indexEssai.jsp?material="+materialName+"'>"+materialName+"</a></td></tr></LI>");
				b=!b;
			}
				
			
			%>
				</table>
			</div>
		</div>	
		<div class="onglet">
			Add a new material
		</div>
		<div class="corps">	
			<div class="corpsContenu">
				<form action="NewMaterial" method="POST">
					<table>
						<tr>
							<td>
								Name
							</td>
							<td>
								<input name="nomMat" type="text" width="50" />
							</td>
					 		<td>
					 			<input class="bouton" type="submit" value="Create new material"/>
					 		</td>
						</tr>
					</table>					
				</form>
			</div>	
		</div>	
	</body>
</html>