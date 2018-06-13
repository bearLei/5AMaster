package unit.address;

import java.util.List;

/**
 * Created by lei on 2018/6/13.
 */

public class Floor {
    private String Floor;
    private List<Room> Room;

    public Floor() {
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public List<Room> getRoom() {
        return Room;
    }

    public void setRoom(List<Room> room) {
        Room = room;
    }
}
