package com.puti.education.util;

import com.puti.education.bean.Student;

import java.util.Comparator;

public class StudentPinyinComparator implements Comparator<Student>{

    public int compare(Student o1, Student o2) {

        if (o2.pinyin.equals("#")) {
            return -1;
        } else if (o1.pinyin.equals("#")) {
            return 1;
        } else {
            return o1.pinyin.compareTo(o2.pinyin);
        }
    }
}
