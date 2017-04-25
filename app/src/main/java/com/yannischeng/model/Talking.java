package com.yannischeng.model;

import java.io.Serializable;

/**
 *
 * Created by 程文佳 on 2016/11/25.
 */

public class Talking implements Serializable {

    private String stuID, vName, vID, date, content, vClassID;

    public Talking(String stuID, String vName, String vID, String date, String content, String vClassID) {
        this.stuID = stuID;
        this.vName = vName;
        this.vID = vID;
        this.date = date;
        this.content = content;
        this.vClassID = vClassID;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvID() {
        return vID;
    }

    public void setvID(String vID) {
        this.vID = vID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getvClassID() {
        return vClassID;
    }

    public void setvClassID(String vClassID) {
        this.vClassID = vClassID;
    }

    @Override
    public String toString() {
        return "Talking{" +
                "stuID='" + stuID + '\'' +
                ", vName='" + vName + '\'' +
                ", vID='" + vID + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", vClassID='" + vClassID + '\'' +
                '}';
    }
}
