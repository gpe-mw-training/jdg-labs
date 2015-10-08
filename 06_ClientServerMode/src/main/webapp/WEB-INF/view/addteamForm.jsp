<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Football Manager Command Screen</title>
</head>
<body>
	<h3>Add Team</h3>
	<p>Type in Team Name</p>
	<form:form method="POST" action="./addteamExec" modelAttribute="player">
	<form:hidden path="name" value="a" />
		<table>
			<tr>
				<td><form:label path="team">Team Name</form:label></td>
				<td><form:input path="team" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>	
		</table>
	</form:form>

</body>

</html>