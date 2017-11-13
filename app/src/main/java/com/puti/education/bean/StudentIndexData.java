package com.puti.education.bean;

import java.util.ArrayList;

/**
 * Created by icebery on 2017/4/25 0025.
 *
 * 学生首页数据
 */

public class StudentIndexData {
    public ArrayList<BehavioralData> behavioralList;
    public int violateRegulations;
    public int synthesisScore;
    public int violateDiscipline;
    public int commend;
    public int anomaly;
    public int dailyBehavior;

    public int academicRecordIndex;
    public int familyIndex;
    public int characterIndex;
    public int schoolAtmosphereIndex;
    public int socialSkillsIndex;
    public int behaviorAbilityIndex;
    public int peerInfluenceIndex;

    public class BehavioralData{
        public String name;
        public int value;
    }
}
