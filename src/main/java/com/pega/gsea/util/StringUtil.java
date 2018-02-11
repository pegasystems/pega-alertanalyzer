package com.pega.gsea.util;

import java.text.DecimalFormat;

public class StringUtil {

    public static String formatDouble(Double input) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setMinimumIntegerDigits(1);
        return df.format(input);
    }

    public static String sqlFmt(String sql) {
        String formattedSQL = new SQLFormatter().format(sql);
        return formattedSQL;
    }


    public static void main(String[] args) {
        System.out.println(sqlFmt("select a,b,c,(select z from q) as t from tbl where x=1 group by q"));
    }
}
