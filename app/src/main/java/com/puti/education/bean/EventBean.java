package com.puti.education.bean;

import java.util.List;


//事件
public class EventBean {

    public String eventuid;
    public String eventtime;
    public String evenaddress;
    public int  eventlevel;
    public int  eventtype;
    public String eventtypename;
    public String eventdescription;
    public int eventcategories;  //1普通事件，2异常事件
    public String event2involveduid;
    public String involvedtype;  //主要事件人，次要事件人

    public String studentuid;
    public String studentname;
    public String studentphoto;

    public int status; //0:已拒绝，1:待确认，2:已确认, 3:待审核, 4:待追踪, 5:已结案
    public String statusname;

    public String reason;
    public String classuid;
    public String classname;
    public String headteacheruid;

    public boolean canTrack;

    public List<InvolvedPeople> involvedPeople;

    //涉事人
    public class InvolvedPeople{

        public String name;
        public String avatar;
    }




}
