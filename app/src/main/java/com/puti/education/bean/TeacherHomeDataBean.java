package com.puti.education.bean;

import java.util.List;

/**
 * Created by xjbin on 2017/5/6 0006.
 *
 * 教师首页数据bean
 *
 * weekEventList	本周事件	json对象数组	本周事件数量
   yearEventsList	年度事件	json对象数组	年度事件数量
   behavioralList	教师行为数据	json对象数组
   powerData	教师能力分	json对象数组
   experienceData	教师经验分	json对象数组

 *
 */

public class TeacherHomeDataBean {

    public List<WeekEventLineData> weekEventList;
    public List<YearEventLineData> yearEventsList;
    public List<TeacherBehaviorBean> behavioralList;
    public List<TeacherPower> powerData;
    public List<TeacherExperience> experienceData;
    public List<TodayEventBean> todayEventsList;

}
