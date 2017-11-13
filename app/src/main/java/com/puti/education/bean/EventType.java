package com.puti.education.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xbjin on 2017/5/10 0010.
 *
 * 事件类型
 *
 * id	事件类型ID	整数
   name	事件类型名称	字符串
   child	子事件类型	数组对象

 */

public class EventType {

    public int id;
    public String name;
    public boolean bAbnormal;
    public List<EventType> child = new ArrayList<>();
    public boolean haveChild;

    public EventType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public EventType() {
    }
}
