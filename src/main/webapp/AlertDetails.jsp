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
	String alertID = request.getParameter("uniqueInt");
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	AlertData alert = analysis.getAlertByID(alertID);
	String timezone = analysis.getTimezone();
%>
<script>

</script>

<style>
#summary h3 {margin: 0;}
#summary {width:650px; padding:0.5em;}
#nearby h3 {margin: 0;}
#nearby {width:650px; padding:0.5em;}
.palLabel{display:block;float:left;width:350px;font-weight:bold;text-align:left;}
.palValue{text-align:left}
.perInteraction{display:block;float:right;width:200px} 
.tbllbl{font-weight:bold}

#sql h3 {margin: 0;}
#sql {width:400px; padding:0.5em;}

#pal h3 {margin: 0;}
#pal {width:400px; padding:0.5em;}

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

<div style="float:right">
	<div id="pal" class="ui-widget-content" >
		<h3 class="ui-widget-header">PAL</h3>

<% if (alert.getMsgID().equals("PEGA0001")){  
%>
<script>
$(function() {

 var data = new Array(
 <%
					HashMap<String,PALField> pal1 = alert.getPalFields();
 					double other = 0;
					if (pal1!=null){
						Set<String> keys1 = pal1.keySet();
						if (keys1!=null){
							boolean first = true;
							for (String k:keys1)
							{
								System.out.println(k);
								if ((!k.contains("Unaccounted") && !k.contains("Elapsed")) || pal1.get(k).getPercOfTotal()==0)
									continue;
								String comma = ",";
								if (pal1.get(k).getPercOfTotal()<0.05)
								{
									other+=pal1.get(k).getPercOfTotal()*100;
									continue;
								}
								if (first)
									comma = "";
								first = false;
								%>
							 <%=comma%>['<%=k%>',<%=StringUtil.formatDouble(pal1.get(k).getPercOfTotal()*100) %>]
							 <%}
							if (other>1)
							{
								%>,['Other',<%=StringUtil.formatDouble(other)%>]<%
							}
						}
					}
					
			%>
		);
		var mychart = new JSChart('chartid','pie');
 //var colors = ['#FACC00','#FB9900','#FACC11','#FB9922','#FACC33','#FB9944','#FACC55','#FB9966','#FACC77','#FB9988'];
 mychart.setDataArray(data);
 //mychart.colorizePie(colors);
 mychart.setTitleColor('#857D7D');
 mychart.setTitle('PAL Breakdown - ReqTime: <%=pal1.get("pxTotalReqTime").getValue() %>(s)');
 mychart.draw();
		
		});
</script>		

	<div id="chartid">

	</div>
<%} %>

		<table width="75%">
		
		<%
			HashMap<String,String> counts = new HashMap<String,String>();
			HashMap<String,String> elapsed = new HashMap<String,String>();
			HashMap<String,String> cpu = new HashMap<String,String>();
			HashMap<String,String> other = new HashMap<String,String>();
			
			String pal = alert.getPalData();
			String[] pData = pal.split(";");
			for (String point:pData)
			{
				String[] nameVal = point.split("=");
				if (nameVal == null || nameVal.length<2)
					continue;
				if (nameVal[1]==null || nameVal[1].equals("null"))
					continue;
				if (nameVal[0].contains("Elapsed"))
					elapsed.put(nameVal[0],nameVal[1]);
				else if (nameVal[0].contains("CPU"))
					cpu.put(nameVal[0],nameVal[1]);
				else if (nameVal[0].contains("Count"))
					counts.put(nameVal[0],nameVal[1]);
				else 
					other.put(nameVal[0],nameVal[1]);
			}		
		%>
		<tr><td colspan=2><b>Elapsed Times</b></td></tr>
		<%
			Set<String> keys = elapsed.keySet();	
			for (String k : keys){
		%>
		<tr><td><%=k %> (<a href="#" title="This could explain what this counter is and what valid values are.">?</a>)</td><td><%=elapsed.get(k) %></td></tr>
		<%
			}
		%>
		<tr><td colspan=2><b>Counts</b></td></tr>
		<%
			keys = counts.keySet();	
			for (String k : keys){
		%>
		<tr><td><%=k %> (<a href="#" title="This could explain what this counter is and what valid values are.">?</a>)</td><td><%=counts.get(k) %></td></tr>
		<%
			}
		%>
		<tr><td colspan=2><b>CPU</b></td></tr>
		<%
			keys = cpu.keySet();	
			for (String k : keys){
		%>
		<tr><td><%=k %> (<a href="#" title="This could explain what this counter is and what valid values are.">?</a>)</td><td><%=cpu.get(k) %></td></tr>
		<%
			}
		%>
				<tr><td colspan=2><b>Other</b></td></tr>
		<%
			keys = other.keySet();	
			for (String k : keys){
		%>
		<tr><td><%=k %> (<a href="#" title="This could explain what this counter is and what valid values are.">?</a>)</td><td><%=other.get(k) %></td></tr>
		<%
			}
		%>
		</table>
	</div>
</div>

<div id="summary" class="ui-widget-content">
	<h3 class="ui-widget-header">Alert</h3>

<table width="100%">
<tr>
	<td class="tbllbl" colspan="4">Alert Line</td>
</tr>
<tr>
	<td class="tblval" colspan="4"> 
	<%if (alert.getMsgID().equals("PEGA0005")){ %>
		<div id="fmtsql" >
	<br><b>Formatted SQL:</b><br/>
	<pre><%=StringUtil.sqlFmt(AlertAnalysis.parse05Line(alert.getLine())).trim() %></pre>
	<% if (alert.getInserts()!=null && alert.getInserts().length()>0){ %>
	<b>Inserts:</b><br/> <%=alert.getInserts() %><br/>
	<%} %>
	</div>
	<%} else { %>
	<%=alert.getLine() %>
	<%} %>
	</td>
</tr>
<tr><td>&nbsp</td></tr>
<tr>
	<td class="tbllbl">Alert Type</td><td class="tblval"><%=alert.getMsgID() %> - <%=AlertDef.getAlertLabel(alert.getMsgID()) %></td>
	<td class="tbllbl">Value</td><td class="tblval"><%=alert.getKpivalue() %></td>
</tr>
<tr>
	<td class="tbllbl">First Activity</td><td class="tblval"><%=alert.getFirstAct() %></td>
	<td class="tbllbl">Last Step</td><td class="tblval"><%=alert.getLastStep() %></td>
</tr>
<tr>
	<td class="tbllbl">Workpool</td><td class="tblval"><%=alert.getWorkpool() %></td>
</tr>

<tr>
	<td class="tbllbl">User</td><td class="tblval"><%=alert.getUserID() %></td>
	<td class="tbllbl">RuleApp</td><td class="tblval"><%=alert.getRuleAppName() %></td>
</tr>
<tr>
	<td class="tbllbl">PP Name</td><td class="tblval"><%=alert.getPrimaryPageName() %></td>
	<td class="tbllbl">PP Class</td><td class="tblval"><%=alert.getPrimaryPageClass() %></td>
</tr>
<tr>
	<td class="tbllbl">SP Name</td><td class="tblval"><%=alert.getStepPageName() %></td>
	<td class="tbllbl">SP Class</td><td class="tblval"><%=alert.getStepPageClass()%></td>
</tr>
<tr>
	<td class="tbllbl">Occurred At</td><td class="tblval"><%=DateUtil.formatForTZ(alert.getTheDate(),timezone) %></td>
	<td class="tbllbl">Allow Checkout</td><td class="tblval"><%=alert.getAllowRuleCheckout() %></td>

</tr>
<tr>
	<td class="tbllbl">Requestor</td><td class="tblval" colspan=3><%=alert.getRequestorID() %></td>
</tr>
<tr>
	<td class="tbllbl">Enc Ruleset</td><td class="tblval" colspan=3><%=alert.getEncRulesetList() %></td>
</tr>
</table>
</div>		
<br/><br/>
<div id="summary" class="ui-widget-content">
	<h3 class="ui-widget-header">Related Alerts (Same interaction) </h3> 
	
<%
	alert.setRelatedAlerts(analysis);
	List<AlertData> related = alert.getRelatedAlerts();
	HashMap<String, List<AlertData>> groupedRelated = AlertAnalysis.groupByAlertType(related);
	Set<String> rKeys = groupedRelated.keySet();
	if (rKeys!=null && rKeys.size()>0) out.println("Click to expand.");
	for (String rk : rKeys){
		List<AlertData> tmpRelated = groupedRelated.get(rk);
	%>
	<div id="relhdr<%=rk%>" class="titletoggle"><b><%=rk %> - <%=tmpRelated.size() %> ocurrences. (<%=AlertDef.getAlertLabel(rk) %>)</b></div>
	<div id="rel<%=rk%>" class="bodytoggle">
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
<br/>
<div id="nearby" class="ui-widget-content">
		<h3 class="ui-widget-header">Nearby Alerts (Within 2 minutes) </h3> 
		
	<%
		List<AlertData> nearby = alert.getNearbyAlerts(analysis,2);
		HashMap<String, List<AlertData>> groupedRelated2 = AlertAnalysis.groupByAlertType(nearby);
		Set<String> rKeys2 = groupedRelated2.keySet();
		if (rKeys!=null && rKeys.size()>0) out.println("Click to expand.");
		for (String rk : rKeys2){
			List<AlertData> tmpRelated = groupedRelated2.get(rk);
		%>
		<div class="titletoggle" id="nearhdr<%=rk%>" ><b><%=rk %> - <%=tmpRelated.size() %> ocurrences. (<%=AlertDef.getAlertLabel(rk) %>)</b></div>
		<div class="bodytoggle" id="near<%=rk%>" >
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