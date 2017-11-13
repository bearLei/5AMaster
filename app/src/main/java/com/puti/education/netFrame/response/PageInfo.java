package com.puti.education.netFrame.response;

public class PageInfo {

	public int index;	//当前面数
	public int count;	//所有页数
	public int size;		//按多少条记录分页
	public int total;	//所有记录集数量


    public PageInfo()
    {
    	super();
    }
    public PageInfo(String str) {
    }
}
