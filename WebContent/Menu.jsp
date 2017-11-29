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

	AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");

	if (analysis==null)
	{
		response.sendRedirect("UploadFile.jsp?INVSESSION=true&fromMenu");
		return;
	}
	String timezone = analysis.getTimezone();
	String pega001ThresholdFilter = (String)request.getSession().getAttribute("PEGA01THRESHOLD");
	if (pega001ThresholdFilter==null) 
		pega001ThresholdFilter = "default";
	Date timeFilterDate = (Date)request.getSession().getAttribute("STARTTIME");
	String timeFilter;
	if (timeFilterDate!=null)
		timeFilter = DateUtil.formatForTZ(((Date)request.getSession().getAttribute("STARTTIME")),timezone)+ 
		" to "+DateUtil.formatForTZ(((Date)request.getSession().getAttribute("ENDTIME")),timezone);
	else
		timeFilter = "None";
	String appFilter = (String)request.getSession().getAttribute("APPLICATIONFILTER");
	if (appFilter == null || appFilter.length()==0)
		appFilter = "ALL";
	String userFilter = (String)request.getSession().getAttribute("USERFILTER");
	if (userFilter == null || userFilter.length()==0)
		userFilter = "ALL";
	
	String nodeFilter = (String)request.getSession().getAttribute("NODEFILTER");
	if (nodeFilter == null || nodeFilter.length()==0)
		nodeFilter = "ALL";
	
	boolean noAlerts = false;
	
	HashMap<String, List<AlertData>> alertsByType = analysis.getAllAlertsByType();
	double durationInMin = 0;
	try{
		durationInMin = (analysis.getLastAlert().getTime()-analysis.getFirstAlert().getTime())/(1000*60);
	}catch (Exception e){
		noAlerts = true;
	}
		
%>


<title>Alert Analysis</title>
<style>
.widget h3 {margin: 0;}
#widget {width:800px; padding:0.5em; padding-left:10px;} 
#DD01 .ui-button-text{ padding: 1px 2px}
#DD05 .ui-button-text{ padding: 1px 2px}
#DD20 .ui-button-text{ padding: 1px 2px}
#noalert {
	margin-top:50px;
	border:1px solid red;
	font-weight: bold;
	font-size:32px;
	padding:5px;
}
</style>
<script>
$(document).ready(function(){

	/* Adding a colortip to any tag with a title attribute: */

	$('.tipText').colorTip({color:'black'});
	
	
	
	$("#selectalert").change(function () {
		var alertType = $("#selectalert option:selected").attr('value');
		$("#selectalert").val("");
		if (alertType!="")
			openAlertByType(alertType);
	
	});

	$("#DD01").button({});	
	$("#DD05").button({});
	$("#DD20").button({});
	
	$("#DD01").click(function(){window.location.href = 'Display01ActionGroups.jsp';});
	$("#DD05").click(function(){window.location.href = 'Display05Groups.jsp';});
	$("#DD20").click(function(){window.location.href = 'Display20Groups.jsp';});
	

});


function openAlertByType(alertType)
{
	window.location.href='AlertsByTypeTable.jsp?AlertType='+alertType;
}
</script>
</head>
<body>
<jsp:include page="I_Top.jsp"/>

<div id="menuright">
	<a href="UploadFile.jsp">Upload File</a> | 
	<a href="SetThreshold.jsp">Set New Threshold</a> |
	<a href="SetUser.jsp">Set User</a> |
	<a href="SetApplication.jsp">Set Application</a> |
	<a href="SetNode.jsp">Set Node</a> |
	<a href="ActionServlet?ACTION=CLEARFILTERS">Clear Filters</a>
</div>
<% if (noAlerts){ %>
<div id="noalert">
No alerts are available for this set of filters.  Please <a href="ActionServlet?ACTION=CLEARFILTERS">Reset Filters</a>.
</div>
<% } else { %>
<div id="keyindicators">
<h1>Key Indicators</h1>
<%
	double totalTime = 0;
	double queryTime = 0;
	double rdbioTime = 0;
	double connectTime = 0;
	double declarativeTime = 0;
	double commitTime = 0;
	double activityTime = 0;
	double fuaTime = 0;
	double totalReqCPU = 0;
	double otherBrowseTime = 0;
	for (int i = 0; i < 40; i++)
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
					try{rdbioTime = grp.getPalData().get("pxRDBIOElapsed").getValue(); } catch (Exception e){}
					try{otherBrowseTime = grp.getPalData().get("pxOtherBrowseElapsed").getValue(); } catch (Exception e){}
					try{connectTime = grp.getPalData().get("pxConnectElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxRuleBrowseElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxJavaCompileElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxJavaAssembleElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxDeclarativeRulesLookupElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxRuleIOElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxInferGeneratedJavaElapsed").getValue(); } catch (Exception e){}
					try{fuaTime += grp.getPalData().get("pxInferGeneratedJavaHLElapsed").getValue(); } catch (Exception e){}
					try{totalReqCPU += grp.getPalData().get("pxTotalReqCPU").getValue(); } catch (Exception e){}
					
				}
				catch (Exception e)
				{
					declarativeTime = 0;
				}
			}
		}
	}
	%>
	<%
	double dbTotal = commitTime + queryTime+rdbioTime+ otherBrowseTime;
	int dbPct = (int)(100*(dbTotal/totalTime));
	int extPct = (int)(100*(connectTime/totalTime));
	int declPct = (int)(100*(declarativeTime/totalTime));
	int fuaPct =  (int) (100*(fuaTime/totalTime));
	int actPct = (int) (100*(activityTime/totalTime));
	int cpuPct = (int) (100*(totalReqCPU/totalTime));
	%>
	<div id="keyindvis" > </div>
	<br/><br/><b>
	<table>
	<tr><td>Total HTTP time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(totalTime) %> (s)</td><td><button id="DD01">Drilldown</button></td></tr>
	<tr><td>Total DB time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(dbTotal) %> (s)</td><td><button id="DD05">Drilldown</button></td></tr>
	<tr><td>Total Connect time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(connectTime) %> (s)</td><td><button id="DD20">Drilldown</button></td></tr>
	<tr><td>Total Decl time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(declarativeTime) %> (s)</td></tr>
	<tr><td>Total First Use time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(fuaTime) %> (s)</td></tr>
	<tr><td>Total CPU time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(totalReqCPU) %> (s)</td></tr>
	<tr><td>Total Activity time:</td><td style="text-align:right;font-weight:normal;"> <%=StringUtil.formatDouble(activityTime) %> (s)</td></tr>
	</table>
	</b>
	<br/><br/>
	<span style="font-size:85%">
		<B>Note:</B> This data is derived from PAL counters from Pega0001 alerts.<br/>Activity time contains all time that is not accounted for in PEGA0001 alerts plus java step time.<br/>
	</span>
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
        data.setValue(4, 0, 'CPU');
        data.setValue(4, 1, <%=cpuPct%>);
        
        var chart = new google.visualization.Gauge(document.getElementById('keyindvis'));
        var options = {width: 450, height: 180, redFrom: 50, redTo: 100,
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
		  data.addRows(<%=alertsByHour.size()%>+1);
		  <%
		  	int rowNum = 0;
		  	Calendar lastAlert = null;
		  	int lastHour = 0;
		  	for (int i = 0 ; i < 1000; i++)
		  	{
		  		List<AlertData> hoursAlerts = alertsByHour.get(i);
		  		if (hoursAlerts == null)
		  			continue;
		  		double tPut = analysis.getAlertImpactPct(hoursAlerts,1);
		  		Calendar cal = Calendar.getInstance();
		  		cal.setTime(analysis.getFirstAlert());
		  		cal.setTimeZone(TimeZone.getTimeZone(timezone));
		  		lastAlert = cal;
		  		lastHour = i;
		  %>
		  data.setValue(<%=rowNum%>, 0, new Date(<%=cal.get(Calendar.YEAR)%>, <%=cal.get(Calendar.MONTH)%> ,<%=cal.get(Calendar.DAY_OF_MONTH)%>,<%=cal.get(Calendar.HOUR_OF_DAY)+i%>,0,0));
		  data.setValue(<%=rowNum%>, 1, <%=hoursAlerts.size()%>);
		  data.setValue(<%=rowNum%>, 2, <%=tPut%>);
		 
		  
		  <%
		  	rowNum++; 
		  	}
		  %>
		  
		  data.setValue(<%=rowNum%>, 0, new Date(<%=lastAlert.get(Calendar.YEAR)%>, <%=lastAlert.get(Calendar.MONTH)%> ,<%=lastAlert.get(Calendar.DAY_OF_MONTH)%>,<%=lastAlert.get(Calendar.HOUR_OF_DAY)+(lastHour+1)%>,0,0));
		  data.setValue(<%=rowNum%>, 1, 0);
		  data.setValue(<%=rowNum%>, 2, 0);
		  
		  var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
		      document.getElementById('timelinevis'));
		   annotatedtimeline.draw(data, {'displayAnnotations': false,displayZoomButtons: false,'scaleColumns':[0,1],   'scaleType': 'allmaximized'});
		  google.visualization.events.addListener(annotatedtimeline,'rangechange',function(){
		  	var range = annotatedtimeline.getVisibleChartRange();
		  	var start = range.start;
		  	var end = range.end;
		  	window.location.href = "ActionServlet?ACTION=SETTIMERANGE&S="+start+"&E="+end;
		  });
	}
      
      
    </script>
    <div id="alertcounts" style="padding-top:10px;">
<h1>Alert Counts</h1>
<select id="selectalert" name="selectalert" style="width:400px;">
<option value="" selected>Alert Counts</option>
<%
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
%>
<option value="<%=id %>"><%=id %> (<%=alertsByType.get(id).size() %>) <%=AlertDef.getAlertLabel(id) %></option>
<%
	}
}
%>
</select>
</div>
</div>
<!-- end key indicators -->

<div id="summary">
<h1>Summary</h1>
<div id="timelinevis"  style="margin-left:10px;width:600px; height:300px;"></div>
<div id="clrtimeline" style="float:right; text-align:right;padding-right:20px;width:200px;font-size:115%;"><a href="ActionServlet?ACTION=CLEARFILTERS">Reset Timeline</a></div>
<span style="padding-left:10px; font-size:115%">Filter time range by moving timeline below.</span>

<table width="89%" style="margin-left:10px;font-size:105%;margin-top:20px;">
<tr><td width="40%" class="tipText" title="Time over KPI for PEGA0001 alerts / duration" style="font-size:115%"><b>Alert Impact %:</b></td>
<td style="font-size:115%;font-weight:bold;text-align:right"><%=StringUtil.formatDouble(analysis.getAlertImpactPct()) %></td>
</tr>
	
<tr><td><b>First Alert:</td><td style="text-align:right"><%=DateUtil.formatForTZ(analysis.getFirstAlert(),timezone) %></td></tr>
<tr><td><b>Last Alert:</td><td style="text-align:right"><%=DateUtil.formatForTZ(analysis.getLastAlert(),timezone) %></td></tr>
<tr><td><b>Duration:</td><td style="text-align:right"><%=StringUtil.formatDouble(durationInMin/60) %> (hrs)</td></tr>
<tr><td><b>Filters:</td><td style="text-align:right">
Threshold: <%=pega001ThresholdFilter %>;&nbsp
User: <%=userFilter %>;&nbsp
App: <%=appFilter %>;&nbsp
Node: <%=nodeFilter %>;&nbsp
</td></tr>

</table>
</div>

<h1>Recommendations</h1>
<ol id="rec">
	<li>Filter by time period and application of interest.</li>
	<li>Use the drilldown buttons to further determine where time is spent.</li>
</ol>

<%} %>


</body>
</html>