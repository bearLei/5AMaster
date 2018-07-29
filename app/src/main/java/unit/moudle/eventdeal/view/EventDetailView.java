package unit.moudle.eventdeal.view;

import android.view.View;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/22.
 */

public interface EventDetailView extends BaseMvpView{
    void setTitle(String title);
    void getHeadHolderView(View view);
    void success(ArrayList<Event2Involved> data);
    String getEventId();
    String getEventDealOneUid();
    void setEventDealOneUid(String uid);
}
