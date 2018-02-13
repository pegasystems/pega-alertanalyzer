package com.pega.gsea.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessUtil {

    private SessUtil(){

    }

    public static boolean requireAlerts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String invSession = request.getParameter("INVSESSION");
        if (invSession == null || !invSession.equals("TRUE")) {

            //this is not a redirect for an invalid session
            //ie check if we need one!
            HttpSession sess = request.getSession(false);
            System.out.println(sess.toString());
            if (sess == null) {
                response.sendRedirect("Upload.jsp?NEWSESSION=TRUE");
                return false;
            } else {
                System.out.println(sess.getId());
                if (sess.isNew()) {
                    response.sendRedirect("UploadFile.jsp?NEWSESSION=TRUE");
                    return false;
                }
                AlertAnalysis analysis = (AlertAnalysis) request.getSession().getAttribute("ALERTS");
                if (analysis == null) {
                    System.out.println("redirect");
                    response.sendRedirect("UploadFile.jsp?INVSESSION=TRUE");
                    return false;
                }
                System.out.println(analysis.toString());
            }
        }
        return true;
    }
}
