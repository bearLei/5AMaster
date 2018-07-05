package unit.moudle.reports;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.api.PutiTeacherModel;
import unit.entity.ParReportInfo;
import unit.entity.ReportInfo;
import unit.widget.EmptyView;
import unit.widget.HeadView;
import unit.widget.LoadingView;
import unit.widget.SpaceItemDecoration;

/**
 * Created by lei on 2018/7/2.
 */

public class PutiParReportsActivity extends PutiActivity {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    @BindView(R.id.loading_view)
    LoadingView loadingView;

    private ArrayList<ReportInfo> mData;
    private ParReportsAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.puti_par_report_activity;
    }

    @Override
    public void BindPtr() {

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
        headview.setTitle("家长举报");

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new ParReportsAdapter(this, mData);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(ViewUtils.dip2px(this, 10)));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        showLoading();
        queryData();
    }

    private void queryData() {
        PutiTeacherModel.getInstance().getParReports(new BaseListener(ParReportInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                ParReportInfo info = (ParReportInfo) infoObj;
                if (mData == null) {
                    mData = new ArrayList<ReportInfo>();
                }
                mData.clear();
                mData.addAll(info.getReports());
                if (mData .size() == 0){
                    showEmptyView();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                showErrorView();
            }
        });
    }

    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    public void showErrorView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showErrorDataView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showNoDataView("暂无数据");
    }
}
