package unit.entity;

import com.baidu.mapapi.SDKInitializer;

import java.io.Serializable;

/**
 * Created by lei on 2018/6/17.
 */
public class Student implements Serializable{
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
    private String IdCard;
    private String Mobile;
    private String Photo;
    private String RealName;
    private String Sex;
    private String Address;
    private String UserUID;
    private String UserName;

    private boolean isAdd;//默认是false,如果是ture，则代表是新增按钮

    public Student() {
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
            return s.getStudentUID().equals(this.getStudentUID());
        }
        return false;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        return this.getStudentUID();
    }
}
