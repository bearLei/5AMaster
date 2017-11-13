package com.puti.education.bean;

/**
 *
 *          最新通知消息
 *
             id	消息id	整数
             title	消息标题	字符串
             content	消息内容	字符串
             bizTime	消息创建时间	字符串	“2017-04-08 11:14:23”
             type	类型	数值
                 1.	在线调查
                 2.	互评
                 3.	通知消息
                 4.	事件佐证（老师）
                 5.	事件分析（家长）
                 6.	驳回
                 7.	推送
                 8.	问卷消息（告诉老师问卷写完了）
                 9.	警示事件


 *
 */

public class NewNotice {

    public String uid;
    public String type;
    public String view;
    public String bizTime;
    public String alertMsg;
    public ExtContent extContent;

    public class ExtContent{
        public int subType;
        public String value;
        public String valueExt;
    }
}
