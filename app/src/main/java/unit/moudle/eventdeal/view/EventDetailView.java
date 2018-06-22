package unit.moudle.eventdeal.view;

import android.view.View;

/**
 * Created by lei on 2018/6/22.
 */

public interface EventDetailView {
    void setTitle(String title);
    void getHeadHolderView(View view);
    void success();
    String getEventId();
}
