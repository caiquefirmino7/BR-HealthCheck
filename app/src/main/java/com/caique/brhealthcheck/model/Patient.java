package com.caique.brhealthcheck.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patients")
public class Patient {

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo( name = "age")
    private String age;

    @ColumnInfo(name = "body_temperature")
    private String bodyTemperature;

    @ColumnInfo(name = "cough_period_days")
    private String  coughPeriodDays;

    @ColumnInfo(name = "headache_period_days")
    private String headachePeriodDays;


    //construtor
    public Patient(String name, String age, String bodyTemperature, String coughPeriodDays, String headachePeriodDays) {
        this.name = name;
        this.age = age;
        this.bodyTemperature = bodyTemperature;
        this.coughPeriodDays = coughPeriodDays;
        this.headachePeriodDays = headachePeriodDays;
    }


    //getters e setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getCoughPeriodDays() {
        return coughPeriodDays;
    }

    public void setCoughPeriodDays(String coughPeriodDays) {
        this.coughPeriodDays = coughPeriodDays;
    }

    public String getHeadachePeriodDays() {
        return headachePeriodDays;
    }

    public void setHeadachePeriodDays(String headachePeriodDays) {
        this.headachePeriodDays = headachePeriodDays;
    }


}
