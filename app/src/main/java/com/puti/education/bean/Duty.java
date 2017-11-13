package com.puti.education.bean;

import java.util.List;

/**
 * Created by xjbin on 2017/5/17 0017.
 */

public class Duty {

    public List<DutyDetail> options;
    public static class DutyDetail{
        public String key;
        public String value;
    }
}
