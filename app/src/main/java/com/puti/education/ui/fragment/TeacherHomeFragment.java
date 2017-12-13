package com.puti.education.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.adapter.TodayEventListAdapter;
import com.puti.education.bean.NewNotice;
import com.puti.education.bean.PushData;
import com.puti.education.bean.TeacherBehaviorBean;
import com.puti.education.bean.TeacherExperience;
import com.puti.education.bean.TeacherHomeDataBean;
import com.puti.education.bean.TeacherPower;
import com.puti.education.bean.TodayEventBean;
import com.puti.education.bean.WeekEventItem;
import com.puti.education.bean.WeekEventLineData;
import com.puti.education.bean.YearEventLineData;
import com.puti.education.bean.YearEventsItem;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.EventTypeChooseActivity;
import com.puti.education.ui.uiCommon.QuestionnaireDetailActivity;
import com.puti.education.ui.uiCommon.WebViewActivity;
import com.puti.education.ui.uiPatriarch.ActionEventDetailActivity;
import com.puti.education.ui.uiPatriarch.GrowthTrackDetailActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeDetailActivity;
import com.puti.education.ui.uiStudent.PracticeDetailActivity;
import com.puti.education.ui.uiTeacher.AddEventZxingActivity;
import com.puti.education.ui.uiTeacher.TeacherAddEventActivity;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.ui.uiTeacher.TeacherMainActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.Key;
import com.puti.education.util.ListViewMeasureUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.AppSwipeRefreshLayout;
import com.puti.education.widget.CommonNestedScrollView;
import com.puti.education.widget.ListViewForScrollView;
import com.puti.education.widget.MultiBarChartView;
import com.puti.education.widget.WeekEventView;
import com.puti.education.widget.YearEventView;
import com.puti.education.R;
import com.puti.education.zxing.ZxingUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/4/18 0018.
 * <p>
 * 教师 首页
 */

public class TeacherHomeFragment extends BaseFragment {

    @BindView(R.id.refresh_layout)
    AppSwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.weekevent_view)
    WeekEventView mWeekEventView;
    @BindView(R.id.layout_multi_bar)
    LinearLayout mMultiBar;
    @BindView(R.id.multi_bar_view)
    MultiBarChartView multiBarChartView;
    @BindView(R.id.today_event_linear)
    LinearLayout mTodayEventLinear;
    @BindView(R.id.teacher_action_layout)
    LinearLayout mActionDataLinear;
    @BindView(R.id.today_event_lv)
    ListView mTodayEventLv;

    @BindView(R.id.weekevent_pro1)
    ProgressBar mWeekEventPro;
    @BindView(R.id.teacher_pro4)
    ProgressBar mTeacherPro;

    @BindView(R.id.tv_notice_title)
    TextView mTvNoticeTitle;
    @BindView(R.id.tv_notice_time)
    TextView mTvNoticeTime;
    @BindView(R.id.newnotice_rel)
    RelativeLayout mLatestNotice;

    //教师行为数据
    @BindView(R.id.tv_uba_title1)
    TextView mUbaTitle1;
    @BindView(R.id.tv_uba_title2)
    TextView mUbaTitle2;
    @BindView(R.id.tv_uba_title3)
    TextView mUbaTitle3;
    @BindView(R.id.tv_uba_title4)
    TextView mUbaTitle4;
    @BindView(R.id.tv_uba_title5)
    TextView mUbaTitle5;
    @BindView(R.id.tv_uba_title6)
    TextView mUbaTitle6;

    @BindView(R.id.tv_uba_value1)
    TextView mUbaValue1;
    @BindView(R.id.tv_uba_value2)
    TextView mUbaValue2;
    @BindView(R.id.tv_uba_value3)
    TextView mUbaValue3;
    @BindView(R.id.tv_uba_value4)
    TextView mUbaValue4;
    @BindView(R.id.tv_uba_value5)
    TextView mUbaValue5;
    @BindView(R.id.tv_uba_value6)
    TextView mUbaValue6;
    @BindView(R.id.scroll_layout)
    CommonNestedScrollView mScrollview;


    private final static String[] WEEK_TITLE_ARRAY = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private NewNotice mNewNotice;
    private List<WeekEventLineData> mWeekEventList;
    private List<TeacherBehaviorBean> mBehaviorList;

    List<TeacherExperience> teacherExperienceList;
    List<TeacherPower> teacherPowerList;
    List<TodayEventBean> todayEventList;

    int tempStep = 0;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_teacher_home;
    }

    @Override
    public void initVariables() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initViews(View view) {

        mActionDataLinear.setVisibility(View.GONE);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_refresh_progress));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewNotice();
                getTeacherHomeData();
            }
        });

    }

    @Override
    public void loadData() {
        getNewNotice();
        getTeacherHomeData();
    }

    private void getTeacherHomeData() {

        TeacherModel.getInstance().getTeacherHomeData(new BaseListener(TeacherHomeDataBean.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {

                TeacherHomeDataBean homeData = (TeacherHomeDataBean) infoObj;
                if (homeData != null) {
                    mWeekEventList = homeData.weekEventList;
                    mBehaviorList = homeData.behavioralList;
                    teacherPowerList = homeData.powerData;
                    teacherExperienceList = homeData.experienceData;
                    todayEventList = homeData.todayEventsList;
                    //setSample();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                refreshTodayEvent();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                mSwipeRefreshLayout.setRefreshing(false);
                mWeekEventPro.setVisibility(View.GONE);
                mTeacherPro.setVisibility(View.GONE);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    public void setSample(){
        ArrayList<TodayEventBean> lists= new ArrayList<TodayEventBean>();
        TodayEventBean one = new TodayEventBean();
        one.studentname = "张三";
        one.eventtype = "违规用电";
        one.involvedtype = "主要涉事人";
        TodayEventBean one2 = new TodayEventBean();
        one2.studentname = "李四";
        one2.eventtype = "打架斗殴";
        one2.involvedtype = "次要涉事人";
        lists.add(one);
        lists.add(one2);
        todayEventList = lists;
    }

    public void setHeight(int height){
        ViewGroup.LayoutParams params = this.mTodayEventLv.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        mTodayEventLv.setLayoutParams(params);
    }


    private void refreshTodayEvent() {

        Handler handler = new Handler();

        if (todayEventList != null && todayEventList.size() > 0) {
            mTodayEventLinear.setVisibility(View.VISIBLE);
            int len = todayEventList.size();
            if (len >= 5){
                setHeight(DisPlayUtil.dip2px(this.getActivity(), 240));
                mTodayEventLv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mScrollview.requestDisallowInterceptTouchEvent(true);
                        return false;
                    }}
                );
            }else{
                setHeight(DisPlayUtil.dip2px(this.getActivity(), len * 50));
            }
            TodayEventListAdapter todayEventListAdapter = new TodayEventListAdapter(getActivity());
            todayEventListAdapter.setmList(todayEventList);
            todayEventListAdapter.notifyDataSetChanged();
            mTodayEventLv.setAdapter(todayEventListAdapter);
            //ListViewMeasureUtil.measureListViewWrongHeight(mTodayEventLv);
        }else{
            mTodayEventLinear.setVisibility(View.GONE);
        }

        //周事件
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWeekEventView.initData(mWeekEventList.get(0).weekEventItems, mWeekEventList.get(1).weekEventItems, WEEK_TITLE_ARRAY);
                mWeekEventPro.setVisibility(View.GONE);
            }
        }, 200);

        //教师行为分析数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressBarValue();
            }
        }, 300);

        //教师能力分和经验分
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((teacherExperienceList != null && teacherExperienceList.size() > 0)) {
                    mMultiBar.setVisibility(View.VISIBLE);
//                    teacherExperienceList.get(teacherExperienceList.size()-1).value +=tempStep;
//                    tempStep++;
                    multiBarChartView.initData(teacherExperienceList, teacherPowerList);
                    mTeacherPro.setVisibility(View.GONE);
                }else {
                    mMultiBar.setVisibility(View.GONE);
                }
            }
        }, 400);

    }

    private void showProgressBarValue() {

        if (mBehaviorList != null && mBehaviorList.size() == 6) {
            mUbaTitle1.setText(mBehaviorList.get(0).name);
            mUbaValue1.setText(mBehaviorList.get(0).value);
            mUbaTitle2.setText(mBehaviorList.get(1).name);
            mUbaValue2.setText(mBehaviorList.get(1).value);
            mUbaTitle3.setText(mBehaviorList.get(2).name);
            mUbaValue3.setText(mBehaviorList.get(2).value);
            mUbaTitle4.setText(mBehaviorList.get(3).name);
            mUbaValue4.setText(mBehaviorList.get(3).value);
            mUbaTitle5.setText(mBehaviorList.get(4).name);
            mUbaValue5.setText(mBehaviorList.get(4).value);
            mUbaTitle6.setText(mBehaviorList.get(5).name);
            mUbaValue6.setText(mBehaviorList.get(5).value);
        }
    }

    //添加事件
    @OnClick(R.id.add_event_rel)
    public void addNewEventClick() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), EventTypeChooseActivity.class);
        startActivity(intent);

    }
    @OnClick(R.id.add_event_sao)
    public void addSao(){
        startActivity(new Intent(getActivity(), AddEventZxingActivity.class));
    }
    //查看系统通知消息
    @OnClick(R.id.newnotice_rel)
    public void lookNetNoticeClick() {
        if (mNewNotice == null || mNewNotice.extContent == null) {
            return;
        }
        switch (mNewNotice.extContent.subType) {
            case PushData.TARGET_QUESTIONNAIRE: //在线调查(公共)
                Intent intent = new Intent();
                intent.putExtra("id", mNewNotice.extContent.value);
                intent.setClass(this.getActivity(), QuestionnaireDetailActivity.class);
                startActivity(intent);
                break;
            case PushData.TARGET_MUTUAL_REVIEW: //互评(公共)
                TeacherMainActivity tMainAy = (TeacherMainActivity) this.getActivity();
                tMainAy.gotoReview();
                break;
            case PushData.TARGET_SYS_MESSAGE:
                Intent intent3 = new Intent();
                intent3.putExtra("type", 1);
                intent3.putExtra("msg_id",mNewNotice.extContent.value);
                intent3.setClass(this.getActivity(), WebViewActivity.class);
                startActivity(intent3);
                break;
            case PushData.TARGET_EVENT_UNCONFIRM:
            case PushData.TARGET_EVENT_UNCHECK:
            case PushData.TARGET_EVENT_CHECKED:
            case PushData.TARGET_EVENT_UNTRACK:
            case PushData.TARGET_EVENT_PUSH_OFFICE:
            case PushData.TARGET_EVENT_FINISHED:
            case PushData.TARGET_EVENT_PUSH_PARENT:
                openEventDetail(mNewNotice.extContent.value, mNewNotice.extContent.valueExt);
                break;
            case PushData.TARGET_TRAIN_STUDENT: //学生录入实践记录详情(教师端)
                openStudentPractice(mNewNotice.extContent.value);
                break;
            case PushData.TARGET_TRAIN_PARENT://家长录入培训记录详情(教师端)
                openParentPractice(mNewNotice.extContent.value);
                break;
            default:
                Intent intent5 = new Intent();
                intent5.putExtra("type", 2);
                intent5.putExtra("msg_title", "通知");
                intent5.putExtra("content", mNewNotice.alertMsg);
                intent5.setClass(this.getActivity(), WebViewActivity.class);
                startActivity(intent5);
                break;
        }

    }

    private void openActionEvent(int id){
        Intent intent4 = new Intent();
        intent4.putExtra("id", id);
        intent4.setClass(this.getActivity(), ActionEventDetailActivity.class);
        startActivity(intent4);
    }

    private void openEventDetail(String eventId, String peopleuid)
    {
        Intent intent4 = new Intent();
        intent4.putExtra("type", 4);
        intent4.putExtra(Key.EVENT_KEY, eventId);
        intent4.putExtra(Key.KEY_PEOPLE_UID, peopleuid);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent4.setClass(this.getContext(), TeacherEventDetailActivity.class);
        this.getContext().startActivity(intent4);
    }

    private void openStudentPractice(String uid){
        TeacherMainActivity tMainAy = (TeacherMainActivity) this.getActivity();
        tMainAy.gotoTab(3);
    }

    private void openParentPractice(String uid){
        TeacherMainActivity tMainAy = (TeacherMainActivity) this.getActivity();
        tMainAy.gotoTab(3);
    }


    private void getNewNotice() {
        CommonModel.getInstance().getLatestNotice(new BaseListener(NewNotice.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                NewNotice tempObj = (NewNotice)infoObj;
                if (tempObj != null && tempObj.extContent != null) {
                    mLatestNotice.setVisibility(View.VISIBLE);
                    mNewNotice = (NewNotice) infoObj;
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

    public void setPonitClickListener(){
        mWeekEventView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZxingUtil.g().onActivityResult(requestCode, resultCode, data, new ZxingUtil.ZxingCallBack() {
            @Override
            public void result(String result) {
                LogUtil.d("lei","扫描结果-->"+result);
            }

            @Override
            public void fail() {

            }
        });
    }
}
