package com.pega.gsea.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ParseAlertLog {

	public static List<AlertData> getDataFromCSVFile(String file) throws Exception
	{
		BufferedReader fr = new BufferedReader(new FileReader(file));
		return getDataFromCSVFile(fr);
	}
	
	public static List<AlertData> getDataFromCSVFile(BufferedReader fr) throws Exception
	{
		ArrayList<AlertData> data = new ArrayList<AlertData>();
		String s=null;
	
		s = fr.readLine();
		s = fr.readLine();
		int c=0;
		while (s!=null)
		{
			//System.out.println(s);
			c++;
			if (c%250==0)
				System.out.println(c);
			AlertData a = AlertData.buildAlertDataFromCSVRecord(s);
			data.add(a);
			s = fr.readLine();
			//skip until we find sequence;
		}
		return data;
	}
	
	public static List<AlertData> getDataFromRawLog(String file) throws Exception
	{
		BufferedReader fr = new BufferedReader(new FileReader(file));
		return getDataFromRawLog(fr);
	}
	
	public static List<AlertData> getDataFromRawLog(BufferedReader fr) throws Exception
	{
		ArrayList<AlertData> data = new ArrayList<AlertData>();
		String s=null;
	
//		s = fr.readLine();
		s = fr.readLine();
		int c=0;
		while (s!=null)
		{
		//	System.out.println(s);
			c++;
			if (c%250==0)
				System.out.println(c);
			AlertData a = AlertData.buildAlertDataFromRawLog(s);
			if (a!=null)
			data.add(a);
			s = fr.readLine();
			//skip until we find sequence;
		}
		return data;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = "C:\\Documents and Settings\\apapf\\My Documents\\Clients\\RX\\AlertLog.csv";
		String s=null;
		try
		{
			BufferedReader fr = new BufferedReader(new FileReader(file));
			s = fr.readLine();
			s = fr.readLine();
			while (s!=null)
			{
				AlertData a = AlertData.buildAlertDataFromCSVRecord(s);
				System.out.println(a.getCategory());
				System.out.println(a.getThreadName());
				//System.out.println(a.getUniqueInteraction());
				
				System.out.println(a.getPalData());
				s = fr.readLine();
				//skip until we find sequence;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(s);
		}

	}

}
