package com.puti.education.bean;

import java.io.Serializable;

/**
 * Created by xjbin on 2017/4/21 0021.
 *
 * 教师端 --添加事件 --（涉事人，知情人综合bean)
 */

public class EventAboutPeople implements Serializable{

    public String avatar;
    public String name;
    public String uid;

    public String dutyType;        //责任等级
    public String involveType;  //责任等级字符串形式
    public String qrcode;

    public int type;  //1.教师；2.学生; 4家长; 5专家

    public boolean isPeople = true;//标识该类实例是否是空值

    public EventAboutPeople(){

    }

    public EventAboutPeople(boolean isPeople){
        this.isPeople = isPeople;
    }

    public EventAboutPeople(String avatar, String name, String uid, int type) {
        this.avatar = avatar;
        this.name = name;
        this.uid = uid;
        this.type = type;
    }

    public EventAboutPeople(String id, String name, int type) {
        this.uid = id;
        this.name = name;
        this.type = type;
    }
}
