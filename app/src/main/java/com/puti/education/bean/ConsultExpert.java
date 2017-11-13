package com.puti.education.bean;

/**
 * Created by xbjin on 2017/4/25 0025.
 *
 * 事件详情---咨询专家
 */

public class ConsultExpert {

    public String name;
    public String avatar;
    public String desc;
    public String contact;
    public String barcode;

    public ConsultExpert(String name, String avatar, String desc) {
        this.name = name;
        this.avatar = avatar;
        this.desc = desc;
    }

    public ConsultExpert() {
    }
}
