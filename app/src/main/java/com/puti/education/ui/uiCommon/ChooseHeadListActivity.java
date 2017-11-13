package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.StudentListAdapter;
import com.puti.education.adapter.TeacherListAdapter;
import com.puti.education.bean.ClassInfo;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.Student;
import com.puti.education.bean.Teacher;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiTeacher.CommitAuditActivity;
import com.puti.education.ui.uiTeacher.TeacherAddEventActivity;
import com.puti.education.util.CharacterParser;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DividerDecoration;
import com.puti.education.util.Key;
import com.puti.education.util.StudentPinyinComparator;
import com.puti.education.util.TeacherPinyinComparator;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;
import com.puti.education.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/20 0020.
 *
 * 班主任选择
 *
 */

public class ChooseHeadListActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.person_rv)
    RecyclerView mRv;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar mQuickIndexBar;

    private List<Teacher> mTeacherList;

    private final static int STUDENT = 1;
    private final static int TEACHER = 2;
    private final static int ALL = 3;

    private TeacherListAdapter mTeacherListAdapter;
    private LinearLayoutManager manager;

    private CommonDropView mCommonDropView;

    private int mSourceWindow;//来自哪个Activity
    private int mSchoolId = 1;//学校id

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_choose_head_teacher;
    }

    @Override
    public void initVariables() {

        Intent intent = getIntent();
        mSourceWindow = intent.getIntExtra(Key.FROM_SOURCE_WINDOW, -1);

        mTeacherList = new ArrayList<>();
        mTeacherListAdapter = new TeacherListAdapter(ChooseHeadListActivity.this);
    }

    @Override
    public void initViews() {
        mTitleTv.setText("选择");

        mRv.setHasFixedSize(true);
        manager = new LinearLayoutManager(ChooseHeadListActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        //条目间的间隔线
        DividerDecoration divider = new DividerDecoration.Builder(ChooseHeadListActivity.this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.line_bg)
                .build();
        mRv.addItemDecoration(divider);

        //字母滑动回调
        mQuickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                    if (mTeacherList == null || mTeacherList.size() == 0){
                        return;
                    }
                    for (int i = 0; i < mTeacherList.size(); i++) {

                        if (letter.equalsIgnoreCase(mTeacherList.get(i).pinyin.charAt(0) + "")) {

                            int position = mTeacherListAdapter.getPositionForSection(mTeacherList.get(i).pinyin.charAt(0));
                            if (position != -1) {
                                //滑动到指定位置
                                manager.scrollToPositionWithOffset(position, 0);
                            }
                            break;
                        }
                    }
            }

            @Override
            public void onReset() {

            }
        });

        mTeacherListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                Teacher teacher = mTeacherList.get(position);
                EventAboutPeople eventAboutPeople = new EventAboutPeople(teacher.avatar, teacher.name, teacher.uid, 1);
                Intent intent = new Intent();
                intent.putExtra(Key.BEAN,eventAboutPeople);
                setResult(CommitAuditActivity.RESULT_ADD_CLASS_LEADER,intent);
                finish();
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadData() {
        int schoolId = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_ID, -1);
        mSchoolId = schoolId;
        if (mSchoolId > 0){
            getHeadTeacherList(mSchoolId);
        }

    }


    /**
     * 教师列表
     * @param schoolId
     */
    private void getHeadTeacherList(int schoolId){

        CommonModel.getInstance().getHeadTeacherList(schoolId,new BaseListener(Teacher.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {

                mTeacherList = (List<Teacher>)listObj;

                if (mTeacherList != null && mTeacherList.size() > 0){
                    CharacterParser characterParser = new CharacterParser();
                    for (Teacher tcher:mTeacherList) {
                        tcher.pinyin = characterParser.getSelling(tcher.name);
                        if (TextUtils.isEmpty(tcher.pinyin)) {
                            tcher.pinyin = "#";
                        } else if (Constant.ALPHABETS.indexOf(tcher.pinyin.charAt(0)) == -1){
                            tcher.pinyin = "#";
                        }
                    }
                    TeacherPinyinComparator pinyinComparator = new TeacherPinyinComparator();
                    Collections.sort(mTeacherList, pinyinComparator);
                    mRv.setAdapter(mTeacherListAdapter);
                    mTeacherListAdapter.setDataList(mTeacherList);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }



    @OnClick(R.id.back_frame)
    public void finishActivityClick(){
        finish();
    }
}
