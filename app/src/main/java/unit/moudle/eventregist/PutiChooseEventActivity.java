package unit.moudle.eventregist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.EventMainTier;
import unit.moudle.eventregist.adapter.EventChooseAdapter;
import unit.moudle.eventregist.ptr.ChooseEventPtr;
import unit.moudle.eventregist.view.ChooseEventView;
import unit.widget.EmptyView;
import unit.widget.HeadView;
import unit.widget.LoadingView;

/**
 * Created by lei on 2018/6/8.
 * 事件登记-事件选择页面
 */

public class PutiChooseEventActivity extends PutiActivity implements ChooseEventView {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    @BindView(R.id.loading_view)
    LoadingView loadingView;

    private ChooseEventPtr mPtr;


    private ArrayList<EventMainTier> mData;
    private EventChooseAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.puti_choose_event_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new ChooseEventPtr(this, this);
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

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new EventChooseAdapter(this, mData);
        }
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        mPtr.star();
        showLoading();
    }

    @Override
    public void handleResult(ArrayList<EventMainTier> list) {
        hideLoading();
        if (list == null || list.size() == 0){
            showEmptyView();
        }
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<EventMainTier> getList() {
        return mData;
    }

    @Override
    public void putPullStatus(int position) {

    }

    @Override
    public void removePullStatus(int position) {

    }

    @Override
    public void setJumpMainPosition(int position) {

    }

    @Override
    public void setJumpSecondPosition(int position) {

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
