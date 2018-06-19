package unit.entity;

import java.util.ArrayList;

/**
 * Created by lei on 2018/6/19.
 * 家长通讯录 展示信息。
 */

public class ParShowContactInfo {
    private String letter;
    private ArrayList<ParContactInfo> contactInfos;

    public ParShowContactInfo() {
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public ArrayList<ParContactInfo> getContactInfos() {
        return contactInfos;
    }

    public void setContactInfos(ArrayList<ParContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }
}
