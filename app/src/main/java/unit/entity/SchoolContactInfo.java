package unit.entity;

import com.puti.education.bean.Schools;

import java.util.ArrayList;

/**
 * Created by lei on 2018/6/19.
 */

public class SchoolContactInfo {
    private String letter;
    private ArrayList<TeacherContactInfo> contactInfos;

    public SchoolContactInfo() {
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public ArrayList<TeacherContactInfo> getContactInfos() {
        return contactInfos;
    }

    public void setContactInfos(ArrayList<TeacherContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }
}
