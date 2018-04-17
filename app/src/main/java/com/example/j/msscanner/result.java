package com.example.j.msscanner;

public class result {
    private String title;
    private String msurl;
    private String qpurl;

    public result(String title, String msurl, String qpurl) {
        this.title = title;
        this.msurl = msurl;
        this.qpurl = qpurl;
    }

    public String getTitle() {
        return title;
    }
    public String getMsurl() {
        return msurl;
    }
    public String getQpurl() {
        return qpurl;
    }
}
