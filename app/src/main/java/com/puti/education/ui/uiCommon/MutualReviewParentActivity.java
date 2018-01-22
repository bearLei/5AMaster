package com.puti.education.ui.uiCommon;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.puti.education.R;
import com.puti.education.bean.RatePeople;
import com.puti.education.bean.RatePeopleItem;
import com.puti.education.bean.ResponseBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.RatingBarView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 * 互评家长
 */

public class MutualReviewParentActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.left_container)
    LinearLayout mLetfContainer;
    @BindView(R.id.et_desc)
    EditText mEditTextDesc;
    @BindView(R.id.btnCommitRatePeople)
    Button mBtnCommit;
    @BindView(R.id.layout_review3)
    LinearLayout mReivewLayout3;
    @BindView(R.id.layout_review4)
    LinearLayout mReivewLayout4;
    @BindView(R.id.tv_review_name1)
    TextView mTvName1;
    @BindView(R.id.tv_review_name2)
    TextView mTvName2;
    @BindView(R.id.tv_review_name3)
    TextView mTvName3;
    @BindView(R.id.tv_review_name4)
    TextView mTvName4;

    private int mRateRole;    //互评角色：1 教师 2 学生 4 家长
    private RatePeople mCurrentPeople;
    private ArrayList<RatePeople> mList;
    private RatePeopleItem mRateItem = new RatePeopleItem();
    private RatingBarView mMatchSchoolRate,mChildEducationRate,mScoreEmphasisRate,mTrainEmphasisRate;


    @Override
    public int getLayoutResourceId() {
        return R.layout.mutual_review_detail_layout;
    }

    @Override
    public void initVariables() {
        mRateRole = Constant.ROLE_PARENTS;
    }

    @Override
    public void initViews() {
        mUiTitle.setText("定期互评");
        mBtnCommit.setEnabled(false);

        mTvName1.setText("与学校配合程度评价");
        mTvName2.setText("子女教育方式方法");
        mTvName3.setText("子女成绩重视程度");
        mTvName4.setText("子女行为培养重视程度");

        mReivewLayout3.setVisibility(View.VISIBLE);
        mReivewLayout4.setVisibility(View.VISIBLE);

        mMatchSchoolRate = (RatingBarView) findViewById(R.id.ratingbar1);
        mMatchSchoolRate.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mRateItem.matchSchoolRate = RatingScore;
            }
        });

        mChildEducationRate = (RatingBarView) findViewById(R.id.ratingbar2);
        mChildEducationRate.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mRateItem.childEducationRate = RatingScore;
            }
        });

        mScoreEmphasisRate = (RatingBarView) findViewById(R.id.ratingbar3);
        mScoreEmphasisRate.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mRateItem.scoreEmphasisRate = RatingScore;
            }
        });

        mTrainEmphasisRate = (RatingBarView) findViewById(R.id.ratingbar4);
        mTrainEmphasisRate.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mRateItem.trainEmphasisRate = RatingScore;
            }
        });

    }

    @Override
    public void loadData() {
        getRateParents();
    }

    public void finishActivity(View v){
        this.finish();
    }

    private void getRateParents(){
        disLoading();
        CommonModel.getInstance().rateParentList(new BaseListener(RatePeople.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){
                    mBtnCommit.setEnabled(true);
                    mList = (ArrayList<RatePeople>)listObj;
                    refreshPeople();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("获取互评家长出错" + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                mBtnCommit.setEnabled(false);
            }
        });
    }

    private void getRateDetail(String uid, int type){
        CommonModel.getInstance().getRateParentDetail(uid, new BaseListener(RatePeopleItem.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (infoObj != null){
                    RatePeopleItem rateItem = (RatePeopleItem) infoObj;
                    setRateValue(rateItem);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show("获取评价详情失败");
            }
        });
    }

    @OnClick(R.id.btnCommitRatePeople)
    public void commitRate(){
        commitPeopleRate();
    }

    private void commitPeopleRate(){
        if (mCurrentPeople == null || TextUtils.isEmpty(mCurrentPeople.uid)){
            ToastUtil.show("没有互评人，请重新确认");
            return;
        }
        if (mRateItem.matchSchoolRate == 0 && mRateItem.childEducationRate == 0 && mRateItem.scoreEmphasisRate == 0
                && mRateItem.trainEmphasisRate == 0){
            ToastUtil.show("请输入评价值");
            return;
        }

        String synthesisValue = mEditTextDesc.getText().toString();
        if (TextUtils.isEmpty(synthesisValue)){
            mRateItem.synthesisRate = " ";
        }

        mRateItem.synthesisRate = synthesisValue;

        mRateItem.type = mRateRole + "";
        mRateItem.uid = mCurrentPeople.uid;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid",mRateItem.uid);
        jsonObject.put("matchSchoolRate",mRateItem.matchSchoolRate);
        jsonObject.put("childEducationRate",mRateItem.childEducationRate);
        jsonObject.put("scoreEmphasisRate",mRateItem.scoreEmphasisRate);
        jsonObject.put("trainEmphasisRate",mRateItem.trainEmphasisRate);
        jsonObject.put("synthesisRate",mRateItem.synthesisRate);

        CommonModel.getInstance().rateParent(jsonObject.toString(), new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                mCurrentPeople.isRate = true;
                //mBtnCommit.setEnabled(false);
                ToastUtil.show("评价成功");
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("评价失败" + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void resetRateValue(){
        mBtnCommit.setEnabled(true);
        mMatchSchoolRate.setStar(0,true);
        mChildEducationRate.setStar(0, true);
        mScoreEmphasisRate.setStar(0, true);
        mTrainEmphasisRate.setStar(0,true);

        mRateItem.matchSchoolRate   = 0;
        mRateItem.childEducationRate= 0;
        mRateItem.scoreEmphasisRate = 0;
        mRateItem.trainEmphasisRate = 0;

        mEditTextDesc.setText(null);
        mEditTextDesc.setBackgroundResource(R.drawable.input_dark_with_right_border);
        mEditTextDesc.setFocusable(true);
        mEditTextDesc.setFocusableInTouchMode(true);

    }
    private void setRateValue(RatePeopleItem rate){
        mMatchSchoolRate.setStar(rate.matchSchoolRate,true);
        mChildEducationRate.setStar(rate.childEducationRate, true);
        mScoreEmphasisRate.setStar(rate.scoreEmphasisRate, true);
        mTrainEmphasisRate.setStar(rate.trainEmphasisRate,true);
        mEditTextDesc.setText(rate.synthesisRate);

        mRateItem.matchSchoolRate = rate.matchSchoolRate;
        mRateItem.childEducationRate= rate.childEducationRate;
        mRateItem.scoreEmphasisRate = rate.scoreEmphasisRate;
        mRateItem.trainEmphasisRate = rate.trainEmphasisRate;

        //不可编辑状态
        //mBtnCommit.setEnabled(false);
        //mEditTextDesc.setFocusable(false);
        //mEditTextDesc.setFocusableInTouchMode(false);
    }


    public void refreshPeople(){
        mLetfContainer.removeAllViews();
        setPeopleDisplay();
    }

    public void setPeopleDisplay(){
        Button button;
        int size = mList.size();
        int height = DisPlayUtil.dip2px(this, 50);
        for (int i = 0; i < size; i++) {

            RatePeople people = mList.get(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            button = new Button(this);
            button.setLayoutParams(layoutParams);
            button.setId(i);
            button.setGravity(Gravity.CENTER);
            button.setTextSize(12);
            button.setTextColor(Color.parseColor("#6B6B6B"));
            button.setBackgroundResource(R.drawable.border_with_dark_right_topbottom);
            button.setText(people.name);
            mLetfContainer.addView(button);
        }



        for (int i = 0; i < size; i++) {

            mLetfContainer.getChildAt(i).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int id = v.getId();
                                    // 设置背景
                                    for (int j = 0; j < mList.size(); j++) {
                                        Button peopleView = (Button)mLetfContainer.getChildAt(j);
                                        if (id != j) {
                                            peopleView.setBackgroundResource(R.drawable.border_with_dark_right_topbottom);
                                        } else {
                                            mCurrentPeople =  mList.get(j);
                                            peopleView.setBackgroundResource(R.drawable.border_green_with_dark_right_topbottom);
                                            int peopleID = 0;
                                            if (!TextUtils.isEmpty(mCurrentPeople.uid) && mCurrentPeople.isRate) {
                                                getRateDetail(mCurrentPeople.uid, mRateRole);
                                            }else{
                                                resetRateValue();
                                            }

                                        }
                                    }

                                }

                            });
        }

        if (mList != null && mList.size() > 0){
            mLetfContainer.getChildAt(0)
                    .setBackgroundResource(R.drawable.border_green_with_dark_right_topbottom);
            mCurrentPeople =  mList.get(0);
            if (mCurrentPeople.isRate) {
                this.getRateDetail(mCurrentPeople.uid, mRateRole);
            }else{
                resetRateValue();
            }
        }

    }




}
