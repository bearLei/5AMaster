package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/20.
 */

public class ClaRecordEntity {

    private List<ClaHeadRecord> ClaHeadRecords;
    private List<ClaTeaRecord> ClaTeaRecords;
    public ClaRecordEntity() {
    }

    public List<ClaHeadRecord> getClaHeadRecords() {
        return ClaHeadRecords;
    }

    public void setClaHeadRecords(List<ClaHeadRecord> claHeadRecords) {
        ClaHeadRecords = claHeadRecords;
    }

    public List<ClaTeaRecord> getClaTeaRecords() {
        return ClaTeaRecords;
    }

    public void setClaTeaRecords(List<ClaTeaRecord> claTeaRecords) {
        ClaTeaRecords = claTeaRecords;
    }

    public class ClaHeadRecord {
        private String Term;
        private String ClassUID;
        private String Class;
        private double Score;
        private double EventCount;

        public ClaHeadRecord() {
        }

        public String getTerm() {
            return Term;
        }

        public void setTerm(String term) {
            Term = term;
        }

        public String getClassUID() {
            return ClassUID;
        }

        public void setClassUID(String classUID) {
            ClassUID = classUID;
        }


        public String getClassName() {
            return Class;
        }

        public void setClass(String aClass) {
            Class = aClass;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }

        public double getEventCount() {
            return EventCount;
        }

        public void setEventCount(double eventCount) {
            EventCount = eventCount;
        }
    }

    public class ClaTeaRecord{
        private String Term;
        private String ClassUID;
        private String Class;
        private String Course;
        private double Score;

        public ClaTeaRecord() {
        }

        public String getTerm() {
            return Term;
        }

        public void setTerm(String term) {
            Term = term;
        }

        public String getClassUID() {
            return ClassUID;
        }

        public void setClassUID(String classUID) {
            ClassUID = classUID;
        }


        public String getClassName() {
            return Class;
        }

        public void setClass(String aClass) {
            Class = aClass;
        }

        public String getCourse() {
            return Course;
        }

        public void setCourse(String course) {
            Course = course;
        }

        public double getScore() {
            return Score;
        }

        public void setScore(double score) {
            Score = score;
        }
    }
}
