package com.pega.gsea.util;

import java.util.Comparator;

public class CompareTotalTime implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof AlertGroup) {
            AlertGroup g1 = (AlertGroup) o1;
            AlertGroup g2 = (AlertGroup) o2;
            if (g1.getTotalTime() > g2.getTotalTime()) {
                return -1;
            } else if (g1.getTotalTime() < g2.getTotalTime()) {
                return 1;
            } else {
                return 0;
            }
        } else if (o1 instanceof AlertData) {
            AlertData g1 = (AlertData) o1;
            AlertData g2 = (AlertData) o2;
            if (g1.getKpivalue() > g2.getKpivalue()) {
                return -1;
            } else if (g1.getKpivalue() < g2.getKpivalue()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

}
