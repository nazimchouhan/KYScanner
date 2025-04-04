package com.example.kyscanner.model;

public class UserModel {
    String KyId;
    String gender;
    String gmail;
    String name;
    boolean event1;
    boolean event2;
    boolean event3;

    public UserModel(String KyId,String gender,String name,String gmail,boolean event1,boolean event2,boolean event3) {
        this.KyId=KyId;
        this.gender=gender;
        this.name = name;
        this.gmail = gmail;
        this.event1=event1;
        this.event2=event2;
        this.event3=event3;
    }

    public String getGmail() {
        return gmail;
    }
    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKyId() {
        return KyId;
    }

    public void setKyId(String kyId) {
        KyId = kyId;
    }

    public boolean isEvent1() {
        return event1;
    }

    public void setEvent1(boolean event1) {
        this.event1 = event1;
    }

    public boolean isEvent2() {
        return event2;
    }

    public void setEvent2(boolean event2) {
        this.event2 = event2;
    }

    public boolean isEvent3() {
        return event3;
    }

    public void setEvent3(boolean event3) {
        this.event3 = event3;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
