package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/7/13.
 */

public class PutiWeekEventImp {

    private Summary Summary;
    private List<WeekEvent> WeekEvents;

    public PutiWeekEventImp() {
    }

    public PutiWeekEventImp.Summary getSummary() {
        return Summary;
    }

    public void setSummary(PutiWeekEventImp.Summary summary) {
        Summary = summary;
    }

    public List<WeekEvent> getWeekEvents() {
        return WeekEvents;
    }

    public void setWeekEvents(List<WeekEvent> weekEvents) {
        WeekEvents = weekEvents;
    }

    public class Summary{
        private int Total;
        private int Important;

        public Summary() {
        }

        public int getTotal() {
            return Total;
        }

        public void setTotal(int total) {
            Total = total;
        }

        public int getImportant() {
            return Important;
        }

        public void setImportant(int important) {
            Important = important;
        }
    }
}
