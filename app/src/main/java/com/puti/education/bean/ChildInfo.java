package com.puti.education.bean;

import java.util.List;

/**
 * Created by xjbin on 2017/5/22 0022.
 *
 * 家长端首页--孩子信息
 *
 * name	小孩名称	字符串	如家庭特征、同伴影响、行为能力、社交能力、学校氛围、性格特征
 * behavioralList	行为分析数据	对象数组
 * violation	违纪次数	整数
 * commend	表彰次数	整数
 * anomaly	异常行为	整数
 *
 */

public class ChildInfo {

    public int relation;
    public String relationName;
    public ChildBehavior student;
}
