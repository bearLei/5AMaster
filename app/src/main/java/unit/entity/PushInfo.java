package unit.entity;

/**
 * Created by lei on 2018/7/2.
 * 推送消息
 */

public class PushInfo {
    private int category;//3事件通知， 4家长举报  5问卷通知
    private int type;

    public PushInfo() {
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
