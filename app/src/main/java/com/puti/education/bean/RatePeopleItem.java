package com.puti.education.bean;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 互评人的评价详情
 */

public class RatePeopleItem {
    public String uid;
    public String type;                    //1教师，2学生，4家长

    public int studyRate;               //学习评价
    public int relationRate;            //关系评价
    public String synthesisRate;        //综合评价

    public int teacherRate;   //教学水平评价
    public int mannerRate;    //态度评价
    public int managerRate;   //管理水平评价
    public int cooperateRate; //家校协同能力

    public int matchSchoolRate;     //与学校配合程度评价
    public int childEducationRate;  //子女教育方式方法
    public int scoreEmphasisRate;   //子女成绩重视程度
    public int trainEmphasisRate;   //子女行为培养重视程度


}
