package com.puti.education.bean;

/**
 * Created by xjbin on 2017/5/6 0006.
 *
 * 年度事件
 *
 *   data	时间（年月）	字符串	“2017-04”
     count	事件个数	整数

 */

public class YearEventsItem {

    public String data;
    public int count;

    public YearEventsItem(int count , String date) {
        this.data = date;
        this.count = count;
    }

    public YearEventsItem() {

    }
}
