package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/20.
 */

public class StudentPortraitInfo {

    private StudentInfom studentInfoms;
    private List<StudentPort> studentPort;
    private StuMoral stuMoral;
    private StuLevel stuLevel;
    private StuHealth stuHealth;
    private StuActivity stuActivity;
    private StuPortrar stuPortrar;

    public StudentPortraitInfo() {
    }

    public StudentInfom getStudentInfoms() {
        return studentInfoms;
    }

    public void setStudentInfoms(StudentInfom studentInfoms) {
        this.studentInfoms = studentInfoms;
    }


    public List<StudentPort> getStudentPort() {
        return studentPort;
    }

    public void setStudentPort(List<StudentPort> studentPort) {
        this.studentPort = studentPort;
    }

    public StuMoral getStuMoral() {
        return stuMoral;
    }

    public void setStuMoral(StuMoral stuMoral) {
        this.stuMoral = stuMoral;
    }

    public StuLevel getStuLevel() {
        return stuLevel;
    }

    public void setStuLevel(StuLevel stuLevel) {
        this.stuLevel = stuLevel;
    }

    public StuHealth getStuHealth() {
        return stuHealth;
    }

    public void setStuHealth(StuHealth stuHealth) {
        this.stuHealth = stuHealth;
    }

    public StuActivity getStuActivity() {
        return stuActivity;
    }

    public void setStuActivity(StuActivity stuActivity) {
        this.stuActivity = stuActivity;
    }

    public StuPortrar getStuPortrar() {
        return stuPortrar;
    }

    public void setStuPortrar(StuPortrar stuPortrar) {
        this.stuPortrar = stuPortrar;
    }

    public class StudentInfom{
        private String ClassName;
        private String UserName;
        private String Sex;
        private String Phtot;
        private String ProfessionalName;
        private String StatusTime;
        private int    Status;

        public StudentInfom() {
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

        public String getSex() {
            return Sex;
        }

        public void setSex(String sex) {
            Sex = sex;
        }

        public String getPhtot() {
            return Phtot;
        }

        public void setPhtot(String phtot) {
            Phtot = phtot;
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
    }

    public class StudentPort{
        private String EventName;
        private double Score;

        public StudentPort() {
        }

        public String getEventName() {
            return EventName;
        }

        public void setEventName(String eventName) {
            EventName = eventName;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }
    }

    public class StuMoral{
        private int IndexType;
        private double Score;

        public StuMoral() {
        }

        public int getIndexType() {
            return IndexType;
        }

        public void setIndexType(int indexType) {
            IndexType = indexType;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }
    }

    public class StuLevel{
        private int IndexType;
        private double Score;

        public StuLevel() {
        }

        public int getIndexType() {
            return IndexType;
        }

        public void setIndexType(int indexType) {
            IndexType = indexType;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }
    }

    public class StuHealth{
        private int IndexType;
        private double Score;

        public StuHealth() {
        }

        public int getIndexType() {
            return IndexType;
        }

        public void setIndexType(int indexType) {
            IndexType = indexType;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }
    }

    public class StuActivity{
        private int IndexType;
        private double Score;

        public StuActivity() {
        }

        public int getIndexType() {
            return IndexType;
        }

        public void setIndexType(int indexType) {
            IndexType = indexType;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }
    }

    public class StuPortrar{
        private double Score;
        private int ranks;
        private int graderanks;
        private int schoolranks;
        private double ContrastiveScore;

        public StuPortrar() {
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public int getGraderanks() {
            return graderanks;
        }

        public void setGraderanks(int graderanks) {
            this.graderanks = graderanks;
        }

        public int getSchoolranks() {
            return schoolranks;
        }

        public void setSchoolranks(int schoolranks) {
            this.schoolranks = schoolranks;
        }

        public double getContrastiveScore() {
            return ContrastiveScore;
        }

        public void setContrastiveScore(double contrastiveScore) {
            ContrastiveScore = contrastiveScore;
        }
    }

}
