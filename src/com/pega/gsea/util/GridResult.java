package com.pega.gsea.util;
import java.util.*;

public class GridResult {

	int page;
	int total;
	int records;
	Object rows;

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}
	
	public static List<Object> buildList(List<?> origList, int max)
	{
		ArrayList<Object> newList = new ArrayList<Object>();
		int cnt = (origList.size()>max ?  max : origList.size());
		for (int i = 0; i < cnt; i++)
			newList.add(origList.get(i));
		System.out.println("New list complete");
		return newList;
	}
	
	
}
