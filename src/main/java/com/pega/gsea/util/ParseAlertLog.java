package com.pega.gsea.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ParseAlertLog {

    public static List<AlertData> getDataFromCSVFile(String file) throws Exception {
        BufferedReader fr = new BufferedReader(new FileReader(file));
        return getDataFromCSVFile(fr);
    }

    public static List<AlertData> getDataFromCSVFile(BufferedReader fr) throws Exception {
        ArrayList<AlertData> data = new ArrayList<AlertData>();
        String readline = null;

        readline = fr.readLine();
        readline = fr.readLine();
        int count = 0;
        while (readline != null) {
            //System.out.println(s);
            count++;
            if (count % 250 == 0) {
                System.out.println(count);
            }
            AlertData alertData = AlertData.buildAlertDataFromCSVRecord(readline);
            data.add(alertData);
            readline = fr.readLine();
            //skip until we find sequence;
        }
        return data;
    }

    public static List<AlertData> getDataFromRawLog(String file) throws Exception {
        BufferedReader fr = new BufferedReader(new FileReader(file));
        return getDataFromRawLog(fr);
    }

    public static List<AlertData> getDataFromRawLog(BufferedReader fr) throws Exception {
        ArrayList<AlertData> data = new ArrayList<AlertData>();
        String readline = null;

        readline = fr.readLine();
        int count = 0;
        while (readline != null) {
            count++;
            if (count % 250 == 0) {
                System.out.println(count);
            }
            AlertData alertData = AlertData.buildAlertDataFromRawLog(readline);
            if (alertData != null) {
                data.add(alertData);
            }
            readline = fr.readLine();
            //skip until we find sequence;
        }
        return data;
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String file = "C:\\Documents and Settings\\apapf\\My Documents\\Clients\\RX\\AlertLog.csv";
        String readline = null;
        try {
            BufferedReader fr = new BufferedReader(new FileReader(file));
            readline = fr.readLine();
            readline = fr.readLine();
            while (readline != null) {
                AlertData alertData = AlertData.buildAlertDataFromCSVRecord(readline);
                System.out.println(alertData.getCategory());
                System.out.println(alertData.getThreadName());
                System.out.println(alertData.getPalData());
                readline = fr.readLine();
                //skip until we find sequence;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(readline);
        }

    }

}
