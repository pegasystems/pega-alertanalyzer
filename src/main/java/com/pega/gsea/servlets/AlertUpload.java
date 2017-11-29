package com.pega.gsea.servlets;

import com.pega.gsea.util.AlertAnalysis;
import com.pega.gsea.util.AlertData;
import com.pega.gsea.util.ParseAlertLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class for Servlet: AlertUpload
 *
 */
 public class AlertUpload extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public AlertUpload() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
        @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
        @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			request.getSession().invalidate();
			FileItemFactory factory = new DiskFileItemFactory();
			
	
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<AlertData>  alerts=null;
			String timezone = null;
			// Parse the request
			List /* FileItem */ items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
	
			    if (!item.isFormField()) {
			        String fieldName = item.getFieldName();
			        String fileName = item.getName();
			        String contentType = item.getContentType();
			        boolean isInMemory = item.isInMemory();
			        long sizeInBytes = item.getSize();
			        InputStream uploadedStream = item.getInputStream();
			        BufferedReader fr = new BufferedReader(new InputStreamReader(uploadedStream, UTF_8));
			        alerts = ParseAlertLog.getDataFromRawLog(fr);
			        System.out.println("Total Memory "+Runtime.getRuntime().totalMemory());    
			        System.out.println("Free Memory  "+Runtime.getRuntime().freeMemory());

			    }
			    else
			    {
			        String name = item.getFieldName();
			        String value = item.getString();
			        if (name.equalsIgnoreCase("timezone"))
			        {
			        	timezone = value;
			        }
			    }
			}
			
			if (alerts==null || alerts.isEmpty())
			{
				response.sendRedirect("UploadFile.jsp?ERROR=true");
				return;
			}
			AlertAnalysis analysis = new AlertAnalysis(alerts);
			analysis.setTimezone(timezone);
			request.getSession().setAttribute("ALERTS", analysis);
			response.sendRedirect("Menu.jsp?msg=File Uploaded Successfully");
		       
		      
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.sendRedirect("UploadFile.jsp?ERROR=true");
		}
	}   	  	    
}