package com.puti.education.netFrame.response;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;


public class DataInfo {

	public String list;
	public String info;
	public PageInfo pageinfo;
	
	public String serverList;
	
    public DataInfo()
    {
    	super();
    }

    public DataInfo(String str) {

    }

    public <T>ArrayList parseList(Class<T> clazz)
    {
        ArrayList<T> objs = null;
        if (list != null && !list.equals("") && !list.equals("{}"))
        {
            try {
                objs = (ArrayList<T>) JSON.parseArray(list, clazz);
            }catch(Exception e){
                String erro = e.getMessage();
            }
        }
        return objs;
    }

    public <T>ArrayList parseServerList(Class<T> clazz)
    {
        ArrayList<T> objs = null;
        if (serverList != null && !serverList.equals(""))
        {
            objs = (ArrayList<T>)JSON.parseArray(serverList, clazz);
        }
        return objs;
    }

    public <T>Object parseInfo(Class<T> clazz)
    {
        Object objs = null;
        if (info != null && !info.equals("") && !info.equals("{}"))
        {
            objs = JSON.parseObject(info, clazz);
        }
        return objs;
    }

    public int parseInfoInt(String keyName)
    {
        int ret = 0;
        if (info != null && !info.equals(""))
        {
            JSONObject infoJson= JSON.parseObject(info);
            if (infoJson != null)
            {
                ret = infoJson.getIntValue(keyName);
            }
        }
        return ret;
    }

    public double parseInfoDouble(String keyName)
    {
        double ret = 0;
        if (info != null && !info.equals(""))
        {
            JSONObject infoJson= JSON.parseObject(info);
            if (infoJson != null)
            {
                ret = infoJson.getDoubleValue(keyName);
            }
        }
        return ret;
    }

}
