package unit.moudle.eventdeal.view;

import android.view.View;

import java.util.ArrayList;

import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/22.
 */

public interface EventDetailView {
    void setTitle(String title);
    void getHeadHolderView(View view);
    void success(ArrayList<Event2Involved> data);
    String getEventId();
}
