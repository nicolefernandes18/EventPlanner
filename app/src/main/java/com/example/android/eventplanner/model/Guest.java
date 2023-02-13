package com.example.android.eventplanner.model;

/**
 * Created by Fascel on 15-04-2018.
 */

public class Guest {


        private String gname;
        private String gcontact;
        private String gemail;
        private String uemail;
        private String token;

        public void setgName(String gname) {
            this.gname = gname;
        }
        public void setgContact(String gcontact) {
            this.gcontact = gcontact;
        }
        public void setgEmail(String gemail) {
            this.gemail = gemail;
        }
        public void setuEmail(String uemail) {
        this.uemail = uemail;
    }


        public String getgName() {return gname; }
        public String getgContact() {return gcontact; }
        public String getgEmail() {
            return gemail;
        }
    public String getuEmail() {
        return uemail;
    }



        public void setToken(String token) {
            this.token = token;
        }


}
