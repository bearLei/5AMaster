package unit.entity;

import java.io.Serializable;

/**
 * Created by lei on 2018/6/29.
 */

public class QuesInfo  implements Serializable{
    private String  UserSurveyUID;
    private String  SurveyUID;
    private String  SurveyName;
    private int     Category;
    private String  Remark;
    private String  PushUID;
    private String  OverTime;
    private int     Status;
    private int     Result;

    public QuesInfo() {
    }

    public String getUserSurveyUID() {
        return UserSurveyUID;
    }

    public void setUserSurveyUID(String userSurveyUID) {
        UserSurveyUID = userSurveyUID;
    }

    public String getSurveyUID() {
        return SurveyUID;
    }

    public void setSurveyUID(String surveyUID) {
        SurveyUID = surveyUID;
    }

    public String getSurveyName() {
        return SurveyName;
    }

    public void setSurveyName(String surveyName) {
        SurveyName = surveyName;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getPushUID() {
        return PushUID;
    }

    public void setPushUID(String pushUID) {
        PushUID = pushUID;
    }

    public String getOverTime() {
        return OverTime;
    }

    public void setOverTime(String overTime) {
        OverTime = overTime;
    }


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }
}
