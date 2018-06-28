package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/19.
 */

public class ContactInfo {
    private List<TeacherContactInfo> Teachers;

    public ContactInfo() {
    }

    public List<TeacherContactInfo> getTeachers() {
        return Teachers;
    }

    public void setTeachers(List<TeacherContactInfo> teachers) {
        Teachers = teachers;
    }
}
