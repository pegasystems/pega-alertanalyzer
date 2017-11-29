package com.pega.gsea.servlets;

import com.pega.gsea.util.AlertAnalysis;
import com.pega.gsea.util.AlertData;
import com.pega.gsea.util.AlertGroup;
import com.pega.gsea.util.CompareTotalTime;
import com.pega.gsea.util.DateUtil;
import com.pega.gsea.util.GridResult;
import flexjson.JSONSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Servlet implementation class for Servlet: ActionServlet
 *
 */
 public class ActionServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ActionServlet() {
           super();
    }   	

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doPost(request,response);
    }  	
	
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String action = request.getParameter("ACTION");
            AlertAnalysis analysis = (AlertAnalysis)request.getSession().getAttribute("ALERTS");
            PrintWriter out = response.getWriter();
            if (action.equalsIgnoreCase("PEGA01BYACTION"))
            {
                    List<AlertGroup> groups = analysis.getAlertGroups();	
                    Collections.sort(groups, new CompareTotalTime());
                    GridResult gResult = new GridResult();
                    gResult.setPage(1);
                    gResult.setTotal(1);
                    gResult.setRecords(groups.size());
                    gResult.setRows(groups);
                    JSONSerializer json = new JSONSerializer().exclude("class","rows.class","rows.palData.class").include("rows.palData").exclude("rows.alerts");
                    String output = json.serialize(gResult);
                    System.out.println(output);
                    response.setContentType("application/json");
                    out.println(output);
                    out.close();
                   System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
                   System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());


            }
            else if (action.equalsIgnoreCase("SETAPPLICATION"))
            {
                    String app = request.getParameter("appFilter");
                    System.out.println("Filtering app by "+app);
                    if (app.equalsIgnoreCase("ALL")){

                            //get other filters
                            String userFilter = analysis.getUserFilter();
                            String nodeFilter = analysis.getNodeFilter();
                            int pega01Threshold = analysis.getPega01ThresholdFilter();
                            System.out.println("Pega01 threshold "+pega01Threshold);
                            Date sDate = analysis.getDateFilterStart();
                            Date eDate = analysis.getDateFilterEnd();
                            analysis.reset();
                            if (userFilter!=null)
                                    analysis.applyUserFilter(userFilter);
                            if (nodeFilter!=null)
                                    analysis.applyNodeFilter(nodeFilter);
                            if (pega01Threshold>=0)
                                    analysis.applyThresholdFilter("PEGA0001", pega01Threshold);
                            if (sDate!=null)
                                    analysis.applyDateFilter(sDate, eDate);
                            request.getSession().setAttribute("APPLICATIONFILTER", null);
                    }
                    else
                    {
                            analysis.applyApplicationFilter(app);
                            request.getSession().setAttribute("APPLICATIONFILTER", ""+app);
                    }
                    response.sendRedirect("Menu.jsp");
            }
            else if (action.equalsIgnoreCase("SETUSER"))
            {
                    String user = request.getParameter("userFilter");
                    System.out.println("Filtering user by "+user);
                    analysis.applyUserFilter(user);
                    request.getSession().setAttribute("USERFILTER", ""+user);
                    response.sendRedirect("Menu.jsp");
            }
            else if (action.equalsIgnoreCase("SETNODE"))
            {
                    String node = request.getParameter("nodeFilter");
                    System.out.println("Filtering node by "+node);
                    analysis.applyNodeFilter(node);
                    request.getSession().setAttribute("NODEFILTER", ""+node);
                    response.sendRedirect("Menu.jsp");
            }
            else if (action.equalsIgnoreCase("SETTHRESHOLD"))
            {
                    int newThreshold = Integer.parseInt(request.getParameter("threshold"));
                    analysis.applyThresholdFilter("PEGA0001", newThreshold);
                    request.getSession().setAttribute("PEGA01THRESHOLD", ""+newThreshold);
                    response.sendRedirect("Menu.jsp");
            }
            else if (action.equalsIgnoreCase("CLEARFILTERS"))
            {
                    request.getSession().setAttribute("PEGA01THRESHOLD", null);
                    request.getSession().setAttribute("STARTTIME", null);
                    request.getSession().setAttribute("ENDTIME", null);
                    request.getSession().setAttribute("APPLICATIONFILTER", null);
                    request.getSession().setAttribute("USERFILTER", null);
                    analysis.reset();

                    response.sendRedirect("Menu.jsp");
            }
            else if (action.equalsIgnoreCase("SETTIMERANGE"))
            {
                    String start = request.getParameter("S");
                    String end = request.getParameter("E");
                    int sDash = start.indexOf(" (");
                    int eDash = end.indexOf(" (");

                    Date startDate=null;
                    Date endDate=null;
                    if (sDash>0)
                    {
                            //firefox format Sun Oct 03 2010 00:00:00 GMT-0400 (Eastern Daylight Time)
                            start = start.replaceAll("GMT","");
                            end = end.replaceAll("GMT","");
                            //Sun Oct 03 2010 00:00:00 GMT-0400 (Eastern Daylight Time)
                            DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z");
                            try
                            {
                                    startDate = df.parse(start.substring(0,sDash)); 
                                    endDate = df.parse(end.substring(0,eDash));
                                    //update for timezone
                                    startDate = DateUtil.changeTimezone(startDate, analysis.getTimezone());
                                    endDate = DateUtil.changeTimezone(endDate, analysis.getTimezone());

                            }
                            catch (ParseException e)
                    {
                                    e.printStackTrace();
                    }
                    }
                    else
                    {
                            //IE format Thu Oct 7 10:13:43 EDT 2010
                            start = start.replaceAll("GMT","");
                            end = end.replaceAll("GMT","");
                            DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                            try
                            {
                                    startDate = df.parse(start); 
                                    endDate = df.parse(end);
                            }
                            catch (ParseException e)
                    {
                                    e.printStackTrace();
                    }
                    }
                    analysis.applyDateFilter(startDate,endDate);
                    request.getSession().setAttribute("STARTTIME", startDate);
                    request.getSession().setAttribute("ENDTIME", endDate);

                    response.sendRedirect("Menu.jsp");
            }
            else if (action.equalsIgnoreCase("ALERTGROUPLIST"))
            {
                    String groupid = request.getParameter("group");
                    AlertGroup group = analysis.getAlertGroup(groupid);
                    List<Object> resList = GridResult.buildList(group.getAlerts(), 100);
                    GridResult gResult = new GridResult();
                    gResult.setPage(1);
                    gResult.setTotal(1);
                    gResult.setRecords(resList.size());
                    gResult.setRows(resList);		
                    System.out.println("serializing "+resList.size()+" records.");
                    JSONSerializer json = new JSONSerializer().exclude("class").include("rows.palFields");
                    String output = json.serialize(gResult);
                    System.out.println(output);
                    response.setContentType("application/json");
                    out.println(output);
                    out.close();
                   System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
                   System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());

            }
            else if (action.equalsIgnoreCase("ALERTSBYTYPE"))
            {
                    String alertType = request.getParameter("AlertType");
                    String all = request.getParameter("ALL");
                    boolean showAll = false;
                    if (all!=null && all.equalsIgnoreCase("ALL"))
                            showAll = true;

                    List<AlertData> group = analysis.getAllAlertsByType().get(alertType);
                    List<Object> resList = GridResult.buildList(group, (showAll?group.size():100));
                    GridResult gResult = new GridResult();
                    gResult.setPage(1);
                    gResult.setTotal(1);
                    gResult.setRecords(resList.size());
                    gResult.setRows(resList);
                    System.out.println("serializing "+resList.size()+" records.");
                    JSONSerializer json = new JSONSerializer().include("rows.uniqueInteraction","rows.displayDate","rows.userID","rows.workpool","rows.ruleAppName","rows.kpivalue").exclude("class","rows.*");
                    String output = json.serialize(gResult);
                    System.out.println(output);
                    System.out.println(output.length());
                    response.setContentType("application/json");
                    out.println(output);
                    out.close();
                   System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
                   System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());

            }
            else if (action.equalsIgnoreCase("PEGA05GROUPS"))
            {
                    List<AlertGroup> groups = analysis.getGroupedPega05(false);	
                    Collections.sort(groups, new CompareTotalTime());
                    GridResult gResult = new GridResult();
                    gResult.setPage(1);
                    gResult.setTotal(1);
                    gResult.setRecords(groups.size());
                    gResult.setRows(groups);
                    JSONSerializer json = new JSONSerializer().exclude("class","rows.class","rows.palData.class");
                    String output = json.serialize(gResult);
                    System.out.println(output);
                    response.setContentType("application/json");
                    out.println(output);
                    out.close();
                   System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
                   System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());

            }
            else if (action.equalsIgnoreCase("PEGA05GROUPSREMCON"))
            {
                    List<AlertGroup> groups = analysis.getGroupedPega05(true);	
                    Collections.sort(groups, new CompareTotalTime());
                    GridResult gResult = new GridResult();
                    gResult.setPage(1);
                    gResult.setTotal(1);
                    gResult.setRecords(groups.size());
                    gResult.setRows(groups);
                    JSONSerializer json = new JSONSerializer().exclude("class","rows.class","rows.palData.class");
                    String output = json.serialize(gResult);
                    System.out.println(output);
                    response.setContentType("application/json");
                    out.println(output);
                    out.close();
                   System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
                   System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());

            }
            else if (action.equalsIgnoreCase("PEGA20GROUPS"))
            {
                    List<AlertGroup> groups = analysis.getGroupedPega20();	
                    Collections.sort(groups, new CompareTotalTime());
                    GridResult gResult = new GridResult();
                    gResult.setPage(1);
                    gResult.setTotal(1);
                    gResult.setRecords(groups.size());
                    gResult.setRows(groups);
                    JSONSerializer json = new JSONSerializer().exclude("class","rows.class","rows.palData.class");
                    String output = json.serialize(gResult);
                    System.out.println(output);
                    response.setContentType("application/json");
                    out.println(output);
                    out.close();
                   System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
                   System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());

            }
    }
}