package com.puti.education.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class EventDetailData {
    public EventDetailBean eventDetail;
    public List<EventDeailInvolvedPeople> involvedPeople;//主要次要涉事学生

    public ArrayList<Proof> eventEvidence;  //事件佐证
    public PunishData deal;


    public ArrayList<ParentData> parents;
    public ArrayList<EventTrackData> eventTracking;
    public ArrayList<EventEvaluation> eventEvaluation;

}
