package com.puti.education.ui.uiCommon;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.puti.education.bean.RateEventItem;
import com.puti.education.bean.RatePeople;
import com.puti.education.bean.RatePeopleItem;
import com.puti.education.bean.ResponseBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.RatingBarView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 * 互评项
 */

public class EventReviewDetailActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.et_desc)
    EditText mEditTextDesc;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;

    private int mScope;
    private RateEventItem mCurrentEvent;
    private ArrayList<RatePeople> mList;


    @Override
    public int getLayoutResourceId() {
        return R.layout.event_review_detail_layout;
    }

    @Override
    public void initVariables() {
    }

    @Override
    public void initViews() {
        mUiTitle.setText("事件互评");
        resetRateValue();

        RatingBarView originRatingbar = (RatingBarView) findViewById(R.id.custom_ratingbar);
        originRatingbar.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mScope = RatingScore;
                //Toast.makeText(EventReviewDetailActivity.this, String.valueOf(RatingScore), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadData() {
        getRateEventDetail();
    }


    private void getRateEventDetail(){
        CommonModel.getInstance().rateEventDetail(new BaseListener(RateEventItem.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                mBtnCommit.setClickable(false);
                mBtnCommit.setEnabled(false);
                if (infoObj != null){
                    mCurrentEvent = (RateEventItem) infoObj;
                    setRateValue(mCurrentEvent);
                    mBtnCommit.setEnabled(true);
                    mBtnCommit.setClickable(true);
                }else{
                    ToastUtil.show("没有事件可评价");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show("获取事件详情出错 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                mBtnCommit.setClickable(false);
                mBtnCommit.setEnabled(false);
            }
        });
    }

    @OnClick(R.id.btn_commit)
    public void commitRateEvent(){
        if (mCurrentEvent == null){
            ToastUtil.show("没有获取到最新待评价事件");
            return;
        }
        if (mScope <= 0){
            ToastUtil.show("请选择评价分值");
            return;
        }

        CommonModel.getInstance().rateLatestEvent(mCurrentEvent, new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                ToastUtil.show("评价成功");
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("评价失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void resetRateValue(){
        mEditTextDesc.setText(null);
        mEditTextDesc.setBackgroundResource(R.drawable.input_dark_with_right_border);
        //mEditTextDesc.setFocusable(false);
        //mEditTextDesc.setFocusableInTouchMode(false);
    }
    private void setRateValue(RateEventItem rate){
        mTitle.setText(rate.title);
        mTvContent.setText(rate.content);

        //mEditTextDesc.setText(rate.rateDesc);

        //不可编辑状态
//        mEditTextDesc.setBackgroundResource(R.color.white);
//        mEditTextDesc.setFocusable(false);
//        mEditTextDesc.setFocusableInTouchMode(false);
    }


    public void refreshPeople(){

    }


}
