package unit.moudle.eventdeal.view;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.Event;

/**
 * Created by lei on 2018/6/17.
 */

public interface EventListView extends BaseMvpView {

    void setDesc(String desc);
    void setClassName(String name);
    void success(ArrayList<Event> events);
}
