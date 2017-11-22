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
url:'ActionServlet?ACTION=PEGA01BYACTION', 
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
	
},

sortable:true,
colNames:['Group Label','Count of Alerts','Avg Alert','Min','Max','Deviation','pxTotalReqTime','Unaccounted For'], 
colModel:[ 
	{name:'groupLabel',label:'Group Label', width:305,key:true,formatter:'showlink',
		formatoptions:{baseLinkUrl:'DD01AlertGroup.jsp',idName:'grouplabel'}},
	{name:'countOfAlerts',width:120,sorttype:'int'},
	{name:'avgTime',width:120,sorttype:'float',formatter:'number'},
	{name:'min',width:120,sorttype:'float',formatter:'number'},
	{name:'max',width:120,sorttype:'float',formatter:'number'},
	{name:'stdDev',width:120,sorttype:'float',formatter:'number'},
	{name:'palData.pxTotalReqTime.value',width:120,sorttype: 'float',formatter:'number'},
	{name:'palData.Unaccounted For.value',width:120, sorttype: 'float',formatter:'number'}
	 ], 
 rowNum:1000, 
 sortname: 'groupLabel', 
 viewrecords: true, 
 sortorder: "desc", 
 caption:"Alerts" }); 

});

</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display Alerts</title>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<table id="alerttable"></table>
</body>
</html>