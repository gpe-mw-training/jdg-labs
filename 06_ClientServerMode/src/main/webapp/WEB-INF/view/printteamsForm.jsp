<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Football Manager Command Screen</title>
</head>
<body>
	<h3>Print List of Teams</h3>
	<p>Hit Submit Button</p>
	<form:form method="POST" action="./printteamsExec" modelAttribute="player">
	<form:hidden path="name" value="a" />
	<form:hidden path="team" value="a" />
		<table>
			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>	
		</table>
	</form:form>

</body>

</html>