package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.puti.education.App;
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
import com.puti.education.util.CharacterParser;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DividerDecoration;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.StudentPinyinComparator;
import com.puti.education.util.TeacherPinyinComparator;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;
import com.puti.education.widget.QuickIndexBar;
import com.puti.education.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/4/20 0020.
 *
 * 教师端  事件添加 学生老师,知情人,家长孩子选择
 *
 */

public class ChoosePersonListActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_right)
    TextView mTvCommit;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.stu_choose_btn)
    RadioButton mStuChooseBtn;
    @BindView(R.id.teacher_choose_btn)
    RadioButton mTeacherChooseBtn;
    @BindView(R.id.choose_btn_linear)
    LinearLayout mClassLinear;
    @BindView(R.id.class_choose_btn)
    Button mClassChooseBtn;
    @BindView(R.id.person_rv)
    RecyclerView mRv;
    @BindView(R.id.quick_indexbar)
    QuickIndexBar mQuickIndexBar;
    @BindView(R.id.et_input_name)
    EditText mNameInput;

    private List<Student> mStudentList;
    private List<Teacher> mTeacherList;

    private final static int STUDENT = 1;
    private final static int TEACHER = 2;
    private final static int ALL = 3;
    private int mListType = STUDENT;

    private StudentListAdapter mStudentAdapter;
    private TeacherListAdapter mTeacherListAdapter;
    private LinearLayoutManager manager;

    private List<ClassInfo> mClassInfoList;
    private List<String> mClassNameList;//班级名字集合
    private CommonDropView mCommonDropView;

    private int mSourceWindow;//来自哪个Activity
    private String mSchoolId;//学校id
    private String mDutyType; //主要责任人，次要责任人，证人，知情人，举报人，
    private String mClassId;//班级id
    private int mTempIndex = -1;
    private boolean mbAbnormal = false;  //是否异常事件，默认为普通事件

    private boolean isToChooseChild;//是否家长详情选择孩子
    private int mChoosedNum = 0;   //已选择多少人
    private int mToChoosedNum = 0; //一次最多选择多少人
    private ArrayList<EventAboutPeople> mChoosedPeople = new ArrayList<EventAboutPeople>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_choose_event_person;
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();

            ArrayList<EventAboutPeople> studentPeoples = getSelectStudent();
            if (studentPeoples != null&& studentPeoples.size() > 0){
                mChoosedPeople.addAll(studentPeoples);
            }

            ArrayList<EventAboutPeople> teacherPeoples = getSelectTeacher();
            if (teacherPeoples != null && teacherPeoples.size() > 0){
                mChoosedPeople.addAll(teacherPeoples);
            }

            if (mChoosedPeople == null || mChoosedPeople.size() <= 0){
                ToastUtil.show("请选择人物");
                return;
            }

            if (!isToChooseChild){
                intent.putExtra(Key.BEAN,mChoosedPeople);
                if (mListType ==ALL){
                    setResult(TeacherAddEventActivity.CODE_RESULT_ADD_EVENT_CHOOSE_KNOW_EVENT_PEOPLE,intent);
                }else{
                    intent.putExtra(Key.INDEX, mTempIndex);
                    setResult(TeacherAddEventActivity.CODE_RESULT_ADD_EVENT_CHOOSE_STU,intent);
                }

                //当从新增事件页面跳转过来时，需要返回责任和人
                if (!TextUtils.isEmpty(mDutyType)){
                    Intent refreshAddEventIntent = new Intent(Constant.BROADCAST_ADD_INVOLVER);
                    refreshAddEventIntent.putExtra(Key.DUTY_TYPE,mDutyType);
                    refreshAddEventIntent.putExtra(Key.DUTY_PEOPLE,mChoosedPeople);
                    sendBroadcast(refreshAddEventIntent);
                }

            }else{

                intent.putExtra(Key.BEAN,mChoosedPeople);
                intent.putExtra(Key.CHILD_CLASS_NAME,"");
                intent.putExtra(Key.INDEX, mTempIndex);
                setResult(TeacherAddEventActivity.CODE_RESULT_ADD_EVENT_CHOOSE_STU,intent);
            }
            finish();
        }
    };

    @Override
    public void initVariables() {

        Intent intent = getIntent();
        mSourceWindow = intent.getIntExtra(Key.FROM_SOURCE_WINDOW,-1);
        mbAbnormal= intent.getBooleanExtra(Key.EVENT_ABNORMOL, false);

        mSchoolId = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_ID, "");

        mToChoosedNum = intent.getIntExtra(Key.NUMBER_TO_NEED, 1);
        mChoosedNum = intent.getIntExtra(Key.NUMBER_IS_CHOOSED, 0);

        //
        if (intent.hasExtra(Key.INDEX)){
            mTempIndex = intent.getIntExtra(Key.INDEX, -1);
        }

        //判断是否从责任等级处转入
        if (intent.hasExtra(Key.DUTY_TYPE)){
            mDutyType = intent.getStringExtra(Key.DUTY_TYPE);
        }

        //学生教师都可以选
        if (intent.hasExtra(Key.CHOOSE_BOTH)){
            mListType = ALL;
            mStuChooseBtn.setChecked(true);
        }
        //只选学生
        else if (intent.hasExtra(Key.CHOOSE_STUDENT)){
            if (intent.hasExtra(Key.TO_CHOOSE_CHILD)){
                isToChooseChild = true;
            }
            mListType = STUDENT;
            mStuChooseBtn.setChecked(true);
            mTeacherChooseBtn.setEnabled(false);
        }
        //只选老师
        else if (intent.hasExtra(Key.CHOOSE_TEACHER)){
            mListType = TEACHER;
            mStuChooseBtn.setChecked(false);
            mStuChooseBtn.setEnabled(false);
            mTeacherChooseBtn.setEnabled(true);

            mClassLinear.setVisibility(View.GONE);
        }

        mTvCommit.setOnClickListener(mOnClickListener);

        mClassNameList = new ArrayList<>();
        mStudentList = new ArrayList<>();
        mTeacherList = new ArrayList<>();
        mStudentAdapter = new StudentListAdapter(ChoosePersonListActivity.this);
        mTeacherListAdapter = new TeacherListAdapter(ChoosePersonListActivity.this);
    }

    private ArrayList<EventAboutPeople> getSelectStudent(){
        if (mStudentList == null || mStudentList.size() <= 0){
            return null;
        }

        EventAboutPeople people = null;
        ArrayList<EventAboutPeople> peoplelist = new ArrayList<EventAboutPeople>();
        for (Student one: mStudentList){
            if (one.isSelected){
                people = new EventAboutPeople(one.avatar, one.name, one.uid, 2);
                peoplelist.add(people);
            }
        }
        return peoplelist;
    }

    private ArrayList<EventAboutPeople> getSelectTeacher(){
        if (mTeacherList == null || mTeacherList.size() <= 0){
            return null;
        }

        EventAboutPeople people = null;
        ArrayList<EventAboutPeople> peoplelist = new ArrayList<EventAboutPeople>();
        for (Teacher one: mTeacherList){
            if (one.isSelected){
                people = new EventAboutPeople(one.avatar, one.name, one.uid, 1);
                peoplelist.add(people);
            }
        }
        return peoplelist;
    }


    @Override
    public void initViews() {
        mTitleTv.setText("选择");
        mTvCommit.setVisibility(View.VISIBLE);
        mNameInput.clearFocus();

        mRv.setHasFixedSize(true);
        manager = new LinearLayoutManager(ChoosePersonListActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        //条目间的间隔线
        DividerDecoration divider = new DividerDecoration.Builder(ChoosePersonListActivity.this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.line_bg)
                .build();
        mRv.addItemDecoration(divider);

        //字母滑动回调
        mQuickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                if (mStudentList != null && mStudentList.size() > 0) {

                    if (mStudentList == null || mStudentList.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < mStudentList.size(); i++) {

                        if (letter.equalsIgnoreCase(mStudentList.get(i).pinyin.charAt(0) + "")) {

                            int position = mStudentAdapter.getPositionForSection(mStudentList.get(i).pinyin.charAt(0));
                            if (position != -1) {
                                //滑动到指定位置
                                manager.scrollToPositionWithOffset(position, 0);
                            }
                            break;
                        }
                    }
                }

                else{

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

            }

            @Override
            public void onReset() {

            }
        });

        mStudentAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                updateStudentStatus(position);
            }
        });

        mTeacherListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                updateTeacherStatus(position);

            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    //老师
                    case R.id.teacher_choose_btn:
                    {
                        if (mStudentList != null){
                            //mStudentList.clear();
                        }
                        mClassLinear.setVisibility(View.GONE);
                        if (mListType == TEACHER){
                            mStuChooseBtn.setEnabled(false);
                        }
                        getTeacherList(mSchoolId, "");
                    }
                    break;
                    //学生
                    case R.id.stu_choose_btn:
                    {
                        if (mTeacherList != null){
                            //mTeacherList.clear();
                        }
                        mClassLinear.setVisibility(View.VISIBLE);
                        if (mListType == STUDENT){
                            mTeacherChooseBtn.setEnabled(false);
                        }
                        getClassList(mSchoolId);
                    }
                    break;
                    case R.id.class_choose_btn:
                    {

                    }
                    break;
                }

            }
        });

        mNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ChoosePersonListActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String searchText = arg0.getText().toString();
                    if (!TextUtils.isEmpty(searchText)){
                        getStudentList(null,searchText);
                    }else{
                        ToastUtil.show("请输入学生姓名");
                    }
                }
                return false;
            }

        });

    }

    private void updateStudentStatus(int position){
        mChoosedNum = 0;

        for (Student one: mStudentList){
            if (one.isSelected){
                mChoosedNum++;
            }
        }

        for (Teacher one: mTeacherList){
            if (one.isSelected){
                mChoosedNum++;
            }
        }

        Student student = mStudentList.get(position);
        if (student != null && student.isSelected){
            if (mChoosedNum > 0){
                mChoosedNum--;
            }
            student.isSelected = false;
        }else{
            if (mChoosedNum < mToChoosedNum){
                mChoosedNum++;
                student.isSelected = true;
            }else{
                ToastUtil.show("一次最多只能选择" + mToChoosedNum + "人");
            }
        }
        mStudentAdapter.notifyItemChanged(position);
        mTvCommit.setText("完成(" + mChoosedNum  + ")");
    }

    private void updateTeacherStatus(int position){
        mChoosedNum = 0;

        for (Student one: mStudentList){
            if (one.isSelected){
                mChoosedNum++;
            }
        }

        for (Teacher one: mTeacherList){
            if (one.isSelected){
                mChoosedNum++;
            }
        }

        Teacher teacher = mTeacherList.get(position);
        if (teacher != null && teacher.isSelected){
            if (mChoosedNum > 0){
                mChoosedNum--;
            }
            teacher.isSelected = false;
        }else{
            if (mChoosedNum < mToChoosedNum){
                mChoosedNum++;
                teacher.isSelected = true;
            }else{
                ToastUtil.show("一次最多只能选择" + mToChoosedNum + "人");
            }
        }
        mTeacherListAdapter.notifyItemChanged(position);
        mTvCommit.setText("完成(" + mChoosedNum  + ")");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //班级选择
    @OnClick(R.id.class_choose_btn)
    public void classChooseClick(){

        if (mClassInfoList != null){
            if (mCommonDropView == null){
                mCommonDropView = new CommonDropView(ChoosePersonListActivity.this,mClassChooseBtn,mClassNameList);
                mCommonDropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ClassInfo classInfo = mClassInfoList.get(position);
                        mClassId = classInfo.uid;

                        ConfigUtil.getInstance(ChoosePersonListActivity.this).put("classid", classInfo.uid);
                        ConfigUtil.getInstance(ChoosePersonListActivity.this).put("classname", classInfo.name);
                        ConfigUtil.getInstance(ChoosePersonListActivity.this).commit();

                        mClassChooseBtn.setText(classInfo.name);
                        getStudentList(classInfo.uid,null);
                        mCommonDropView.dismiss();
                    }
                });
            }
            mCommonDropView.showAsDropDown(mClassChooseBtn);

        }else{
            ToastUtil.show("没有班级信息");
        }
    }

    @Override
    public void loadData() {
        if (mListType == TEACHER){
            getTeacherList(mSchoolId, "");
        }else{
            getClassList(mSchoolId);
        }

    }



    /**
     * 班级
     * @param schoolId
     */
    private void getClassList(String schoolId){

        CommonModel.getInstance().getClassList(schoolId,new BaseListener(ClassInfo.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {

                List<ClassInfo> allClasses = (List<ClassInfo>) listObj;
                mClassNameList.clear();

                //如果是异常事件，则教师去选择学生,则只能选择所任课的学生; 如果是学生处教师，则可以选择所有班级
                //如果是普通事件，则都可以选择
                boolean isAffairs = ConfigUtil.getInstance(ChoosePersonListActivity.this).get(Constant.KEY_IS_STUDENT_AFFAIRS, false);
                if (mbAbnormal || isAffairs){
                    mClassInfoList = allClasses;
                }else{
                    if (!TextUtils.isEmpty(mDutyType)){
                        setLeadClasses(allClasses);
                    }else{
                        mClassInfoList = allClasses;
                    }
                }

                String lastClassUid = ConfigUtil.getInstance(ChoosePersonListActivity.this).get("classid", "");
                String lastClassName= ConfigUtil.getInstance(ChoosePersonListActivity.this).get("classname", "");
                boolean haveLastClass = false;

                if (mClassInfoList != null && mClassInfoList.size() > 0){
                    for (int i = 0 ;i< mClassInfoList.size();i++){
                        mClassNameList.add(mClassInfoList.get(i).name);
                        if (!TextUtils.isEmpty(lastClassUid) && mClassInfoList.get(i).uid.equals(lastClassUid)){
                            haveLastClass = true;
                        }
                    }

                    if (haveLastClass){
                        mClassChooseBtn.setText(lastClassName);
                        getStudentList(lastClassUid,null);
                    }else{
                        getStudentList(mClassInfoList.get(0).uid,null);
                    }

                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }

        });

    }

    //如果是教师,则只显示所任课的班级
    private void setLeadClasses(List<ClassInfo> allClass){
        if (allClass == null || allClass.size() <= 0){
            return;
        }

        List<ClassInfo> tempClass = null;
        App mApp = (App)ChoosePersonListActivity.this.getApplication();
        if (mApp != null){
            tempClass = mApp.getClassList();
            if (tempClass == null || tempClass.size() <= 0){
                return;
            }

            mClassInfoList = new ArrayList<ClassInfo>();
            for (ClassInfo tempOne: tempClass){
                for (ClassInfo allOne: allClass){
                    if (tempOne.uid.equals(allOne.uid)){
                        mClassInfoList.add(allOne);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 学生列表
     * @param classId
     */
    private void getStudentList(String classId,String keyword){

        disLoading();

        CommonModel.getInstance().getStudentList(classId,keyword,new BaseListener(Student.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                mStudentList = (List<Student>) listObj;
                if (mStudentList != null && mStudentList.size() > 0){
                    int size = mStudentList.size();
                    boolean haveChoose = false;


                    CharacterParser characterParser = new CharacterParser();
                    for (int i = 0; i<size;i++){
                        Student student = mStudentList.get(i);
                        student.pinyin = characterParser.getSelling(student.name);
                        if (TextUtils.isEmpty(student.pinyin)) {
                            student.pinyin = "#";
                        } else if (Constant.ALPHABETS.indexOf(student.pinyin.charAt(0)) == -1){
                            student.pinyin = "#";
                        }

                    }
                    StudentPinyinComparator pinyinComparator = new StudentPinyinComparator();
                    Collections.sort(mStudentList, pinyinComparator);
                    mRv.setAdapter(mStudentAdapter);
                    mStudentAdapter.setDataList(mStudentList);
                }else{
                    mRv.setAdapter(mStudentAdapter);
                    mStudentAdapter.setDataList(new ArrayList<Student>(1));
                    ToastUtil.show("没有数据");
                }

                hideLoading();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    /**
     * 教师列表
     * @param schoolId
     */
    private void getTeacherList(String schoolId,String keyword){
        disLoading();
        CommonModel.getInstance().getTeacherList(schoolId, keyword, new BaseListener(Teacher.class){

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
                }else{
                    mRv.setAdapter(mTeacherListAdapter);
                    mTeacherListAdapter.setDataList(new ArrayList<Teacher>(1));
                }
                hideLoading();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }



    @OnClick(R.id.back_frame)
    public void finishActivityClick(){
        finish();
    }


}
