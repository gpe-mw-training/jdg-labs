<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.StringTokenizer" %>
<html>
<head>
<title>The Football Manager Application</title>
</head>
<body>
<!--  Insert While Loop Scriptlet -->
	<h2>List of All Teams</h2>
	<table>
	<thead>
	</thead>
	<tbody>
<%    
		StringTokenizer st = new StringTokenizer((String)request.getAttribute("output"),String.format("%n"));
		while (st.hasMoreTokens()) {
	    out.println("<tr><td>"+st.nextToken()+"</td></tr>");
		}
%> 
     </tbody>
	</table>
</body>
</html>