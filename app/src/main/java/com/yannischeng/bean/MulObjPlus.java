package com.yannischeng.bean;

import java.util.List;

public class MulObjPlus {

    private List<OfficialStudentInfo> list;
    private String manCount;
    private String WomanCount;
    private String manCAll;
    private String WomanCAll;

    public MulObjPlus(List<OfficialStudentInfo> list, String manCount, String womanCount, String manCAll, String womanCAll) {
        this.list = list;
        this.manCount = manCount;
        WomanCount = womanCount;
        this.manCAll = manCAll;
        WomanCAll = womanCAll;
    }

    public List<OfficialStudentInfo> getList() {
        return list;
    }

    public void setList(List<OfficialStudentInfo> list) {
        this.list = list;
    }

    public String getManCount() {
        return manCount;
    }

    public void setManCount(String manCount) {
        this.manCount = manCount;
    }

    public String getWomanCount() {
        return WomanCount;
    }

    public void setWomanCount(String womanCount) {
        WomanCount = womanCount;
    }

    public String getManCAll() {
        return manCAll;
    }

    public void setManCAll(String manCAll) {
        this.manCAll = manCAll;
    }

    public String getWomanCAll() {
        return WomanCAll;
    }

    public void setWomanCAll(String womanCAll) {
        WomanCAll = womanCAll;
    }

    @Override
    public String toString() {
        return "MulObjPlus{" +
                "list=" + list +
                ", manCount='" + manCount + '\'' +
                ", WomanCount='" + WomanCount + '\'' +
                ", manCAll='" + manCAll + '\'' +
                ", WomanCAll='" + WomanCAll + '\'' +
                '}';
    }
}
