package unit.entity;

/**
 * Created by lei on 2018/6/8.
 * 事件类型二层次
 */

public class EventGroup {
    private String GroupUID;
    private String GroupName;

    public EventGroup() {
    }

    public EventGroup(String groupUID, String groupName) {
        GroupUID = groupUID;
        GroupName = groupName;
    }

    public String getGroupUID() {
        return GroupUID;
    }

    public void setGroupUID(String groupUID) {
        GroupUID = groupUID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
}
