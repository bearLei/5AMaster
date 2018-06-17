package unit.entity;

/**
 * Created by lei on 2018/6/17.
 */
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

    private boolean isAdd;//默认是false,如果是ture，则代表是新增按钮

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

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj instanceof Student){
            Student s = (Student) obj;
            if (s.getStudentUID().equals(this.getStudentUID())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getStudentUID();
    }
}
