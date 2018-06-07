package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/7.
 * 消息实体
 */

public class MessageEntity {

    private List<MessageInfo> messageList;

    public MessageEntity() {
    }

    public List<MessageInfo> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageInfo> messageList) {
        this.messageList = messageList;
    }

    public class MessageInfo{
        private String msgId;
        private String msgTitle;
        private int msgType;
        private String scheme;

        public MessageInfo() {
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgTitle() {
            return msgTitle;
        }

        public void setMsgTitle(String msgTitle) {
            this.msgTitle = msgTitle;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }
    }
}
