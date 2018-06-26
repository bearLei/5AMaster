package unit.moudle.classevent.view;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.Event;

/**
 * Created by lei on 2018/6/26.
 */

public interface ClassEventView extends BaseMvpView {

    void setStatus(String status);
    void setClassName(String className);
    void succuess(ArrayList<Event> data);
}
