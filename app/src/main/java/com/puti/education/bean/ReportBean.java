package com.puti.education.bean;

import java.io.Serializable;
import java.util.ArrayList;


//举报
public class ReportBean implements Serializable {
    public String uid;
    public String time;
    public String desc;
    public int confirm;  //0未确认1已确认2已拒绝
    public UserDetail user;
    public ArrayList<Proof> records;

}
