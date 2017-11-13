package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.R;
import com.puti.education.adapter.EventReviewListAdapter;
import com.puti.education.bean.EventEvaluation;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.ListViewForScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class FinishEventActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTvTitle;
    @BindView(R.id.layout_parent_score)
    LinearLayout mLayoutParentScore;
    @BindView(R.id.layout_expert_score)
    LinearLayout mLayoutExpertScore;
    @BindView(R.id.lv_parents)
    ListViewForScrollView mLvParents;
    @BindView(R.id.lv_expert)
    ListViewForScrollView mLvExperts;
    @BindView(R.id.et_review_input)
    EditText mEtReviewInput;

    private String mEventUid;
    private String mUserUid;


    private ArrayList<EventEvaluation> mParentList;
    private ArrayList<EventEvaluation> mExpertList;

    private EventReviewListAdapter mReviewParentsAdapter;
    private EventReviewListAdapter mReviewExpertsAdapter;
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_finish_event;
    }

    @Override
    public void initVariables() {
        mEventUid = this.getIntent().getStringExtra(Key.KEY_EVENT_UID);
        mUserUid  = this.getIntent().getStringExtra(Key.KEY_PEOPLE_UID);
        mParentList= (ArrayList<EventEvaluation>)this.getIntent().getSerializableExtra(Key.KEY_PARENT_DATA);
        mExpertList= (ArrayList<EventEvaluation>)this.getIntent().getSerializableExtra(Key.KEY_EXPERT_DATA);
    }

    @Override
    public void initViews() {
        mTvTitle.setText("结束事件");
        mEtReviewInput.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtReviewInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }});
        if (mParentList != null && mParentList.size() > 0) {
            mLayoutParentScore.setVisibility(View.VISIBLE);
            mReviewParentsAdapter = new EventReviewListAdapter(this, 1);
            mReviewParentsAdapter.setmList(mParentList);
            mLvParents.setAdapter(mReviewParentsAdapter);
            mLvParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }else{
            mLayoutParentScore.setVisibility(View.VISIBLE);
        }
        if (mExpertList != null && mExpertList.size() > 0) {
            mLayoutExpertScore.setVisibility(View.VISIBLE);
            mReviewExpertsAdapter = new EventReviewListAdapter(this, 2);
            mReviewExpertsAdapter.setmList(mExpertList);
            mLvExperts.setAdapter(mReviewExpertsAdapter);
            mLvExperts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }else {
            mLayoutExpertScore.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.btn_event_btn)
    public void commitFinished() {

        String str = createEventFinishedParamStr();
        if (str == null) {
            return;
        }
        disLoading();
        TeacherModel.getInstance().eventFinished(str, new BaseListener() {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();

                JSONObject jsonObject = JSONObject.parseObject(infoObj.toString());
                ToastUtil.show("结束事件成功");
                sendBrodcastRefresh();
//                if (jsonObject.containsKey(Constant.UID)) {
//                    ToastUtil.show("结束事件成功");
//                } else {
//                    ToastUtil.show("结束事件失败");
//                }
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });

    }

    //刷新列表
    private void sendBrodcastRefresh(){
        Intent intent = new Intent();
        intent.setAction(Constant.BROADCAST_REFRESH_EVENT);
        this.sendBroadcast(intent);
    }

    private String createEventFinishedParamStr() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventUid", mEventUid);
        jsonObject.put("studentUid", mUserUid);
        jsonObject.put("Reason", mEtReviewInput.getText().toString());

        JSONArray subArr = new JSONArray();

        if (mParentList != null && mParentList.size() > 0){
            JSONObject subjson;
            for (EventEvaluation one: mParentList){
                subjson = new JSONObject();
                subjson.put("personnelType", one.personneltype);
                subjson.put("personnelUid", one.personneluid);
                if (one.score >= 0){
                    subjson.put("sige", 2);
                }else{
                    subjson.put("sige", 1);
                }
                subjson.put("score", one.score);
                subArr.add(subjson);
            }
        }

        if (mExpertList != null && mExpertList.size() > 0){
            JSONObject subjson;
            for (EventEvaluation one: mExpertList){
                subjson = new JSONObject();
                subjson.put("personnelType", one.personneltype);
                subjson.put("personnelUid", one.personneluid);
                if (one.score >= 0){
                    subjson.put("sige", 2);
                }else{
                    subjson.put("sige", 1);
                }
                subjson.put("score", one.score);
                subArr.add(subjson);
            }
        }
        jsonObject.put("scores", subArr);

        LogUtil.i("params", jsonObject.toString());

        return jsonObject.toString();
    }

}
