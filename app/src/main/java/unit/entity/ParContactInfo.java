package unit.entity;

import com.puti.education.bean.Schools;

import java.util.List;

/**
 * Created by lei on 2018/6/19.
 * 家长通讯录 具体信息
 */

public class ParContactInfo {
    private List<ParContactDetailInfo> Parents;

    public ParContactInfo() {
    }

    public List<ParContactDetailInfo> getParents() {
        return Parents;
    }

    public void setParents(List<ParContactDetailInfo> parents) {
        Parents = parents;
    }

    public class ParContactDetailInfo{
        private String StudentName;
        private String Mobile;
        private String Father;
        private String FatherPhone;
        private String Mother;
        private String MotherPhone;
        private String Guardian;
        private String GuardianPhone;
        private String Parent;
        private String ParentPhone;
        public ParContactDetailInfo() {
        }

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String studentName) {
            StudentName = studentName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getFather() {
            return Father;
        }

        public void setFather(String father) {
            Father = father;
        }

        public String getFatherPhone() {
            return FatherPhone;
        }

        public void setFatherPhone(String fatherPhone) {
            FatherPhone = fatherPhone;
        }

        public String getMother() {
            return Mother;
        }

        public void setMother(String mother) {
            Mother = mother;
        }

        public String getMotherPhone() {
            return MotherPhone;
        }

        public void setMotherPhone(String motherPhone) {
            MotherPhone = motherPhone;
        }

        public String getGuardian() {
            return Guardian;
        }

        public void setGuardian(String guardian) {
            Guardian = guardian;
        }

        public String getGuardianPhone() {
            return GuardianPhone;
        }

        public void setGuardianPhone(String guardianPhone) {
            GuardianPhone = guardianPhone;
        }

        public String getParent() {
            return Parent;
        }

        public void setParent(String parent) {
            Parent = parent;
        }

        public String getParentPhone() {
            return ParentPhone;
        }

        public void setParentPhone(String parentPhone) {
            ParentPhone = parentPhone;
        }
    }

}
