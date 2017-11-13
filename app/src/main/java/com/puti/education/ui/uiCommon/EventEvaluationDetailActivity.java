package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.adapter.ParentFromStudent;
import com.puti.education.bean.EventEvaluationBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.RatingSmallBarView;

import butterknife.BindView;
import butterknife.OnClick;

public class EventEvaluationDetailActivity extends BaseActivity{

    @BindView(R.id.back_frame)
    FrameLayout mBackFrame;
    @BindView(R.id.title_textview)
    TextView mUiTitle;

    @BindView(R.id.tv_time)
    TextView mTvDate;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.rbv_event_level)
    RatingSmallBarView mRatebarLevel;
    @BindView(R.id.rbv_event_score)
    RatingSmallBarView mRatebarScore;
    @BindView(R.id.rbv_event_review)
    EditText mEtReview;

    private String mEventUid;
    private String mPeopleUid;
    private int mReviewValue;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_event_evaluation_detail;
    }

    @Override
    public void initVariables() {

    }


    @Override
    public void initViews() {
        mUiTitle.setText("评价事件");

        mEtReview.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtReview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }});

        EventEvaluationBean evenbean = (EventEvaluationBean)this.getIntent().getSerializableExtra(Key.BEAN);
        if (evenbean != null){
            mTvDate.setText(evenbean.eventTime);
            mTvAddress.setText(evenbean.eventAddress);
            mTvType.setText(evenbean.eventType);
            mRatebarLevel.setStar(evenbean.eventLevel, true);
            mRatebarLevel.setClickable(false);
            mTvDesc.setText(evenbean.eventDescription);
            mTvName.setText(evenbean.studentName);

            mEventUid = evenbean.eventUID;
            mPeopleUid= evenbean.studentUID;

            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle, evenbean.studentAvatar, mIvAvatar);
            mRatebarScore.setOnRatingListener(new RatingSmallBarView.OnRatingListener() {
                @Override
                public void onRating(Object bindObject, int RatingScore) {
                    mReviewValue = RatingScore;
                }
            });
        }


    }



    //事件确认提交
    private void commitConfirm(){
        if (TextUtils.isEmpty(mEventUid)){
            ToastUtil.show("事件ID为空");
            return;
        }
        if (TextUtils.isEmpty(mPeopleUid)){
            ToastUtil.show("人物ID为空");
            return;
        }
        String str = createConfirmParamStr();
        if (str == null){
            return;
        }
        disLoading();
        CommonModel.getInstance().evaluationEvent(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                ToastUtil.show("事件评价成功");
                Intent intent = new Intent();
                intent.setAction(Constant.BROADCAST_REFRESH_EVALUATION);
                EventEvaluationDetailActivity.this.sendBroadcast(intent);
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    //创建事件确认json参数
    private String createConfirmParamStr() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventuid", mEventUid);
        jsonObject.put("studentuid", mPeopleUid);
        jsonObject.put("desc", mEtReview.getText().toString());
        jsonObject.put("score", mReviewValue);

        return jsonObject.toString();
    }


    @Override
    public void loadData() {

    }

    @OnClick(R.id.btn_commit)
    public void commitEventEvaluation(){
        commitConfirm();
    }

}
