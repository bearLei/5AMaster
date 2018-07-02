package unit.entity;

/**
 * Created by lei on 2018/7/2.
 */

public class PutiAvatarInfo {
    private String UserUID;
    private String UserName;
    private String RealName;
    private String Photo;

    public PutiAvatarInfo() {
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
