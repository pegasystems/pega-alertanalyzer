package com.pega.gsea.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;

public class HttpUtil {

    private HttpUtil(){

    }

    public static String doPost(String argUrl, HashMap<String, String> fields) {
        StringBuffer ret = new StringBuffer();
        try {
            // Construct data
            String data = "";
            Set<String> keys = fields.keySet();
            for (String k : keys) {
                data += URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(fields.get(k), "UTF-8");
                data += "&";
            }
            // Send data
            URL url = new URL(argUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
            wr.write(data);
            wr.flush(); // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.defaultCharset()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                ret.append(line);
                ret.append("\n");
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            // TODO handle this exception
        }
        return ret.toString();
    }

}
