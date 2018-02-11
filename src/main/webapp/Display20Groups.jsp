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
%>
<script>



$(function() {

jQuery("#alerttable").jqGrid({ 
url:'ActionServlet?ACTION=PEGA20GROUPS', 
datatype: "json", 
height:800,
width:1200,
autoWidth:true,
jsonReader : {
  id: 'groupLabel',
   repeatitems: false
  
},
loadComplete : function () {
	var dataArr = $("#alerttable").getRowData();
	$("#alerttable").setGridParam({datatype:'local'});
	$("#alerttable").setGridParam({data:dataArr});
	$("#alerttable").hideCol('alertID');
},

sortable:true,
colNames:['Group Label','Count of Alerts','Total Time','Avg Time','Min','Max', 'Stddev','AlertID'], 
colModel:[ 
	{name:'groupLabel',label:'Group Label', width:700,key:true,formatter:mylink,unformat:unlink},
	{name:'countOfAlerts',width:100,sorttype:'int'},
	{name:'totalTime',width:100,sorttype:'float',formatter:'number'},
	{name:'avgTime',width:100,sorttype:'float',formatter:'number'},
	{name:'min',width:120,sorttype:'float',formatter:'number'},
	{name:'max',width:120,sorttype:'float',formatter:'number'},
	
	{name:'stdDev',width:120,sorttype:'float',formatter:'number'},
	{name:'alertID',width:0}
	], 
 rowNum:1000, 
 sortname: 'groupLabel',  
 viewrecords: true, 
 sortorder: "desc", 
 caption:"Connector Alerts" }); 

});

function mylink ( cellvalue, options, rowObject )
{
	var val;
	if (rowObject.groupLabel)
		val = rowObject.groupLabel;
	else
		val = cellvalue;
	var thelink = '<a href="DD20AlertGroup.jsp?groupid='+rowObject.alertID+'">'+val+'</a>';
	return thelink;
}

function unlink( cellvalue, options, cellObject )
{
	return cellvalue;
}

</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display PEGA20(Connector) Alerts</title>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<table id="alerttable"></table>
</body>
</html>