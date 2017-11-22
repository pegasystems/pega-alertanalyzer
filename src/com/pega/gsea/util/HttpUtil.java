package com.pega.gsea.util;
import java.net.*;
import java.io.*;
import java.util.*;
public class HttpUtil {

	
	public static String doPost(String u, HashMap<String,String> fields)
	{
		StringBuffer ret = new StringBuffer();
		try { 
			// Construct data 
				String data = "";
				Set<String> keys = fields.keySet();
				for (String k : keys){
					data+= URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(fields.get(k), "UTF-8");
					data+="&";
				}
				// Send data 
				URL url = new URL(u); 
				URLConnection conn = url.openConnection(); 
				conn.setDoOutput(true); 
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
				wr.write(data); wr.flush(); // Get the response 
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				String line; 
				while ((line = rd.readLine()) != null) { 
					System.out.println(line);
					ret.append(line);
					ret.append("\n");
					} 
				wr.close(); rd.close(); 
			}
			catch (Exception e) { } 
		return ret.toString();
	}
	
}
