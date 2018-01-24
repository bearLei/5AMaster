package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.ParentFromStudent;
import com.puti.education.adapter.QuestionnaireEtDetailAdapter;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventCourse;
import com.puti.education.bean.Question;
import com.puti.education.bean.Questionnaire;
import com.puti.education.bean.ResponseBean;
import com.puti.education.event.QuestionRefreshEvent;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiTeacher.TeacherAddEventActivity;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonListActivityNew;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonParameter;
import com.puti.education.ui.uiTeacher.chooseperson.event.ChooseCompleteEvent;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by icebery on 2017/4/15 0015.
 */

public class QuestionnaireDetailActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    //@BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.recycler_detail)
    RecyclerView mDetailRecyclerView;

    @BindView(R.id.courseName)
    TextView courseName;//评价课程
    @BindView(R.id.teacherName)
    TextView teacherName;//评价对象
    @BindView(R.id.question_object)
    LinearLayout VQuestionObject;//评价对象组

    private String mId = null;
    private Questionnaire mQtDetail;
    private QuestionnaireEtDetailAdapter mDetailAdapter;

    private View mItemView;
    private ArrayList<EventCourse> mCourseList;
    private ArrayList<String> mSchoolCourseList;

    private int mPosition;//位置
    @Override
    public int getLayoutResourceId() {
        return R.layout.qt_detail_layout;
    }

    @Override
    public void initVariables() {
        mId = getIntent().getStringExtra("id");

        if (TextUtils.isEmpty(mId)) {
            ToastUtil.show("ID为空");
            return;
        }

        mDetailAdapter = new QuestionnaireEtDetailAdapter(this);

//        ViewGroup.LayoutParams layoutParams = mDetailRecyclerView.getLayoutParams();
//        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        mDetailRecyclerView.setLayoutParams(layoutParams);

        mDetailRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mDetailRecyclerView.setLayoutManager(linearLayoutManager);
        mDetailRecyclerView.setAdapter(mDetailAdapter);
        mDetailAdapter.setDrowChooseListener(mPeopleChooseListener);
        mDetailAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                Question qt = mQtDetail.items.get(position);
                if (qt != null) {
                    //
                }
            }
        });

        mItemView = LayoutInflater.from(this).inflate(R.layout.item_qt_commit_view, null);
        mBtnCommit = (Button) mItemView.findViewById(R.id.btn_commit);
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitQt();
            }
        });
        mDetailAdapter.addFooterView(mItemView);
    }

    @Override
    public void initViews() {
        mUiTitle.setText("问卷详情");
    }

    @Override
    public void loadData() {
        getQtDetail(mId);
    }

    public void finishActivity(View v) {
        this.finish();
    }

    private void getQtDetail(String uid) {
        CommonModel.getInstance().getQuestionnaireDetail(uid, new BaseListener(Questionnaire.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (infoObj != null) {
                    mQtDetail = (Questionnaire) infoObj;
                    if (TextUtils.isEmpty(mQtDetail.desc)){
                        mTvContent.setVisibility(View.GONE);
                    }else {
                        mTvContent.setVisibility(View.VISIBLE);
                        mTvContent.setText(mQtDetail.desc);
                    }
                    mTvTitle.setText(mQtDetail.title);

                    //处理评价对象  0.无对象 1.教师和课程对象
                    if (mQtDetail.targetType == 1){
                        VQuestionObject.setVisibility(View.VISIBLE);
                        courseName.setText(TextUtils.isEmpty(mQtDetail.courseName) ? "" : mQtDetail.courseName);
                        teacherName.setText(TextUtils.isEmpty(mQtDetail.teacherName) ? "" : mQtDetail.teacherName);
                    }else {
                        VQuestionObject.setVisibility(View.GONE);
                    }

                    if (mQtDetail.items != null && mQtDetail.items.size() > 0) {
                        mDetailAdapter.setDataList(mQtDetail.items);
                    }
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("获取详情出错 " + (TextUtils.isEmpty(errorMessage) ? "" : errorMessage));
            }
        });
    }

    //@OnClick(R.id.btn_commit)
    public void commitQt() {
        if (mQtDetail == null) {
            ToastUtil.show("问卷详情为空不能提交");
            return;
        }
        for (int i = 0; i < mQtDetail.items.size(); i++) {
            Question question = mQtDetail.items.get(i);
            if (question.isRequired && !question.isAnswerd){
                ToastUtil.show("请回答完所有必答的问题");
                return;
            }
        }

        CommonModel.getInstance().questionnaireCommit(mId, mQtDetail.items, null, new BaseListener(ResponseBean.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (infoObj != null) {
                    ResponseBean respon = (ResponseBean) infoObj;
                    Intent intent = new Intent();
                    intent.putExtra("result", respon.result);
                    intent.setClass(QuestionnaireDetailActivity.this, QuestionnaireResult.class);
                    startActivity(intent);
                    finish();
                    EventBus.getDefault().post(new QuestionRefreshEvent());
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("提交问题失败 " + (TextUtils.isEmpty(errorMessage) ? "" : errorMessage));
            }
        });
    }

    private View.OnClickListener mPeopleChooseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag(R.id.position_key);
            mPosition = pos;
            Question qt = mQtDetail.items.get(pos);
            switch (qt.type) {
                case Constant.TYPE_TEACHER_UID:
                    chooseTeacher();
                    break;
                case Constant.TYPE_STUDENT_UID:
                    chooseStudent();
                    break;
                case Constant.TYPE_PARENT_UID:
                    chooseParent(pos);
                    break;
                case Constant.TYPE_COURSE_UID:
                    //getRecommandCourse(pos, v);
                    getSchoolCourse(pos, v);
                    break;
            }
        }
    };


    private void chooseTeacher() {
        Intent intent = new Intent();
        intent.putExtra(ChoosePersonParameter.REFER,ChoosePersonParameter.REFER_QS_TEA);
        intent.setClass(this, ChoosePersonListActivityNew.class);
       startActivity(intent);
    }

    private void chooseStudent() {
        Intent intent = new Intent();
        intent.putExtra(ChoosePersonParameter.REFER,ChoosePersonParameter.REFER_QS_STU);
        intent.setClass(this, ChoosePersonListActivityNew.class);
        startActivity(intent);
    }

    private void chooseParent(int position) {
        int role = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);
        if (role != Constant.ROLE_STUDENT) {
            ToastUtil.show("非学生角色, 不能选择家长");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Key.INDEX, position);
        intent.setClass(this, EventParentChooseActivity.class);
        startActivityForResult(intent, 3002);
    }

    //获取推荐课程
    private void getRecommandCourse(final int coursePosition, final View v) {
        //disLoading("获取推荐课程");
        TeacherModel.getInstance().recommandCourse(new BaseListener(EventCourse.class) {
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                if (listObj != null) {
                    mCourseList = (ArrayList<EventCourse>) listObj;
                    ArrayList<String> courseArr = new ArrayList<String>();
                    for (EventCourse one : mCourseList) {
                        courseArr.add(one.name);
                    }
                    chooseCourseType(coursePosition, (TextView) v, courseArr);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                //hideLoading();
                ToastUtil.show("获取推荐课程失败 " + (TextUtils.isEmpty(errorMessage) ? "" : errorMessage));
            }
        });
    }

    //获取学校课程
    private void getSchoolCourse(final int coursePosition, final View v) {
        //disLoading("获取推荐课程");
        String schoolUid = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_ID, "");
        if (TextUtils.isEmpty(schoolUid)) {
            ToastUtil.show("学校ID为空");
            return;
        }
        CommonModel.getInstance().getCourseList(schoolUid, new BaseListener(String.class) {
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                if (listObj != null) {
                    mSchoolCourseList = (ArrayList<String>) listObj;
                    chooseCourseType(coursePosition, (TextView) v, mSchoolCourseList);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                //hideLoading();
                ToastUtil.show("获取推荐课程失败 " + (TextUtils.isEmpty(errorMessage) ? "" : errorMessage));
            }
        });
    }


    private void chooseCourseType(final int coursePosition, final TextView dropView, ArrayList<String> values) {

        if (values != null && values.size() > 0) {
            final CommonDropView keyvalueDropView = new CommonDropView(this, dropView, values);
            keyvalueDropView.showAsDropDown(dropView);
            keyvalueDropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    TextView mTextView = (TextView) dropView;
                    mTextView.setText(mSchoolCourseList.get(position));
                    // String courseUid = mCourseList.get(position).uid;
                    String name = mSchoolCourseList.get(position);
                    if (position >= 0) {
                        mQtDetail.items.get(coursePosition).answerd = name;
                        mQtDetail.items.get(coursePosition).answerd2 = name;
                        mDetailAdapter.update(coursePosition);

                    }
                    keyvalueDropView.dismiss();
                }
            });
        } else {
            ToastUtil.show("没有数据");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (intent == null) {
            return;
        }
        switch (resultCode) {

            //曹 这里引用是什么意思
            case TeacherAddEventActivity.CODE_RESULT_ADD_EVENT_CHOOSE_STU: {
                ArrayList<EventAboutPeople> people = (ArrayList<EventAboutPeople>) intent.getSerializableExtra(Key.BEAN);
                int position = intent.getIntExtra(Key.INDEX, -1);
                if (position >= 0 && people != null && people.size() > 0) {
                    mQtDetail.items.get(position).answerd = people.get(0).uid;
                    mQtDetail.items.get(position).answerd2 = people.get(0).name;
                    mDetailAdapter.update(position);
                }
            }
            break;
            case TeacherAddEventActivity.CODE_RESULT_ADD_EVENT_CHOOSE_KNOW_EVENT_PEOPLE: {
                ArrayList<EventAboutPeople> people = (ArrayList<EventAboutPeople>) intent.getSerializableExtra(Key.BEAN);
                int position = intent.getIntExtra(Key.INDEX, -1);
                if (position >= 0 && people != null && people.size() > 0) {
                    mQtDetail.items.get(position).answerd = people.get(0).uid;
                    mQtDetail.items.get(position).answerd2 = people.get(0).name;
                    mDetailAdapter.update(position);
                }
            }
            break;
            case Constant.ROLE_PARENTS: {
                ParentFromStudent people = (ParentFromStudent) intent.getSerializableExtra(Key.BEAN);
                int position = intent.getIntExtra(Key.INDEX, -1);
                if (position >= 0 && people != null) {
                    mQtDetail.items.get(position).answerd = people.uid;
                    mQtDetail.items.get(position).answerd2 = people.name;
                    mDetailAdapter.update(position);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    //接收选择学生或者选择老师后的事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(ChooseCompleteEvent event){
        if (event != null){
            ArrayList<EventAboutPeople> peoples = event.getmList();
            if (mPosition >= 0 && peoples != null && peoples.size() > 0) {
                mQtDetail.items.get(mPosition).answerd = peoples.get(0).uid;
                mQtDetail.items.get(mPosition).answerd2 = peoples.get(0).name;
                mDetailAdapter.update(mPosition);
            }
        }
    }
}
