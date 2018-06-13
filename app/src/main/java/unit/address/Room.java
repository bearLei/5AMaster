package unit.address;

/**
 * Created by lei on 2018/6/13.
 */

public class Room {
    private String Room;
    private String UID;
    private int Capacity;

    public Room() {
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }
}
