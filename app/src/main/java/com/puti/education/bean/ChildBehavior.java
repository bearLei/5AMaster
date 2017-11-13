package com.puti.education.bean;

/**
 * Created by xjbin on 2017/5/22 0022.
 * <p>
 * name	特征名称	字符串	如家庭特征、同伴影响、行为能力、社交能力、学校氛围、性格特征
 * value	行为分析数据值	整数
 */

public class ChildBehavior {
    public String uid;
    public String name;
    public double academicRecordIndex;
    public double familyIndex;
    public double characterIndex;
    public double schoolAtmosphereIndex;
    public double socialSkillsIndex;
    public double behaviorAbilityIndex;
    public double peerInfluenceIndex;

    public int violateRegulations;
    public int violateDiscipline;
    public int commend;
    public int anomaly;
    public int dailyBehavior;
}
