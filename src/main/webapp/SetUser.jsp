<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="com.pega.gsea.util.*" %>
     <%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Set Application</title>
<style>
	#setuser h3 { margin:0}
	#setuser { padding:5px; margin-top:10px}
	#setuserbtn {margin-top:10px;}

</style>
<%

	if (!SessUtil.requireAlerts(request,response))
		return;
		
%>
<script>
$(document).ready(function(){
	$("#setuserbtn").button({});
});
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<div id="setuser" class="ui-widget-content">
	<h3 class="ui-widget-header">Set User</h3>
<form action="ActionServlet" method="get">
<p>
<%
String userFilter = (String)request.getSession().getAttribute("USERFILTER");
if (userFilter == null)
	userFilter = "";
%>
Select User:<br>
<select name="userFilter">
<option value="ALL">ALL</option>
<%
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	List<String> users = analysis.getUniqueUsers();
	for (String user : users)
	{
%>
	<option value="<%=user %>"><%=user %></option>
<%  } %>
</select>
<input type="hidden" name="ACTION" value="SETUSER"/>
</p>
<div>
<input type="submit" value="Set User" id="setuserbtn">
</div>
</form>
</div>
</body>
</html>