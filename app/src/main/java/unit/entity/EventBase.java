package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/8.
 */

public class EventBase {
    private boolean Success;
    private String Msg;
    private List<EventTypeEntity> Data;
    private int code;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public List<EventTypeEntity> getData() {
        return Data;
    }

    public void setData(List<EventTypeEntity> data) {
        Data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
