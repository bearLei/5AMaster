package unit.address;
import java.util.List;

/**
 * Created by lei on 2018/6/13.
 */

public class SpaceEnitity {
    private String Build;
    private int Type;
    private String TypeName;
    private List<Floor> Floor;

    public SpaceEnitity() {
    }

    public String getBuild() {
        return Build;
    }

    public void setBuild(String build) {
        Build = build;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public List<Floor> getFloor() {
        return Floor;
    }

    public void setFloor(List<Floor> floor) {
        Floor = floor;
    }
}
