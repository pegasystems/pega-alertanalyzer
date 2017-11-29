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
	#setnode h3 { margin:0}
	#setnode { padding:5px; margin-top:10px}
	#setnodebtn {margin-top:10px;}

</style>
<%

	if (!SessUtil.requireAlerts(request,response))
		return;
		
%>
<script>
$(document).ready(function(){
	$("#setnodebtn").button({});
});
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<div id="setnode" class="ui-widget-content">
	<h3 class="ui-widget-header">Set Node</h3>
<form action="ActionServlet" method="get">
<p>
<%
String nodeFilter = (String)request.getSession().getAttribute("NODEFILTER");
if (nodeFilter == null)
	nodeFilter = "";
%>
Select Node:<br>
<select name="nodeFilter">
<option value="">ALL</option>
<%
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	List<String> nodes = analysis.getUniqueNodes();
	HashMap<String, List<AlertData>> alertsByNode = analysis.getAlertsByNode();
	for (String node : nodes)
	{
		List<AlertData> nodesAlerts = alertsByNode.get(node);
%>
	<option value="<%=node %>"><%=node %> (Alerts: <%=nodesAlerts.size() %>)</option>
<%  } %>
</select>
<input type="hidden" name="ACTION" value="SETNODE"/>
</p>
<div>
<input type="submit" value="Set Node" id="setnodebtn">
</div>
</form>
</div>
</body>
</html>