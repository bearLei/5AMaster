package unit.entity;

/**
 * Created by lei on 2018/6/26.
 */

public class DealEntity {
    private String InvolStudentDealUID;
    private String Punishment;
    private String PunishmentInfo;
    private double Score;
    private String HeadTime;
    private String HeadTeacher;
    private String HeadTeacherUID;
    private boolean NeedValid;
    private boolean NeedPsycholog;
    private boolean NeedParentNotice;
    private String ValidTime;
    private double OfficeValid;
    private String OfficeValiderUID;
    private String OfficeValider;
    private String OfficeValidRemark;


    public DealEntity() {
    }

    public String getInvolStudentDealUID() {
        return InvolStudentDealUID;
    }

    public void setInvolStudentDealUID(String involStudentDealUID) {
        InvolStudentDealUID = involStudentDealUID;
    }

    public String getPunishment() {
        return Punishment;
    }

    public void setPunishment(String punishment) {
        Punishment = punishment;
    }

    public String getPunishmentInfo() {
        return PunishmentInfo;
    }

    public void setPunishmentInfo(String punishmentInfo) {
        PunishmentInfo = punishmentInfo;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public String getHeadTime() {
        return HeadTime;
    }

    public void setHeadTime(String headTime) {
        HeadTime = headTime;
    }

    public String getHeadTeacher() {
        return HeadTeacher;
    }

    public void setHeadTeacher(String headTeacher) {
        HeadTeacher = headTeacher;
    }

    public String getHeadTeacherUID() {
        return HeadTeacherUID;
    }

    public void setHeadTeacherUID(String headTeacherUID) {
        HeadTeacherUID = headTeacherUID;
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

    public String getValidTime() {
        return ValidTime;
    }

    public void setValidTime(String validTime) {
        ValidTime = validTime;
    }

    public double getOfficeValid() {
        return OfficeValid;
    }

    public void setOfficeValid(double officeValid) {
        OfficeValid = officeValid;
    }

    public String getOfficeValiderUID() {
        return OfficeValiderUID;
    }

    public void setOfficeValiderUID(String officeValiderUID) {
        OfficeValiderUID = officeValiderUID;
    }

    public String getOfficeValider() {
        return OfficeValider;
    }

    public void setOfficeValider(String officeValider) {
        OfficeValider = officeValider;
    }

    public String getOfficeValidRemark() {
        return OfficeValidRemark;
    }

    public void setOfficeValidRemark(String officeValidRemark) {
        OfficeValidRemark = officeValidRemark;
    }
}
