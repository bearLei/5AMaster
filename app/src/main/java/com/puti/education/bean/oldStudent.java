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

public class oldStudent implements Serializable{

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

    public oldStudent(String uid, String name, String number, String avatar, String sex, String major) {
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.avatar = avatar;
        this.sex = sex;
        this.major = major;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public oldStudent(){

    }

}
