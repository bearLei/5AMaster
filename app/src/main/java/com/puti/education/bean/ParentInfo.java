package com.puti.education.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xjibn on 2017/5/25 0025.
 *
 * 家长个人详情
 *
         id	id	整数
         name	姓名	字符串
         idcard	身份证	字符串
         sex	性别	字符串
         nation	民族	字符串
         mobile	电话	字符串
         birthday	出生日期	字符串
         age	年龄	整数
         accountNature 	户口性质	字符串
         register	户籍	字符串
         address	家庭住址	字符串
         education	学历	字符串
         job	职业职业	字符串
         volunteerNumber	义工编号	字符串
         childList	子女信息	对象数组

 */

public class ParentInfo implements Serializable{
    public String uid;
    public String name;
    public String idCard;
    public String sex;
    public String nation;
    public String mobile;
    public String birthday;
    public String age;
    public String accountNature;
    public String register;
    public String address;
    public String education;
    public String job;
    public String volunteerNumber;
    public List<Student> childList;
}
