package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/14.
 */

public class StudentEntity {
    private List<Student> Students;

    public StudentEntity() {
    }

    public List<Student> getStudents() {
        return Students;
    }

    public void setStudents(List<Student> students) {
        Students = students;
    }

}
