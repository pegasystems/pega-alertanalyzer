package com.pega.gsea.servlets;

import com.pega.gsea.util.AlertAnalysis;
import com.pega.gsea.util.AlertData;
import com.pega.gsea.util.ParseAlertLog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

/**
 * Servlet implementation class for Servlet: AlertUpload.
 */
public class RuntimeAnalysisServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    static final long serialVersionUID = 1L;

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public RuntimeAnalysisServlet() {
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

        try {
            String diagUrl = request.getParameter("diagUrl");

            if (diagUrl == null || "".equals(diagUrl) || diagUrl.length() == 0) {
                throw new ServletException("Do diagnostic URL received as request parameter in order to download the ALERT logs from PRPC node.");
            }


            diagUrl = diagUrl.substring((diagUrl.indexOf("=") + 1));
            System.out.println("diagUrl >>> " + diagUrl);

            String line = "";
            List<AlertData> alerts = null;
            String timezone = "GMT-00:00";
            HttpURLConnection urlConnection = null;

            try {
                String name = request.getParameter("name");
                if (name == null) {
                    name = "";
                }

                String password = request.getParameter("password");
                if (password == null) {
                    password = "";
                }


                String authString = name + ":" + password;
                byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes(Charset.defaultCharset()));
                String authStringEnc = new String(authEncBytes, Charset.defaultCharset());
                //System.out.println("Base64 encoded auth string: " + authStringEnc);

                URL url = new URL(diagUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.defaultCharset()));
                alerts = ParseAlertLog.getDataFromRawLog(in);
                System.out.println("Total Memory " + Runtime.getRuntime().totalMemory());
                System.out.println("Free Memory  " + Runtime.getRuntime().freeMemory());
            } catch (Exception e) {
                if (urlConnection.getResponseCode() == 401) {
                    getServletContext().getRequestDispatcher("/RuntimeAnalysis.jsp?diagnosticurl=" + diagUrl).forward(request, response);
                }
                e.printStackTrace();
            }


            //request.getSession().invalidate();
            if (alerts == null || alerts.size() == 0) {
                response.sendRedirect("UploadFile.jsp?ERROR=true");
                return;
            }
            AlertAnalysis analysis = new AlertAnalysis(alerts);
            analysis.setTimezone(timezone);
            request.getSession().setAttribute("ALERTS", analysis);


            response.sendRedirect("Menu.jsp?msg=File Uploaded Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UploadFile.jsp?ERROR=true");
        }
    }
}
