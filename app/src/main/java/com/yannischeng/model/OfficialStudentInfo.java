package com.yannischeng.model;

import java.io.Serializable;

/**
 * 数据库中的学生个人信息（官方）
 *
 * Created by 程文佳   2016年11月14日18:41:56
 */
public class OfficialStudentInfo implements Serializable {

    private String idOfficialStu;    //学号 - 1
    private String idCardOfficialStu;    //身份证号 - 2
    private String nameStu;    //姓名 - 3
    private String nationStu;    //民族 - 4
    private String sexStu;     //性别 - 5
    private String dateStu;    //出生日期 - 6
    private String heightStu;    //身高 - 7
    private String weightStu;    //体重 - 8
    private String addressStu;    //家庭住址 - 9
    private int stuId;       //在数据库中的序号 - 10
    private int IdAll;       //在所有学生中的ID值 - 11
    private int classId;     //所在班级的ID值 - 12

    public OfficialStudentInfo() {
    }

    public OfficialStudentInfo(String idOfficialStu, String idCardOfficialStu, String nameStu, String nationStu,
                               String sexStu, String dateStu, String heightStu, String weightStu, String addressStu, int id, int idAll,
                               int classId) {
        super();
        this.idOfficialStu = idOfficialStu;
        this.idCardOfficialStu = idCardOfficialStu;
        this.nameStu = nameStu;
        this.nationStu = nationStu;
        this.sexStu = sexStu;
        this.dateStu = dateStu;
        this.heightStu = heightStu;
        this.weightStu = weightStu;
        this.addressStu = addressStu;
        stuId = id;
        IdAll = idAll;
        this.classId = classId;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public int getIdAll() {
        return IdAll;
    }

    public void setIdAll(int idAll) {
        IdAll = idAll;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getIdOfficialStu() {
        return idOfficialStu;
    }

    public void setIdOfficialStu(String idOfficialStu) {
        this.idOfficialStu = idOfficialStu;
    }

    public String getIdCardOfficialStu() {
        return idCardOfficialStu;
    }

    public void setIdCardOfficialStu(String idCardOfficialStu) {
        this.idCardOfficialStu = idCardOfficialStu;
    }

    public String getNameStu() {
        return nameStu;
    }

    public void setNameStu(String nameStu) {
        this.nameStu = nameStu;
    }

    public String getNationStu() {
        return nationStu;
    }

    public void setNationStu(String nationStu) {
        this.nationStu = nationStu;
    }

    public String getSexStu() {
        return sexStu;
    }

    public void setSexStu(String sexStu) {
        this.sexStu = sexStu;
    }

    public String getDateStu() {
        return dateStu;
    }

    public void setDateStu(String dateStu) {
        this.dateStu = dateStu;
    }

    public String getHeightStu() {
        return heightStu;
    }

    public void setHeightStu(String heightStu) {
        this.heightStu = heightStu;
    }

    public String getWeightStu() {
        return weightStu;
    }

    public void setWeightStu(String weightStu) {
        this.weightStu = weightStu;
    }

    public int getNumId() {
        return stuId;
    }

    public void setNumId(int numId) {
        this.stuId = numId;
    }

    public String getAddressStu() {
        return addressStu;
    }

    public void setAddressStu(String addressStu) {
        this.addressStu = addressStu;
    }

    @Override
    public String toString() {
        return "OfficialStudentInfo [idOfficialStu=" + idOfficialStu + ", idCardOfficialStu=" + idCardOfficialStu
                + ", nameStu=" + nameStu + ", nationStu=" + nationStu + ", sexStu=" + sexStu + ", dateStu=" + dateStu
                + ", heightStu=" + heightStu + ", weightStu=" + weightStu + ", addressStu=" + addressStu + ", stuId="
                + stuId + ", IdAll=" + IdAll + ", classId=" + classId + "]";
    }

}
