package com.nawinc27.mac.blooddonorfrontend;

import android.util.Log;

public class ListItemHistory {

    int times;
    String date;
    String location;
    String hospital;


    public ListItemHistory(int times, String date, String location, String hospital) {
        this.times = times;
        this.date = "วันที่  " + date;
        this.location = "สถานที่ที่บริจาค  " + location;
        this.hospital = "โรงพยาบาลที่รับบริจาค  " +hospital;


    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
