package unit.eventbus;

/**
 * Created by lei on 2018/6/27.
 */

public class DealEventDissEvent {
    private String eventId;

    public DealEventDissEvent(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
