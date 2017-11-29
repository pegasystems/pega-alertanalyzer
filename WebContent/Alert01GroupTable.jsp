<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.*" %>
 <%@ page import="com.pega.gsea.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>

<%
	String groupid = request.getParameter("group");
	if (!SessUtil.requireAlerts(request,response))
		return;
%>
<script>

function percentFmt ( cellvalue, options, rowObject )
{
	if (cellvalue && cellvalue>0)
	// format the cellvalue to new format
	{
		return roundNumber((cellvalue*100),2)+"%";
	}
	else
	{
		return "0%";
	}
}

function unPercent(cellvalue, options, cellObject )
{
	var p =  cellvalue.substring(0,cellvalue.length-1);
	if (p==0)
		return 0;
	return p/100;
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
url:'ActionServlet?ACTION=ALERTGROUPLIST&group=<%=groupid%>', 
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
colNames:['View Alert','AlertID','User','WorkPool','Value','Missing Time%','CPU%','Connect %','OtherIO %','RDBIO %','OtherBrowse%','Commit%','Decl Exec %','%Decl Lookup%','Rule Browse%','Rule IO%','Java Step%','Java ASM %','Compile %','InferGeneratedJava%', 'InferGeneratedJavaHL%'], 
colModel:[ 
	{name:'uniqueInteraction',formatter:alertLink,unformat:unlink},
	{name:'msgID', width:80},
	{name:'userID',width:120,sorttype:'string'},
	{name:'workpool',width:175,sorttype: 'string'},
	{name:'kpivalue',width:120, sorttype: 'float',formatter:'number'},
	{name:'palFields.Unaccounted For.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxTotalReqCPU.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxConnectElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent}, 
	{name:'palFields.pxOtherIOElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxRDBIOElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxOtherBrowseElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxCommitElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent}, 
	{name:'palFields.pxDeclarativeRulesInvokedElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent}, 
	{name:'palFields.pxDeclarativeRulesLookupElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent}, 
	{name:'palFields.pxRuleBrowseElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxRuleIOElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxJavaStepElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxJavaAssembleElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxJavaCompileElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxInferGeneratedJavaElapsed.percOfTotal', formatter:percentFmt,unformat:unPercent},
	{name:'palFields.pxInferGeneratedJavaHL.percOfTotal', formatter:percentFmt,unformat:unPercent}
	
	 ], 
 rowNum:1000, 
 sortname: 'groupLabel', 
 viewrecords: true, 
 sortorder: "desc", 
 caption:"Alerts for <%=groupid%>" }); 

});

</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display Alerts</title>
</head>
<body>
<jsp:include page="I_Top.jsp"/>
<i>For Performance Reasons Only 100 Alerts are Shown</i><br/><br/>
<table id="alerttable"></table>
</body>
</html>