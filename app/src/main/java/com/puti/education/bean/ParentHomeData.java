package com.puti.education.bean;

import java.util.List;

/**
 * Created by xjbin on 2017/5/22 0022.
 *
 * 家长首页信息
 * childrenList	孩子们行为数据	json对象数组
 * relativeDesc	亲子关系描述	json对象数组
 */

public class ParentHomeData {

    public List<ChildInfo>  childrenList; //亲子行为描述
    public List<ChildRelativeDes> relativeDesc;//亲子关系描述


}
