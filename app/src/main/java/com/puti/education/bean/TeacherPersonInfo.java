package com.puti.education.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xjbin on 2017/5/12 0012.
 *
 * id	id	整数
 * number	教师工号	字符串
 * school	教师所在学校	字符串
 * teachMajor	任课专业	字符串
 * className	所在班级	字符串
 * workingAge	工龄	数值
 * joinDate	入职时间	字符串
 * name	姓名	字符串
 * idcard	身份证	字符串
 * sex	性别	字符串
 * nation	民族	字符串
 * mobile	电话	字符串
 * birthday	出生日期	字符串
 * stateless 	国籍	字符串
 * maritalStatus	婚否	布尔
 */

public class TeacherPersonInfo implements Serializable{
    public  String uid;
    public String name;
    public String code;
    public String department;
    public String professional;

    public String idcard;
    public String sex;
    public String nation;
    public String phone;
    public String photo;
    public String maritalStatus;

    public String nature;
    public String birthday;
    public String nationality;  //国家
    public String national;     //民族

    public String school;
    public boolean bStudentAffairs;  //是否学生处老师
    public ArrayList<ClassInfo> classList;

    public String teachMajor;

    public String workingAge;
    public String startYear;

    public String stateless;
    public String registerType;
}
