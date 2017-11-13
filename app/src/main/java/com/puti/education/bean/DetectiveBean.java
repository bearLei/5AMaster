package com.puti.education.bean;


import java.io.Serializable;
import java.util.ArrayList;

//巡检
public class DetectiveBean implements Serializable {

    public TaskBean task;    //巡检任务UID
    public String patrolUID;  //巡检结果UID

    public String time;
    public String address;
    public String lat;
    public String lng;

    public String result;
    public int confirm; //0未确认 1已确认 2已拒绝

    public UserDetail submiter;
    public ArrayList<Proof> records;

    public static class TaskBean implements Serializable{
        public String taskUID;
        public int   type;
        public UserDetail publisher;
        public String title;
        public String lat;
        public String lng;
        public String startTime;
        public String endTime;

    }


}
