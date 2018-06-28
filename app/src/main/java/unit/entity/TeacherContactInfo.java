package unit.entity;

/**
 * Created by lei on 2018/6/28.
 */

public class TeacherContactInfo {
    private String TeacherUID;
    private String  UserName;
    private String UserUID;
    private String Sex;
    private String RealName;
    private String Mobile;
    private String Roles;

    public TeacherContactInfo() {
    }

    public String getTeacherUID() {
        return TeacherUID;
    }

    public void setTeacherUID(String teacherUID) {
        TeacherUID = teacherUID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getRoles() {
        return Roles;
    }

    public void setRoles(String roles) {
        Roles = roles;
    }
}
