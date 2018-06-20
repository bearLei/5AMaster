package unit.entity;

/**
 * Created by lei on 2018/6/20.
 */

public class UserIdInfo {
    private String UID;
    private int CurrentPersonType;

    public UserIdInfo() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getCurrentPersonType() {
        return CurrentPersonType;
    }

    public void setCurrentPersonType(int currentPersonType) {
        CurrentPersonType = currentPersonType;
    }
}
