package unit.entity;

/**
 * Created by lei on 2018/6/6.
 * 首页统计信息实体
 */

public class HomeCountEntity {
    private int weekCount;
    private int myPost;

    public HomeCountEntity() {
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    public int getMyPost() {
        return myPost;
    }

    public void setMyPost(int myPost) {
        this.myPost = myPost;
    }
}
