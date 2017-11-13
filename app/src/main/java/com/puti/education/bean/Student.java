package com.puti.education.bean;

import java.io.Serializable;

/**
 * Created by xjbin on 2017/4/20 0020.
 *
 *  id	学生id号	整数
    name	学生姓名	字符串
    number	学生学号	字符串
    avatar	学生头像	字符串
    sex	性别	整数	0未知 1 男 2女
    major	所学专业	字符串
 */

public class Student implements Serializable{

    public String uid;
    public String name;
    public String number;
    public String avatar;
    public String sex;
    public String major;
    public String pinyin;

    public String school;

    public String className;
    public boolean isSelected;

    public Student(String uid, String name, String number, String avatar, String sex, String major) {
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.avatar = avatar;
        this.sex = sex;
        this.major = major;
    }

    public Student(){

    }

}
