package com.puti.education.bean;

import java.util.List;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 事件详情---事件评价
 */

public class Comment{
    public String desc;
    List<CommentValue> commentValue;//比较数组

    class CommentValue{
        public String type;
        public String time;
        public float value;
    }

}



