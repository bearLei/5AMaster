package com.puti.education.ui.uiStudent;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.PracticeTrainListAdapter;
import com.puti.education.adapter.PraticeListAdapter;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.bean.Practice;
import com.puti.education.bean.Student;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeAddActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeDetailActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.ToastUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/15 0015.
 *
 * 学生实践记录
 */

public class PracticeListActivity extends BaseActivity implements LRecyclerView.LScrollListener{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.pratice_rv)
    LRecyclerView mRv;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;
    @BindView(R.id.right_img)
    ImageView mRightImg;


    private LRecyclerViewAdapter baseCommonListAdapter;
    private PracticeTrainListAdapter recordListAdapter;

    private String mType = null;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private int mPageCount;
    private List<Practice> mPracticeList;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_practicelist;
    }

    @Override
    public void initVariables() {

    }

    @OnClick(R.id.back_frame)
    public void finishActivity(){
        this.finish();
    }

    @OnClick(R.id.frame_img)
    public void addPractice(){
        Intent intent = new Intent();
        intent.setClass(this, TrainPracticeAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void initViews() {

        mTitleTv.setText("实践记录");
        mRightImg.setImageResource(R.mipmap.ic_add);

        recordListAdapter = new PracticeTrainListAdapter(this);
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
                // type	类型	1.培训记录2实践记录,3成长
                Practice practiceInst = recordListAdapter.mList.get(position);
                Intent intent = new Intent();
                intent.putExtra("uid", practiceInst.uid);
                intent.setClass(PracticeListActivity.this, TrainPracticeDetailActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        getPracticeList(mType, mPageIndex+"",mPageSize+"");
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

        getPracticeList(mType, mPageIndex+"",mPageSize+"");
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

    private void getPracticeList(String type, String pageIndex,String pageSize){
        getStudentPracticeList(type, pageIndex,pageSize);
    }



    //实践记录
    private void getStudentPracticeList(String type, String pageIndex,String pageSize){

        StudentModel.getInstance().getPracticeList(type, pageIndex,pageSize,new BaseListener(Practice.class){

            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);

                mPracticeList = (List<Practice>) listObj;
                if (pageInfo != null){
                    mPageIndex = pageInfo.index;
                    mPageCount = pageInfo.count;
                    mPageSize = pageInfo.size;
                }

                if (mPracticeList != null && mPracticeList.size() > 0){
                    if (mPageIndex == 1){
                        recordListAdapter.setDataList(mPracticeList);
                        mRv.refreshComplete();
                    }else {
                        recordListAdapter.insertList(mPracticeList);

                        if (mPageIndex == mPageCount){
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.TheEnd);
                        }else{
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.Normal);
                        }

                    }
                }else{
                    mRv.refreshComplete();
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
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
                mRv.refreshComplete();

            }
        });

    }

    public void finishActivity(View view){
        finish();
    }
}
