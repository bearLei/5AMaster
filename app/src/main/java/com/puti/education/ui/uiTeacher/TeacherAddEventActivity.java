package com.puti.education.ui.uiTeacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.App;
import com.puti.education.R;
import com.puti.education.adapter.EventAboutPeopleAdapter;
import com.puti.education.bean.AddEventResponseBean;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventAddress;
import com.puti.education.bean.EventType;
import com.puti.education.bean.LocalFile;
import com.puti.education.bean.StudentDetailBean;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.netFrame.netModel.UploadModel;
import com.puti.education.speech.SpeechUtil;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.EventDutyChooseActivity;
import com.puti.education.ui.uiCommon.VideoRecordActivity;
import com.puti.education.ui.uiStudent.StudentReportAddActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.TimeUtils;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.DropWithBackView;
import com.puti.education.widget.EventAddressListDialog;
import com.puti.education.widget.GridViewForScrollView;
import com.puti.education.widget.RatingBarView;
import com.puti.education.widget.RatingSmallBarView;
import com.puti.education.widget.TimeDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/4/19 0019.
 *
 * 教师端  事件新增
 */

public class TeacherAddEventActivity extends BaseActivity{

    public final static int CODE_RESULT_ADD_EVENT_CHOOSE_STU = 555;
    public final static int CODE_RESULT_ADD_EVENT_CHOOSE_KNOW_EVENT_PEOPLE = 666;

    public final static int CODE_RESULT_TO_ADD_QUESTION = 888;

    public static final int CODE_ZXING = 999;//二维码扫描

    private Handler mHandler = new Handler();

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.event_time_tv)
    TextView mChooseTimeTv;
    @BindView(R.id.event_source_address_edit)
    EditText mInputAddressEdit;
    @BindView(R.id.involved_people_grid)
    GridViewForScrollView mInvolvedPeopleGv;
    @BindView(R.id.event_choose_eventtype_tv)
    TextView mEventTypeChooseTv;//事件类型
    @BindView(R.id.event_des_tv)
    EditText mDesEditText;
    @BindView(R.id.rbv_event_level)
    RatingSmallBarView mRbvEventLevel;
    @BindView(R.id.layout_push_office)
    LinearLayout mLayoutPush;
    @BindView(R.id.rg_push_office)
    RadioGroup mPushRaidoGroup;
    @BindView(R.id.layout_record)
    LinearLayout mLayoutRecord;

    @BindView(R.id.text_pic_record_linear)
    LinearLayout mTextPicLinear;
    @BindView(R.id.media_record_linear)
    LinearLayout mediaRecordLinear;
    @BindView(R.id.video_record_linear)
    LinearLayout mVideoRecordLinear;
    @BindView(R.id.right_frame)
    FrameLayout mRightFrame;//二维码扫描


    private String mLat, mLng;
    private int mEventLevel = 0;
    private int mEventTyPeId = -1;
    private String mDutyType;
    private boolean mIsPushOffice = false; //是否推送学生处

    private EventAboutPeopleAdapter mInvolvePeopleAdapter = null;//涉事人
    private ArrayList<EventAboutPeople> mInvolvePeopleList = null;//知情人列表

    private EventAboutPeopleAdapter mknowEventAdapter = null;//知情人
    private List<EventAboutPeople> mknowEventPeopleList = null;

    private List<EventType> mEventTypeList = null;
    private List<String> mEventTypeNameList = null;
    private DropWithBackView mEventTypeListPop;

    private String mEventTypeName;
    private boolean mbAbnormal = false;
    private EventAboutPeople mAddSign;

    private String mImageTextStr;

    private ArrayList<String> mImagePaths;
    private ArrayList<LocalFile> mAudioLocalFileList;
    private String mVideoPaths;

    private ArrayList<UploadFileBean> mUploadedImages;
    private ArrayList<UploadFileBean> mUploadAudios;
    private ArrayList<UploadFileBean> mUploadVideos;


    private EventAddressListDialog mEventAddressListDialog = null;
    private EventAddReceiver mEventReceiver = null;



    public class EventAddReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d("EventAddReceiver"," onReceive ");
            if(action.equals(Constant.BROADCAST_ADD_INVOLVER)) {
                mDutyType = intent.getStringExtra(Key.DUTY_TYPE);
                //选择完传递回来的学生列表
                ArrayList<EventAboutPeople> involvePeoples = (ArrayList<EventAboutPeople>)intent.getSerializableExtra(Key.DUTY_PEOPLE);
                if  (involvePeoples != null && involvePeoples.size() > 0){
                   opraterList(involvePeoples,false);
                }
            }
        }
    }
    //处理选择完学生后的列表组合，过滤下相同的  mmp的 一个列表的身份类型 你保存个成员变量？？？？ 待优化
    private void opraterList(ArrayList<EventAboutPeople> involvePeoples,boolean resetType){
        for (int i = 0; i < mInvolvePeopleList.size(); i++) {
            EventAboutPeople people = mInvolvePeopleList.get(i);
            if (!people.isPeople){
                mInvolvePeopleList.remove(i);
                break;
            }
        }
        ArrayList<EventAboutPeople> tempList = involvePeoples;
        for (int i = 0; i < tempList.size(); i++) {
            if (mInvolvePeopleList.size() > i){
                for (int j = 0; j < mInvolvePeopleList.size(); j++) {
                    EventAboutPeople eventAboutPeople = mInvolvePeopleList.get(j);
                        if (tempList.size() > i && tempList.get(i).uid.equals(eventAboutPeople.uid)) {
                            tempList.remove(i);
                    }
                }
            }
        }
        mInvolvePeopleList.addAll(tempList);
        if (!resetType){
        setDutyTypeExt(involvePeoples, mDutyType);
        }
        mInvolvePeopleList.add(mAddSign);
        mInvolvePeopleAdapter.notifyDataSetChanged();
    }
    public void setReceiver()
    {
        mEventReceiver = new EventAddReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BROADCAST_ADD_INVOLVER);
        this.registerReceiver(mEventReceiver, intentFilter);
    }

    public void cancelReceiver()
    {
        if (mEventReceiver != null)
        {
            LogUtil.d("", "cancelReceiver");
            this.unregisterReceiver(mEventReceiver);
            mEventReceiver = null;
        }
    }

    private void setDutyTypeExt(ArrayList<EventAboutPeople> peoples, String dutytype){
        if (peoples != null && peoples.size() > 0){
            for (EventAboutPeople one: peoples){
                setDutyType(one, dutytype);
            }
        }

    }

    private void setDutyType(EventAboutPeople people, String dutytype){
        if (!people.isPeople) return;
        people.dutyType = dutytype;
        if (dutytype.equals(Constant.EVENT_DUTY_MAJOR)) {
            people.involveType = "主要责任人";
        }else if (dutytype.equals(Constant.EVENT_DUTY_MINOR)) {
            people.involveType = "次要责任人";
        }else if (dutytype.equals(Constant.EVENT_DUTY_WITNESS)){
            people.involveType = "证人";
        }else if (dutytype.equals(Constant.EVENT_DUTY_KNOWN)){
            people.involveType = "知情者";
        }else if (dutytype.equals(Constant.EVENT_DUTY_REPORT)) {
            people.involveType = "举报者";
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_teacher_event_add;
    }


    @Override
    public void initVariables() {

        // TODO: 2017/11/17 初始化一堆列表，获取上层携带的参数  这里考虑下传递的二维码扫描结果
        mAddSign = new EventAboutPeople(false);
        mInvolvePeopleList = new ArrayList<>();

        mknowEventPeopleList = new ArrayList<>();
        mknowEventPeopleList.add(mAddSign);

        mEventTypeList = new ArrayList<>();
        mEventTypeNameList = new ArrayList<>();

        mUploadedImages = new ArrayList<>();
        mImagePaths = new ArrayList<>();

        mUploadAudios = new ArrayList<>();
        mAudioLocalFileList = new ArrayList<>();

        mUploadVideos = new ArrayList<>();

        mbAbnormal = this.getIntent().getBooleanExtra("isabnormal", false);
        mEventTyPeId = this.getIntent().getIntExtra("eventtypeid", -1);
        mEventTypeName = this.getIntent().getStringExtra("eventtypename");
        mEventTypeChooseTv.setText(mEventTypeName);
        getIntent().getSerializableExtra(AddEventZxingActivity.ZXING_LIST);
        if (getIntent().getSerializableExtra(AddEventZxingActivity.ZXING_LIST) != null) {
            mInvolvePeopleList.addAll((ArrayList<EventAboutPeople>) getIntent().getSerializableExtra(AddEventZxingActivity.ZXING_LIST));
        }

        mInvolvePeopleList.add(mAddSign);
    }

    @Override
    public void initViews() {
        mTitleTv.setText("新增事件");
        //涉事人
        mRightFrame.setVisibility(View.VISIBLE);
        mInvolvePeopleAdapter = new EventAboutPeopleAdapter(this);
        mInvolvedPeopleGv.setAdapter(mInvolvePeopleAdapter);
        mInvolvePeopleAdapter.setmList(mInvolvePeopleList);
//        mInvolvePeopleAdapter.notifyDataSetChanged();
        mInvolvedPeopleGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventAboutPeople people = mInvolvePeopleList.get(position);
                if (!people.isPeople){
                    Intent intent = new Intent();
                    if (mbAbnormal){
                        //当为异常事件的时候, 需跳选择责任类型界面
                        intent.putExtra("type", 0);
                        intent.putExtra(Key.EVENT_ABNORMOL, mbAbnormal);
                        intent.setClass(TeacherAddEventActivity.this,EventDutyChooseActivity.class);
                        startActivity(intent);
                    }else{
                        //当为普通事件的时候, 不选择责任类型, 因为都默认是主要事件人, 故直接跳到人物选择界面
                        intent.putExtra("type", 1);
                        intent.putExtra(Key.EVENT_ABNORMOL, mbAbnormal);
                        intent.putExtra(Key.DUTY_TYPE, Constant.EVENT_DUTY_MAJOR);
                        intent.putExtra(Key.CHOOSE_STUDENT,1);
                        intent.putExtra(Key.NUMBER_TO_NEED, Constant.CHOOSE_PEOPLE_MAX);
                        intent.putExtra(Key.NUMBER_IS_CHOOSED, 0);
                        intent.putExtra(Key.BEAN, (Serializable) mInvolvePeopleList);
                        intent.setClass(TeacherAddEventActivity.this, ChoosePersonListActivity.class);
                        startActivity(intent);
                    }

                }else{
                    mInvolvePeopleList.remove(position);
                    mInvolvePeopleAdapter.notifyDataSetChanged();
                }
            }
        });

        if (!mbAbnormal){
            mRbvEventLevel.setStarCount(2);
            //mRbvEventLevel.postInvalidate();
            mRbvEventLevel.requestLayout();
            mRbvEventLevel.postInvalidate();
        }
        mRbvEventLevel.setOnRatingListener(new RatingSmallBarView.OnRatingListener() {
            @Override
            public void onRating(Object BindViewObject, int RatingScore) {
                mEventLevel = RatingScore;
            }
        });

        //是否发送至学生处
        if (!mbAbnormal){
            mLayoutPush.setVisibility(View.GONE);
        }else{
            mPushRaidoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.push_yes_radio){
                        mIsPushOffice = true;

                    }else{
                        mIsPushOffice = false;
                    }
                }
            });
            mPushRaidoGroup.check(R.id.push_no_radio);
        }



        setReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (intent == null){
            return;
        }

        switch (resultCode){

            case CODE_RESULT_ADD_EVENT_CHOOSE_STU:
            {
                EventAboutPeople people = (EventAboutPeople)intent.getSerializableExtra(Key.BEAN);
                if (isHasSameEelement(mInvolvePeopleList,people)){
                    return;
                }
                mInvolvePeopleList.add(mInvolvePeopleList.size()-1,people);
                mInvolvePeopleAdapter.notifyDataSetChanged();
                getStudentDetail(people.uid);
            }
                break;
            case CODE_RESULT_ADD_EVENT_CHOOSE_KNOW_EVENT_PEOPLE:
            {
                EventAboutPeople people = (EventAboutPeople)intent.getSerializableExtra(Key.BEAN);
                if (isHasSameEelement(mknowEventPeopleList,people)){
                    return;
                }
                mknowEventPeopleList.add(mknowEventPeopleList.size()-1,people);
                mknowEventAdapter.notifyDataSetChanged();
            }
                break;
            case Constant.CODE_RESULT_VIDEO:
            {
                mVideoPaths = intent.getStringExtra(Key.RECORD_VIDEO);
            }
                break;
            case Constant.CODE_RESULT_IMG_TEXT:
            {
                mImagePaths.clear();
                mImageTextStr = intent.getStringExtra(Key.RECORD_IMG_TEXT);
                List<String> tempImgList = intent.getStringArrayListExtra(Key.BEAN);
                mImagePaths.addAll(tempImgList);
                LogUtil.i("imgepath","" + mImagePaths.toString());
            }
                break;
            case Constant.CODE_RESULT_MEDIA:
            {
                mAudioLocalFileList.clear();
                List<LocalFile> temImgList =intent.getParcelableArrayListExtra(Key.BEAN);
                mAudioLocalFileList.addAll(temImgList);
            }
                break;

            //二维码
            case CODE_ZXING:
               if (intent.getSerializableExtra(AddEventZxingActivity.ZXING_LIST) != null
                       && mInvolvePeopleList != null){
                   ArrayList<EventAboutPeople> list = (ArrayList<EventAboutPeople>) intent.getSerializableExtra(AddEventZxingActivity.ZXING_LIST);
                   if (list != null && list.size() > 0) {
                       for (int i = 0; i < list.size(); i++) {
//                           mDutyType = "1";
                          opraterList(list,true);
                       }
                   }
               }
                break;
        }


        super.onActivityResult(requestCode, resultCode, intent);
    }

    private boolean isHasSameEelement(List<EventAboutPeople> peopleList,EventAboutPeople selectPeople){
        for (EventAboutPeople p: peopleList){
            if (!TextUtils.isEmpty(p.uid) && p.uid.equals(selectPeople.uid)){
                ToastUtil.show("已选择"+selectPeople.name);
                return true;
            }
        }
        return false;
    }

    private boolean isHasSameEelement(List<EventAboutPeople> peopleList,ArrayList<EventAboutPeople> selectPeople){
        boolean isFound = false;
        for (EventAboutPeople p: selectPeople){
            isFound = false;
            for (EventAboutPeople oldone: peopleList){
                if (!TextUtils.isEmpty(p.uid) && p.uid.equals(oldone.uid)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound){
                peopleList.add(p);
            }
        }
        return false;
    }

    @Override
    public void loadData() {

    }

    //二维码点击
    @OnClick(R.id.right_frame)
    public void chooseFormZxing(){
        Intent intent = new Intent(this,AddEventZxingActivity.class);
        intent.putExtra("refer",2);
        intent.putExtra("eventType",mbAbnormal);
        startActivityForResult(intent,CODE_ZXING);
    }

    @OnClick(R.id.choose_address_frame)
    public void chooseEventAddressClick(){

        if (mEventAddressListDialog == null){

            getEventAddressList();

        }else{

            mEventAddressListDialog.show();
        }

    }

    //选择事件类型
    @OnClick(R.id.event_choose_eventtype_tv)
    public void chooseEventTypeClick(){
        //getEventTypeList();
    }
    @OnClick(R.id.speech_input)
    public void speech(){
        //动态申请下录音权限
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.RECORD_AUDIO},1);
        }else {
            startSpeech();
        }
    }

    private void startSpeech(){
        new SpeechUtil(this).createDialog(this, new SpeechUtil.SpeechResultCallBack() {
            @Override
            public void result(String s) {

                mDesEditText.append(s);
            }
        });
    }
    //新增事件
    @OnClick(R.id.event_commit_btn)
    public void eventAddClick(){
        if (mImagePaths.size() == 0){
            if (mAudioLocalFileList.size() == 0){
                if (TextUtils.isEmpty(mVideoPaths)){
                    addEventRequest();
                }else{
                    uploadVideo();
                }
            }else{
                uploadAudio();
            }

        }else{
            uploadImages();
        }

    }

    private void addEventRequest(){
        if (!mbAbnormal) {
            addNormalEventRequest();
        }else{
            addAbnormalEventRequest();
        }
    }

    //选择时间
    @OnClick(R.id.event_time_tv)
    public void chooseEventTimeClick(){

        TimeDialog timeDialog = null;
        if (timeDialog == null){
            timeDialog = new TimeDialog(this,mChooseTimeTv);
            timeDialog.setMyOnItemClickListener(new TimeDialog.OnTimeItemClickListener() {
                @Override
                public void onItemClick(String timeStr) {
                    if (!TextUtils.isEmpty(timeStr)){
                        String nowTime = TimeUtils.getCurrentTime();
                        if (TimeUtils.compareDate(timeStr, nowTime) > 0){
                            ToastUtil.show("事件时间大于当前时间");
                        }
                    }

                }

            });
        }
        timeDialog.show();
    }




    //添加图片
    @OnClick(R.id.text_pic_record_linear)
    public void addTextPicRecordLinear(){
        Intent intent = new Intent(this,AddEventWithPictrueTextActivity.class);
        intent.putStringArrayListExtra(Key.BEAN,mImagePaths);
        intent.putExtra(Key.RECORD_IMG_TEXT,TextUtils.isEmpty(mImageTextStr)?"":mImageTextStr);
        startActivityForResult(intent,Constant.CODE_REQUEST_IMG_TEXT);
    }

    //添加音频
    @OnClick(R.id.media_record_linear)
    public void addMediaRecordLinear(){
        Intent intent = new Intent(this,SystemAudioListChooseActivity.class);
        intent.putParcelableArrayListExtra(Key.BEAN,mAudioLocalFileList);
        startActivityForResult(intent,Constant.CODE_REQUEST_MEDIA);
    }

    //添加视频
    @OnClick(R.id.video_record_linear)
    public void addTextRecordClick(){
        Intent intent = new Intent(this,VideoRecordActivity.class);
        intent.putExtra(Key.RECORD_VIDEO,mVideoPaths);
        startActivityForResult(intent,Constant.CODE_REQUEST_VIDEO);
    }

    /**
     * 学生详情
     * 目的：获得学生对应的家长信息
     */
    private void getStudentDetail(final String studentId){

        disLoading();
        CommonModel.getInstance().getStudentDetail(studentId,new BaseListener(StudentDetailBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
//                    StudentDetailBean bean = (StudentDetailBean) infoObj;
//                    if (bean.fatherId > 0) {
//                        ParentFromStudent parentFromStudent1 = new ParentFromStudent(bean.fatherId, bean.fatherName, bean.fatherAvatar, studentId);
//                        mParentList.add(parentFromStudent1);
//                    }
//                    if (bean.motherId > 0){
//                        ParentFromStudent parentFromStudent2 = new ParentFromStudent(bean.motherId,bean.motherName,bean.motherAvatar,studentId);
//                        mParentList.add(parentFromStudent2);
//                    }
//                    mEventParentAdapter.notifyDataSetChanged();
                }else{
                    ToastUtil.show("获取学生详情失败");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }


    /**
     * 异常事件新增
     */
    private void addAbnormalEventRequest(){

        String bodyStr  = createBodyStr();
        if (bodyStr == null){
            return;
        }

        disLoading("加载中...", false);

        TeacherModel.getInstance().addAbnormalEvent(bodyStr, new BaseListener(AddEventResponseBean.class){
                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        if (infoObj != null){
                            AddEventResponseBean bean = (AddEventResponseBean)infoObj;
                            if (TextUtils.isEmpty(bean.number)){
                                ToastUtil.show("新建事件成功");
                                sendBrodcastRefresh();
                                finish();
                            }
                        }else{
                            ToastUtil.show("新建事件失败");
                        }
                        hideLoading();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        hideLoading();
                        ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
                    }
                });

    }

    /**
     * 普通事件新增
     */
    private void addNormalEventRequest(){

        String bodyStr  = createBodyStr();
        if (bodyStr == null){
            return;
        }

        disLoading("加载中...", false);

        TeacherModel.getInstance().addNormalEvent(bodyStr, new BaseListener(AddEventResponseBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (infoObj != null){
                    AddEventResponseBean bean = (AddEventResponseBean)infoObj;
                    if (TextUtils.isEmpty(bean.number)){
                        ToastUtil.show("新建事件成功");
                        sendBrodcastRefresh();
                        finish();
                    }
                }else{
                    ToastUtil.show("新建事件失败");
                }
                hideLoading();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }

    private void sendBrodcastRefresh(){
        Intent intent = new Intent();
        intent.setAction(Constant.BROADCAST_REFRESH_EVENT);
        this.sendBroadcast(intent);
    }

    private String createBodyStr(){
        String choosedTime = mChooseTimeTv.getText().toString();
        if (TextUtils.isEmpty(choosedTime)){
            ToastUtil.show("请输入事件发生时间");
            return null;
        }

        if (TextUtils.isEmpty(mInputAddressEdit.getText().toString())){
            ToastUtil.show("请输入事件发生地址");
            return null;
        }


        if (mInvolvePeopleList.size() == 1){
            ToastUtil.show("请选择涉事人");
            return null;
        }

        if (mEventTyPeId == -1){
            ToastUtil.show("请选择事件类型");
            return null;
        }

        if (TextUtils.isEmpty(mDesEditText.getText().toString())){
            ToastUtil.show("请输入事件描述");
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bizTime",mChooseTimeTv.getText().toString());
        jsonObject.put("address",mInputAddressEdit.getText().toString());

        mLat = "";mLng= "";
        if (App.mMyLocation != null){
            mLat = "" + App.mMyLocation.getLatitude();
            mLng = "" + App.mMyLocation.getLongitude();
        }

        jsonObject.put("longitude",mLng);
        jsonObject.put("latitude",mLat);
        jsonObject.put("eventLevel",mEventLevel);
        jsonObject.put("eventType",mEventTyPeId);
        jsonObject.put("desc",mDesEditText.getText().toString());

        JSONObject subjsonObject;
        if (!mbAbnormal) {
            //主要涉事人
            JSONArray subMainInvolver = new JSONArray();
            for (EventAboutPeople people:mInvolvePeopleList){
                if (people.isPeople && (people.dutyType.equals(Constant.EVENT_DUTY_MAJOR ))){
                    subMainInvolver.add(people.uid);
                }
            }
            jsonObject.put("students",subMainInvolver);
        }else{
            //主要涉事人
            JSONArray subMainInvolver = new JSONArray();
            for (EventAboutPeople people:mInvolvePeopleList){
                if (people.isPeople && (people.dutyType.equals(Constant.EVENT_DUTY_MAJOR ))){
                    subMainInvolver.add(people.uid);
                }
            }
            jsonObject.put("primaryInvolvedPeople",subMainInvolver);

            //次要涉事人
            JSONArray subMinorInvolver = new JSONArray();
            for (EventAboutPeople people:mInvolvePeopleList){
                if (people.isPeople && (people.dutyType.equals(Constant.EVENT_DUTY_MINOR))){
                    subMinorInvolver.add(people.uid);
                }
            }
            jsonObject.put("secondaryInvolvedPeople",subMinorInvolver);

            //知情人(可选)
            JSONArray subPeople = new JSONArray();
            if (mInvolvePeopleList.size() > 1){
                for (EventAboutPeople people : mInvolvePeopleList){
                    if (people.isPeople && (people.dutyType.equals(Constant.EVENT_DUTY_KNOWN))){
                        subjsonObject = new JSONObject();
                        subjsonObject.put("type", people.type);
                        subjsonObject.put("uid", people.uid);
                        subPeople.add(subjsonObject);
                    }
                }
            }
            jsonObject.put("insider",subPeople);

            //证人(可选)
            JSONArray subWitness = new JSONArray();
            for (EventAboutPeople people : mInvolvePeopleList){
                if (people.isPeople && people.dutyType.equals(Constant.EVENT_DUTY_WITNESS)){
                    subjsonObject = new JSONObject();
                    subjsonObject.put("type", people.type);
                    subjsonObject.put("uid", people.uid);
                    subWitness.add(subjsonObject);
                }
            }
            jsonObject.put("witness",subWitness);

            //举报人(可选)
            JSONArray subInformant = new JSONArray();
            for (EventAboutPeople people : mInvolvePeopleList){
                if (people.isPeople && people.dutyType.equals(Constant.EVENT_DUTY_REPORT)){
                    subjsonObject = new JSONObject();
                    subjsonObject.put("type", people.type);
                    subjsonObject.put("uid", people.uid);
                    subInformant.add(subjsonObject);
                }
            }
            jsonObject.put("informant",subInformant);

            jsonObject.put("officepush",mIsPushOffice);

        }

        //提交佐证记录  //0. 图文，　1.音频, 2.视频, 3.文档
        //提交图片
        JSONArray subRecords = new JSONArray();
        if (mUploadedImages != null && mUploadedImages.size() > 0) {
            for (UploadFileBean ufile : mUploadedImages) {
                if (ufile != null) {
                    subjsonObject = new JSONObject();
                    subjsonObject.put("name", ufile.localName);
                    subjsonObject.put("description", mImageTextStr);
                    subjsonObject.put("url", ufile.fileuid);
                    subjsonObject.put("type", "0");
                    subRecords.add(subjsonObject);
                }
            }
        }

        //提交音视频文件
        if (mUploadAudios != null && mUploadAudios.size() > 0) {
            for (UploadFileBean ufile : mUploadAudios) {
                if (ufile != null) {
                    subjsonObject = new JSONObject();
                    subjsonObject.put("name", ufile.localName);
                    subjsonObject.put("description", mImageTextStr);
                    subjsonObject.put("url", ufile.fileuid);
                    subjsonObject.put("type", "1");
                    subRecords.add(subjsonObject);
                }
            }
        }

        //提交视频文件
        if (mUploadVideos != null && mUploadVideos.size() > 0) {
            for (UploadFileBean ufile : mUploadVideos) {
                if (ufile != null) {
                    subjsonObject = new JSONObject();
                    subjsonObject.put("name", ufile.localName);
                    subjsonObject.put("description", mImageTextStr);
                    subjsonObject.put("url", ufile.fileuid);
                    subjsonObject.put("type", "2");
                    subRecords.add(subjsonObject);
                }
            }
        }


        jsonObject.put("evidence", subRecords);

        LogUtil.i("params",jsonObject.toString());

        return jsonObject.toString();
    }

    private void uploadImages(){

        disLoading("正在上传图片", false);
        UploadModel.getInstance().uploadManyWithLuBan(this, mHandler, mImagePaths, 0, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){
                    mUploadedImages = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("图片上传成功");

                    if (mAudioLocalFileList.size() == 0){
                        if (TextUtils.isEmpty(mVideoPaths)){
                            addEventRequest();
                        }else{
                            uploadVideo();
                        }

                    }else{
                        uploadAudio();
                    }

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("上传图片失败"+ (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void uploadAudio(){

        ArrayList<String> tempAudioList = new ArrayList<>();
        for (LocalFile localFile :mAudioLocalFileList){
            tempAudioList.add(localFile.localPath);
        }
        disLoading("正在上传音频", false);
        UploadModel.getInstance().uploadMany(tempAudioList, 1, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){

                    mUploadAudios = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("音频上传成功");

                    //成功后添加事件
                    if (TextUtils.isEmpty(mVideoPaths)){
                        addEventRequest();
                    }else{
                        uploadVideo();
                    }

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("上传音频失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void uploadVideo(){

        ArrayList<String> tempVideoList = new ArrayList<>();
        if (!TextUtils.isEmpty(mVideoPaths)){
            tempVideoList.add(mVideoPaths);
        }
        disLoading("正在上传视频", false);
        UploadModel.getInstance().uploadMany(tempVideoList, 1, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){

                    mUploadVideos = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("视频上传成功");

                    //成功后添加事件
                    addEventRequest();

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("上传视频失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void getEventAddressList(){

        disLoading();

        CommonModel.getInstance().getEventAddressList(new BaseListener(EventAddress.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();
                if (listObj != null){
                    final List<EventAddress> tempList = (List<EventAddress>)listObj;
                    if (tempList.size() > 0){

                        mEventAddressListDialog = new EventAddressListDialog(TeacherAddEventActivity.this,tempList);
                        mEventAddressListDialog.show();
                        mEventAddressListDialog.setMyItemOnclickListener(new EventAddressListDialog.MyItemOnclickListener() {
                            @Override
                            public void onItemClick(int position) {
                                EventAddress eventAddress = tempList.get(position);
                                mInputAddressEdit.setText(eventAddress.address);
                                mEventAddressListDialog.dismiss();
                            }
                        });

                    }else{
                        ToastUtil.show("没有数据");
                    }

                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
                hideLoading();
            }

        });

    }

    @OnClick(R.id.back_frame)
    public void finishActivityClick(){
        finish();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startSpeech();
        }else {
            Toast.makeText(this,"用户拒绝了权限",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
