package unit.home;

import android.view.View;

import com.puti.education.base.BaseMvpView;

/**
 * Created by lei on 2018/6/5.
 */

public interface HomeView extends BaseMvpView {

    void addHeadLayout(View view);
    void addCountLayout(View view);
    void addPowerLayout(View view);
    void addFeedBackLayout(View view);
    void addToolLayout(View view);

}