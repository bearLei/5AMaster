package com.puti.education.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class EventEvaluation implements Serializable {
    public String uid;
    public int personneltype;
    public String personneluid;
    public String name;
    public String content;
    public double score;
    public UserDetail user;

}
