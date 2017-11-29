<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="I_Head.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Alert File</title>
<style>
	#upload h3 { margin:0}
	#upload { padding:5px; margin-top:10px}
	#upbtn {margin-top:10px;}

</style>
<script>
$(document).ready(function(){
	$("#upbtn").button({});
});

</script>
</head>
<body>
<% 
/*
String line;
try 
{ 
	java.net.URL url = new java.net.URL(diagURI); 
	java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream())); 
	line = in.readLine(); 

	System.out.println( line );	

	in.close(); 
}
catch (Exception e)
{ 
	e.printStackTrace(); 
} 
*/

%>
<jsp:include page="I_Top.jsp"/>
<div id="upload" class="ui-widget-content">
	<h3 class="ui-widget-header">Upload File</h3>
<form action="AlertUpload"
enctype="multipart/form-data" method="post">
<%
	String err = request.getParameter("ERROR");
	if (err!=null && err.equalsIgnoreCase("TRUE")){
%>
<span style="font-size:20px;color:red">An error has occurred.  Please ensure that a supported version (v6) of the alert file has been selected.</span>
<br/>
<%} %>

<%
	String invSession = request.getParameter("INVSESSION");
	if (invSession!=null && invSession.equalsIgnoreCase("TRUE")){
%>
<span style="font-size:20px;color:red">No alert data is available, this may be due to a session timeout.  Please upload an alert file.</span>
<br/>
<%} %>

<p>
<b>PRPC Alert Log:</b><br>
<input type="file" name="datafile" id="datafile" size="40">
</p>
	
<b>Application Timezone:</b><br/>
<select name="timezone" id="timezone">       
<option value="GMT-12:00">(GMT -12:00) Eniwetok, Kwajalein</option>       
<option value="-GMT-11:00">(GMT -11:00) Midway Island, Samoa</option>      
 <option value="GMT-10:00">(GMT -10:00) Hawaii</option>       
 <option value="GMT-09:00">(GMT -9:00) Alaska</option>      
  <option value="GMT-08:00">(GMT -8:00) Pacific Time (US &amp; Canada)</option>   
      <option value="GMT-07:00">(GMT -7:00) Mountain Time (US &amp; Canada)</option>  
           <option value="GMT-06:00">(GMT -6:00) Central Time (US &amp; Canada), Mexico City</option>    
              <option value="GMT-05:00">(GMT -5:00) Eastern Time (US &amp; Canada), Bogota, Lima</option> 
                    <option value="GMT-04:00">(GMT -4:00) Atlantic Time (Canada), Caracas, La Paz</option>  
                         <option value="GMT-03:30">(GMT -3:30) Newfoundland</option> 
                               <option value="GMT-03:00">(GMT -3:00) Brazil, Buenos Aires, Georgetown</option> 
                                     <option value="GMT-02:00">(GMT -2:00) Mid-Atlantic</option> 
                                           <option value="GMT-01:00">(GMT -1:00 hour) Azores, Cape Verde Islands</option> 
 <option value="GMT-00:00">(GMT) Western Europe Time, London, Lisbon, Casablanca</option>
        <option value="GMT+01:00">(GMT +1:00 hour) Brussels, Copenhagen, Madrid, Paris</option> 
              <option value="GMT+02:00">(GMT +2:00) Kaliningrad, South Africa</option>     
                <option value="GMT+03:00">(GMT +3:00) Baghdad, Riyadh, Moscow, St. Petersburg</option>   
                    <option value="GMT+03:30">(GMT +3:30) Tehran</option>   
                        <option value="GMT+04:00">(GMT +4:00) Abu Dhabi, Muscat, Baku, Tbilisi</option>   
                            <option value="GMT+04:30">(GMT +4:30) Kabul</option>  
                                 <option value="GMT+05:00">(GMT +5:00) Ekaterinburg, Islamabad, Karachi, Tashkent</option> 
                                       <option value="GMT+05:30">(GMT +5:30) Bombay, Calcutta, Madras, New Delhi</option>
                                              <option value="GMT+05:45">(GMT +5:45) Kathmandu</option>
                                                     <option value="GMT+06:00">(GMT +6:00) Almaty, Dhaka, Colombo</option> 
                                                           <option value="GMT+07:00">(GMT +7:00) Bangkok, Hanoi, Jakarta</option>
                                                                  <option value="GMT+08:00">(GMT +8:00) Beijing, Perth, Singapore, Hong Kong</option>
                                                                         <option value="GMT+09:00">(GMT +9:00) Tokyo, Seoul, Osaka, Sapporo, Yakutsk</option> 
                                                                               <option value="GMT+09:30">(GMT +9:30) Adelaide, Darwin</option> 
                                                                                     <option value="GMT+10:00">(GMT +10:00) Eastern Australia, Guam, Vladivostok</option> 
                                                                                           <option value="GMT+11:00">(GMT +11:00) Magadan, Solomon Islands, New Caledonia</option>  
                                                                                                <option value="GMT+12:00">(GMT +12:00) Auckland, Wellington, Fiji, Kamchatka</option>
        </select> 

<br/>Please select timezone based on the appropriate offset (e.g. -04:00) not the timezone name.  (This can be off due to daylight savings time).
<div>
<input id="upbtn" type="submit" value="Upload">
</div>
</form>
</div>
</body>
</html>