package com.puti.education.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.EventListAdapter;
import com.puti.education.bean.EventBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.uiCommon.EventTypeChooseActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeAddActivity;
import com.puti.education.ui.uiTeacher.AddEventZxingActivity;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.ui.uiTeacher.TeacherMainActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.AppSwipeRefreshLayout;
import com.puti.education.R;
import com.puti.education.widget.CommonDropView;
import com.puti.education.zxing.ZxingUtil;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/18 0018.
 *
 * 教师 事件
 */

public class TeacherEventListFragment extends BaseListFragment implements LRecyclerView.LScrollListener{

    @BindView(R.id.back_img)
    ImageView mImgBack;
    @BindView(R.id.back_frame)
    FrameLayout mFrameZxing;
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
    @BindView(R.id.layout_event_status)
    FrameLayout mLayoutStatus;
    @BindView(R.id.layout_event_type)
    FrameLayout mLayoutType;
    @BindView(R.id.id_event_status)
    TextView mTvStatus;
    @BindView(R.id.id_event_type)
    TextView mTvType;

    LRecyclerViewAdapter mLRecyclerViewAdapter;
    EventListAdapter mEventAdapter;

    private int mPageSize = 15;
    private int mPageIndex = 1;
    private int mPageCount = 0;
    private boolean mIsRefreshing = false;


    private RefreshEventReceiver mRefreshReceiver = null;



    public class RefreshEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d("EventAddReceiver"," onReceive ");
            if(action.equals(Constant.BROADCAST_REFRESH_EVENT)) {
                onRefresh();
            }
        }
    }

    public void setReceiver()
    {
        mRefreshReceiver = new RefreshEventReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BROADCAST_REFRESH_EVENT);
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
        return R.layout.fragment_teacher_eventlist;
    }

    @Override
    public void initVariables() {
        setReceiver();
    }

    @Override
    public void initViews(View view) {
//        mImgBack.setImageResource(R.drawable.credit_scan);
        mImgBack.setVisibility(View.GONE);
        mImgNew.setImageResource(R.mipmap.ic_add);
        mTitleTv.setText("事件列表");
        mFrameNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), EventTypeChooseActivity.class);
                startActivity(intent);
            }
        });


        mEventAdapter = new EventListAdapter(mContext);
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
                EventBean eventBean = mEventAdapter.mList.get(position);
                Intent intent = new Intent(getActivity(), TeacherEventDetailActivity.class);
                intent.putExtra(Key.EVENT_KEY,eventBean.eventuid);
                intent.putExtra(Key.KEY_PEOPLE_UID, eventBean.studentuid);
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
        if (mIsRefreshing){
            return;
        }
        mIsRefreshing = true;
        TeacherModel.getInstance().getTeacherEventList(mEventType,mEventStatus,pageIndex,mPageSize,new BaseListener(EventBean.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {

                List<EventBean> tempList = (List<EventBean>) listObj;

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
                    mEventAdapter.setDataList(tempList);
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
                mIsRefreshing = false;
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
                mRv.refreshComplete();
                mIsRefreshing = false;
            }
        });
    }

    private CommonDropView mDropViewStatus;
    private CommonDropView mDropViewType;
    private String mEventStatus = null;
    private int mEventType;

    @OnClick(R.id.layout_event_status)
    public void chooseStatu(){
        final List<String> dataList = new ArrayList<>();
        dataList.add("已拒绝");
        dataList.add("待确认");
        dataList.add("已确认");
        dataList.add("待审核");
        dataList.add("待追踪");
        dataList.add("已结案");
        dataList.add("全部");

        if (mDropViewStatus == null){
            mDropViewStatus = new CommonDropView(TeacherEventListFragment.this.getActivity(), mLayoutStatus, dataList);
            mDropViewStatus.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String statusname = dataList.get(position);
                    switch (position) {
                        case 0:
                            mEventStatus = "0";
                            mTvStatus.setText(statusname);
                            break;
                        case 1:
                            mEventStatus = "1";
                            mTvStatus.setText(statusname);
                            break;
                        case 2:
                            mEventStatus = "2";
                            mTvStatus.setText(statusname);
                            break;
                        case 3:
                            mEventStatus = "3";
                            mTvStatus.setText(statusname);
                            break;
                        case 4:
                            mEventStatus = "4";
                            mTvStatus.setText(statusname);
                            break;
                        case 5:
                            mEventStatus = "5";
                            mTvStatus.setText(statusname);
                            break;
                        default:
                            mEventStatus = null;
                            mTvStatus.setText(statusname);
                            break;
                    }
                    mDropViewStatus.dismiss();
                    onRefresh();
                }
            });
        }
        mDropViewStatus.showAsDropDown(mLayoutStatus);

    }

    @OnClick(R.id.layout_event_type)
    public void chooseType(){
        final List<String> dataList = new ArrayList<>();
        dataList.add("违规");
        dataList.add("违纪");
        dataList.add("异常行为");
        dataList.add("日常管理");
        dataList.add("良好表现");
        dataList.add("全部");

        if (mDropViewType == null){
            mDropViewType = new CommonDropView(TeacherEventListFragment.this.getActivity(), mLayoutType, dataList);
            mDropViewType.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String statusname = dataList.get(position);
                    switch (position) {
                        case 0:
                            mEventType = 1;
                            mTvType.setText(statusname);
                            break;
                        case 1:
                            mEventType = 49;
                            mTvType.setText(statusname);
                            break;
                        case 2:
                            mEventType = 77;
                            mTvType.setText(statusname);
                            break;
                        case 3:
                            mEventType = 95;
                            mTvType.setText(statusname);
                            break;
                        case 4:
                            mEventType = 106;
                            mTvType.setText(statusname);
                            break;
                        default:
                            mEventType = 0;
                            mTvType.setText(statusname);
                            break;

                    }
                    mDropViewType.dismiss();
                    onRefresh();
                }
            });
        }
        mDropViewType.showAsDropDown(mLayoutType);

    }
}
