package unit.moudle.eventdeal.view;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.PutiEvents;

/**
 * Created by lei on 2018/6/17.
 */

public interface EventListView extends BaseMvpView {

    void setDesc(String desc);
    void setClassName(String name);
    void success(ArrayList<PutiEvents.Event> events);
}
