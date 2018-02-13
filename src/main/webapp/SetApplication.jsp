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
	#setapp h3 { margin:0}
	#setapp { padding:5px; margin-top:10px}
	#setappbtn {margin-top:10px;}

</style>
<%

	if (!SessUtil.requireAlerts(request,response))
		return;
		
%>
<script>
$(document).ready(function(){
	$("#setappbtn").button({});
});
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<div id="setapp" class="ui-widget-content">
	<h3 class="ui-widget-header">Set Application</h3>
<form action="ActionServlet" method="get">
<p>
<%
String appFilter = (String)request.getSession().getAttribute("APPLICATIONFILTER");
if (appFilter == null)
	appFilter = "";
%>
Select Application:<br>
<select name="appFilter">
<option value="ALL">ALL</option>
<%
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	List<String> apps = analysis.getUniqueApps();
	for (String app : apps)
	{
%>
	<option value="<%=app %>"><%=app %></option>
<%  } %>
</select>
<input type="hidden" name="ACTION" value="SETAPPLICATION"/>
</p>
<div>
<input type="submit" value="Set Application" id="setappbtn">
</div>
</form>
</div>
</body>
</html>