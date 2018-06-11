package unit.moudle.eventregist.view;

import com.puti.education.base.BaseMvpView;

import java.util.ArrayList;

import unit.entity.EventMainTier;
import unit.entity.EventTypeEntity;

/**
 * Created by lei on 2018/6/8.
 * 事件选择页面
 */

public interface ChooseEventView extends BaseMvpView {
    void handleResult(ArrayList<EventMainTier> list);
    ArrayList<EventMainTier> getList();
    void putPullStatus(int position);
    void removePullStatus(int position);
    void setJumpMainPosition(int position);
    void setJumpSecondPosition(int position);
}
