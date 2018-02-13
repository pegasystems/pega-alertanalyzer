<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.*" %>
 <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>
<script src="js/jscharts.js" 					type="text/javascript"/></script>
 
<%
	if (!SessUtil.requireAlerts(request,response))
		return;
	String alertGroupLabel = request.getParameter("grouplabel");
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	AlertGroup group = analysis.getAlertGroup(alertGroupLabel);
%>
<script>
$(function() {

 var data = new Array(
 	<%
		HashMap<String,PALField> pal1 = group.getPalData();
		Set<String> keys1 = pal1.keySet();
		boolean first = true;
		double other = 0;
		for (String k:keys1)
		{
			if (k.equalsIgnoreCase("pxtotalreqtime")||pal1.get(k).getPercOfTotal()==0)
				continue;
			if (pal1.get(k).getPercOfTotal()<0.05)
			{
				other+=pal1.get(k).getPercOfTotal()*100;
				continue;
			}
			String comma = ",";
		
			if (first)
				comma = "";
			first = false;
	%>
 <%=comma%>['<%=k%>',<%=StringUtil.formatDouble(pal1.get(k).getPercOfTotal()*100) %>]
 <%}
 	if (other>1){
 		%>,['Other',<%=StringUtil.formatDouble(other)%>]<%
 	}
 	%>
 );
 var mychart = new JSChart('chartid','pie');
 //var colors = ['#FACC00','#FB9900','#FACC11','#FB9922','#FACC33','#FB9944','#FACC55','#FB9966','#FACC77','#FB9988'];
 mychart.setDataArray(data);
 //mychart.colorizePie(colors);
 mychart.setTitleColor('#857D7D');
 mychart.setTitle('PAL Breakdown - Total time: <%=(int)(group.getTotalTime()/1000)%> (s)');
 mychart.draw();
 
 tableToGrid("#alertgrouptable", {
 	height:350,
 	sortable:true,
 	datatype:'local',
 	 viewrecords: true, 
 	sortorder: "desc", 
 	caption:"PAL Data (times are in seconds)"}); 

});
</script>

<style>
#summary h3 {margin: 0;}
#summary {width:800px; padding:0.5em;}
.palLabel{display:block;float:left;width:350px;font-weight:bold;text-align:left;}
.palValue{text-align:left}
.perInteraction{display:block;float:right;width:200px} 
.titletoggle
{
	cursor: pointer;
	text-decoration: underline;
}
</style>
 <script>

 </script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Alert Group</title>
</head>
<body>
<%

%>
<jsp:include page="I_Top.jsp"/>
<div id="summary" class="ui-widget-content">
	<h3 class="ui-widget-header">Alert Group: <%=alertGroupLabel %></h3>
	<div style="float:right" id="chartid"></div>
	<p>
	<b>Total Interactions: <%=group.getCountOfAlerts() %> (<a href="Alert01GroupTable.jsp?group=<%=alertGroupLabel %>">View Individual Alerts</a>)</b>
	<br/>
	<br><b>Avg Interaction Time: </b><%=StringUtil.formatDouble(group.getAvgTime()) %> ms<br/>
	<br><b>Min Time: </b><%=StringUtil.formatDouble(group.getMin()) %> ms<br/>
	<br><b>Max Time: </b><%=StringUtil.formatDouble(group.getMax()) %> ms<br/>
	<br><b>STDDEV: </b><%=StringUtil.formatDouble(group.getStdDev()) %> ms<br/>
	<b>	<%
		double coefVariance = group.getStdDev() / group.getAvgTime();

		if (coefVariance > .5)
			out.println("This is considered to be a very high variance and implies that something environment may be occasionally contributing to these alerts.");
		else if (coefVariance > .25)
			out.println("This is considered to be a high variance and implies that something environment may be occasionally contributing to these alerts.");
		else if (coefVariance < .10)
			out.println("This is considered to be a low variance and implies that these alerts can be easily reproduced.");
		else
			out.println("This is considered a normal variance.");

	%>
	</b>
	</p>
	<div style="clear:both;"></div>

<table id="alertgrouptable">
	<thead>
	<tr>
		<th>PAL Reading</th><th>Value</th><th>Per Interaction</th><th>Perc Total</th>
	</tr>
	</thead>
	<tbody>
	<%
		HashMap<String,PALField> pal = group.getPalData();
		Set<String> keys = pal.keySet();
		for (String k:keys)
		{
		
		
	%>
	<tr>
		<td><%=k %></td>
		<td><%=StringUtil.formatDouble(pal.get(k).getValue()) %></td>
		<td><%=StringUtil.formatDouble(pal.get(k).getNormalizedValue()) %></td>
		<td><%=StringUtil.formatDouble(pal.get(k).getPercOfTotal()*100) %>%</td>
	</tr>
	<%} %>
	</tbody>	
	</table>
		<p><i>This is the accumalted PAL data for all PEGA0001s for this activity</i></p>
</div>
<br/><br/>
<div id="summary" class="ui-widget-content">
	<h3 class="ui-widget-header">Related Alerts for Group (Same Interactions)</h3> 
	
<%
	group.setRelatedAlerts(analysis);
	List<AlertData> related = group.getRelatedAlerts();
	HashMap<String, List<AlertData>> groupedRelated = AlertAnalysis.groupByAlertType(related);
	Set<String> rKeys = groupedRelated.keySet();
	if (rKeys!=null && rKeys.size()>0) out.println("Click to expand.");
	for (String rk : rKeys){
		List<AlertData> tmpRelated = groupedRelated.get(rk);
	%>
	
	<div class="titletoggle" id="relhdr<%=rk%>" ><b><%=rk %> - <%=tmpRelated.size() %> ocurrences. (<%=AlertDef.getAlertLabel(rk) %>)</b><br/></div>
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