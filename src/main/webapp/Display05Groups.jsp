<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.*" %>
 <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>

<script>
<%
if (!SessUtil.requireAlerts(request,response))
	return;
	//for removing constants
	String remCon = request.getParameter("REMCON");
	if (remCon!=null)
		remCon = "REMCON";
	else 
		remCon = "";
%>


$(function() {

$("#groupbtn").button({});

$("#groupbtn").click(function(){
	
	var loc = $("#groupbtn").attr('href');
	window.location.href = loc;

});

jQuery("#alerttable").jqGrid({ 
url:'ActionServlet?ACTION=PEGA05GROUPS<%=remCon%>', 
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
	{name:'alertID',width:0}], 
	
 rowNum:1000, 
 sortname: 'groupLabel', 
 viewrecords: true, 
 sortorder: "desc", 
 caption:"Alerts" }); 

});

function myEscape(mystring){
	return mystring.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;")
}

function mylink ( cellvalue, options, rowObject )
{
	var val;
	if (rowObject.groupLabel)
		val = rowObject.groupLabel;
	else
		val = cellvalue;
	val = myEscape(val);
	var thelink = '<a href="DD05AlertGroup.jsp?groupid='+rowObject.alertID+'">'+val+'</a>';
	return thelink;
}

function unlink( cellvalue, options, cellObject )
{
	return cellvalue;
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display PEGA05 Alerts</title>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<% if (remCon.length()==0){ %>
<button id="groupbtn" href="Display05Groups.jsp?REMCON=remcon">Group SQL</button><br/>
<% } else { %>
<button id="groupbtn" href="Display05Groups.jsp">Un-Group SQL</button><br/>
<%} %>
<table id="alerttable"></table>
</body>
</html>