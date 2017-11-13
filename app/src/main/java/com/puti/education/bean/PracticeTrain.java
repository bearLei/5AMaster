package com.puti.education.bean;

import java.util.List;

public class PracticeTrain {

    public String id;
    public String time;

    public String address;
    public int type;

    public String name;
    public String desc;
    public String content;
    public String result;

    public String startTime;
    public String endTime;

    public boolean isNeedLink;
    public boolean isPushHeadTeacher;
    public List<EventAboutPeople> headTeacherList;

    public List<EventAboutPeople> childs;
    public List<String> photo;

    public static class HeadTeacher{
        public int id;
        public String name;
        public String avatar;
    }

}
