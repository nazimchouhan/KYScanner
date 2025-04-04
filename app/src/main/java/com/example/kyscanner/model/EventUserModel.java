package com.example.kyscanner.model;

public class EventUserModel {
    String KYID,Name,PhoneNo,CollegeName;

    public EventUserModel(String KYID, String name, String phoneNo, String collegeName) {
        this.KYID = KYID;
        this.Name = name;
        this.PhoneNo = phoneNo;
        this.CollegeName = collegeName;
    }

    public String getKYID() {
        return KYID;
    }

    public void setKYID(String KYID) {
        this.KYID = KYID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getCollegeName() {
        return CollegeName;
    }

    public void setCollegeName(String collegeName) {
        CollegeName = collegeName;
    }
}
