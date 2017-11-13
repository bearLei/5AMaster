package com.puti.education.ui.uiCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.EventEvaluationListAdapter;
import com.puti.education.bean.EventEvaluationBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventEvaluationActivity extends BaseActivity implements LRecyclerView.LScrollListener{

    @BindView(R.id.record_rv)
    LRecyclerView mRv;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;
    @BindView(R.id.back_frame)
    FrameLayout mBackFrame;
    @BindView(R.id.title_textview)
    TextView mUiTitle;

    private int mPageIndex = 1;
    private int mPageSize = 20;
    private int mPageCount;

    private LRecyclerViewAdapter baseCommonListAdapter;
    private EventEvaluationListAdapter recordListAdapter;
    private RefreshListReceiver mRefreshReceiver = null;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_evaluation;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelReceiver();
    }

    public class RefreshListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d("RefreshListReceiver"," onReceive ");
            if(action.equals(Constant.BROADCAST_REFRESH_EVALUATION)) {
                onRefresh();
            }
        }
    }

    public void setReceiver()
    {
        mRefreshReceiver = new RefreshListReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BROADCAST_REFRESH_EVALUATION);
        this.registerReceiver(mRefreshReceiver, intentFilter);
    }

    public void cancelReceiver()
    {
        if (mRefreshReceiver != null)
        {
            LogUtil.d("", "cancelReceiver");
            this.unregisterReceiver(mRefreshReceiver);
            mRefreshReceiver = null;
        }
    }


    @Override
    public void initVariables() {
        recordListAdapter = new EventEvaluationListAdapter(this);
        baseCommonListAdapter = new LRecyclerViewAdapter(this,recordListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(baseCommonListAdapter);
        mRv.setLScrollListener(this);
        mRv.setRefreshing(true);
        recordListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                EventEvaluationBean practiceInst = recordListAdapter.mList.get(position);
                openEvaluationDetail(practiceInst);
            }
        });
        setReceiver();
    }

    private void openEvaluationDetail(EventEvaluationBean event){
        Intent intent = new Intent();
        intent.putExtra(Key.BEAN, event);
        intent.setClass(this, EventEvaluationDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {

    }


    private void getEventEvaluationList(int pageIndex, int pageSize){
        CommonModel.getInstance().getEventEvaluationList(pageIndex, pageSize, new BaseListener(EventEvaluationBean.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);

                List<EventEvaluationBean> tempList = (List<EventEvaluationBean>) listObj;

                if (pageInfo != null){
                    mPageIndex = pageInfo.index;
                    mPageCount = pageInfo.count;
                    mPageSize = pageInfo.size;
                }

                if (tempList != null && tempList.size() > 0){
                    if (mPageIndex == 1){
                        recordListAdapter.setDataList(tempList);
                        mRv.refreshComplete();
                    }else {
                        recordListAdapter.insertList(tempList);

                        if (mPageIndex == mPageCount){
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.TheEnd);
                        }else{
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.Normal);
                        }

                    }
                }else{
                    if (mPageIndex == 1){
                        recordListAdapter.setDataList(tempList);
                        mRv.refreshComplete();
                    }
                }

                if (recordListAdapter.mList.size() > 0){
                    if (mEmptyRealtive.getVisibility() == View.VISIBLE){
                        mEmptyRealtive.setVisibility(View.GONE);
                    }
                }else{
                    if (mEmptyRealtive.getVisibility() == View.GONE){
                        mEmptyRealtive.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("获取事件互评记录出错 " +(TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }


    @Override
    public void onRefresh() {
        mPageIndex = 1;
        getEventEvaluationList(mPageIndex, mPageSize);

    }

    @Override
    public void onBottom() {
        ++mPageIndex;

        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRv);
        if(state == LoadingFooter.State.Loading) {
            return;
        }

        if (mPageIndex <= mPageCount) {
            RecyclerViewStateUtils.setFooterViewState(this, mRv, mPageSize, LoadingFooter.State.Loading, null);
        } else {
            RecyclerViewStateUtils.setFooterViewState(this, mRv, mPageSize, LoadingFooter.State.TheEnd, null);
            return;
        }

        getEventEvaluationList(mPageIndex, mPageSize);
    }

    @Override
    public void onScrollUp() {
    }
    @Override
    public void onScrollDown() {
    }
    @Override
    public void onScrolled(int distanceX, int distanceY) {
    }

}
