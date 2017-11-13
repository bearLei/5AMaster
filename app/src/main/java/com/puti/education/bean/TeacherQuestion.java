package com.puti.education.bean;

/**
 *  教师问券列表 元素
 * id	问卷id	整数
   title	问卷标题	字符串
   question	问卷内容	数组	[“你还好吗？”, “你叫什么名字？”]
 */

public class TeacherQuestion {

    public int id;
    public String title;
    public String question;

    public TeacherQuestion(String title, String question) {
        this.title = title;
        this.question = question;
    }

    public TeacherQuestion() {
    }
}
