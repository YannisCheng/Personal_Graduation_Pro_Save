package com.yannischeng.bean;

import java.util.List;

public class MulObj {

    private List<OfficialStudentInfo> list;
    private String manCount;
    private String WomanCount;

    public MulObj(List<OfficialStudentInfo> list, String manCount, String womanCount) {
        this.list = list;
        this.manCount = manCount;
        WomanCount = womanCount;
    }

    public void setManCount(String manCount) {
        this.manCount = manCount;
    }

    public void setWomanCount(String womanCount) {
        WomanCount = womanCount;
    }

    public String getManCount() {
        return manCount;
    }

    public String getWomanCount() {
        return WomanCount;
    }

    public List<OfficialStudentInfo> getList() {
        return list;
    }

    public void setList(List<OfficialStudentInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MulObj{" +
                "list=" + list +
                ", manCount='" + manCount + '\'' +
                ", WomanCount='" + WomanCount + '\'' +
                '}';
    }
}
