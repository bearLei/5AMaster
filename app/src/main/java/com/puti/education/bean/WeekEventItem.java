package com.puti.education.bean;

/**
 * Created by xjbin on 2017/5/6 0006.
 *
 *   周事件
 *
 *  data	时间	字符串	“2017-04-08”
    count	事件个数	整数

 */

public class WeekEventItem {

    public String data;
    public int count;

    public WeekEventItem(int count,String date) {
        this.data = date;
        this.count = count;
    }

    public WeekEventItem() {

    }
}
