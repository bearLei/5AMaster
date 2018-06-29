package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/29.
 */

public class QuesDetailInfo {
    private String SurveyName;
    private String CategoryName;
    private String Remark;
    private List<Question> Questions;

    public QuesDetailInfo() {
    }

    public String getSurveyName() {
        return SurveyName;
    }

    public void setSurveyName(String surveyName) {
        SurveyName = surveyName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> questions) {
        Questions = questions;
    }

}
