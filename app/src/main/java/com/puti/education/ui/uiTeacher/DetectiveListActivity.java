package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
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
import com.puti.education.adapter.DetectiveListAdapter;
import com.puti.education.adapter.ReportListAdapter;
import com.puti.education.bean.DetectiveBean;
import com.puti.education.bean.ReportBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiStudent.StudentReportAddActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DetectiveListActivity extends BaseActivity implements LRecyclerView.LScrollListener{


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
    DetectiveListAdapter mEventAdapter;

    private int mPageSize = 15;
    private int mPageIndex = 1;
    private int mPageCount = 0;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_detective_list;
    }

    @Override
    public void initVariables() {

    }


    @OnClick(R.id.back_frame)
    public void exitActivity(){
        finish();
    }

    @Override
    public void initViews() {
        mTitleTv.setText("巡检列表");
        mFrameNew.setVisibility(View.GONE);
        mFrameNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(DetectiveListActivity.this, StudentReportAddActivity.class);
//                startActivity(intent);
            }
        });

        mEventAdapter = new DetectiveListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setRefreshProgressStyle(AVLoadingIndicatorView.BallSpinFadeLoader);
        mRv.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mEventAdapter);
        mRv.setAdapter(mLRecyclerViewAdapter);
        mRv.setLScrollListener(this);
        mRv.setRefreshing(true);
        mEventAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                DetectiveBean eventBean = mEventAdapter.mList.get(position);
                Intent intent = new Intent();
                intent.putExtra(Key.BEAN, eventBean);
                intent.setClass(DetectiveListActivity.this, DetectiveConfirmActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        getDetectiveList(mPageIndex);
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

        getDetectiveList(mPageIndex);
    }

    @Override
    public void onScrolled(int distanceX, int distanceY) {

    }


    private void getDetectiveList(final int pageIndex){
        TeacherModel.getInstance().getDetectiveList(pageIndex,mPageSize,new BaseListener(DetectiveBean.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {

                List<DetectiveBean> tempList = (List<DetectiveBean>) listObj;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            onRefresh();
        }
    }
}
