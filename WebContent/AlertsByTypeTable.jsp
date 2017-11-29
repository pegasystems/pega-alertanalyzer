<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.*" %>
 <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>

<%
	String alertType = request.getParameter("AlertType");
	String all = request.getParameter("ALL");
	if (all==null)
		all="";
if (!SessUtil.requireAlerts(request,response))
	return;
AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");

%>
<script>

function percentFmt ( cellvalue, options, rowObject )
{
	if (cellvalue)
	// format the cellvalue to new format
	{
		return roundNumber((cellvalue*100),2)+"%";
	}
	else
	{
		return "0%";
	}
}

function roundNumber(num, dec) {
	var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
	return result;
}

function alertLink(cellvalue,options,rowObject)
{
	return '<a href="AlertDetails.jsp?uniqueInt='+rowObject.uniqueInteraction+'">View Alert Details</a>';
}



function unlink( cellvalue, options, cellObject )
{
	//<a href="AlertDetails.jsp?uniqueInt=915_1296770232569">View Alert Details</a>
	var h = cellObject.innerHTML;
	var startIdx = h.indexOf('?uniqueInt=')+11;
	var endIdx = h.indexOf('">');
	var r = h.substring(startIdx,endIdx);
	return r;
}


$(function() {

jQuery("#alerttable").jqGrid({ 
url:'ActionServlet?ACTION=ALERTSBYTYPE&AlertType=<%=alertType%>&ALL=<%=all%>', 
datatype: "json", 
height:800,
autoWidth:true,
jsonReader : {
  id: 'groupLabel',
   repeatitems: false
  
},
loadComplete : function () {
	var dataArr = $("#alerttable").getRowData();
	$("#alerttable").setGridParam({datatype:'local'});
	$("#alerttable").setGridParam({data:dataArr});
	
},

sortable:true,
colNames:['View Alert','Time','User','WorkPool','Application','Value'], 
colModel:[ 
	{name:'uniqueInteraction',formatter:alertLink,unformat:unlink},
	{name:'displayDate', width:200},
	{name:'userID',width:150,sorttype:'string'},
	{name:'workpool',width:220,sorttype: 'string'},
	{name:'ruleAppName',width:220,sorttype: 'string'},
	{name:'kpivalue',width:100, sorttype: 'float',formatter:'number'}
	
	 ], 
 rowNum:1000, 
 sortname: 'groupLabel', 
 viewrecords: true, 
 sortorder: "desc", 
 caption:"<%=alertType%> Alerts - <%=AlertDef.getAlertLabel(alertType)%>" 
 }); 

});

</script>
<style>
#warning {

	margin-top:5px;
	margin-bottom:10px;
	padding:5px;
	color:red;
	font-weight:bold;
	border: 1px solid red;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display Alerts</title>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<%
	int count = analysis.getAllAlertsByType().get(alertType).size();
%>
<% if(all!=null && all.equalsIgnoreCase("ALL")){ %>
<i>Showing all entries.  <a href="AlertsByTypeTable.jsp?AlertType=<%=alertType %>">Click here to only show 100.</a><br/><br/></i>
<%}else{ %>
<i>For Performance Reasons Only 100 of <%=count %> Alerts are Shown</i>(<a href="AlertsByTypeTable.jsp?AlertType=<%=alertType %>&ALL=ALL">Show All</a>)<br/><br/>
<%} %>
<%
	if (alertType.equalsIgnoreCase("PEGA0001") || alertType.equalsIgnoreCase("PEGA0005") || alertType.equalsIgnoreCase("PEGA0020")){
		String link = "";
		if (alertType.equalsIgnoreCase("PEGA0001"))
			link = "Display01ActionGroups.jsp";
		else if (alertType.equalsIgnoreCase("PEGA0005"))
			link = "Display05Groups.jsp";
		else if (alertType.equalsIgnoreCase("PEGA0020"))
			link="Display20Groups.jsp";
%>
<div id="warning">Warning: You are using the generic alert viewer, for this alert type a more specialized drill down can be used.  
<a href="<%=link %>">Click here for drilldown.</a></div>
<%} %>
<table id="alerttable"></table>
</body>
</html>