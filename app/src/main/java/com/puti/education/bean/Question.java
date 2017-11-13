package com.puti.education.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class Question {
    public String uid;
    public int type; //1.单选；2.多选；3.判断；4.文字填空
    public String categoryName;
    public String question;
    public String optionAnswer;  //"A, 非常肯定; B,不确定"

    public boolean isAnswerd; //是否回答
    public String answerd;   //[1],[2,3],[1/0]["你好啊"]
    public String answerd2;   //铺佑显示作用

}
