package com.example.android.eventplanner.model;

/**
 * Created by Fascel on 18-04-2018.
 */

public class Task {

    private String tname;
    private String tnotes;
    private String uemail;
    private String token;

    public void setTname(String tname) {
        this.tname=tname;
    }
    public void setTnotes(String tnotes) { this.tnotes = tnotes; }

    public void setuEmail(String uemail) {
        this.uemail = uemail;
    }


    public String getTName() {return tname; }
    public String getTNotes() {return tnotes; }

    public String getuEmail() {
        return uemail;
    }



    public void setToken(String token) {
        this.token = token;
    }


}
