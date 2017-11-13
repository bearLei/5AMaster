package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.NotificationMsgListAdapter;
import com.puti.education.bean.EventBean;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xjibn on 2017/4/28 0028.
 *
 * 通知中心
 */

public class MsgListActivity extends BaseActivity implements LRecyclerView.LScrollListener{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.msg_rv)
    LRecyclerView mRv;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;

    private LRecyclerViewAdapter baseCommonListAdapter;
    private NotificationMsgListAdapter msgListAdapter;

    private int mPageIndex = 1;
    private int mPageSize = 20;
    private int mPageCount;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_teacher_msg_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initVariables() {

    }


    @Override
    public void initViews() {

        mTitleTv.setText("通知中心");
        msgListAdapter = new NotificationMsgListAdapter(this);
        baseCommonListAdapter = new LRecyclerViewAdapter(this,msgListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(baseCommonListAdapter);
        mRv.setLScrollListener(this);
        mRv.setRefreshing(true);
        msgListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                // type	类型	整数	1 系统通知　2 事件消息　其它？
                NotificationMsg notifyMsg = msgListAdapter.mList.get(position);
                //openDetail(notifyMsg);

            }
        });

    }

    public void openDetail(NotificationMsg notifyMsg){
        Intent intent = new Intent();
        intent.putExtra("type", 1);
        intent.putExtra("msg_type", notifyMsg.type);
        intent.putExtra("msg_uid", notifyMsg.uid);
        intent.putExtra("msg_title", notifyMsg.title);
        intent.setClass(this, WebViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        getMsgList(mPageIndex,mPageSize);
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

        getMsgList(mPageIndex,mPageSize);
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

    //系统通知
    private void getMsgList(int pageIndex,int pageSize){

        CommonModel.getInstance().getMsgList(pageIndex,pageSize,new BaseListener(NotificationMsg.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);

                List<NotificationMsg> tempList = (List<NotificationMsg>) listObj;

                if (pageInfo != null){
                    mPageIndex = pageInfo.index;
                    mPageCount = pageInfo.count;
                    mPageSize = pageInfo.size;
                }

                if (tempList != null && tempList.size() > 0){
                    if (mPageIndex == 1){
                        msgListAdapter.setDataList(tempList);
                        mRv.refreshComplete();
                    }else {
                        msgListAdapter.insertList(tempList);

                        if (mPageIndex == mPageCount){
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.TheEnd);
                        }else{
                            RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.Normal);
                        }

                    }
                }else{
                    mRv.refreshComplete();
                }

                if (msgListAdapter.mList.size() > 0){
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
