package com.puti.education.bean;

import com.puti.education.adapter.ParentFromStudent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjbin on 2017/5/17 0017.
 *
 * 提交审核列表元素 item
 */

public class InvolvedSettingBean {

    public int id;
    public String name;
    public String avartar;
    public String className;

    public String dutyRank;
    public String warningRank;

    public boolean isParentCheck;//家长
    public List<EventAboutPeople> parentList = new ArrayList<>();

    public boolean isStuDePartmentCheck;//学生处

    public boolean isOtherClassLeaderCheck;//其他班主任
    public List<EventAboutPeople> otherLeaderList = new ArrayList<>(5);

    public boolean isFollow;//是否跟踪

    public List<EventAboutPeople> professorList = new ArrayList<>(5);//专家、


    public InvolvedSettingBean(int id, String name,String avartar, String className) {
        this.id = id;
        this.avartar = avartar;
        this.className = className;
        this.name = name;
    }

    public InvolvedSettingBean() {

    }
}
