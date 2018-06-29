package unit.entity;

/**
 * Created by lei on 2018/6/6.
 * 首页统计信息实体
 */

public class HomeCountEntity {
    private int schoolWeekCount;
    private int myWriteCount;

    public HomeCountEntity() {
    }

    public int getSchoolWeekCount() {
        return schoolWeekCount;
    }

    public void setSchoolWeekCount(int schoolWeekCount) {
        this.schoolWeekCount = schoolWeekCount;
    }

    public int getMyWriteCount() {
        return myWriteCount;
    }

    public void setMyWriteCount(int myWriteCount) {
        this.myWriteCount = myWriteCount;
    }
}
