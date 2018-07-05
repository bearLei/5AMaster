package unit.entity;

/**
 * Created by lei on 2018/7/5.
 */

public class ReportInfo {

    private String UID;
    private String Title;
    private String Time;
    private String TimeSpan;
    private String ParentName;
    private String Description;
    private int Status;
    private String StatusName;

    public ReportInfo() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTimeSpan() {
        return TimeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        TimeSpan = timeSpan;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }
}
