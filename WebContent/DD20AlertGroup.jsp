<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.*" %>
 <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>
<%
	if (!SessUtil.requireAlerts(request,response))
		return;
	String alertGroupLabel = request.getParameter("groupid");
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	AlertGroup group = analysis.getPega20ById(alertGroupLabel);
%>
<script>
$(function() {

 tableToGrid("#alertgrouptable", {
 	height:350,
 	sortable:true,
 	datatype:'local',
 	 viewrecords: true, 
 	sortorder: "desc", 
 	caption:"PAL Data"}); 

});
	

</script>

<style>
#summary h3 {margin: 0;}
#summary {width:700px; padding:0.5em;}
.palLabel{display:block;float:left;width:350px;font-weight:bold;text-align:left;}
.palValue{text-align:left}
.perInteraction{display:block;float:right;width:200px} 
#individualAlerts{float:right;padding:10px;width:300px;}
.titletoggle
{
	cursor: pointer;
	text-decoration: underline;
}
</style>
 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Alert Group</title>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<div id="individualAlerts" class="ui-widget-content">
	<h3 class="ui-widget-header">Alerts In Group</h3>
	<%
		List<AlertData> theAlerts = group.getAlerts();
		for (AlertData a : theAlerts)
		{ 
	%>
		<a href="AlertDetails.jsp?uniqueInt=<%=a.getUniqueInteraction()%>">View Alert</a> - <%=a.getKpivalue() %> ms<br/>
	<%
		}
	%>
</div>

<div id="summary" class="ui-widget-content">
	<h3 class="ui-widget-header">Alert Group: <%=alertGroupLabel %></h3>
	<p>
	<b>Total Interactions: <%=group.getCountOfAlerts() %></b>
	<br/>
	<br><b>Avg Interaction Time: </b><%=group.getAvgTime() %><br/>
	<br><b>Min Time: </b><%=group.getMin() %><br/>
	<br><b>Max Time: </b><%=group.getMax() %><br/>
	<br><b>STDDEV: </b><%=group.getStdDev() %><br/><br/>
		<b>	<%
		double coefVariance = group.getStdDev() / group.getAvgTime();

		if (coefVariance > .5)
			out.println("This is considered to be a very high variance and implies that something environment may be occasionally contributing to these alerts.<br/>");
		else if (coefVariance > .25)
			out.println("This is considered to be a high variance and implies that something environment may be occasionally contributing to these alerts.<br/>");
		else if (coefVariance < .10)
			out.println("This is considered to be a low variance and implies that these alerts can be easily reproduced.<br/>");
		else
			out.println("This is considered a normal variance.<br/>");

	%></b> 
	<br><b>Connector:</b><%=alertGroupLabel %>
	</p>
	

</div> 
<br/><br/>
<div id="summary" class="ui-widget-content">
	<h3 class="ui-widget-header">Related Alerts for Group</h3> 
<%
	group.setRelatedAlerts(analysis);
	List<AlertData> related = group.getRelatedAlerts();
	HashMap<String, List<AlertData>> groupedRelated = AlertAnalysis.groupByAlertType(related);
	Set<String> rKeys = groupedRelated.keySet();
	if (rKeys!=null && rKeys.size()>0) out.println("Click to expand.");
	
	if (rKeys==null||rKeys.size()==0){
		out.print("<b>No Related Alerts</b>");
	}
	else
	{
		for (String rk : rKeys){
			List<AlertData> tmpRelated = groupedRelated.get(rk);
		%>
		<div class="titletoggle" id="relhdr<%=rk%>" ><b><%=rk %> - <%=tmpRelated.size() %> ocurrences. (<%=AlertDef.getAlertLabel(rk) %>)</b></div>
		<div class="bodytoggle" id="relhdr<%=rk%>" >
		<%
			for (AlertData a : tmpRelated)
			{
		%>
			&nbsp;&nbsp;<%=a.getMsgID() %> - <%=a.getKpivalue() %> <a href="AlertDetails.jsp?uniqueInt=<%=a.getUniqueInteraction() %>">View Alert</a><br/>
		<%		
			}
		%></div><%
		}
	}
	%>
</div>		
<script>
$(document).ready(function(){
	
	$(".bodytoggle").hide();
	$(".titletoggle").click(function(){
		var tmp = $(this).next(".bodytoggle");
		tmp.slideToggle(500);
	
	});

});
</script>
	
</body>
</html>