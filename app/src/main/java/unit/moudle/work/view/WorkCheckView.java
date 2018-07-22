package unit.moudle.work.view;

import android.view.View;

import com.puti.education.base.BaseMvpView;

/**
 * Created by lei on 2018/6/19.
 */

public interface WorkCheckView extends BaseMvpView{
    void setClassName(String name);
    void addChartView(View view);
    void addUnUsedView(View view);
    void showSuccessView();
}
