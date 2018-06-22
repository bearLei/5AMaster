package com.puti.education.listener;


import com.puti.education.netFrame.response.PageInfo;

/**
 *
 * 如有新的业务需求，可添加方法或者继承实现回调
 */
public  class BaseListener {

    private Class mClass;

    public BaseListener(Class t){
        mClass = t;
    }

    public BaseListener() {

    }

    public Class getSubClass(){
        return mClass;
    }
    /**
     * jsonobj
     */
    public void responseResult(Object infoObj, Object listObj, int code,boolean status){

    }

    public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo,int code, boolean status){

    }

    //子线程调用(可实现为主线程，做前期UI处理)
    //非必须实现（根据业务需求）
    public void onstart(){}

    //非必须实现（根据业务需求）
    public void onCompleted(){}

    //必须实现
    public void requestFailed(boolean status,int code, String errorMessage){

    }



}
