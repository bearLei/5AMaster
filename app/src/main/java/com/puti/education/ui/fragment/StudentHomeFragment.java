package com.puti.education.ui.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventBean;
import com.puti.education.bean.LoginUser;
import com.puti.education.bean.NewNotice;
import com.puti.education.bean.PushData;
import com.puti.education.bean.Student;
import com.puti.education.bean.StudentIndexData;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.QuestionnaireDetailActivity;
import com.puti.education.ui.uiCommon.WebViewActivity;
import com.puti.education.ui.uiPatriarch.ActionEventAddActivity;
import com.puti.education.ui.uiStudent.ActionEventActivity;
import com.puti.education.ui.uiStudent.PracticeAddActivity;
import com.puti.education.ui.uiStudent.StudentMainActivity;
import com.puti.education.ui.uiTeacher.TeacherMainActivity;
import com.puti.education.util.NumberUtils;
import com.puti.education.util.ThreadUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.AppSwipeRefreshLayout;
import com.puti.education.widget.RadarView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 问卷调查模块
 */

public class StudentHomeFragment extends BaseFragment {

    @BindView(R.id.radarview)
    RadarView mRadarView;
    @BindView(R.id.tv_offence_count)
    TextView mTvOffenceCount;
    @BindView(R.id.tv_honour_count)
    TextView mTvHonourCount;
    @BindView(R.id.tv_exception_value)
    TextView mTvExceptionCount;
    @BindView(R.id.tv_violation_value)
    TextView mTvViolation;
    @BindView(R.id.tv_daily_value)
    TextView mTvDailyValue;
    @BindView(R.id.tv_notice_title)
    TextView mTvNoticeTitle;
    @BindView(R.id.tv_notice_time)
    TextView mTvNoticeTime;
    @BindView(R.id.newnotice_rel)
    RelativeLayout mLatestNotice;
    @BindView(R.id.refresh_layout)
    AppSwipeRefreshLayout mSwipeRefreshLayout;

    private NewNotice mNewNotice;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_student_home;
    }

    @Override
    public void initVariables() {

    }

    @OnClick(R.id.frame_img)
    public void addPractice(){
        Intent intent = new Intent();
        intent.putExtra("lng", "");
        intent.putExtra("lat", "");
        intent.setClass(this.getActivity(), ActionEventAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void initViews(View view) {
        String tText[] = {"学生习惯", "同伴影响", "行为能力", "社交能力", "学校氛围", "性格特征"};
        double tValue[]= {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        mRadarView.setTextContent(tText);
        mRadarView.setValueContent(tValue);
        mRadarView.setCenterValue("0%");
        mRadarView.startDraw();

        mLatestNotice.setVisibility(View.GONE);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_refresh_progress));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewNotice();
                getIndexData();
            }
        });
    }

    public void refreshRadarView(StudentIndexData datas){
        String tText[] = {"学生习惯", "同伴影响", "行为能力", "社交能力", "学校氛围", "性格特征"};
        double tValue[]= {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        if (datas != null){
            tValue[0] = getPercentValue(datas.academicRecordIndex);
            tValue[1] = getPercentValue(datas.peerInfluenceIndex);
            tValue[2] = getPercentValue(datas.behaviorAbilityIndex);
            tValue[3] = getPercentValue(datas.socialSkillsIndex);
            tValue[4] = getPercentValue(datas.schoolAtmosphereIndex);
            tValue[5] = getPercentValue(datas.characterIndex);
        }

        mRadarView.setTextContent(tText);
        mRadarView.setValueContent(tValue);
        mRadarView.setCenterValue(datas.synthesisScore + "%");
        mRadarView.startDraw();
    }

    private double getPercentValue(int intvalue){
        if (intvalue < 0){
            return 0;
        }else{
            return NumberUtils.div(intvalue, 100, 2);
        }
    }


    @Override
    public void loadData() {
        getIndexData();
        getNewNotice();

    }


    @OnClick(R.id.img_event_record)
    public void eventRecords(){
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), ActionEventActivity.class);
        startActivity(intent);
    }

    private void getIndexData(){
        StudentModel.getInstance().studentIndex(new BaseListener(StudentIndexData.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                StudentIndexData studentOjb = (StudentIndexData)infoObj;
                if (studentOjb != null) {
                    mTvOffenceCount.setText(studentOjb.violateDiscipline + "");
                    mTvHonourCount.setText(studentOjb.commend + "");
                    mTvExceptionCount.setText(studentOjb.anomaly + "");
                    mTvViolation.setText(studentOjb.violateRegulations + "");
                    mTvDailyValue.setText(studentOjb.dailyBehavior + "");
                    refreshRadarView(studentOjb);
                }
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtil.show("获取学生首页数据出错 "  + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    //查看系统通知消息
    @OnClick(R.id.newnotice_rel)
    public void lookNetNoticeClick(){
        if (mNewNotice == null || mNewNotice.extContent == null){
            return;
        }
        switch (mNewNotice.extContent.subType){
            case PushData.TARGET_QUESTIONNAIRE: //在线调查
                Intent intent = new Intent();
                intent.putExtra("id", mNewNotice.extContent.value);
                intent.setClass(this.getActivity(), QuestionnaireDetailActivity.class);
                startActivity(intent);
                break;
            case PushData.TARGET_MUTUAL_REVIEW: //互评
                StudentMainActivity tMainAy = (StudentMainActivity)this.getActivity();
                tMainAy.gotoReview();
                break;
            case PushData.TARGET_SYS_MESSAGE://通知消息
                Intent intent3 = new Intent();
                intent3.putExtra("type", 1);
                intent3.putExtra("msg_id", mNewNotice.uid);
                intent3.setClass(this.getActivity(), WebViewActivity.class);
                startActivity(intent3);
                break;
            default:
                Intent intent5 = new Intent();
                intent5.putExtra("type", 2);
                intent5.putExtra("msg_title", mNewNotice.alertMsg);
                intent5.putExtra("content", mNewNotice.alertMsg);
                intent5.setClass(this.getActivity(), WebViewActivity.class);
                startActivity(intent5);
                break;
        }

    }

    private void getNewNotice(){
        CommonModel.getInstance().getLatestNotice(new BaseListener(NewNotice.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                NewNotice tempObj = (NewNotice)infoObj;
                if (tempObj != null && tempObj.extContent != null){
                    mLatestNotice.setVisibility(View.VISIBLE);
                    mNewNotice = tempObj;
                    mTvNoticeTitle.setText(mNewNotice.alertMsg);
                    mTvNoticeTime.setText(mNewNotice.bizTime);
                }else{
                    mLatestNotice.setVisibility(View.GONE);
                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                mLatestNotice.setVisibility(View.GONE);
                ToastUtil.show("获取最新消息出错" + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }
}
