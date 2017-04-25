package com.yannischeng.model;

import java.io.Serializable;

/**
 * 用户自定义信息model类
 *
 * Created by 程文佳
 */
public class CustomeInfo implements Serializable {

    private String addressNow;
    private String jobNow;
    private String connectNow;
    private String otherNow;

    public CustomeInfo(String addressNow, String jobNow, String connectNow, String otherNow) {
        super();
        this.addressNow = addressNow;
        this.jobNow = jobNow;
        this.connectNow = connectNow;
        this.otherNow = otherNow;
    }

    public String getAddressNow() {
        return addressNow;
    }

    public void setAddressNow(String addressNow) {
        this.addressNow = addressNow;
    }

    public String getJobNow() {
        return jobNow;
    }

    public void setJobNow(String jobNow) {
        this.jobNow = jobNow;
    }

    public String getConnectNow() {
        return connectNow;
    }

    public void setConnectNow(String connectNow) {
        this.connectNow = connectNow;
    }

    public String getOtherNow() {
        return otherNow;
    }

    public void setOtherNow(String otherNow) {
        this.otherNow = otherNow;
    }

    @Override
    public String toString() {
        return "CustomeInfo [addressNow=" + addressNow + ", jobNow=" + jobNow + ", connectNow=" + connectNow
                + ", otherNow=" + otherNow + "]";
    }


}
