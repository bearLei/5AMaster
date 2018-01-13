package com.puti.education.ui.uiTeacher.chooseperson.mul;

/**
 * Created by lenovo on 2018/1/12.
 */

public class MulPersonBean {
    private String personnelUid;
    private String realName;
    private String phone;
    private boolean isVolunteer;
    private boolean isSelected;
    private String pinyin;
    public String getPersonnelUid() {
        return personnelUid;
    }

    public void setPersonnelUid(String personnelUid) {
        this.personnelUid = personnelUid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public void setVolunteer(boolean volunteer) {
        isVolunteer = volunteer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
