package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.puti.education.adapter.SampleListAdapter;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.bean.SampleInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;

public class SampleListsActivity extends BaseActivity implements LRecyclerView.LScrollListener{
    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.msg_rv)
    LRecyclerView mRv;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;

    private int mType;  //1个案分析　　2措施对策

    private LRecyclerViewAdapter baseCommonListAdapter;
    private SampleListAdapter msgListAdapter;
    private ArrayList<SampleInfo> mList;
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_sample_lists;
    }

    @Override
    public void initVariables() {
        mType = this.getIntent().getIntExtra(Key.EVENT_TITLE, -1);
    }

    @Override
    public void initViews() {
        if (mType == 1){
            mTitleTv.setText("个案分析");
        }else if(mType == 2){
            mTitleTv.setText("措施对策");
        }

        msgListAdapter = new SampleListAdapter(this);
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
                SampleInfo sample = msgListAdapter.mList.get(position);
                Intent intent = new Intent();
                intent.putExtra("type", mType);
                intent.putExtra("value", sample.value);
                setResult(Constant.CODE_RESULT_SAMPLE, intent);
                finish();

            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onRefresh() {
        getDealTemplate(mType);
    }

    @Override
    public void onScrollUp() {

    }

    @Override
    public void onScrollDown() {

    }

    @Override
    public void onBottom() {

    }

    @Override
    public void onScrolled(int distanceX, int distanceY) {

    }


    //获取处理模板
    private void getDealTemplate(int type){
        //disLoading("获取处理模板");
        TeacherModel.getInstance().getDealTemplate(type, new BaseListener(String.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                if (listObj != null){
                    ArrayList<String> tempFile = (ArrayList<String>)listObj;
                    //ToastUtil.show("获取处理模板成功");

                    if (tempFile != null && tempFile.size() > 0){
                        ArrayList<SampleInfo> samarr = new ArrayList();
                        for (String one: tempFile){
                            SampleInfo samOne = new SampleInfo();
                            samOne.value = one;
                            samarr.add(samOne);
                        }
                        msgListAdapter.setDataList(samarr);
                        mRv.refreshComplete();
                        RecyclerViewStateUtils.setFooterViewState(mRv, LoadingFooter.State.Normal);
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
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                //hideLoading();
                ToastUtil.show("获取处理模板失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

}
