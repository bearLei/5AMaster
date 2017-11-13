package com.puti.education.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

@Root(name = "Schools",strict = false) //注解必须这样写，name值与xml节点一致
public class Schools implements Serializable {
    public static class School{
        @Element
        public String school_prefix;
        @Element
        public String school_uid;
        @Element
        public String server_url;

        public String name;
        public String avatar;
    }

    @ElementList(entry = "school" , inline = true)
    public List<School> school;
}
