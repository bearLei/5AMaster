package unit.moudle.work;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.moudle.work.ptr.WorkCheckPtr;
import unit.moudle.work.view.WorkCheckView;
import unit.widget.EmptyView;
import unit.widget.HeadView;
import unit.widget.LoadingView;

/**
 * Created by lei on 2018/6/19.
 */

public class PutiWorkCheckActivity extends PutiActivity implements WorkCheckView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fillter_class)
    TextView fillterClass;
    @BindView(R.id.chart_Container)
    LinearLayout chartContainer;
    @BindView(R.id.loading_view)
    LoadingView loadingView;
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.unUsed_Container)
    LinearLayout unUsedContainer;


    private WorkCheckPtr mPtr;

    @Override
    public int getContentView() {
        return R.layout.puti_work_check_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new WorkCheckPtr(this, this);
        }
    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        headview.setTitle("工作检查");
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void setClassName(String name) {
        fillterClass.setText(name);
    }

    @Override
    public void addChartView(View view) {
        chartContainer.removeAllViews();
        chartContainer.addView(view);
    }

    @Override
    public void addUnUsedView(View view) {
        unUsedContainer.removeAllViews();
        unUsedContainer.addView(view);
    }

    @Override
    public void showSuccessView() {
        emptyView.setVisibility(View.GONE);
        hideLoading();
        container.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.fillter_class)
    public void onClick() {
        mPtr.showClassDialog(fillterClass);
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showErrorDataView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showNoDataView("暂无数据");
    }



}
