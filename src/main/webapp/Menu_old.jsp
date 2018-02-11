<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="java.util.*" %>
 <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>

<title>Alert Analysis</title>
<style>
.widget h3 {margin: 0;}
#widget {width:800px; padding:0.5em; padding-left:10px;}
</style>
<script>
$(document).ready(function(){

	/* Adding a colortip to any tag with a title attribute: */

	$('.tipText').colorTip({color:'black'});

});
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<%
	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
	String timezone = analysis.getTimezone();
	if (analysis!=null){
		String pega01Filter = (String)request.getSession().getAttribute("PEGA01THRESHOLD");
		if (pega01Filter==null) 
			pega01Filter = "None";
		Date timeFilterDate = (Date)request.getSession().getAttribute("STARTTIME");
		String timeFilter;
		if (timeFilterDate!=null)
			timeFilter = DateUtil.formatForTZ(((Date)request.getSession().getAttribute("STARTTIME")),timezone)+ 
			" to "+DateUtil.formatForTZ(((Date)request.getSession().getAttribute("ENDTIME")),timezone);
		else
			timeFilter = "None";
		String appFilter = (String)request.getSession().getAttribute("APPLICATIONFILTER");
		if (appFilter == null || appFilter.length()==0)
			appFilter = "All Apps";
		String nodeFilter = (String)request.getSession().getAttribute("NODEFILTER");
		if (nodeFilter == null || nodeFilter.length()==0)
			nodeFilter = "All Nodes";
		
%>
<br/>
<div id="summary" class="ui-widget-content widget">
	<h3 class="ui-widget-header">Menu</h3>
	<br/>
	<div id="filter" style="float:right; padding-right:400px;">
<b>Filters:</b><br/>
Pega01 Filter: <%=pega01Filter %><br/>
Time Filter: <%=timeFilter %><br/> 
App Filter: <%=appFilter %> <br/>
Node Filter: <%=nodeFilter %> <br/>
<a href="ActionServlet?ACTION=CLEARFILTERS">Clear Filters</a><br/>
</div>
<div id="menuitems">
<a href="UploadFile.jsp">Upload File</a><br/>

<a href="SetThreshold.jsp">Set New Threshold</a><br/>
<a href="SetApplication.jsp">Set Application</a><br/>
<a href="SetNode.jsp">Set Node</a><br/>
<a href="Display01ActionGroups.jsp">Pega0001 DrillDown</a><br/>
<a href="Display05Groups.jsp">Pega0005 DrillDown</a><br/>
<a href="Display20Groups.jsp">Pega0020 DrillDown</a><br/><br/>
</div>
<%
	}
	else
	{
		response.sendRedirect("UploadFile.jsp");
		return;
	}
	String msg = request.getParameter("msg");
	if (msg == null) msg = "";
%><span style="color:red"><%=msg %></span>
</div>
<br/><br/>
<% if (analysis!=null){
		HashMap<String, List<AlertData>> alertsByType = analysis.getAllAlertsByType();
		double durationInMin = (analysis.getLastAlert().getTime()-analysis.getFirstAlert().getTime())/(1000*60);
		%>
<div class="ui-widget-content widget">
	<h3 class="ui-widget-header">Summary</h3>
	<div style="float:right;padding-right:10px;">
	<h2>Timeline</h2>
	<div id="timeline" style="width:500px; height:375px;"></div>
	<div style="width:100%;text-align:center"><i>Filter time range by moving timeline window.</i>
		<br>You can reset the timeframe to the full window by <a href="ActionServlet?ACTION=CLEARFILTERS">clicking here.</a><br/></div>
	</div>
	<h2>Summary</h2>
	<span style="font-size:130%"><b>
		<div  class="tipText" title="Time over KPI for PEGA0001 alerts / duration">Response Time Alert Impact %:</div>
		 &nbsp;&nbsp;<%=StringUtil.formatDouble(analysis.getAlertImpactPct()) %>%</b><br/><br/></span>
	
	<b>First Alert: <%=DateUtil.formatForTZ(analysis.getFirstAlert(),timezone) %></b><br/>
	<b>Last Alert: <%=DateUtil.formatForTZ(analysis.getLastAlert(),timezone) %></b><br/>
	<b>Duration: <%=StringUtil.formatDouble(durationInMin/60) %> (hrs) </b><br/><br/>
	<h2 >Alert Counts 
		<div class="tipText" title="These links provide access to the raw alerts,<br/> 
		use the drilldowns for more intuitive views.">(?)</div></h2>
	<%
	double totalTime = 0;
	double queryTime = 0;
	double connectTime = 0;
	double declarativeTime = 0;
	double commitTime = 0;
	double activityTime = 0;
	double fuaTime = 0;
	for (int i = 0; i < 60; i++)
	{
		String id = "PEGA00";
		if ( i<10)
			id+="0"+i;
		else
			id+=""+i;
		if (alertsByType.containsKey(id)){
			AlertGroup grp = new AlertGroup();
			grp.setAlerts(alertsByType.get(id));
			if (i==1)
			{
				totalTime = grp.getTotalTime()/1000;
				try{
					grp.setPalData(AlertAnalysis.summarizePAL(grp.getAlerts()));
				//	grp.printPAL();
					//pal fields already in seconds
					System.out.println(grp.getPalData().get("Unaccounted For").getValue());
					try{declarativeTime = grp.getPalData().get("pxDeclarativeRulesInvokedElapsed").getValue(); } catch (Exception e){}
					try{activityTime = grp.getPalData().get("Unaccounted For").getValue(); } catch (Exception e){}
					try{activityTime += grp.getPalData().get("pxJavaStepElapsed").getValue(); } catch (Exception e){}
					try{commitTime = grp.getPalData().get("pxCommitElapsed").getValue(); } catch (Exception e){}
					try{queryTime = grp.getPalData().get("pxOtherIOElapsed").getValue(); } catch (Exception e){}
					try{connectTime = grp.getPalData().get("pxConnectElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxRuleBrowseElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxJavaCompileElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxJavaAssembleElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxDeclarativeRulesLookupElapsed").getValue(); } catch (Exception e){}
					
				}
				catch (Exception e)
				{
					declarativeTime = 0;
				}
			}

	%>
		<b><a href="AlertsByTypeTable.jsp?AlertType=<%=id %>"><%=id %></a> <%=AlertDef.getAlertLabel(id) %></b> - <%=alertsByType.get(id).size() %> occurrences.<br/>
	<% 
		}
	}
	%>
	<%
	double dbTotal = commitTime + queryTime;
	int dbPct = (int)(100*(dbTotal/totalTime));
	int extPct = (int)(100*(connectTime/totalTime));
	int declPct = (int)(100*(declarativeTime/totalTime));
	int fuaPct =  (int) (100*(fuaTime/totalTime));
	int actPct = (int) (100*(activityTime/totalTime));
	%>
	<h2>Key Indicators</h2>
	<i>Note: This data is derived from PAL counters from Pega0001 alerts.<br/>Activity time contains all time that is not accounted for in PEGA0001 alerts plus java step time.</i><br/>
	<div id="chart_div"></div>
	<br/><br/><b>
	<table>
	<tr><td>Total HTTP time:</td><td> <%=StringUtil.formatDouble(totalTime) %> (s)</td></tr>
	<tr><td>Total DB time:</td><td> <%=StringUtil.formatDouble(dbTotal) %> (s)</td></tr>
	<tr><td>Total Connect time:</td><td> <%=StringUtil.formatDouble(connectTime) %> (s)</td></tr>
	<tr><td>Total Decl time:</td><td> <%=StringUtil.formatDouble(declarativeTime) %> (s)</td></tr>
	<tr><td>Total First Use time:</td><td> <%=StringUtil.formatDouble(fuaTime) %> (s)</td></tr>
	<tr><td>Total Activity time:</td><td> <%=StringUtil.formatDouble(activityTime) %> (s) </td></tr>
	</table>
	</b>
	<script type='text/javascript' src='https://www.google.com/jsapi'></script>
    <script type='text/javascript'>
      google.load('visualization', '1', {packages:['gauge']});
      google.load('visualization', '1', {'packages':['annotatedtimeline']});
   
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Label');
        data.addColumn('number', 'Value');
        data.addRows(5);
        data.setValue(0, 0, 'DB');
        data.setValue(0, 1, <%=dbPct%>);
        data.setValue(1, 0, 'External');
        data.setValue(1, 1, <%=extPct%>);
		data.setValue(2, 0, 'Declarative');
        data.setValue(2, 1, <%=declPct%>);
        data.setValue(3, 0, 'FirstUse');
        data.setValue(3, 1, <%=fuaPct%>);
        data.setValue(4, 0, 'Activity');
        data.setValue(4, 1, <%=actPct%>);
        
        var chart = new google.visualization.Gauge(document.getElementById('chart_div'));
        var options = {width: 500, height: 200, redFrom: 50, redTo: 100,
            yellowFrom:30, yellowTo: 50, minorTicks: 5};
        chart.draw(data, options);
        drawVisualization();
      }
      
      function drawVisualization() {
      	  var data = new google.visualization.DataTable();
		  data.addColumn('datetime', 'Hour');
		  data.addColumn('number', 'Alert Count');
		  data.addColumn('number', 'Alert Impact%');
		  <%
		  	HashMap<Integer,List<AlertData>> alertsByHour = analysis.getAlertsByHour();
		  %>
		  data.addRows(<%=alertsByHour.size()%>);
		  <%
		  	int rowNum = 0;
		  	for (int i = 0 ; i < 1000; i++)
		  	{
		  		List<AlertData> hoursAlerts = alertsByHour.get(i);
		  		if (hoursAlerts == null)
		  			continue;
		  		double tPut = analysis.getAlertImpactPct(hoursAlerts,1);
		  		Calendar cal = Calendar.getInstance();
		  		cal.setTime(analysis.getFirstAlert());
		  		cal.setTimeZone(TimeZone.getTimeZone(timezone));
		  %>
		  data.setValue(<%=rowNum%>, 0, new Date(<%=cal.get(Calendar.YEAR)%>, <%=cal.get(Calendar.MONTH)%> ,<%=cal.get(Calendar.DAY_OF_MONTH)%>,<%=cal.get(Calendar.HOUR_OF_DAY)+i%>,0,0));
		  data.setValue(<%=rowNum%>, 1, <%=hoursAlerts.size()%>);
		  data.setValue(<%=rowNum%>, 2, <%=tPut%>);
		  
		  <%
		  	rowNum++; 
		  	}
		  %>
		  
		  var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
		      document.getElementById('timeline'));
		   annotatedtimeline.draw(data, {'displayAnnotations': false,displayZoomButtons: false,'scaleColumns':[0,1],   'scaleType': 'allmaximized'});
		  google.visualization.events.addListener(annotatedtimeline,'rangechange',function(){
		  	var range = annotatedtimeline.getVisibleChartRange();
		  	var start = range.start;
		  	var end = range.end;
		  	window.location.href = "ActionServlet?ACTION=SETTIMERANGE&S="+start+"&E="+end;
		  });
	}
      
      
    </script>

	<br/><br/>
	
</div>	
<%  } %>

<br/><b><%=msg%></b>
</body>
</html>