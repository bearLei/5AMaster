package com.puti.education.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class UserDetail implements Serializable {
    public String type;  //1老师　２学生　３义工　　４家长 5专家
    public String uid;
    public String name;
    public String avatar;
}
