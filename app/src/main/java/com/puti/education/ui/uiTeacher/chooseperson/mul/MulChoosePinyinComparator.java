package com.puti.education.ui.uiTeacher.chooseperson.mul;

import java.util.Comparator;

/**
 * Created by lei on 2018/1/11.
 */

public class MulChoosePinyinComparator implements Comparator<MulPersonBean> {

    @Override
    public int compare(MulPersonBean o1, MulPersonBean o2) {
        if (o2.getPinyin().equals("#")) {
            return -1;
        } else if (o1.getPinyin().equals("#")) {
            return 1;
        } else {
            return o1.getPinyin().compareTo(o2.getPinyin());
        }
    }
}
