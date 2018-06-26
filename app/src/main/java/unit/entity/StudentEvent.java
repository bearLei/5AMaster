package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/26.
 */

public class StudentEvent {

    private Info Info;
    private List<Evidence> Evidences;

    private List<DealEntity> Deals;

    public StudentEvent() {
    }

    public StudentEvent.Info getInfo() {
        return Info;
    }

    public void setInfo(StudentEvent.Info info) {
        Info = info;
    }

    public List<Evidence> getEvidences() {
        return Evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        Evidences = evidences;
    }

    public List<DealEntity> getDeals() {
        return Deals;
    }

    public void setDeals(List<DealEntity> deals) {
        Deals = deals;
    }

    public class Info{
        private String EventUID;
        private String Address;
        private String Time;
        private String Description;
        private String TypeUID;
        private String Categories;
        private String EventTypeName;
        private String Writer;
        private String WriterType;
        private String IndexType;
        private String Event2InvolvedUID;
        private String StudentUID;
        private String StudentName;
        private String ClassUID;
        private String ClassName;
        private int Status;
        private String StatusName;
        private String Reason;
        private String HeadUID;
        private String HeadName;
        private String DefaultDownScore;
        private boolean DefaultNeedParentNotice;
        private boolean DefaultNeedPsycholog;
        private boolean DefaultNeedValid;
        private String DefaultPunishment;
        private int DefaultSign;
        private int DefaultUpScore;
        private String DealPunishment;
        private String DealScore;
        private boolean NeedValid;
        private boolean NeedPsycholog;
        private boolean NeedParentNotice;

        public Info() {
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

        public String getEventTypeName() {
            return EventTypeName;
        }

        public void setEventTypeName(String eventTypeName) {
            EventTypeName = eventTypeName;
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

        public String getIndexType() {
            return IndexType;
        }

        public void setIndexType(String indexType) {
            IndexType = indexType;
        }

        public String getEvent2InvolvedUID() {
            return Event2InvolvedUID;
        }

        public void setEvent2InvolvedUID(String event2InvolvedUID) {
            Event2InvolvedUID = event2InvolvedUID;
        }

        public String getStudentUID() {
            return StudentUID;
        }

        public void setStudentUID(String studentUID) {
            StudentUID = studentUID;
        }

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String studentName) {
            StudentName = studentName;
        }

        public String getClassUID() {
            return ClassUID;
        }

        public void setClassUID(String classUID) {
            ClassUID = classUID;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
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

        public String getReason() {
            return Reason;
        }

        public void setReason(String reason) {
            Reason = reason;
        }

        public String getHeadUID() {
            return HeadUID;
        }

        public void setHeadUID(String headUID) {
            HeadUID = headUID;
        }

        public String getHeadName() {
            return HeadName;
        }

        public void setHeadName(String headName) {
            HeadName = headName;
        }

        public String getDefaultDownScore() {
            return DefaultDownScore;
        }

        public void setDefaultDownScore(String defaultDownScore) {
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

        public String getDealPunishment() {
            return DealPunishment;
        }

        public void setDealPunishment(String dealPunishment) {
            DealPunishment = dealPunishment;
        }

        public String getDealScore() {
            return DealScore;
        }

        public void setDealScore(String dealScore) {
            DealScore = dealScore;
        }

        public boolean isNeedValid() {
            return NeedValid;
        }

        public void setNeedValid(boolean needValid) {
            NeedValid = needValid;
        }

        public boolean isNeedPsycholog() {
            return NeedPsycholog;
        }

        public void setNeedPsycholog(boolean needPsycholog) {
            NeedPsycholog = needPsycholog;
        }

        public boolean isNeedParentNotice() {
            return NeedParentNotice;
        }

        public void setNeedParentNotice(boolean needParentNotice) {
            NeedParentNotice = needParentNotice;
        }
    }

}
