package com.pega.gsea.util;
import java.text.*;
import java.util.HashMap;
public class StringUtil {

	public static String formatDouble(Double d)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		df.setMinimumIntegerDigits(1);
		return df.format(d);
	}
	
	public static String sqlFmt(String sql)
	{
		/*HashMap<String,String> fields = new HashMap<String,String>();
		fields.put("data", sql);
		fields.put("format","text");
		fields.put("keyword_case","upper");
		fields.put("reindent","true");
		fields.put("n_indents","2");
		return HttpUtil.doPost("http://sqlformat.appspot.com/format/", fields);*/
		String formattedSQL = new SQLFormatter().format(sql);
		return formattedSQL;
	}
	

	
	public static void main(String[] args)
	{
		System.out.println(sqlFmt("select a,b,c,(select z from q) as t from tbl where x=1 group by q"));
	}
}
 