package com.puti.education.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.QuestionnaireListAdapter;
import com.puti.education.bean.Questionnaire;
import com.puti.education.event.QuestionRefreshEvent;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.R;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.EventEvaluationActivity;
import com.puti.education.ui.uiCommon.EventReviewDetailActivity;
import com.puti.education.ui.uiCommon.MutualReviewDetailActivity;
import com.puti.education.ui.uiCommon.MutualReviewParentActivity;
import com.puti.education.ui.uiCommon.MutualReviewTeacherActivity;
import com.puti.education.ui.uiCommon.QtExceptionEventDetailActivity;
import com.puti.education.ui.uiCommon.QuestionnaireDetailActivity;
import com.puti.education.ui.uiTeacher.chooseperson.event.ChooseCompleteEvent;
import com.puti.education.util.Constant;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 问卷调查模块
 */

public class SurveyFragment extends BaseFragment implements LRecyclerView.LScrollListener{
    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.btn_question)
    TextView mBtnQuestion;
    @BindView(R.id.btn_review)
    TextView mBtnReview;

    @BindView(R.id.layout_questions)
    FrameLayout mLayoutQuestions;
    @BindView(R.id.recycler_questions)
    LRecyclerView mQtRecyclerView;
    @BindView(R.id.empty_rel)
    RelativeLayout mEmptyRealtive;
    @BindView(R.id.review_layout)
    LinearLayout mReviewLayout;

    private ArrayList<Questionnaire> mQtList;
    private QuestionnaireListAdapter mQtAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public int getLayoutResourceId() {
        return R.layout.main_survery_layout;
    }

    @OnClick(R.id.review_event_layout)
    public void rateEvent(){

        //startExceptionQtDetail(0);
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), EventEvaluationActivity.class);
        startActivity(intent);
    }

    @Override
    public void initVariables() {
        mQtAdapter = new QuestionnaireListAdapter(this.getActivity());



        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mQtRecyclerView.setLayoutManager(layoutManager);
        mQtRecyclerView.setRefreshProgressStyle(AVLoadingIndicatorView.BallSpinFadeLoader);
        mQtRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(getActivity(), mQtAdapter);
        mQtRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mQtRecyclerView.setLScrollListener(this);
        mQtRecyclerView.setRefreshing(true);

        mQtAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                Questionnaire qt = mQtList.get(position);
                if (qt != null){
                    startQtDetail(qt.uid);
                    //startExceptionQtDetail(qt.id);
                }
            }
        });
    }

    @Override
    public void initViews(View view) {
        mUiTitle.setText("问卷互评");
        questionTab();
    }

    @Override
    public void loadData() {
        getQtList();
    }

    private void getQtList(){
        CommonModel.getInstance().getQuestionnaireList(new BaseListener(Questionnaire.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (listObj != null){
                    mQtList = (ArrayList<Questionnaire>) listObj;
                    mQtAdapter.setDataList(mQtList);

                    mQtRecyclerView.refreshComplete();

                    if (mQtAdapter.mList.size() > 0){
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
        });
    }

    private void startQtDetail(String uid){
        Intent intent = new Intent();
        intent.putExtra("id", uid);
        intent.setClass(this.getActivity(), QuestionnaireDetailActivity.class);
        this.startActivity(intent);
    }

    private void startExceptionQtDetail(int id){
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.setClass(this.getActivity(), QtExceptionEventDetailActivity.class);
        this.startActivity(intent);
    }

    @OnClick(R.id.btn_question)
    public void questionTab(){
        mBtnQuestion.setBackgroundResource(R.drawable.tab_select_on_green);
        mBtnReview.setBackgroundResource(R.color.white);

        mReviewLayout.setVisibility(View.GONE);
        mLayoutQuestions.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_review)
    public void reviewTab(){
        mBtnQuestion.setBackgroundResource(R.color.white);
        mBtnReview.setBackgroundResource(R.drawable.tab_select_on_green);
        mReviewLayout.setVisibility(View.VISIBLE);
        mLayoutQuestions.setVisibility(View.GONE);
    }

    @OnClick(R.id.review_teacher_layout)
    public void reviewTeacher(){
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_ROLE_TYPE, Constant.ROLE_TEACHER);
        intent.setClass(this.getActivity(), MutualReviewTeacherActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.review_student_layout)
    public void reviewStudent(){
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_ROLE_TYPE, Constant.ROLE_STUDENT);
        intent.setClass(this.getActivity(), MutualReviewDetailActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.review_parent_layout)
    public void reviewParent(){
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_ROLE_TYPE, Constant.ROLE_PARENTS);
        intent.setClass(this.getActivity(), MutualReviewParentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getQtList();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(QuestionRefreshEvent event) {
        getQtList();
    }
}
