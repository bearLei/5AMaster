package unit.entity;

/**
 * Created by lei on 2018/6/28.
 */

public class CursorInfo {

    private int Weekday;
    private int Indexs;
    private String CourseName;

    public CursorInfo() {
    }

    public int getWeekday() {
        return Weekday;
    }

    public void setWeekday(int weekday) {
        Weekday = weekday;
    }

    public int getIndexs() {
        return Indexs;
    }

    public void setIndexs(int indexs) {
        Indexs = indexs;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }
}
