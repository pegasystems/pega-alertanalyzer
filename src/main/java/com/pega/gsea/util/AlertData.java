package com.pega.gsea.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AlertData {
    Date theDate;
    String displayDate;
    String timezone;    //what timezone should we display date in
    int version;
    String category;
    String msgID;
    long kpivalue;
    long kpithreshold;
    int kpiover;
    String severity;
    String serverID;
    String requestorID;
    String userID;
    String workpool;
    String ruleAppName;
    String encRulesetList;
    String allowRuleCheckout;
    int interaction;
    int uniqueInteraction;
    String threadName;
    String pegaThreadName;
    String loggerName;
    String stack;
    String lastInput;
    String firstAct;
    String lastStep;
    String primaryPageClass;
    String primaryPageName;
    String stepPageClass;
    String stepPageName;
    String problemCorrelation;
    String line;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    int milli;
    String pegaStack;
    String traceData;
    String palData;
    String paramPage;
    HashMap<String, String> palInfo = null;
    HashMap<String, PALField> palFields = null;
    List<AlertData> relatedAlerts; //related alerts for this alert
    int hourInterval;

    String inserts;    //used for sql alerts

    public static AlertData buildAlertDataFromCSVRecord(String rec) {
        SimpleTokenizer tok = new SimpleTokenizer(rec, ',');
        AlertData alertData = new AlertData();
        tok.nextToken();
        alertData.setVersion(Integer.parseInt(tok.nextToken()));
        alertData.setCategory(tok.nextToken());
        alertData.setMsgID(tok.nextToken());
        alertData.setKpivalue(Integer.parseInt(tok.nextToken()));
        alertData.setKpithreshold(Integer.parseInt(tok.nextToken()));
        alertData.setKpiover(Integer.parseInt(tok.nextToken()));
        alertData.setSeverity(tok.nextToken());
        alertData.setServerID(tok.nextToken());
        alertData.setRequestorID(tok.nextToken());
        alertData.setUserID(tok.nextToken());
        alertData.setWorkpool(tok.nextToken());
        alertData.setRuleAppName(tok.nextToken());
        alertData.setEncRulesetList(tok.nextToken());
        alertData.setAllowRuleCheckout(tok.nextToken());
        alertData.setInteraction(Integer.parseInt(tok.nextToken()));
        alertData.setUniqueInteraction(Integer.parseInt(tok.nextToken()));
        alertData.setThreadName(tok.nextToken());
        alertData.setPegaThreadName(tok.nextToken());
        alertData.setLoggerName(tok.nextToken());
        alertData.setStack(tok.nextToken());
        alertData.setLastInput(tok.nextToken());
        alertData.setFirstAct(tok.nextToken());
        alertData.setLastStep(tok.nextToken());
        alertData.setPrimaryPageClass(tok.nextToken());
        alertData.setPrimaryPageName(tok.nextToken());
        alertData.setStepPageClass(tok.nextToken());
        alertData.setStepPageName(tok.nextToken());
        alertData.setProblemCorrelation(tok.nextToken());
        alertData.setLine(tok.nextToken());
        //skip date stuff
        tok.nextToken();
        tok.nextToken();
        tok.nextToken();
        tok.nextToken();
        tok.nextToken();
        tok.nextToken();
        tok.nextToken();

        alertData.setPegaStack(tok.nextToken());
        alertData.setTraceData(tok.nextToken());
        alertData.setPalData(tok.nextToken());
        alertData.setParamPage(tok.nextToken());
        return alertData;

    }

    // { Ankur
    private static String makeBackwardCompatible(String line) {
        if (line == null || line.trim().length() == 0) {
            return "";
        }

        //System.out.println("Processing Alert Line : "+line);
        //System.out.println("Using split token : \\\u002A");
        String[] strTest;

        strTest = line.split("\\*");

        int numTokens = strTest.length;
        //System.out.println("Number of split Tokens (index is 0 based) >>> "+numTokens);


        ArrayList<String> retList = null;

        if (numTokens == 32) {
            //System.out.println("This is a version 7 alert");

            for (int index = 0; index < strTest.length; index++) {
                if (index == 6) {
                    strTest[6] = "tenantid";
                } else if (index == 7) {
                    strTest[7] = "tenantidhash";
                }
            }

            retList = new ArrayList<String>(Arrays.asList(strTest));

            ArrayList<String> toRemove = new ArrayList<String>();
            toRemove.add("tenantid");
            toRemove.add("tenantidhash");
            retList.removeAll(toRemove);

            //System.out.println("retList size >>> "+retList.size());

        } else if (numTokens == 30) {
            //System.out.println("This is a version 6 alert");

            return line;
        } else {
            //System.out.println("This is a version 8 alert");

            for (int index = 0; index < strTest.length; index++) {
                if (index == 6) {
                    strTest[6] = "tenantid";
                } else if (index == 7) {
                    strTest[7] = "tenantidhash";
                } else if (index == 15) {
                    strTest[15] = "correlationID";
                } else if (index == 24) {
                    strTest[24] = "Future1";
                } else if (index == 25) {
                    strTest[25] = "Future2";
                } else if (index == 26) {
                    strTest[26] = "Future3";
                } else if (index == 27) {
                    strTest[27] = "Future4";
                }
            }

            retList = new ArrayList<String>(Arrays.asList(strTest));

            ArrayList<String> toRemove = new ArrayList<String>();
            toRemove.add("tenantid");
            toRemove.add("tenantidhash");
            toRemove.add("correlationID");
            toRemove.add("Future1");
            toRemove.add("Future2");
            toRemove.add("Future3");
            toRemove.add("Future4");

            retList.removeAll(toRemove);
        }

        StringBuilder bldr = new StringBuilder();
        for (String temp : retList) {
            bldr.append(temp).append("*");
        }

        String retVal = bldr.toString();
        return retVal;
    }

    public static AlertData buildAlertDataFromRawLog(String rec) {
        rec = makeBackwardCompatible(rec);
        //System.out.println("rec >>>> "+rec);

        SimpleTokenizer tok = new SimpleTokenizer(rec, '*');
        //System.out.println("Number of tokens >>> "+tok.countTokens());
        if (tok.countTokens() < 31) {
            return null;
        }
        AlertData alertData = new AlertData();
        String dtStr = tok.nextToken();

        if (dtStr.indexOf(" - ") != -1) {
            //System.out.println("dtStr >>> "+dtStr);
            int index = dtStr.indexOf(" - ");
            dtStr = (dtStr.substring(index + 3));
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS zzz");
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z");
        try {
            alertData.theDate = df.parse(dtStr);
        } catch (Exception e) {
            System.out.println(dtStr + " >>> " + alertData.theDate.toString());
            e.printStackTrace();
            return null; // return null AlertData for this record as Date parsing failed.
        }
        alertData.setVersion(Integer.parseInt(tok.nextToken()));
        alertData.setMsgID(tok.nextToken());
        alertData.setKpivalue(Long.parseLong(tok.nextToken()));
        alertData.setKpithreshold(Long.parseLong(tok.nextToken()));
        alertData.setServerID(tok.nextToken());
        alertData.setRequestorID(tok.nextToken());
        alertData.setUserID(tok.nextToken());
        alertData.setWorkpool(tok.nextToken());
        alertData.setRuleAppName(tok.nextToken());
        alertData.setEncRulesetList(tok.nextToken());
        alertData.setAllowRuleCheckout(tok.nextToken());
        alertData.setInteraction(Integer.parseInt(tok.nextToken()));
        alertData.setUniqueInteraction(Integer.parseInt(tok.nextToken()));
        alertData.setThreadName(tok.nextToken());
        alertData.setPegaThreadName(tok.nextToken());
        alertData.setLoggerName(tok.nextToken());
        alertData.setStack(tok.nextToken());
        alertData.setLastInput(tok.nextToken());
        alertData.setFirstAct(tok.nextToken());
        alertData.setLastStep(tok.nextToken());
        alertData.setTraceData(tok.nextToken());
        alertData.setPalData(tok.nextToken());

        alertData.setPrimaryPageClass(tok.nextToken());
        alertData.setPrimaryPageName(tok.nextToken());
        alertData.setStepPageClass(tok.nextToken());
        alertData.setStepPageName(tok.nextToken());
        alertData.setPegaStack(tok.nextToken());
        alertData.setParamPage(tok.nextToken());

        alertData.setLine(tok.nextToken());

        return alertData;
    }

    public String getInserts() {
        return inserts;
    }

    public void setInserts(String inserts) {
        this.inserts = inserts;
    }

    public int getHourInterval() {
        return hourInterval;
    }

    public void setHourInterval(AlertAnalysis analysis) {

        if (theDate != null) {
            hourInterval = (int) (theDate.getTime() - analysis.getFirstAlert().getTime()) / (1000 * 60 * 60);
        }
    }

    public List<AlertData> getNearbyAlerts(AlertAnalysis analysis, int sec) {
        ArrayList<AlertData> relList = new ArrayList<AlertData>();
        for (AlertData a : analysis.getAllAlerts()) {
            if (this.getUniqueInteraction().equals(a.getUniqueInteraction())) {
                continue;
            }
            if (DateUtil.isWithinSecs(this, a, sec)) {
                relList.add(a);
            }
        }
        return relList;
    }

    public HashMap<String, PALField> getPalFields() {
        if (palFields == null) {
            palFields = AlertAnalysis.summarizePAL(this);
        }
        return palFields;
    }

    public String getInteractionKey() {
        return requestorID + "_" + interaction;
    }

    @Override
    public String toString() {
        return msgID + " " + kpivalue;
    }

    public HashMap<String, String> getPalInfo() {
        if (palInfo == null && palData != null) {
            //System.out.println("parsing pal Info");
            palInfo = new HashMap<String, String>();
            SimpleTokenizer tok = new SimpleTokenizer(palData, ';');
            while (tok.hasMoreTokens()) {
                String token = tok.nextToken();
                String[] arr = token.split("=");
                if (arr.length == 2) {
                    String name = token.split("=")[0].trim();
                    String val = token.split("=")[1].trim();
                    palInfo.put(name, val);
                }
            }
        }
        return palInfo;
    }
    // } Ankur

    public Date getTheDate() {
        return theDate;
    }

    public void setTheDate(Date theDate) {
        this.theDate = theDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public long getKpivalue() {
        return kpivalue;
    }

    public void setKpivalue(long kpivalue) {
        this.kpivalue = kpivalue;
    }

    public long getKpithreshold() {
        return kpithreshold;
    }

    public void setKpithreshold(long kpithreshold) {
        this.kpithreshold = kpithreshold;
    }

    public int getKpiover() {
        return kpiover;
    }

    public void setKpiover(int kpiover) {
        this.kpiover = kpiover;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getRequestorID() {
        return requestorID;
    }

    public void setRequestorID(String requestorID) {
        this.requestorID = requestorID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getWorkpool() {
        return workpool;
    }

    public void setWorkpool(String workpool) {
        this.workpool = workpool;
    }

    public String getRuleAppName() {
        return ruleAppName;
    }

    public void setRuleAppName(String ruleAppName) {
        this.ruleAppName = ruleAppName;
    }

    public String getEncRulesetList() {
        return encRulesetList;
    }

    public void setEncRulesetList(String encRulesetList) {
        this.encRulesetList = encRulesetList;
    }

    public String getAllowRuleCheckout() {
        return allowRuleCheckout;
    }

    public void setAllowRuleCheckout(String allowRuleCheckout) {
        this.allowRuleCheckout = allowRuleCheckout;
    }

    public int getInteraction() {
        return interaction;
    }

    public void setInteraction(int interaction) {
        this.interaction = interaction;
    }

    public String getUniqueInteraction() {

        //since unique interaction resets at 10000 need to append to it the time in ms.
        //System.out.println("UniqueInteraction >>> "+uniqueInteraction);
        String tmpInt = "" + uniqueInteraction + "_" + this.getTheDate().getTime();

        return tmpInt;
    }

    public void setUniqueInteraction(int uniqueInteraction) {
        this.uniqueInteraction = uniqueInteraction;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getLastInput() {
        return lastInput;
    }

    public void setLastInput(String lastInput) {
        this.lastInput = lastInput;
    }

    public String getFirstAct() {
        return firstAct;
    }

    public void setFirstAct(String firstAct) {
        this.firstAct = firstAct;
    }

    public String getLastStep() {
        return lastStep;
    }

    public void setLastStep(String lastStep) {
        this.lastStep = lastStep;
    }

    public String getPrimaryPageClass() {
        return primaryPageClass;
    }

    public void setPrimaryPageClass(String primaryPageClass) {
        this.primaryPageClass = primaryPageClass;
    }

    public String getPrimaryPageName() {
        return primaryPageName;
    }

    public void setPrimaryPageName(String primaryPageName) {
        this.primaryPageName = primaryPageName;
    }

    public String getStepPageClass() {
        return stepPageClass;
    }

    public void setStepPageClass(String stepPageClass) {
        this.stepPageClass = stepPageClass;
    }

    public String getStepPageName() {
        return stepPageName;
    }

    public void setStepPageName(String stepPageName) {
        this.stepPageName = stepPageName;
    }

    public String getProblemCorrelation() {
        return problemCorrelation;
    }

    public void setProblemCorrelation(String problemCorrelation) {
        this.problemCorrelation = problemCorrelation;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMilli() {
        return milli;
    }

    public void setMilli(int milli) {
        this.milli = milli;
    }

    public String getPegaStack() {
        return pegaStack;
    }

    public void setPegaStack(String pegaStack) {
        this.pegaStack = pegaStack;
    }

    public String getTraceData() {
        return traceData;
    }

    public void setTraceData(String traceData) {
        this.traceData = traceData;
    }

    public String getPalData() {
        return palData;
    }

    public void setPalData(String palData) {
        this.palData = palData;
    }

    public String getParamPage() {
        return paramPage;
    }

    public void setParamPage(String paramPage) {
        this.paramPage = paramPage;
    }

    public String getPegaThreadName() {
        return pegaThreadName;
    }

    public void setPegaThreadName(String pegaThreadName) {
        this.pegaThreadName = pegaThreadName;
    }

    public List<AlertData> getRelatedAlerts() {
        return relatedAlerts;
    }

    public void setRelatedAlerts(AlertAnalysis analysis) {
        System.out.println("Running set related alerts");
        relatedAlerts = new ArrayList<AlertData>();
        HashMap<String, String> tmpKeys = new HashMap<String, String>();
        //load up all keys for this group
        String curAlertType = "";

        tmpKeys.put(this.getInteractionKey(), this.getInteractionKey());
        curAlertType = this.getMsgID();
        List<AlertData> tmpRelated = analysis.getAllAlertsByInteractionID().get(this.getInteractionKey());
        for (AlertData b : tmpRelated) {
            if (!b.getMsgID().equals(curAlertType)) {
                relatedAlerts.add(b);
            }
        }
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getDisplayDate() {
        return DateUtil.formatForTZ(this.getTheDate(), timezone);
    }


}
