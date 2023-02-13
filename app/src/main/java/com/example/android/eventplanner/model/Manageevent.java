package com.example.android.eventplanner.model;

/**
 * Created by Fascel on 15-04-2018.
 */

public class Manageevent {


    private String ename;
    private String evenue;
    private String etype;
    private String edate;
    private String etime;
    private String uemail;
    private String token;

    public void seteName(String ename) {
        this.ename = ename;
    }
    public void seteVenue(String evenue) {
        this.evenue = evenue;
    }
    public void seteType(String etype) {
        this.etype = etype;
    }
    public void seteDate(String edate) {
        this.edate = edate;
    }
    public void seteTime(String etime) {
        this.etime = etime;
    }
    public void setuEmail(String uemail) {
        this.uemail = uemail;
    }


    public String geteName() {
        return ename;
    }

    public String geteVenue() {
        return evenue;
    }

    public String geteType() {
        return etype;
    }

    public String geteDate() {
        return edate;
    }

    public String geteTime() {
        return etime;
    }


    public void setToken(String token) {
        this.token = token;
    }


}
