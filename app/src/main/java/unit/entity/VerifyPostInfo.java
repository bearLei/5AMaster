package unit.entity;

/**
 * Created by lei on 2018/6/5.
 * 用户登录时候需要验证码的时候post的信息
 */

public class VerifyPostInfo {
    private String uuidKey;
    private String vericode;


    public VerifyPostInfo() {
    }

    public String getUuidKey() {
        return uuidKey;
    }

    public void setUuidKey(String uuidKey) {
        this.uuidKey = uuidKey;
    }

    public String getVericode() {
        return vericode;
    }

    public void setVericode(String vericode) {
        this.vericode = vericode;
    }
}
