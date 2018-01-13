package com.puti.education.ui.uiTeacher.chooseperson.event;

import com.puti.education.bean.EventAboutPeople;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/1/13.
 */

public class ChooseCompleteEvent {
    private ArrayList<EventAboutPeople> mList;
    private String duty;
    public ArrayList<EventAboutPeople> getmList() {
        return mList;
    }

    public void setmList(ArrayList<EventAboutPeople> mList) {
        this.mList = mList;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
