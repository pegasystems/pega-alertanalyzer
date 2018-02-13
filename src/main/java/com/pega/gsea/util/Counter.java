package com.pega.gsea.util;

public class Counter {

    String id;
    double cnt;

    public Counter(String id, double cnt) {
        super();
        this.id = id;
        this.cnt = cnt;
    }

    public Counter(String id) {
        super();
        this.id = id;
        this.cnt = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCnt() {
        return cnt;
    }

    public void setCnt(double cnt) {
        this.cnt = cnt;
    }

    public void incCnt() {
        this.cnt++;
    }


}
