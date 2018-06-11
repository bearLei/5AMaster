package unit.entity;

/**
 * Created by lei on 2018/6/8.
 * 事件类型3层次
 */

public class EventDetail {
    private String TypeUID;
    private String TypeName;
    private String Punishment;
    private boolean NeedValid;
    private boolean NeedPsycholog;
    private boolean NeedParentNotice;
    private int WarnCause;
    private int Frequency;
    private int Categories;
    private int Sign;
    private int DownScore;
    private int UpScore;
    private String Remark;
    private boolean ApplyParent;
    private boolean ApplySchoolManager;
    private boolean ApplyStudent;
    private boolean ApplyTeacher;


    public EventDetail() {
    }

    public String getTypeUID() {
        return TypeUID;
    }

    public void setTypeUID(String typeUID) {
        TypeUID = typeUID;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getPunishment() {
        return Punishment;
    }

    public void setPunishment(String punishment) {
        Punishment = punishment;
    }

    public boolean isNeedValid() {
        return NeedValid;
    }

    public void setNeedValid(boolean needValid) {
        NeedValid = needValid;
    }

    public boolean isNeedPsycholog() {
        return NeedPsycholog;
    }

    public void setNeedPsycholog(boolean needPsycholog) {
        NeedPsycholog = needPsycholog;
    }

    public boolean isNeedParentNotice() {
        return NeedParentNotice;
    }

    public void setNeedParentNotice(boolean needParentNotice) {
        NeedParentNotice = needParentNotice;
    }

    public int getWarnCause() {
        return WarnCause;
    }

    public void setWarnCause(int warnCause) {
        WarnCause = warnCause;
    }

    public int getFrequency() {
        return Frequency;
    }

    public void setFrequency(int frequency) {
        Frequency = frequency;
    }

    public int getCategories() {
        return Categories;
    }

    public void setCategories(int categories) {
        Categories = categories;
    }

    public int getSign() {
        return Sign;
    }

    public void setSign(int sign) {
        Sign = sign;
    }

    public int getDownScore() {
        return DownScore;
    }

    public void setDownScore(int downScore) {
        DownScore = downScore;
    }

    public int getUpScore() {
        return UpScore;
    }

    public void setUpScore(int upScore) {
        UpScore = upScore;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public boolean isApplyParent() {
        return ApplyParent;
    }

    public void setApplyParent(boolean applyParent) {
        ApplyParent = applyParent;
    }

    public boolean isApplySchoolManager() {
        return ApplySchoolManager;
    }

    public void setApplySchoolManager(boolean applySchoolManager) {
        ApplySchoolManager = applySchoolManager;
    }

    public boolean isApplyStudent() {
        return ApplyStudent;
    }

    public void setApplyStudent(boolean applyStudent) {
        ApplyStudent = applyStudent;
    }

    public boolean isApplyTeacher() {
        return ApplyTeacher;
    }

    public void setApplyTeacher(boolean applyTeacher) {
        ApplyTeacher = applyTeacher;
    }
}
