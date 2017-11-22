<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Set Threshold</title>
<style>
	#setthreshold h3 { margin:0}
	#setthreshold { padding:5px; margin-top:10px}
	#setthresholdbtn {margin-top:10px;}

</style>
<%

	if (!SessUtil.requireAlerts(request,response))
		return;
		
%>
<script>
$(document).ready(function(){
	$("#setthresholdbtn").button({});
});
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<div id="setthreshold" class="ui-widget-content">
	<h3 class="ui-widget-header">SetThreshold</h3>
<form action="ActionServlet" method="get">
<p>
<%
String pega01Filter = (String)request.getSession().getAttribute("PEGA01THRESHOLD");
if (pega01Filter == null)
	pega01Filter = "1000";
%>
<b>Pega0001 Threshold (in ms):</b><br>
<input type="hidden" name="ACTION" value="SETTHRESHOLD"/>
<input type="text" name="threshold" size="8" value="<%=pega01Filter %>">
<br><br/>
<span>This new threshold will filter out any PEGA0001 alerts that are below it.<br/>This allows you to focus on the worst performing interactions regardless of the system's current threshold.</span>
</p>
<div> 
<input type="submit" value="Set Threshold" id="setthresholdbtn">
</div>
</form>
</div>
</body> 
</html>