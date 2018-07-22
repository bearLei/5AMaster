package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/7/22.
 */

public class PutiUnUsedEntity {
    private List<String> termList;
    private List<String> monthList;
    private List<String> weekList;

    public PutiUnUsedEntity() {
    }

    public List<String> getTermList() {
        return termList;
    }

    public void setTermList(List<String> termList) {
        this.termList = termList;
    }

    public List<String> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<String> monthList) {
        this.monthList = monthList;
    }

    public List<String> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<String> weekList) {
        this.weekList = weekList;
    }
}
