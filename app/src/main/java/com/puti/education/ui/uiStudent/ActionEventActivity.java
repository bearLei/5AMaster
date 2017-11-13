package com.puti.education.ui.uiStudent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.puti.education.R;
import com.puti.education.adapter.ActionEventListAdapter;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.EventListAdapter;
import com.puti.education.bean.EventBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.TeacherEventListFragment;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/5/10 0010.
 */

public class ActionEventActivity extends BaseActivity implements LRecyclerView.LScrollListener {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.lv_events)
    LRecyclerView mEtRecyclerView;
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

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ActionEventListAdapter mEventAdapter;

    private int mPageSize = 20;
    private int mPageIndex = 1;
    private int mPageCount = 0;

    private String mUid;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_action_events_layout;
    }

    @Override
    public void initVariables() {
        mUid = this.getIntent().getStringExtra("id");
    }

    @Override
    public void initViews() {
        mTitleTv.setText("事件列表");

        mEventAdapter = new ActionEventListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEtRecyclerView.setLayoutManager(layoutManager);
        mEtRecyclerView.setRefreshProgressStyle(AVLoadingIndicatorView.BallSpinFadeLoader);
        mEtRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mEventAdapter);
        mEtRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mEtRecyclerView.setLScrollListener(this);
        mEtRecyclerView.setRefreshing(true);

        mEventAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                EventBean eventBean = mEventAdapter.mList.get(position);

                /*
                *-1 都不可以操作（任课老师）
                * 1 各个状态都可以操作（班主任又是学生处老师）　
                * 2 各个状态都可以操作,除了审核（班主任）
                * 3　只可操作审核（学生处）　
                * 4　只可操作追踪（家长）
                */

                int isOperate = -1;
                int role = ConfigUtil.getInstance(ActionEventActivity.this).get(Constant.KEY_ROLE_TYPE, -1);
                String userUid = ConfigUtil.getInstance(ActionEventActivity.this).get(Constant.KEY_USER_ID, "");
                if (role == Constant.ROLE_TEACHER){
                    boolean  isOffice = ConfigUtil.getInstance(ActionEventActivity.this).get(Constant.KEY_IS_STUDENT_AFFAIRS, false);
                    if (isOffice && eventBean.headteacheruid.equals(userUid)){
                        isOperate = 1;
                    }else if (isOffice){
                        isOperate = 3;
                    }else if (eventBean.headteacheruid.equals(userUid)){
                        isOperate = 2;
                    }
                }else if (role == Constant.ROLE_PARENTS){
                    isOperate = 4;
                }

                Intent intent = new Intent(ActionEventActivity.this, TeacherEventDetailActivity.class);
                intent.putExtra(Key.KEY_OPRE_STATUS,isOperate);
                intent.putExtra(Key.EVENT_KEY,eventBean.eventuid);
                intent.putExtra(Key.KEY_PEOPLE_UID, eventBean.studentuid);


                startActivity(intent);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void disLoading() {
        super.disLoading();
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        getList(mPageIndex);
    }

    @Override
    public void onScrollUp() {

    }

    @Override
    public void onScrollDown() {

    }

    @Override
    public void onBottom() {
        ++mPageIndex;

        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mEtRecyclerView);
        if(state == LoadingFooter.State.Loading) {
            return;
        }

        if (mPageIndex <= mPageCount) {
            RecyclerViewStateUtils.setFooterViewState(this, mEtRecyclerView, mPageSize, LoadingFooter.State.Loading, null);
        } else {
            RecyclerViewStateUtils.setFooterViewState(this, mEtRecyclerView, mPageSize, LoadingFooter.State.TheEnd, null);
            return;
        }

        getList(mPageIndex);
    }

    @Override
    public void onScrolled(int distanceX, int distanceY) {

    }

    private void getList(final int pageIndex){
        if (TextUtils.isEmpty(mUid)){
            getStudentEventList(pageIndex);
        }else{
            getParentChildEventList(mUid, pageIndex);
        }
    }

    private void getStudentEventList(final int pageIndex) {

        StudentModel.getInstance().studentEventList(mEventType, mEventStatus, pageIndex, mPageSize, new BaseListener(EventBean.class) {

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {

                List<EventBean> tempList = (List<EventBean>) listObj;

                if (pageInfo != null) {
                    mPageIndex = pageInfo.index;
                    mPageCount = pageInfo.count;
                    mPageSize = pageInfo.size;
                }

                if (tempList != null && tempList.size() > 0) {
                    if (mPageIndex == 1) {
                        mEventAdapter.setDataList(tempList);
                        mEtRecyclerView.refreshComplete();
                    } else {
                        mEventAdapter.insertList(tempList);

                        if (mPageIndex == mPageCount) {
                            RecyclerViewStateUtils.setFooterViewState(mEtRecyclerView, LoadingFooter.State.TheEnd);
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(mEtRecyclerView, LoadingFooter.State.Normal);
                        }

                    }
                }else{
                    mEtRecyclerView.refreshComplete();
                }

                if (mEventAdapter.mList.size() > 0) {
                    if (mEmptyRealtive.getVisibility() == View.VISIBLE) {
                        mEmptyRealtive.setVisibility(View.GONE);
                    }
                } else {
                    if (mEmptyRealtive.getVisibility() == View.GONE) {
                        mEmptyRealtive.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                if (errorMessage != null) {
                    ToastUtil.show(errorMessage);
                }
            }
        });
    }


    private void getParentChildEventList(String uid, final int pageIndex) {

        PatriarchModel.getInstance().parentChildEventList(uid, mEventType, mEventStatus,pageIndex, mPageSize, new BaseListener(EventBean.class) {

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {

                List<EventBean> tempList = (List<EventBean>) listObj;

                if (pageInfo != null) {
                    mPageIndex = pageInfo.index;
                    mPageCount = pageInfo.count;
                    mPageSize = pageInfo.size;
                }

                if (tempList != null && tempList.size() > 0) {
                    if (mPageIndex == 1) {
                        mEventAdapter.setDataList(tempList);
                        mEtRecyclerView.refreshComplete();
                    } else {
                        mEventAdapter.insertList(tempList);

                        if (mPageIndex == mPageCount) {
                            RecyclerViewStateUtils.setFooterViewState(mEtRecyclerView, LoadingFooter.State.TheEnd);
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(mEtRecyclerView, LoadingFooter.State.Normal);
                        }

                    }
                }else{
                    mEtRecyclerView.refreshComplete();
                }

                if (mEventAdapter.mList.size() > 0) {
                    if (mEmptyRealtive.getVisibility() == View.VISIBLE) {
                        mEmptyRealtive.setVisibility(View.GONE);
                    }
                } else {
                    if (mEmptyRealtive.getVisibility() == View.GONE) {
                        mEmptyRealtive.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                if (errorMessage != null) {
                    ToastUtil.show(errorMessage);
                }
            }
        });
    }

    private CommonDropView mDropViewStatus;
    private CommonDropView mDropViewType;
    private String mEventStatus = null;
    private int mEventType = 0;

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
            mDropViewStatus = new CommonDropView(ActionEventActivity.this, mLayoutStatus, dataList);
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
            mDropViewType = new CommonDropView(ActionEventActivity.this, mLayoutType, dataList);
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
