package com.puti.education.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.EventListAdapter;
import com.puti.education.adapter.ReportListAdapter;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventBean;
import com.puti.education.bean.ReportBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.fragment.BaseListFragment;
import com.puti.education.ui.uiCommon.EventTypeChooseActivity;
import com.puti.education.ui.uiStudent.StudentReportAddActivity;
import com.puti.education.ui.uiStudent.StudentReportDetailActivity;
import com.puti.education.ui.uiTeacher.TeacherAddEventActivity;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/19 0018.
 *
 * 学生举报列表
 */

public class StudentReportListFragment extends BaseListFragment implements LRecyclerView.LScrollListener{

    @BindView(R.id.back_img)
    ImageView mImgBack;
    @BindView(R.id.right_img)
    ImageView mImgNew;
    @BindView(R.id.frame_img)
    FrameLayout mFrameNew;
    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.eventlist_lv)
    LRecyclerView mRv;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;

    LRecyclerViewAdapter mLRecyclerViewAdapter;
    ReportListAdapter mEventAdapter;

    private int mPageSize = 15;
    private int mPageIndex = 1;
    private int mPageCount = 0;

    private RefreshDataReceiver mRefreshReceiver = null;



    public class RefreshDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d("EventAddReceiver"," onReceive ");
            if(action.equals(Constant.BROADCAST_REFRESH_REPORT)) {
                onRefresh();
            }
        }
    }

    public void setReceiver()
    {
        mRefreshReceiver = new RefreshDataReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BROADCAST_REFRESH_REPORT);
        this.getActivity().registerReceiver(mRefreshReceiver, intentFilter);
    }

    public void cancelReceiver()
    {
        if (mRefreshReceiver != null)
        {
            LogUtil.d("", "cancelReceiver");
            this.getActivity().unregisterReceiver(mRefreshReceiver);
            mRefreshReceiver = null;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_student_reportlist;
    }

    @Override
    public void initVariables() {
        setReceiver();
    }

    @Override
    public void initViews(View view) {
        mImgBack.setVisibility(View.GONE);
        mImgNew.setImageResource(R.mipmap.ic_add);
        mTitleTv.setText("举报列表");
        mFrameNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StudentReportAddActivity.class);
                startActivity(intent);
            }
        });

        mEventAdapter = new ReportListAdapter(mContext, Constant.ROLE_STUDENT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setRefreshProgressStyle(AVLoadingIndicatorView.BallSpinFadeLoader);
        mRv.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(getActivity(), mEventAdapter);
        mRv.setAdapter(mLRecyclerViewAdapter);
        mRv.setLScrollListener(this);
        mRv.setRefreshing(true);
        mEventAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                ReportBean eventBean = mEventAdapter.mList.get(position);
                Intent intent = new Intent();
                intent.putExtra(Key.BEAN, eventBean);
                intent.setClass(getActivity(), StudentReportDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelReceiver();
    }

    @Override
    public void loadData() {

    }


    @Override
    public void onRefresh() {
        mPageIndex = 1;
        getEventList(mPageIndex);
    }

    @Override
    public void onBottom() {

        ++mPageIndex;

        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRv);
        if(state == LoadingFooter.State.Loading) {
            return;
        }

        if (mPageIndex <= mPageCount) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRv, mPageSize, LoadingFooter.State.Loading, null);
        } else {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRv, mPageSize, LoadingFooter.State.TheEnd, null);
            return;
        }

       getEventList(mPageIndex);
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

    private void getEventList(final int pageIndex){
        StudentModel.getInstance().getReportList(pageIndex,mPageSize,new BaseListener(ReportBean.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {

                List<ReportBean> tempList = (List<ReportBean>) listObj;

                if (pageInfo != null){
                    mPageIndex = pageInfo.index;
                    mPageCount = pageInfo.count;
                    mPageSize = pageInfo.size;
                }

                if (tempList != null && tempList.size() > 0){
                    if (mPageIndex == 1){
                        mEventAdapter.setDataList(tempList);
                        mRv.refreshComplete();
                    }else {
                        mEventAdapter.insertList(tempList);

                        if (mPageIndex == mPageCount){
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.TheEnd);
                        }else{
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.Normal);
                        }

                    }
                }else{
                    mRv.refreshComplete();
                }

                if (mEventAdapter.mList.size() > 0){
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
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
                mRv.refreshComplete();
            }
        });
    }




}
