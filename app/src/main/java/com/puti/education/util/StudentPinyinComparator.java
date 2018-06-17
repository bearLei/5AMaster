package com.puti.education.util;

import com.puti.education.bean.oldStudent;

import java.util.Comparator;

public class StudentPinyinComparator implements Comparator<oldStudent>{

    public int compare(oldStudent o1, oldStudent o2) {

        if (o2.pinyin.equals("#")) {
            return -1;
        } else if (o1.pinyin.equals("#")) {
            return 1;
        } else {
            return o1.pinyin.compareTo(o2.pinyin);
        }
    }
}
