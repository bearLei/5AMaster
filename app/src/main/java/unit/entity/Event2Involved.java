package unit.entity;

/**
 * Created by lei on 2018/6/22.
 */

public class Event2Involved {
    private String Event2InvolvedUID;
    private String StudentUID;
    private String StudentName;
    private String ClassUID;
    private String ClassName;
    private int    Status;
    private String HeadUID;
    private String HeadName;
    private String StatusName;
    private String Reason;
    private String    Score;
    private String InvolStudentDealUID;

    private int defaultUpScore;
    private int defaultDownScore;
    private int sign;
    private boolean defaultNeedVaied;
    private boolean defaultNeedParent;
    private boolean defaultPsy;


    public Event2Involved() {
    }

    public String getEvent2InvolvedUID() {
        return Event2InvolvedUID;
    }

    public void setEvent2InvolvedUID(String event2InvolvedUID) {
        Event2InvolvedUID = event2InvolvedUID;
    }

    public String getStudentUID() {
        return StudentUID;
    }

    public void setStudentUID(String studentUID) {
        StudentUID = studentUID;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getHeadUID() {
        return HeadUID;
    }

    public void setHeadUID(String headUID) {
        HeadUID = headUID;
    }

    public String getHeadName() {
        return HeadName;
    }

    public void setHeadName(String headName) {
        HeadName = headName;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getInvolStudentDealUID() {
        return InvolStudentDealUID;
    }

    public void setInvolStudentDealUID(String involStudentDealUID) {
        InvolStudentDealUID = involStudentDealUID;
    }

    public int getDefaultUpScore() {
        return defaultUpScore;
    }

    public void setDefaultUpScore(int defaultUpScore) {
        this.defaultUpScore = defaultUpScore;
    }

    public int getDefaultDownScore() {
        return defaultDownScore;
    }

    public void setDefaultDownScore(int defaultDownScore) {
        this.defaultDownScore = defaultDownScore;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean isDefaultNeedVaied() {
        return defaultNeedVaied;
    }

    public void setDefaultNeedVaied(boolean defaultNeedVaied) {
        this.defaultNeedVaied = defaultNeedVaied;
    }

    public boolean isDefaultNeedParent() {
        return defaultNeedParent;
    }

    public void setDefaultNeedParent(boolean defaultNeedParent) {
        this.defaultNeedParent = defaultNeedParent;
    }

    public boolean isDefaultPsy() {
        return defaultPsy;
    }

    public void setDefaultPsy(boolean defaultPsy) {
        this.defaultPsy = defaultPsy;
    }
}
