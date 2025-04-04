package com.example.kyscanner.model;

public class hospitalityModel {
    String name;
    String KyID, College,mobileNo,gmail;
    boolean hasfood,hasAccomodation,hasTshirt;

    public hospitalityModel(String name, String kyID, String college, String mobileNo, String gmail, boolean hasfood, boolean hasAccomodation, boolean hasTshirt) {
        this.name = name;
        this.KyID = kyID;
        this.College = college;
        this.mobileNo = mobileNo;
        this.gmail = gmail;
        this.hasfood = hasfood;
        this.hasAccomodation = hasAccomodation;
        this.hasTshirt = hasTshirt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKyID() {
        return KyID;
    }

    public void setKyID(String kyID) {
        KyID = kyID;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public boolean isHasfood() {
        return hasfood;
    }

    public void setHasfood(boolean hasfood) {
        this.hasfood = hasfood;
    }

    public boolean isHasAccomodation() {
        return hasAccomodation;
    }

    public void setHasAccomodation(boolean hasAccomodation) {
        this.hasAccomodation = hasAccomodation;
    }

    public boolean isHasTshirt() {
        return hasTshirt;
    }

    public void setHasTshirt(boolean hasTshirt) {
        this.hasTshirt = hasTshirt;
    }
}
