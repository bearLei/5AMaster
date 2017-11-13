package com.puti.education.bean;

import java.io.Serializable;

/**
 * Created by xjbin on 2017/4/20 0020.
 *
 *    id	老师id号	整数
    name	老师姓名	字符串
    number	老师编号	字符串
    avatar	老师头像	字符串
    sex	性别	整数	0未知 1 男 2女
    IsHeadTeacher	是否班主任	布尔
    teachMajor	所教专业	字符串

 */

public class Teacher implements Serializable {

    public String uid;
    public String name;
    public String number;
    public String avatar;
    public int sex;
    public boolean IsHeadTeacher;
    public String  teachMajor;

    public String pinyin;
    public String className;

    public boolean isSelected;

    public Teacher(String name) {
        this.name = name;
    }
    public Teacher(){}
}
