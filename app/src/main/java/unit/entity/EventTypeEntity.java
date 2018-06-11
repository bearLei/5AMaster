package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/8.
 * 事件类型实体 1层次
 */

public class EventTypeEntity {
    private int IndexType;
    private List<EventMainTier> Types;

    public EventTypeEntity(){

    }

    public int getIndexType() {
        return IndexType;
    }

    public void setIndexType(int indexType) {
        IndexType = indexType;
    }

    public List<EventMainTier> getTypes() {
        return Types;
    }

    public void setTypes(List<EventMainTier> types) {
        Types = types;
    }
}

