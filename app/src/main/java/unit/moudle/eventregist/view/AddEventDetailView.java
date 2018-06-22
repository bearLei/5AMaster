package unit.moudle.eventregist.view;

import android.view.View;

import com.puti.education.base.BaseMvpView;

/**
 * Created by lei on 2018/6/11.
 */

public interface AddEventDetailView extends BaseMvpView {

    void addChooseStuView(View view);
    void addTimeAndSpaceView(View view);
    void addDescView(View view);
    void addEvidenceView(View view);
    String getEventType();
}
