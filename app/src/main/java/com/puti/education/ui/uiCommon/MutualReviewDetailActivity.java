package com.puti.education.ui.uiCommon;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.puti.education.bean.RatePeople;
import com.puti.education.bean.RatePeopleItem;
import com.puti.education.bean.ResponseBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.R;
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
 * 互评学生
 */

public class MutualReviewDetailActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.left_container)
    LinearLayout mLetfContainer;
    @BindView(R.id.et_desc)
    EditText mEditTextDesc;
    @BindView(R.id.btnCommitRatePeople)
    Button mBtnCommit;
    @BindView(R.id.tv_review_name1)
    TextView mTvName1;
    @BindView(R.id.tv_review_name2)
    TextView mTvName2;



    private int mRateRole;    //互评角色：1 教师 2 学生 4 家长
    private RatePeople mCurrentPeople;
    private ArrayList<RatePeople> mList;
    private RatePeopleItem mRateItem = new RatePeopleItem();
    RatingBarView mStudyRatingbar, mRelativeRatingbar;


    @Override
    public int getLayoutResourceId() {
        return R.layout.mutual_review_detail_layout;
    }

    @Override
    public void initVariables() {
        mRateRole = Constant.ROLE_STUDENT;
    }

    @Override
    public void initViews() {
        mUiTitle.setText("定期互评");
        mTvName1.setText("学习评价");
        mTvName2.setText("关系评价");
        mBtnCommit.setEnabled(false);

        mStudyRatingbar = (RatingBarView) findViewById(R.id.ratingbar1);
        mStudyRatingbar.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mRateItem.studyRate = RatingScore;
            }
        });

        mRelativeRatingbar = (RatingBarView) findViewById(R.id.ratingbar2);
        mRelativeRatingbar.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mRateItem.relationRate = RatingScore;
            }
        });
    }

    @Override
    public void loadData() {
        getRateStudents();
    }

    public void finishActivity(View v){
        this.finish();
    }

    private void getRateTeachers(){
        CommonModel.getInstance().rateTeacherList(new BaseListener(RatePeople.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                    if (listObj != null){
                        mBtnCommit.setEnabled(true);
                        mList = (ArrayList<RatePeople>)listObj;
                        refreshPeople();
                    }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                mBtnCommit.setEnabled(false);
            }
        });
    }

    private void getRateStudents(){
        disLoading();
        CommonModel.getInstance().rateStudentList(new BaseListener(RatePeople.class){

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
                ToastUtil.show("获取互评学生出错" + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                mBtnCommit.setEnabled(false);

            }
        });
    }

    private void getRateDetail(String uid){
        disLoading();
        CommonModel.getInstance().ratePeopleDetail(uid, new BaseListener(RatePeopleItem.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    RatePeopleItem rateItem = (RatePeopleItem) infoObj;
                    setRateValue(rateItem);
                    mBtnCommit.setEnabled(true);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show("获取评价详情失败" + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                mBtnCommit.setEnabled(false);
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
        if (mRateItem.studyRate == 0 && mRateItem.relationRate == 0){
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
        jsonObject.put("studyRate",mRateItem.studyRate);
        jsonObject.put("relationRate",mRateItem.relationRate);
        jsonObject.put("synthesisRate",mRateItem.synthesisRate);

        CommonModel.getInstance().ratePeople(jsonObject.toString(), new BaseListener(ResponseBean.class){

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
        mStudyRatingbar.setStar(0, true);
        mRelativeRatingbar.setStar(0, true);
        mRateItem.studyRate     = 0;
        mRateItem.relationRate  = 0;

        mEditTextDesc.setText(null);
        mEditTextDesc.setBackgroundResource(R.drawable.input_dark_with_right_border);
        mEditTextDesc.setFocusable(true);
        mEditTextDesc.setFocusableInTouchMode(true);

    }
    private void setRateValue(RatePeopleItem rate){

        mStudyRatingbar.setStar(rate.studyRate, true);
        mRelativeRatingbar.setStar(rate.relationRate, true);
        mEditTextDesc.setText(rate.synthesisRate);

        mRateItem.studyRate = rate.studyRate;
        mRateItem.relationRate = rate.relationRate;

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
                                                mBtnCommit.setEnabled(true);
                                                getRateDetail(mCurrentPeople.uid);
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
                this.getRateDetail(mCurrentPeople.uid);
            }else{
                resetRateValue();
            }
        }

    }




}
