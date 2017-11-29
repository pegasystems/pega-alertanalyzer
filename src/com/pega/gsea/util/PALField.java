package com.pega.gsea.util;

public class PALField {

	String field;
	double value;
	int count;
	double totalTime;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}
	
	public  double getNormalizedValue()
	{
		return value/count;
	}
	
	public double getPercOfTotal()
	{
		return value/totalTime;
	}
	
}
