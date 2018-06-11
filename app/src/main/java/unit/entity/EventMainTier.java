package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/8.
 * 事件的主层级
 * 包括二层级的Group和三层级的子事件
 */

public class EventMainTier {
    private EventGroup GroupName;
    private List<EventDetail> Types;
    private int indexType;
    public EventGroup getGroupName() {
        return GroupName;
    }

    public void setGroupName(EventGroup groupName) {
        GroupName = groupName;
    }

    public List<EventDetail> getTypes() {
        return Types;
    }

    public void setTypes(List<EventDetail> types) {
        this.Types = types;
    }

    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }
}
