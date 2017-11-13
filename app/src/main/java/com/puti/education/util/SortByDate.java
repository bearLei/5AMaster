package com.puti.education.util;

import com.puti.education.bean.LocalFile;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/3 0003.
 * 对文件列表按时间倒序,最新排最前
 */



public class SortByDate implements Comparator {

    public int compare(Object o1, Object o2) {
        LocalFile s1 = (LocalFile) o1;
        LocalFile s2 = (LocalFile) o2;

        long diff = s1.modifyTime - s2.modifyTime;
        if (diff > 0)
            return -1;
        else if (diff == 0)
            return 0;
        else
            return 1;
    }
}
