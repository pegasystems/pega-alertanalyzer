package com.pega.gsea.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AlertAnalysis {

    public static String[] elapsedTimeFields = {"pxTotalReqTime", "pxDeclarativeRulesInvokedElapsed",
        "pxDeclarativeRulesLookupElapsed", "pxOtherIOElapsed", "pxRDBIOElapsed",
        "pxRuleBrowseElapsed", "pxJavaAssembleElapsed", "pxJavaStepElapsed", "pxConnectElapsed",
        "pxCommitElapsed", "pxJavaCompileElapsed", "pxTotalReqCPU", "pxRuleIOElapsed", "pxOtherBrowseElapsed",
        "pxInferGeneratedJavaElapsed", "pxInferGeneratedJavaHLElapsed"};

    String timezone;

    List<AlertData> unfilteredAlerts;

    List<AlertData> theData;
    List<AlertGroup> alertGroups = null;        //for pega01s only
    HashMap<String, AlertGroup> pega05Groups = null;    //for pega05 groups (last one)
    HashMap<String, AlertGroup> pega20Groups = null;    //for pega20Groups groups (last one)

    HashMap<String, AlertData> alertsByID;
    HashMap<String, List<AlertData>> alertsByType;
    HashMap<String, List<AlertData>> alertsByInteractionID;
    HashMap<Integer, List<AlertData>> alertsByHour;
    HashMap<String, List<AlertData>> alertsByNode;

    List<String> uniqueApps;
    List<String> uniqueNodes;
    List<String> uniqueUsers;

    Date firstAlert = null;
    Date lastAlert = null;

    //current filter variables
    int pega01ThresholdFilter = -1;
    String applicationFilter = null;
    Date dateFilterStart = null;
    Date dateFilterEnd = null;
    String nodeFilter = null;
    String userFilter = null;


    public AlertAnalysis(List<AlertData> theData) {
        unfilteredAlerts = new ArrayList<>(theData);
        initAlertAnalysis(theData);

    }

    public AlertAnalysis(String alertCSVFile) {
        try {
            theData = ParseAlertLog.getDataFromCSVFile(alertCSVFile);
        } catch (Exception e) {
            e.printStackTrace();
            theData = null;
        }
    }

    //takes a list of alerts and group them by interaction ID
    public static HashMap<String, List<AlertData>> groupByInteractionID(List<AlertData> alerts) {
        HashMap<String, List<AlertData>> results = new HashMap<String, List<AlertData>>();
        for (AlertData a : alerts) {
            if (results.containsKey(a.getInteractionKey())) {
                results.get(a.getInteractionKey()).add(a);
            } else {
                List<AlertData> tmp = new ArrayList<AlertData>();
                tmp.add(a);
                results.put(a.getInteractionKey(), tmp);
            }
        }
        return results;
    }

    //takes a list of alerts and group them by node
    public static HashMap<String, List<AlertData>> groupByNode(List<AlertData> alerts) {
        HashMap<String, List<AlertData>> results = new HashMap<String, List<AlertData>>();
        for (AlertData a : alerts) {
            if (results.containsKey(a.getServerID())) {
                results.get(a.getServerID()).add(a);
            } else {
                List<AlertData> tmp = new ArrayList<AlertData>();
                tmp.add(a);
                results.put(a.getServerID(), tmp);
            }
        }
        return results;
    }

    //takes a list of alerts and group them by alert type
    public static HashMap<String, List<AlertData>> groupByAlertType(List<AlertData> alerts) {
        HashMap<String, List<AlertData>> results = new HashMap<String, List<AlertData>>();
        for (AlertData a : alerts) {
            if (results.containsKey(a.getMsgID())) {
                results.get(a.getMsgID()).add(a);
            } else {
                List<AlertData> tmp = new ArrayList<AlertData>();
                tmp.add(a);
                results.put(a.getMsgID(), tmp);
            }
        }
        return results;
    }

    //summarizing pal for a list of results
    public static HashMap<String, PALField> summarizePAL(List<AlertData> alerts) {

        HashMap<String, Double> res = new HashMap<String, Double>();
        for (AlertData a : alerts) {
            for (String field : elapsedTimeFields) {
                double time = getPalDouble(a.getPalInfo().get(field));
                if (res.get(field) == null) {
                    Double doubleTime = time;
                    res.put(field, doubleTime);
                } else {
                    Double curVal = (Double) res.get(field);
                    curVal = Double.valueOf(curVal.doubleValue() + time);
                    res.put(field, curVal);
                }
            }
            //other fields to include

        }
        double totalTime = res.get("pxTotalReqTime");
        double timers = 0;
        for (int i = 1; i < elapsedTimeFields.length; i++) {
            timers += res.get(elapsedTimeFields[i]);
        }
        double unAcct = totalTime - timers;
        if (unAcct < 0) {
            unAcct = 0;
        }

        res.put("Unaccounted For", unAcct);

        HashMap<String, PALField> fields = new HashMap<String, PALField>();
        Set<String> keys = res.keySet();
        for (String k : keys) {
            PALField field = new PALField();
            field.setCount(alerts.size());
            field.setField(k);
            field.setValue(res.get(k));
            field.setTotalTime(totalTime);
            fields.put(k, field);
        }

        return fields;

    }

    //summarizing pal for a single alert
    public static HashMap<String, PALField> summarizePAL(AlertData alert) {
        System.out.println("Summarizing PAL");
        HashMap<String, String> res = alert.getPalInfo();

        double totalTime = getPalDouble(res.get("pxTotalReqTime"));
        double timers = 0;
        for (int i = 1; i < elapsedTimeFields.length; i++) {
            timers += getPalDouble(res.get(elapsedTimeFields[i]));
        }
        double unAcct = totalTime - timers;
        if (unAcct < 0) {
            unAcct = 0;
        }
        HashMap<String, PALField> fields = new HashMap<String, PALField>();
        PALField palField = new PALField();
        palField.setCount(1);
        palField.setField("Unaccounted For");
        palField.setValue(unAcct);
        palField.setTotalTime(totalTime);
        fields.put("Unaccounted For", palField);
        Set<String> keys = res.keySet();
        for (String k : keys) {
            PALField field = new PALField();
            field.setCount(1);
            field.setField(k);
            field.setValue(getPalDouble(res.get(k)));
            field.setTotalTime(totalTime);
            fields.put(k, field);
        }
        System.out.println("Summarizing PAL Complete");
        return fields;

    }

    public static String parse20Line(String input) {

        int endidx = input.indexOf("#");
        if (endidx < 0) {
            endidx = input.length();
        }
        return input.substring(0, endidx).trim();
    }

    public static String parse05Line(String input) {
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");

        int startidx = input.indexOf("SQL:") + 5;
        int endidx = input.indexOf("inserts:");
        if (endidx < startidx) {
            endidx = input.length();
        }
        return input.substring(startidx, endidx).trim().replace("\'", "\\\'");
    }

    public static String getInserts(String sql) {
        sql = sql.replaceAll("<", "&lt;");
        sql = sql.replaceAll(">", "&gt;");
        int endidx = sql.indexOf("inserts:");
        if (endidx > 0) {
            String inserts = sql.substring(endidx + 8).trim();
            System.out.println("Inserts: " + inserts);
            return inserts;
        }
        return null;
    }

    private static String removeSQLConstants(String sql) {
        //TODO remove from CASE WHEN END
        int startCase = sql.toUpperCase().indexOf("CASE WHEN");
        if (startCase > 0) {
            int endCase = sql.toUpperCase().lastIndexOf(" END ");
            while (true) {
                int startQuote = sql.indexOf('\'');
                if (startQuote < 0 || startQuote > endCase) {
                    break;
                }
                int endQuote = sql.indexOf('\'', startQuote + 1);
                String tmp = sql.substring(startQuote + 1, endQuote);
                sql = sql.replace("\'" + tmp + "\'", "???");
            }
        }


        //remove from where clause

        try {
            int whereStarts = sql.toUpperCase().indexOf("WHERE");
            if (whereStarts < 0) {
                return sql;
            }
            String where = sql.substring(whereStarts);
            while (true) {
                int startQuote = where.indexOf('\'');
                if (startQuote < 0) {
                    break;
                }
                int endQuote = where.indexOf('\'', startQuote + 1);
                String tmp = where.substring(startQuote + 1, endQuote);
                where = where.replace("\'" + tmp + "\'", "???");
            }
            return sql.substring(0, whereStarts) + " " + where;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(sql);
            return sql;
        }
    }

    public static double getPalDouble(String value) {
        if (value == null) {
            return 0;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void printAlerts(List<AlertData> alerts) {
        for (AlertData a : alerts) {
            System.out.println(a.toString());
        }
    }

    public void applyThresholdFilter(String alertType, int threshold) {
        pega01ThresholdFilter = threshold;            //needs to be updated if we support other alert thresholds.
        ArrayList<AlertData> newData = new ArrayList<>();
        int origSize = theData.size();
        //get the current alert set
        for (AlertData a : theData) {
            if (a.getMsgID().equals(alertType)) {
                //same alert type we are filtering,add it only if its over threshold
                if (a.getKpivalue() >= threshold) {
                    newData.add(a);
                }
            } else {
                //not alert type were filtering it so add it
                newData.add(a);
            }
        }
        int newSize = newData.size();
        System.out.println("Applied filtering: old size: " + origSize + " new size: " + newSize);
        initAlertAnalysis(newData);
    }

    public void applyApplicationFilter(String app) {
        applicationFilter = app;
        ArrayList<AlertData> newData = new ArrayList<AlertData>();
        int origSize = theData.size();
        //get the current alert set
        for (AlertData a : theData) {
            if (a.getRuleAppName().equals(app)) {
                newData.add(a);
            }
        }
        int newSize = newData.size();
        System.out.println("Applied app filtering: old size: " + origSize + " new size: " + newSize);
        initAlertAnalysis(newData);
    }

    public void applyUserFilter(String user) {
        userFilter = user;
        ArrayList<AlertData> newData = new ArrayList<AlertData>();
        int origSize = theData.size();
        //get the current alert set
        for (AlertData a : theData) {
            if (a.getUserID().equals(user)) {
                newData.add(a);
            }
        }
        int newSize = newData.size();
        System.out.println("Applied user filtering: old size: " + origSize + " new size: " + newSize);
        initAlertAnalysis(newData);
    }

    public void applyNodeFilter(String node) {
        nodeFilter = node;
        ArrayList<AlertData> newData = new ArrayList<AlertData>();
        int origSize = theData.size();
        //get the current alert set
        for (AlertData a : theData) {
            if (a.getServerID().equals(node)) {
                newData.add(a);
            }
        }
        int newSize = newData.size();
        System.out.println("Applied node filtering: old size: " + origSize + " new size: " + newSize);
        initAlertAnalysis(newData);
    }

    public void applyDateFilter(Date start, Date end) {
        dateFilterStart = start;
        dateFilterEnd = end;
        ArrayList<AlertData> newData = new ArrayList<AlertData>();
        int origSize = theData.size();
        //get the current alert set
        System.out.println("Start Date:" + start.toGMTString());
        for (AlertData a : theData) {
            System.out.println("\nStart Date:" + start.toGMTString());
            System.out.println("End Date:" + end.toGMTString());
            System.out.println("This Date:" + a.getTheDate().toGMTString());

            if (a.getTheDate().after(start) && a.getTheDate().before(end)) {
                newData.add(a);
            }
        }
        int newSize = newData.size();
        System.out.println("Applied filtering: old size: " + origSize + " new size: " + newSize);
        initAlertAnalysis(newData);
    }

    //this is a full reset
    public void reset() {
        clearOutData();
        pega01ThresholdFilter = -1;
        applicationFilter = null;
        dateFilterStart = null;
        dateFilterEnd = null;
        nodeFilter = null;
        initAlertAnalysis(unfilteredAlerts);

    }

    public double getAlertImpactPct() {
        double duration = (lastAlert.getTime() - firstAlert.getTime()) / (1000 * 60 * 60);    //duraction in hours
        return getAlertImpactPct(theData, duration);
    }

    //NOTE this really isn't throughput its the time over KPI divided by duration
    //returned in seconds per hour
    public double getAlertImpactPct(List<AlertData> data, double duration) {
        double retVal;
        double sumKPIOver = 0;
        for (AlertData a : data) {
            if (!a.getMsgID().equalsIgnoreCase("PEGA0001")) {
                continue;
            }
            sumKPIOver += a.getKpivalue() - a.getKpithreshold();
        }
        return 100 * (((sumKPIOver / 1000) / duration) / 3600);
    }

    public void clearOutData() {
        alertGroups = null;        //for pega01s only
        pega05Groups = null;    //for pega05 groups (last one)
        pega20Groups = null;    //for pega20Groups groups (last one)

        alertsByID = null;
        alertsByType = null;
        alertsByInteractionID = null;
        alertsByHour = null;
        firstAlert = null;
        lastAlert = null;


    }

    private void initAlertAnalysis(List<AlertData> theData) {
        clearOutData();
        this.theData = theData;
        alertsByID = new HashMap<String, AlertData>();
        alertsByInteractionID = new HashMap<String, List<AlertData>>();
        uniqueApps = new ArrayList<String>();
        uniqueNodes = new ArrayList<String>();
        uniqueUsers = new ArrayList<String>();

        for (AlertData a : theData) {
            String uniqInteraction = a.getUniqueInteraction();

            alertsByID.put("" + uniqInteraction, a);
            if (firstAlert == null || a.getTheDate().before(firstAlert)) {
                firstAlert = a.getTheDate();
            }
            if (lastAlert == null || a.getTheDate().after(lastAlert)) {
                lastAlert = a.getTheDate();
            }

            String app = a.getRuleAppName();
            if (!uniqueApps.contains(app)) {
                uniqueApps.add(app);
            }
            String node = a.getServerID();
            if (!uniqueNodes.contains(node)) {
                uniqueNodes.add(node);
            }
            String user = a.getUserID();
            if (!uniqueUsers.contains(user)) {
                uniqueUsers.add(user);
            }

        }
        alertsByHour = this.groupByHourInterval(theData);
        alertsByType = groupByAlertType(theData);
        alertsByInteractionID = groupByInteractionID(theData);
        alertsByNode = groupByNode(theData);
    }

    public List<AlertData> getAllAlerts() {
        return theData;
    }

    public HashMap<String, List<AlertData>> getAllAlertsByInteractionID() {
        return alertsByInteractionID;
    }

    public AlertData getAlertByID(String id) {
        return alertsByID.get(id);
    }

    public HashMap<String, List<AlertData>> getAllAlertsByType() {
        return alertsByType;
    }

    public HashMap<Integer, List<AlertData>> groupByHourInterval(List<AlertData> alerts) {
        HashMap<Integer, List<AlertData>> results = new HashMap<Integer, List<AlertData>>();
        for (AlertData a : alerts) {
            a.setHourInterval(this);
            if (results.containsKey(a.getHourInterval())) {
                results.get(a.getHourInterval()).add(a);
            } else {
                List<AlertData> tmp = new ArrayList<AlertData>();
                tmp.add(a);
                results.put(a.getHourInterval(), tmp);
            }
        }
        return results;
    }

    //for pega01s only
    public AlertGroup getAlertGroup(String groupLabel) {
        List<AlertGroup> groups = getAlertGroups();
        for (AlertGroup g : groups) {
            if (g.getGroupLabel().equals(groupLabel)) {
                return g;
            }
        }
        return null;
    }

    //for pega01s only
    public List<AlertGroup> getAlertGroups() {
        if (alertGroups != null) {
            return alertGroups;
        }

        List<AlertGroup> groups = new ArrayList<AlertGroup>();

        HashMap<String, List<AlertData>> res = pega01ByAction();
        Set<String> keys = res.keySet();
        for (String k : keys) {
            AlertGroup alertGroup = new AlertGroup();
            alertGroup.setAlerts(res.get(k));
            alertGroup.setPalData(AlertAnalysis.summarizePAL(alertGroup.getAlerts()));
            alertGroup.setGroupLabel(k);
            groups.add(alertGroup);
        }
        alertGroups = groups;
        return groups;
    }

    public AlertGroup getPega20ById(String id) {
        if (pega20Groups != null) {
            return pega20Groups.get(id);
        } else {
            return null;
        }
    }

    public AlertGroup getPega05ById(String id) {
        if (pega05Groups != null) {
            return pega05Groups.get(id);
        } else {
            return null;
        }
    }

    public List<AlertGroup> getGroupedPega05(boolean removeConstants) {
        System.out.println("Calling getGroupedPega05 removeConstants:" + removeConstants);
        List<AlertData> pega05s = alertsByType.get("PEGA0005");
        List<AlertGroup> groups = new ArrayList<AlertGroup>();
        HashMap<String, List<AlertData>> res = new HashMap<String, List<AlertData>>();
        for (AlertData a : pega05s) {
            String inserts = getInserts(a.getLine());
            a.setInserts(inserts);

            String eventKey = parse05Line(a.getLine());
            if (removeConstants) {
                eventKey = removeSQLConstants(eventKey);
            }


            if (res.get(eventKey) == null) {
                List<AlertData> tmpArr = new ArrayList<AlertData>();
                tmpArr.add(a);
                res.put(eventKey, tmpArr);
            } else {
                List<AlertData> tmpArr = (List<AlertData>) res.get(eventKey);
                tmpArr.add(a);
            }
        }
        Set<String> keys = res.keySet();
        int cnt = 1;
        pega05Groups = new HashMap<String, AlertGroup>();
        for (String k : keys) {
            cnt++;
            AlertGroup alertGroup = new AlertGroup();
            alertGroup.setAlertID("SQL" + cnt);
            alertGroup.setAlerts(res.get(k));
            alertGroup.setPalData(null);
            alertGroup.setGroupLabel(k);
            groups.add(alertGroup);
            pega05Groups.put(alertGroup.getAlertID(), alertGroup);
        }

        return groups;
    }

    public List<AlertGroup> getGroupedPega20() {
        List<AlertData> pega20s = alertsByType.get("PEGA0020");
        if (pega20s == null) {
            return null;
        }
        List<AlertGroup> groups = new ArrayList<AlertGroup>();
        HashMap<String, List<AlertData>> res = new HashMap<String, List<AlertData>>();
        for (AlertData a : pega20s) {
            String eventKey = parse20Line(a.getLine());
            if (res.get(eventKey) == null) {
                List<AlertData> tmpArr = new ArrayList<AlertData>();
                tmpArr.add(a);
                res.put(eventKey, tmpArr);
            } else {
                List<AlertData> tmpArr = (List<AlertData>) res.get(eventKey);
                tmpArr.add(a);
            }
        }
        Set<String> keys = res.keySet();
        int cnt = 1;
        pega20Groups = new HashMap<String, AlertGroup>();
        for (String k : keys) {
            cnt++;
            AlertGroup alertGroup = new AlertGroup();
            alertGroup.setAlertID(k);
            alertGroup.setAlerts(res.get(k));
            alertGroup.setPalData(null);
            alertGroup.setGroupLabel(k);
            groups.add(alertGroup);
            pega20Groups.put(alertGroup.getAlertID(), alertGroup);
        }

        return groups;
    }

    public HashMap<String, List<AlertData>> pega01ByAction() {
        List<AlertData> tmp = queryByAlertId("PEGA0001");
        HashMap<String, List<AlertData>> res = new HashMap<String, List<AlertData>>();
        for (AlertData a : tmp) {
            String eventKey = a.getFirstAct();
            if (res.get(eventKey) == null) {
                List<AlertData> tmpArr = new ArrayList<AlertData>();
                tmpArr.add(a);
                res.put(eventKey, tmpArr);
            } else {
                List<AlertData> tmpArr = (List<AlertData>) res.get(eventKey);
                tmpArr.add(a);
            }
        }
        return res;
    }

    public List<AlertData> queryByAlertId(String alertID) {
        ArrayList<AlertData> subList = new ArrayList<AlertData>();
        for (AlertData a : theData) {
            if (a.getMsgID().equalsIgnoreCase(alertID)) {
                subList.add(a);
            }
        }
        return subList;
    }

    public Date getFirstAlert() {
        return firstAlert;
    }

    public void setFirstAlert(Date firstAlert) {
        this.firstAlert = firstAlert;
    }

    public Date getLastAlert() {
        return lastAlert;
    }

    public void setLastAlert(Date lastAlert) {
        this.lastAlert = lastAlert;
    }

    public HashMap<Integer, List<AlertData>> getAlertsByHour() {
        return alertsByHour;
    }

    public void setAlertsByHour(HashMap<Integer, List<AlertData>> alertsByHour) {
        this.alertsByHour = alertsByHour;
    }

    public List<String> getUniqueApps() {
        return uniqueApps;
    }

    public List<String> getUniqueUsers() {
        return uniqueUsers;
    }

    public List<String> getUniqueNodes() {
        return uniqueNodes;
    }

    public HashMap<String, List<AlertData>> getAlertsByNode() {
        return alertsByNode;
    }

    public String getTimezone() {
        //System.out.println("getting tz:" + timezone);
        return timezone;
    }

    public void setTimezone(String timezone) {
        //System.out.println("setting timezone "+timezone);
        this.timezone = timezone;
        for (AlertData a : unfilteredAlerts) {
            a.setTimezone(timezone);
        }
    }

    public int getPega01ThresholdFilter() {
        return pega01ThresholdFilter;
    }

    public void setPega01ThresholdFilter(int pega01ThresholdFilter) {
        this.pega01ThresholdFilter = pega01ThresholdFilter;
    }

    public String getApplicationFilter() {
        return applicationFilter;
    }

    public void setApplicationFilter(String applicationFilter) {
        this.applicationFilter = applicationFilter;
    }

    public String getUserFilter() {
        return userFilter;
    }

    public Date getDateFilterStart() {
        return dateFilterStart;
    }

    public void setDateFilterStart(Date dateFilterStart) {
        this.dateFilterStart = dateFilterStart;
    }

    public Date getDateFilterEnd() {
        return dateFilterEnd;
    }

    public void setDateFilterEnd(Date dateFilterEnd) {
        this.dateFilterEnd = dateFilterEnd;
    }

    public String getNodeFilter() {
        return nodeFilter;
    }

    public void setNodeFilter(String nodeFilter) {
        this.nodeFilter = nodeFilter;
    }
}
