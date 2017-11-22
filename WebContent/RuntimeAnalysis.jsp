<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Alert File</title>
<style>
	#upload h3 { margin:0}
	#upload { padding:5px; margin-top:10px}
	#upbtn {margin-top:10px;}

</style>
<% 
	if (request.getParameter("diagnosticurl") != null) { %>
	
	<script>
	do {
	var userName = prompt("Username",""); 
    
	} while (userName == null || userName == "");
	
	do {
		var passWord = prompt("Password","");
	    
		} while (passWord == null || passWord == "");

	</script>	
<% 	}
%>

<script>
$(document).ready(function(){
	$("#upbtn").button({});
	
	var aUrl = document.getElementById("ALERT_URL");
	aUrl.value = decodeURIComponent('<%=request.getQueryString()%>');
	document.getElementById("name").value = userName;
	document.getElementById("password").value = passWord;
//	alert(aUrl.value);
	document.getElementById("frmRuntimeAnalysis").submit();
});
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<img src="./images/spinning.gif">
<div id="upload" class="ui-widget-content">
	<h3 class="ui-widget-header">Parsing...</h3>
<form id="frmRuntimeAnalysis" action="RuntimeAnalysis" method="post">

<input type="hidden" name="diagUrl" id="ALERT_URL"/>
<input type="hidden" name="name" id="name"/>
<input type="hidden" name="password" id="password"/>

</div>
</form>
</div>
</body>
</html>