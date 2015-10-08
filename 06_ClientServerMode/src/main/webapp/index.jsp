<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Performance Optimization using JBoss Data Grid - Lab 06 Client Server Mode</title>
<script type="text/javascript">
function OnSubmitForm()
{
  if(document.myform.operation[0].checked == true)
  {
    document.myform.action ="printteams";
  }
  else
  if(document.myform.operation[1].checked == true)
  {
    document.myform.action ="addteam";
  }
  else
	  if(document.myform.operation[2].checked == true)
	  {
	    document.myform.action ="removeteam";
	  }
	  else
		  if(document.myform.operation[3].checked == true)
		  {
		    document.myform.action ="addplayer";
		  }
		  else
			  if(document.myform.operation[4].checked == true)
			  {
			    document.myform.action ="removeplayer";
			  }
  return true;
}
</script>
</head>

<body>
	<h1>The Football Manager Application</h1>
	<p>Select your command, Football Manager:</p>
<form name="myform" onsubmit="return OnSubmitForm();">
   <input type="radio" name="operation" value="1" checked>Print List of Teams
     <input type="radio" name="operation" value="2">Add Team
      <input type="radio" name="operation" value="3">Remove Team
        <input type="radio" name="operation" value="4">Add Player
          <input type="radio" name="operation" value="5">Remove Player
   <p>
   <input type="submit" name="submit" value="Proceed">
   </p>
</form>
</body>

</html>