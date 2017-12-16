package com.puti.education.bean;

/**
 * Created by xjbin on 2017/5/6 0006.
 *
 *  data	时间（年月）	字符串	“2017-04”
    value	行为分析数据值	整数
 *
 */

public class TeacherPower {

    public String data;
    public float value;

    public TeacherPower(String date, int value) {
        this.data = date;
        this.value = value;
    }

    public TeacherPower() {
    }
}
