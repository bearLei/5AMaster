package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/22.
 */

public class DealEventMain {
    private String EventUID;
    private String Address;
    private String Time;
    private String TimeSpan;
    private String Description;
    private String TypeUID;
    private String Categories;
    private int DefaultDownScore;
    private boolean DefaultNeedParentNotice;
    private boolean DefaultNeedPsycholog;
    private boolean DefaultNeedValid;
    private String DefaultPunishment;
    private int DefaultSign;
    private int DefaultUpScore;
    private String EventTypeName;
    private String IndexType;
    private String Writer;
    private String WriterType;
    private List<Event2Involved> Event2Involveds;
    private List<Evidence> Evidences;

    public DealEventMain() {
    }

    public String getEventUID() {
        return EventUID;
    }

    public void setEventUID(String eventUID) {
        EventUID = eventUID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTypeUID() {
        return TypeUID;
    }

    public void setTypeUID(String typeUID) {
        TypeUID = typeUID;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public int getDefaultDownScore() {
        return DefaultDownScore;
    }

    public void setDefaultDownScore(int defaultDownScore) {
        DefaultDownScore = defaultDownScore;
    }

    public boolean isDefaultNeedParentNotice() {
        return DefaultNeedParentNotice;
    }

    public void setDefaultNeedParentNotice(boolean defaultNeedParentNotice) {
        DefaultNeedParentNotice = defaultNeedParentNotice;
    }

    public boolean isDefaultNeedPsycholog() {
        return DefaultNeedPsycholog;
    }

    public void setDefaultNeedPsycholog(boolean defaultNeedPsycholog) {
        DefaultNeedPsycholog = defaultNeedPsycholog;
    }

    public boolean isDefaultNeedValid() {
        return DefaultNeedValid;
    }

    public void setDefaultNeedValid(boolean defaultNeedValid) {
        DefaultNeedValid = defaultNeedValid;
    }

    public String getDefaultPunishment() {
        return DefaultPunishment;
    }

    public void setDefaultPunishment(String defaultPunishment) {
        DefaultPunishment = defaultPunishment;
    }

    public int getDefaultSign() {
        return DefaultSign;
    }

    public void setDefaultSign(int defaultSign) {
        DefaultSign = defaultSign;
    }

    public int getDefaultUpScore() {
        return DefaultUpScore;
    }

    public void setDefaultUpScore(int defaultUpScore) {
        DefaultUpScore = defaultUpScore;
    }

    public String getEventTypeName() {
        return EventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        EventTypeName = eventTypeName;
    }

    public String getIndexType() {
        return IndexType;
    }

    public void setIndexType(String indexType) {
        IndexType = indexType;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getWriterType() {
        return WriterType;
    }

    public void setWriterType(String writerType) {
        WriterType = writerType;
    }

    public List<Event2Involved> getEvent2Involveds() {
        return Event2Involveds;
    }

    public void setEvent2Involveds(List<Event2Involved> event2Involveds) {
        Event2Involveds = event2Involveds;
    }

    public List<Evidence> getEvidences() {
        return Evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        Evidences = evidences;
    }
}
