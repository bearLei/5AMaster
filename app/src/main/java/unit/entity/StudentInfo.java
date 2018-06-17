package unit.entity;

/**
 * Created by lei on 2018/6/18.
 */

public class StudentInfo {
    private StuBasicInfo stuBasicInfo;
    private StuFatherInfo stuFatherInfo;
    private StuMotherInfo stuMotherInfo;
    private StuGuardianInfo stuGuardianInfo;
    private StudentInfoms studentInfoms;
    private StuHeadInfo stuHeadInfo;
    private StuDorminfo stuDorminfo;

    public StudentInfo() {
    }

    public StuBasicInfo getStuBasicInfo() {
        return stuBasicInfo;
    }

    public void setStuBasicInfo(StuBasicInfo stuBasicInfo) {
        this.stuBasicInfo = stuBasicInfo;
    }

    public StuFatherInfo getStuFatherInfo() {
        return stuFatherInfo;
    }

    public void setStuFatherInfo(StuFatherInfo stuFatherInfo) {
        this.stuFatherInfo = stuFatherInfo;
    }

    public StuMotherInfo getStuMotherInfo() {
        return stuMotherInfo;
    }

    public void setStuMotherInfo(StuMotherInfo stuMotherInfo) {
        this.stuMotherInfo = stuMotherInfo;
    }

    public StuGuardianInfo getStuGuardianInfo() {
        return stuGuardianInfo;
    }

    public void setStuGuardianInfo(StuGuardianInfo stuGuardianInfo) {
        this.stuGuardianInfo = stuGuardianInfo;
    }

    public StudentInfoms getStudentInfoms() {
        return studentInfoms;
    }

    public void setStudentInfoms(StudentInfoms studentInfoms) {
        this.studentInfoms = studentInfoms;
    }

    public StuHeadInfo getStuHeadInfo() {
        return stuHeadInfo;
    }

    public void setStuHeadInfo(StuHeadInfo stuHeadInfo) {
        this.stuHeadInfo = stuHeadInfo;
    }

    public StuDorminfo getStuDorminfo() {
        return stuDorminfo;
    }

    public void setStuDorminfo(StuDorminfo stuDorminfo) {
        this.stuDorminfo = stuDorminfo;
    }

    public class StuBasicInfo{
         private String UserName;
         private String Photo;
         private String Sex;
         private String Birthday;
         private int    CensusType;
         private String CensusRegister;
         private String Mobile;
         private String IdCard;
         private String Address;

        public StuBasicInfo() {
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String photo) {
            Photo = photo;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String sex) {
            Sex = sex;
        }

        public String getBirthday() {
            return Birthday;
        }

        public void setBirthday(String birthday) {
            Birthday = birthday;
        }

        public int getCensusType() {
            return CensusType;
        }

        public void setCensusType(int censusType) {
            CensusType = censusType;
        }

        public String getCensusRegister() {
            return CensusRegister;
        }

        public void setCensusRegister(String censusRegister) {
            CensusRegister = censusRegister;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getIdCard() {
            return IdCard;
        }

        public void setIdCard(String idCard) {
            IdCard = idCard;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }
    }

    public class StuFatherInfo{
        private String UserName;
        private String Mobile;
        private String IdCard;

        public StuFatherInfo() {
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getIdCard() {
            return IdCard;
        }

        public void setIdCard(String idCard) {
            IdCard = idCard;
        }
    }

    public class StuMotherInfo{
        private String UserName;
        private String Mobile;
        private String IdCard;
        private int Relation;

        public StuMotherInfo() {
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getIdCard() {
            return IdCard;
        }

        public void setIdCard(String idCard) {
            IdCard = idCard;
        }

        public int getRelation() {
            return Relation;
        }

        public void setRelation(int relation) {
            Relation = relation;
        }
    }
    private class StuGuardianInfo{
        private String UserName;
        private String Mobile;
        private String IdCard;
        private int Relation;

        public StuGuardianInfo() {
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getIdCard() {
            return IdCard;
        }

        public void setIdCard(String idCard) {
            IdCard = idCard;
        }

        public int getRelation() {
            return Relation;
        }

        public void setRelation(int relation) {
            Relation = relation;
        }
    }
    public class StudentInfoms{
        private String ClassName;
        private String UserName;
        private String ProfessionalName;
        private String StatusTime;
        private int    Status;
        private String Department;

        public StudentInfoms() {
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getProfessionalName() {
            return ProfessionalName;
        }

        public void setProfessionalName(String professionalName) {
            ProfessionalName = professionalName;
        }

        public String getStatusTime() {
            return StatusTime;
        }

        public void setStatusTime(String statusTime) {
            StatusTime = statusTime;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getDepartment() {
            return Department;
        }

        public void setDepartment(String department) {
            Department = department;
        }
    }

    public class StuHeadInfo{
        private String UserName;
        private String Mobile;
        private int    StudentCount;

        public StuHeadInfo() {
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public int getStudentCount() {
            return StudentCount;
        }

        public void setStudentCount(int studentCount) {
            StudentCount = studentCount;
        }
    }

    public class StuDorminfo{
        private boolean Status;
        private String   Build;
        private String   Floor;
        private String   Room;
        private int      Capacity;

        public StuDorminfo() {
        }

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getBuild() {
            return Build;
        }

        public void setBuild(String build) {
            Build = build;
        }

        public String getFloor() {
            return Floor;
        }

        public void setFloor(String floor) {
            Floor = floor;
        }

        public String getRoom() {
            return Room;
        }

        public void setRoom(String room) {
            Room = room;
        }

        public int getCapacity() {
            return Capacity;
        }

        public void setCapacity(int capacity) {
            Capacity = capacity;
        }
    }
}
