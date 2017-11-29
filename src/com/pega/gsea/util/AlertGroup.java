package com.pega.gsea.util;
import java.util.*;
public class AlertGroup {

	List<AlertData> alerts;		//pega01 alerts for this group
	List<AlertData> relatedAlerts; //related alerts for this group
	String groupLabel;
	HashMap<String, PALField> palData;	//summarized pal data for the group
	int countOfAlerts;
	double totalTime;
	double avgTime;
	String alertID;
	double stdDev;
	double min = Double.MAX_VALUE;
	double max = Double.MAX_VALUE * -1;
	
	
	public double getStdDev() {
		return stdDev;
	}
	public double getMin() {
		return min;
	}
	public double getMax() {
		return max;
	}
	public String getAlertID() {
		return alertID;
	}
	public void setAlertID(String alertID) {
		this.alertID = alertID;
	}
	public double getAvgTime() {
		
		return avgTime;
	}
	public double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}
	public List<AlertData> getAlerts() {
		return alerts;
	}
	
	public void setAlerts(List<AlertData> alerts) {
		this.alerts = alerts;
		countOfAlerts = alerts.size();
		totalTime = 0;
		min = Double.MAX_VALUE;
		max = Double.MAX_VALUE * -1;
		for (AlertData a : alerts)
		{
			double val = a.getKpivalue();
			totalTime += val;
			if (val <  min)
				min = val;
			if (val>max)
				max = val;
		}
		avgTime = totalTime/countOfAlerts;
		double sum = 0;
	    for ( AlertData a : alerts)
	         {
	         final double v = a.getKpivalue() - avgTime;
	         sum += v * v;
	         }
	      // Change to ( n - 1 ) to n if you have complete data instead of a sample.
	     stdDev = Math.sqrt( sum / ( countOfAlerts ) );

	}
	
	public String getGroupLabel() {
		return groupLabel;
	}
	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}
	public HashMap<String, PALField> getPalData() {
		return palData;
	}
	public void setPalData(HashMap<String, PALField> palData) {
		this.palData = palData;
	}
	public int getCountOfAlerts() {
		return countOfAlerts;
	}
	
	public void printPAL()
	{
		HashMap<String,PALField> pal = this.getPalData();
		Set<String> k = pal.keySet();
		for (String key : k)
		{
			System.out.println(key +" = "+pal.get(key).getValue());
		}
	}
	
	public void setRelatedAlerts(AlertAnalysis analysis)
	{
		System.out.println("Running set related alerts");
		relatedAlerts = new ArrayList<AlertData>();
		HashMap<String,String> tmpKeys = new HashMap<String,String>();
		//load up all keys for this group
		String curAlertType="";
		for (AlertData a:alerts)
		{
			List<AlertData> tmpRelated = analysis.getAllAlertsByInteractionID().get(a.getInteractionKey());
			curAlertType = a.getMsgID();
			for (AlertData b:tmpRelated)
			{
				if (!b.getMsgID().equals(curAlertType))
				{
					relatedAlerts.add(b);
				}
			}
		}
	}
	
	public List<AlertData> getRelatedAlerts() {
		return relatedAlerts;
	}
	
	
}
