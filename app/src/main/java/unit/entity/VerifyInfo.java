package unit.entity;

import com.puti.education.speech.SpeechUtil;

/**
 * Created by lei on 2018/6/5.
 * 图形验证码信息
 */

public class VerifyInfo {
    private boolean needVerify;
    private String uuidKey;
    private String verifyPic;

    public VerifyInfo() {
    }

    public boolean isNeedVerify() {
        return needVerify;
    }

    public void setNeedVerify(boolean needVerify) {
        this.needVerify = needVerify;
    }

    public String getUuidKey() {
        return uuidKey;
    }

    public void setUuidKey(String uuidKey) {
        this.uuidKey = uuidKey;
    }

    public String getVerifyPic() {
        return verifyPic;
    }

    public void setVerifyPic(String verifyPic) {
        this.verifyPic = verifyPic;
    }
}
