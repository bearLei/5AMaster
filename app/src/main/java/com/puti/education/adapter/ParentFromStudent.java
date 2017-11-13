package com.puti.education.adapter;

import java.io.Serializable;

/**
 * Created by xjbin on 2017/5/11 0011.
 *
 * 学生家长信息
 */

public class ParentFromStudent implements Serializable {

    public String uid;
    public String name;
    public String avatar;
    public boolean isSelected;//是否选中
    public String childId;//孩子id
    public boolean isPeople;

    public ParentFromStudent(){
    }

    public ParentFromStudent(boolean isPeople){
        this.isPeople = isPeople;
    }

    public ParentFromStudent(String uid, String name, String avatar,String childId) {
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
        this.childId = childId;
        this.isPeople = true;
    }
}
