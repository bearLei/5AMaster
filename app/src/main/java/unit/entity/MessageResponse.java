package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/7/21.
 */

public class MessageResponse {
    private List<MessageEntity> Msgs;

    public MessageResponse() {
    }

    public List<MessageEntity> getMsgs() {
        return Msgs;
    }

    public void setMsgs(List<MessageEntity> msgs) {
        Msgs = msgs;
    }
}
