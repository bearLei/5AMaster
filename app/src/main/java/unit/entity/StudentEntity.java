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

    public class Student {
        private String StudentName;
        private String StudentUID;
        private int Status;
        private String SchoolUID;
        private String SchoolName;
        private String HeadTeacher;
        private String ClassUID;
        private String ClassName;
        private String Birthday;
        private int Score;
        private int ClassIndex;


        public Student() {
        }

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String studentName) {
            StudentName = studentName;
        }

        public String getStudentUID() {
            return StudentUID;
        }

        public void setStudentUID(String studentUID) {
            StudentUID = studentUID;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getSchoolUID() {
            return SchoolUID;
        }

        public void setSchoolUID(String schoolUID) {
            SchoolUID = schoolUID;
        }

        public String getSchoolName() {
            return SchoolName;
        }

        public void setSchoolName(String schoolName) {
            SchoolName = schoolName;
        }

        public String getHeadTeacher() {
            return HeadTeacher;
        }

        public void setHeadTeacher(String headTeacher) {
            HeadTeacher = headTeacher;
        }

        public String getClassUID() {
            return ClassUID;
        }

        public void setClassUID(String classUID) {
            ClassUID = classUID;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public String getBirthday() {
            return Birthday;
        }

        public void setBirthday(String birthday) {
            Birthday = birthday;
        }

        public int getScore() {
            return Score;
        }

        public void setScore(int score) {
            Score = score;
        }

        public int getClassIndex() {
            return ClassIndex;
        }

        public void setClassIndex(int classIndex) {
            ClassIndex = classIndex;
        }
    }
}
