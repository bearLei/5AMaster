package com.puti.education.util;

import com.puti.education.bean.Professor;

import java.util.Comparator;

public class ProfessorPinyinComparator implements Comparator<Professor>{

    public int compare(Professor o1, Professor o2) {

        if (o2.pinyin.equals("#")) {
            return -1;
        } else if (o1.pinyin.equals("#")) {
            return 1;
        } else {
            return o1.pinyin.compareTo(o2.pinyin);
        }
    }
}
