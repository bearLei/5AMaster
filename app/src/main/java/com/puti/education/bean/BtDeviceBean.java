package com.puti.education.bean;

import java.util.List;


//蓝牙设备
public class BtDeviceBean {

    public String   deviceName;
    public String   deviceMac;
    public int   deviceStatus;       //0. 未连接， 1.已连接， 2.已断开
    public int   deviceRssi;

}
