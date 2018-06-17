package com.puti.education.util;

import com.puti.education.bean.Teacher;

import java.util.Comparator;

public class TeacherPinyinComparator implements Comparator<Teacher>{

    public int compare(Teacher o1, Teacher o2) {

        if (o2.pinyin.equals("#")) {
            return -1;
        } else if (o1.pinyin.equals("#")) {
            return 1;
        } else {
            return o1.pinyin.compareTo(o2.pinyin);
        }
    }
}
