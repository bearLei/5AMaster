package com.puti.education.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class Questionnaire {
    public String uid;
    public String name;
    public String type;
    public String status;

    public String title;
    public String desc;
    public String result;
    public ArrayList<Question> items;

    public String courseName;
    public String teacherName;
    public int targetType;
}
