package com.puti.education.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.puti.education.adapter.PracticeTrainListAdapter;
import com.puti.education.bean.Practice;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiPatriarch.GrowthTrackDetailActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeAddActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeDetailActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.TimePopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 家校协同
 */

public class TeacherSynergyFragment extends BaseFragment implements LRecyclerView.LScrollListener{
    @BindView(R.id.btn_parent)
    TextView mBtnParent;
    @BindView(R.id.btn_student)
    TextView mBtnStudent;
    @BindView(R.id.layout_search_topbar)
    RelativeLayout mTopLayout;
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

    private int mType = 1; //1.家长 2学生
    private String mStrTime = null;

    private LRecyclerViewAdapter baseCommonListAdapter;
    private PracticeTrainListAdapter recordListAdapter;

    @Override
    public int getLayoutResourceId() {
        return R.layout.teacher_synergy_layout;
    }


    @Override
    public void initVariables() {
        recordListAdapter = new PracticeTrainListAdapter(this.getActivity());
        baseCommonListAdapter = new LRecyclerViewAdapter(this.getActivity(),recordListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(baseCommonListAdapter);
        mRv.setLScrollListener(this);
        mRv.setRefreshing(true);
        recordListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                // type	类型	1.培训记录2实践记录,3成长
                Practice practiceInst = recordListAdapter.mList.get(position);
                openDetail(practiceInst);

            }
        });
    }

    public void openDetail(Practice practice){
//        if (mType == 1 || mType == 2){
            Intent intent = new Intent();
            intent.putExtra(Key.BEAN, practice);
            intent.setClass(this.getActivity(), TrainPracticeDetailActivity.class);
            startActivity(intent);
//        }else{
//            Intent intent = new Intent();
//            intent.putExtra("id", practice.id);
//            intent.setClass(this.getActivity(), GrowthTrackDetailActivity.class);
//            startActivity(intent);
//        }
    }


    @Override
    public void initViews(View view) {
        mUiTitle.setText("家校协同");
        mBackFrame.setVisibility(View.GONE);

        mBtnStudent.setBackgroundResource(R.color.white);
        mBtnParent.setBackgroundResource(R.drawable.tab_select_on_green);
    }

    @Override
    public void loadData() {
        trainTab();
    }

    private void getStudentTrainList(int type, int pageIndex, int pageSize){
        TeacherModel.getInstance().getStudentTrainList(type, pageIndex, pageSize, new BaseListener(Practice.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);

                List<Practice> tempList = (List<Practice>) listObj;

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
                ToastUtil.show("获取实践记录出错 " +(TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }


    private void getParentTrainList(int type, int pageIndex, int pageSize){
        TeacherModel.getInstance().getParentTrainList(type, pageIndex, pageSize, new BaseListener(Practice.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);

                List<Practice> tempList = (List<Practice>) listObj;

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
                ToastUtil.show("获取实践记录出错 " +(TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        if (mType == 1){
            getParentTrainList(0, mPageIndex, mPageSize);
        }else if (mType == 2){
            getStudentTrainList(0, mPageIndex, mPageSize);
        }

    }

    @Override
    public void onBottom() {
        ++mPageIndex;

        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRv);
        if(state == LoadingFooter.State.Loading) {
            return;
        }

        if (mPageIndex <= mPageCount) {
            RecyclerViewStateUtils.setFooterViewState(this.getActivity(), mRv, mPageSize, LoadingFooter.State.Loading, null);
        } else {
            RecyclerViewStateUtils.setFooterViewState(this.getActivity(), mRv, mPageSize, LoadingFooter.State.TheEnd, null);
            return;
        }

        if (mType == 1){
            getParentTrainList(0, mPageIndex, mPageSize);
        }else if (mType == 2){
            getStudentTrainList(0, mPageIndex, mPageSize);
        }
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


    @OnClick(R.id.btn_parent)
    public void trainTab(){
        mBtnParent.setBackgroundResource(R.drawable.tab_select_on_green);
        mBtnStudent.setBackgroundResource(R.color.white);
        mType = 1;
        mPageIndex = 1;
        getParentTrainList(0, mPageIndex,mPageSize);
    }

    @OnClick(R.id.btn_student)
    public void practiceTab(){
        mBtnParent.setBackgroundResource(R.color.white);
        mBtnStudent.setBackgroundResource(R.drawable.tab_select_on_green);
        mType = 2;
        mPageIndex = 1;
        getStudentTrainList(0, mPageIndex,mPageSize);
    }

}
