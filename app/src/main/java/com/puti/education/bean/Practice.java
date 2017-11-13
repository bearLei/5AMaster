package com.puti.education.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xjbin on 2017/5/16 0016.
 *
 * id	实践记录id	整数
 * content	实践内容	字符串
 * title	实践标题	字符串
 * time	实践时间	字符串
 */

public class Practice implements Serializable{

    public String uid;
    public String type;
    public String typename;
    public String title;
    public String content;
    public String desc;
    public String username;

    public String time;
    public String result;
    public String address;

    public String name;
    public List<String> photo;

}
