package com.puti.education.ui.uiTeacher.chooseperson;

/**
 * created by lei at 2017/12/30
 */

public class ChoosePersonParameter {
    public final static String EVENT_ABNORMOL = "event_abnormal";
    public static final String REFER = "refer";
    public final static String DUTY_TYPE = "duty_type";

    public static final int REFER_DUTY_ALL = 11;//来自责任选择页面-证人知情人
    public static final int REFER_DUTY_STUDENGT = 12;//来自责任选择页面-涉事人
    public static final int REFER_QS_STU = 21;//来自问卷详情选择学生
    public static final int REFER_QS_TEA = 22;//来自问卷详情选择教师
    public static final int REFER_STU_REPORT_ADD_STU = 31;//学生举报页面_选择学生
    public static final int REFER_STU_REPORT_ADD_TEA = 32;//学生举报页面_选择老师+学生
    public static final int REFER_STU_REPORT_DETAIL_STU = 33;//学生举报详情页面_选择学生
    public static final int REFER_STU_REPORT_DETAIL_TEA = 34;//学生举报详情页面_选择教师+学生
    public static final int REFER_TEA_ADD_EVENT = 41;//教师添加事件页面

     public static final int RESULT_QS_STU  = 111;
     public static final int RESULT_QS_TEA   = 112;


    public static  int LIMIT = 0;//剩余可选的个数；
}
