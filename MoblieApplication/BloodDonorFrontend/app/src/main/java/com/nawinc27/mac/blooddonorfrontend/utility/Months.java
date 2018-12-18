package com.nawinc27.mac.blooddonorfrontend.utility;

import java.util.HashMap;
import java.util.Map;

public class Months {

    private static Months instance;
    private static Map<String, String> months = new HashMap<String, String>();

    private Months(){

        this.months.put("1","มกราคม");
        this.months.put("2","กุมภาพันธ์");
        this.months.put("3","มีนาคม");
        this.months.put("4","เมษายน");
        this.months.put("5","พฤษภาคม");
        this.months.put("6","มิถุนายน");
        this.months.put("7","กรกฎาคม");
        this.months.put("8","สิงหาคม");
        this.months.put("9","กันยายน");
        this.months.put("10","ตุลาคม");
        this.months.put("11","พฤศจิกายน");
        this.months.put("12","ธันวาคม");

    }

    public static Months getInstance(){
        if (instance == null){
            instance = new Months();
        }

        return instance;
    }

    public Map<String, String> getMonths() {
        return months;
    }

    public void setMonths(Map<String, String> months) {
        this.months = months;
    }
}
