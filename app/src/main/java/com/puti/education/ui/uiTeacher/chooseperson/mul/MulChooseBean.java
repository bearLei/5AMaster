package com.puti.education.ui.uiTeacher.chooseperson.mul;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/1/12.
 */

public class MulChooseBean {
    private int personnelType;
    private String personnelTypeName;
    private ArrayList<MulPersonBean> list;

    public int getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(int personnelType) {
        this.personnelType = personnelType;
    }

    public String getPersonnelTypeName() {
        return personnelTypeName;
    }

    public void setPersonnelTypeName(String personnelTypeName) {
        this.personnelTypeName = personnelTypeName;
    }

    public ArrayList<MulPersonBean> getList() {
        return list;
    }

    public void setList(ArrayList<MulPersonBean> list) {
        this.list = list;
    }
}
