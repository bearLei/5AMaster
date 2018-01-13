package com.puti.education.ui.uiTeacher.chooseperson.single;

import java.util.Comparator;

/**
 * Created by lei on 2018/1/11.
 */

public class SingleChoosePinyinComparator implements Comparator<SingleChooseBean> {

    @Override
    public int compare(SingleChooseBean o1, SingleChooseBean o2) {
        if (o2.getPinyin().equals("#")) {
            return -1;
        } else if (o1.getPinyin().equals("#")) {
            return 1;
        } else {
            return o1.getPinyin().compareTo(o2.getPinyin());
        }
    }
}
