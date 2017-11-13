package com.puti.education.bean;

/**
 * Created by icebery on 2017/4/25 0025.
 *
 * 登录用户
 */

public class LoginUser {
    public int type;        //用户类型1教师，2学生，4家长，5专家，3社会义工人员
    public String uid;
    public String token;
    public String name;
    public String avatar;
    public boolean isVolunteer;
    public String volunteerNo;
    public boolean isUpdated;
}
