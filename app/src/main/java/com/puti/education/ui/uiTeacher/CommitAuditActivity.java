package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewUtils;
import com.puti.education.R;
import com.puti.education.adapter.InvolvedEditAdapter;
import com.puti.education.bean.Duty;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventDeailInvolvedPeople;
import com.puti.education.bean.InvolvedSettingBean;
import com.puti.education.bean.KeyValue;
import com.puti.education.bean.LocalFile;
import com.puti.education.bean.StudentDetailBean;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.bean.Warn;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.ChooseHeadListActivity;
import com.puti.education.ui.uiCommon.ProfessorListActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/16 0016.
 *
 * 事件处理---提交审核
 */

public class CommitAuditActivity extends BaseActivity {

    private final static int REQEUST_ADD_CLASS_LEADER = 111;
    public final static  int RESULT_ADD_CLASS_LEADER = 333;

    public final static  int RESULT_CHOOSE_INVOLVED_PEOPLE = 555;
    public final static  int REQUEST_CHOOSE_INVOLVED_PEOPLE = 666;

    public final static int CODE_RESULT_ADD_STR = 999;
    public final static int CODE_REQUEST_ADD_STR = 1000;

    public final static int CODE_RESULT_IMG_TEXT = 1001;
    public final static int CODE_REQUEST_IMG_TEXT = 1002;

    public final static int CODE_RESULT_MEDIA = 1003;
    public final static int CODE_REQUEST_MEDIA = 1004;

    private Handler mHandler = new Handler();

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.event_edit_lv)
    RecyclerView mRv;

    @BindView(R.id.text_record_linear)
    LinearLayout textRecordLinear;
    @BindView(R.id.text_pic_record_linear)
    LinearLayout textPicRecordLinear;
    @BindView(R.id.media_record_linear)
    LinearLayout mediaRecordLinear;

    private InvolvedEditAdapter mEditAdapter;
    private List<InvolvedSettingBean> mPeopleList;

    private LRecyclerViewAdapter lRecyclerViewAdapter;

    private List<Duty.DutyDetail> mDutyWarnList;
    private List<KeyValue> mWarnList;

    private HashMap<Integer,String> mEditValuseMap;

    private int mCurentClickItemIndex = -1;
    private int mEventId = -1;

    private ArrayList<EventDeailInvolvedPeople> involvedPeopleList;

    //图文记录等信息
    private String mTextRecordStr;
    private String mImageTextStr;
    private ArrayList<UploadFileBean> mUploadedImages;
    private ArrayList<String> mImagePaths;
    private List<UploadFileBean> mUploadFiles;
    private ArrayList<LocalFile> mAudioLocalFileList;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_commit_audit;
    }

    @Override
    public void initVariables() {

        mDutyWarnList = new ArrayList<>(10);
        mWarnList = new ArrayList<>(10);
        mEditValuseMap = new HashMap<>(10);

        mUploadedImages = new ArrayList<>();
        mImagePaths = new ArrayList<>(9);
        mUploadFiles = new ArrayList<>(6);
        mAudioLocalFileList = new ArrayList<>(6);

        Intent intent = getIntent();
        mEventId = intent.getIntExtra(Key.EVENT_ID,-1);
        LogUtil.i("mEventId",mEventId+"");
        involvedPeopleList = (ArrayList<EventDeailInvolvedPeople>)intent.getSerializableExtra(Key.BEAN);

        if (involvedPeopleList != null){
            mPeopleList = new ArrayList<>();
            EventDeailInvolvedPeople people = null;
            int size = involvedPeopleList.size();

            InvolvedSettingBean involvedSettingBean = null;
            for (int i = 0; i< size;i++){
                people = involvedPeopleList.get(i);
                //involvedSettingBean = new InvolvedSettingBean(people.id,people.name,people.avatar,people.className);
                involvedSettingBean.otherLeaderList.add(new EventAboutPeople(false));
                involvedSettingBean.professorList.add(new EventAboutPeople(false));
                mPeopleList.add(involvedSettingBean);
            }
            LogUtil.i("people size",mPeopleList.size()+"");
        }
    }

    @Override
    public void initViews() {
        mTitleTv.setText("事件处理");
        mEditAdapter = new InvolvedEditAdapter(this);
        mEditAdapter.setmEditMap(mEditValuseMap);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(CommitAuditActivity.this,mEditAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(lRecyclerViewAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.footer_edit_involvedlist,null);
        Button addBtn = (Button) footerView.findViewById(R.id.add_btn);
        Button commitBtn = (Button) footerView.findViewById(R.id.event_commit_check_btn);
        RecyclerViewUtils.setFooterView(mRv,footerView);

        //提交
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditAdapter.mList == null || mEditAdapter.mList.size() <= 0){
                    ToastUtil.show("请添加涉事人");
                    return;
                }

                for (InvolvedSettingBean bean: mEditAdapter.mList){
                    if (TextUtils.isEmpty(bean.dutyRank)){
                        ToastUtil.show("请填写" + bean.name + "的责任等级");
                        return;
                    }
                    if (TextUtils.isEmpty(bean.warningRank)){
                        ToastUtil.show("请填写" + bean.name + "的警告等级");
                        return;
                    }
                }


                if (mImagePaths.size() == 0){

                    if (mAudioLocalFileList.size() == 0){
                        commit();
                    }else{
                        uploadAudio();
                    }

                }else{
                    uploadImages();
                }
            }
        });

        //添加
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommitAuditActivity.this,InvolvedPeopleChooseActivity.class);
                //intent.putParcelableArrayListExtra(Key.BEAN,involvedPeopleList);
                startActivityForResult(intent,REQUEST_CHOOSE_INVOLVED_PEOPLE);
            }
        });


        //删除item
        mEditAdapter.setDelOnclickListener(new InvolvedEditAdapter.DelOnclickListener() {
            @Override
            public void delClick(int position) {
                mEditValuseMap.remove(position);
                mEditAdapter.mList.remove(position);
                mEditAdapter.notifyDataSetChanged();
            }
        });

        //添加其他班主任
        mEditAdapter.setAddClassLeaderListener(new InvolvedEditAdapter.AddClassLeaderListener() {
            @Override
            public void addclick(int position) {
                mCurentClickItemIndex = position;
                Intent intent = new Intent(CommitAuditActivity.this,ChooseHeadListActivity.class);
                startActivityForResult(intent,REQEUST_ADD_CLASS_LEADER);
            }
        });
        //删除操作
        mEditAdapter.setChildGvDeleteClickLintener(new InvolvedEditAdapter.ChildGvDeleteClickLintener() {
            @Override
            public void click(int parentPositon, int position) {
                mEditAdapter.mList.get(parentPositon).otherLeaderList.remove(position);
                mEditAdapter.update(parentPositon);
            }
        });

        //添加专家
        mEditAdapter.setAddProfessorListener(new InvolvedEditAdapter.AddProfessorListener() {
            @Override
            public void addclick(int position) {
                mCurentClickItemIndex = position;
                Intent intent = new Intent(CommitAuditActivity.this,ProfessorListActivity.class);
                startActivityForResult(intent,Constant.CODE_REQUEST_PROFESSOR);
            }
        });

        mEditAdapter.setProDeleteClickLintener(new InvolvedEditAdapter.ProDeleteClickLintener() {
            @Override
            public void click(int parentPositon, int position) {
                mEditAdapter.mList.get(parentPositon).professorList.remove(position);
                mEditAdapter.update(parentPositon);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null){
            mCurentClickItemIndex = -1;
            return;
        }

        if (resultCode == RESULT_ADD_CLASS_LEADER){
            EventAboutPeople people = (EventAboutPeople) data.getSerializableExtra(Key.BEAN);
            List<EventAboutPeople> tempList = mEditAdapter.mList.get(mCurentClickItemIndex).otherLeaderList;
            for (int i = 0;i<tempList.size();i++){
                if (tempList.get(i).uid.equals(people.uid)){
                    ToastUtil.show("不能添加相同的人");
                    return;
                }
            }
            tempList.add(tempList.size()-1,people);
            mEditAdapter.update(mCurentClickItemIndex);

        }else if (resultCode == Constant.CODE_RESULT_PROFESSOR){

            EventAboutPeople people = (EventAboutPeople) data.getSerializableExtra(Key.BEAN);
            List<EventAboutPeople> tempList = mEditAdapter.mList.get(mCurentClickItemIndex).professorList;
            for (int i = 0;i<tempList.size();i++){
                if (tempList.get(i).uid.equals(people.uid)){
                    ToastUtil.show("不能添加相同的人");
                    return;
                }
            }
            tempList.add(tempList.size()-1,people);
            mEditAdapter.update(mCurentClickItemIndex);

        }else if (resultCode == RESULT_CHOOSE_INVOLVED_PEOPLE){

            EventDeailInvolvedPeople people =  (EventDeailInvolvedPeople) data.getParcelableExtra(Key.BEAN);
            InvolvedSettingBean involvedSettingBean = createInvolvedSettingBean(people);

            if (!isContainItem(involvedSettingBean)){
                mEditValuseMap.put(mEditAdapter.mList.size(),"");
                mEditAdapter.insert(involvedSettingBean);
                getStudentDetail(involvedSettingBean.id +"",mEditAdapter.mList.size() - 1);// 获取学生家长
            }else{
                ToastUtil.show("列表已经包含该涉事人");
            }

        }else if (resultCode == CODE_RESULT_ADD_STR){

            mTextRecordStr = data.getStringExtra(Key.RECORD_IMG_TEXT);
            LogUtil.i("record text",mTextRecordStr);

        }else if (resultCode == CODE_RESULT_IMG_TEXT){

            mImagePaths.clear();
            mImageTextStr = data.getStringExtra(Key.RECORD_IMG_TEXT);
            List<String> tempImgList = data.getStringArrayListExtra(Key.BEAN);
            mImagePaths.addAll(tempImgList);
            LogUtil.i("record imglist",mImagePaths.size()+"");

        }else if (resultCode == CODE_RESULT_MEDIA){

            mAudioLocalFileList.clear();
            List<LocalFile> temImgList =data.getParcelableArrayListExtra(Key.BEAN);
            mAudioLocalFileList.addAll(temImgList);
            LogUtil.i("record audio list",mAudioLocalFileList.size()+"");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //添加文字
    @OnClick(R.id.text_record_linear)
    public void addTextRecordClick(){
        Intent intent = new Intent(this,AddEventWithInputTextActivity.class);
        intent.putExtra(Key.RECORD_IMG_TEXT,mTextRecordStr);
        startActivityForResult(intent,CODE_REQUEST_ADD_STR);
    }

    //添加文字图片
    @OnClick(R.id.text_pic_record_linear)
    public void addTextPicRecordLinear(){
        Intent intent = new Intent(this,AddEventWithPictrueTextActivity.class);
        intent.putStringArrayListExtra(Key.BEAN,mImagePaths);
        intent.putExtra(Key.RECORD_IMG_TEXT,TextUtils.isEmpty(mImageTextStr)?"":mImageTextStr);
        startActivityForResult(intent,CODE_REQUEST_IMG_TEXT);
    }

    //添加音频
    @OnClick(R.id.media_record_linear)
    public void addMediaRecordLinear(){
        Intent intent = new Intent(this,SystemAudioListChooseActivity.class);
        intent.putParcelableArrayListExtra(Key.BEAN,mAudioLocalFileList);
        startActivityForResult(intent,CODE_REQUEST_MEDIA);
    }

    private InvolvedSettingBean createInvolvedSettingBean(EventDeailInvolvedPeople people){
        InvolvedSettingBean involvedSettingBean = null;//new InvolvedSettingBean(people.id,people.name,people.avatar,people.className);
        involvedSettingBean.otherLeaderList.add(new EventAboutPeople(false));
        involvedSettingBean.professorList.add(new EventAboutPeople(false));
        return involvedSettingBean;
    }

    private boolean isContainItem(InvolvedSettingBean currentChooseInvolvedSettingBean){
        for (InvolvedSettingBean bean : mEditAdapter.mList){
            if (currentChooseInvolvedSettingBean.id == bean.id){
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadData() {
        getDutyWarnList();
    }

    private void commit(){



        String str = createCommitParamStr();
        if (str == null){
            return;
        }
        disLoading();
        TeacherModel.getInstance().commitEventAudit(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();

                JSONObject jsonObject = JSONObject.parseObject(infoObj.toString());
                if (jsonObject.containsKey(Constant.COUNT)){
                    ToastUtil.show("提交审核成功");
                }else{
                    ToastUtil.show("提交审核失败");
                }
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });

    }

    private String createCommitParamStr(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",mEventId);

        //提交图片
        ArrayList<String> tempImages = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            tempImages = new ArrayList<>();
            for (UploadFileBean ufile: mUploadedImages){
                if (ufile != null){
                    tempImages.add(ufile.url);
                }
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(tempImages);
            JSONObject imgtextObj = new JSONObject();
            imgtextObj.put("desc",TextUtils.isEmpty(mImageTextStr)?"":mImageTextStr);
            imgtextObj.put("ImageList",jsonArray);
            jsonObject.put("ImageTextRecord",imgtextObj);
        }

        //提交文件
        ArrayList<String> tempFiles ;
        if (mUploadFiles != null && mUploadFiles.size() > 0){
            tempFiles = new ArrayList<>();
            for (UploadFileBean ufile: mUploadFiles){
                if (ufile != null){
                    tempFiles.add(ufile.url);
                }
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(tempFiles);
            jsonObject.put("audioRecord",jsonArray);
        }

        //提交文字记录
        if (!TextUtils.isEmpty(mTextRecordStr)){
            jsonObject.put("textRecord",mTextRecordStr);
        }

        JSONArray jsonArray = new JSONArray();

        InvolvedSettingBean bean = null;
        JSONObject itemObj = null;

        boolean haveMainDutyPeople = false;
        for (int i=0; i< mEditAdapter.mList.size();i++){
            bean = mEditAdapter.mList.get(i);
            itemObj = new JSONObject();
            itemObj.put("id",bean.id);
            itemObj.put("dutyRank",bean.dutyRank);
            if ("主要责任人".equals(bean.dutyRank.trim())){
                haveMainDutyPeople = true;
            }
            itemObj.put("warningRank",bean.warningRank);
            itemObj.put("isPushStudentOffice",bean.isStuDePartmentCheck);
            itemObj.put("isTrack",bean.isFollow);
            itemObj.put("desc",mEditValuseMap.get(i));

            if (bean.isParentCheck && bean.parentList != null && bean.parentList.size() > 0){
                JSONArray parentArray = new JSONArray();
                for (int j = 0;j < bean.parentList.size(); j++){
                    parentArray.add(bean.parentList.get(j).uid);
                }
                itemObj.put("pushParent",parentArray);
            }else{
                itemObj.put("pushParent",new JSONArray());
            }

            if (bean.otherLeaderList != null && bean.otherLeaderList.size() > 1){
                JSONArray teacherArray = new JSONArray();
                for (int j = 0;j < bean.otherLeaderList.size()-1; j++){
                    teacherArray.add(bean.otherLeaderList.get(j).uid);
                }
                itemObj.put("pushTeacher",teacherArray);
            }else{
                itemObj.put("pushTeacher",new JSONArray());
            }

            if (bean.professorList != null && bean.professorList.size() > 1){
                JSONArray professorArray = new JSONArray();
                for (int j = 0;j < bean.professorList.size()-1; j++){
                    professorArray.add(bean.professorList.get(j).uid);
                }
                itemObj.put("expert",professorArray);
            }else{
                itemObj.put("expert",new JSONArray());
            }

            jsonArray.add(itemObj);
        }

        if (!haveMainDutyPeople){
            ToastUtil.show("请选择一个主要责任人");
            return null;
        }

        jsonObject.put("items",jsonArray);

        LogUtil.i("params",jsonObject.toString());
        return jsonObject.toString();
    }

    //责任等级
    private void getDutyWarnList(){

        disLoading("数据初始化中...");

        TeacherModel.getInstance().getDutyWarnList(new BaseListener(Duty.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (infoObj != null){
                    Duty duty = (Duty) infoObj;
                    mDutyWarnList.addAll(duty.options);
                    mEditAdapter.setmDutyList(mDutyWarnList);
                    getWarnList();

                }else{
                    hideLoading();
                    ToastUtil.show("责任等级获取失败！");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    //警告等级
    private void getWarnList(){

        TeacherModel.getInstance().getWarnList(new BaseListener(Warn.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                if (infoObj != null){
                    Warn warn = (Warn) infoObj;
                    //mWarnList.addAll(warn.options);
                    //mEditAdapter.setmWarnList(mWarnList);
                }else{
                    ToastUtil.show("警告等级获取失败！");
                }
                hideLoading();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    private void getStudentDetail(String studentId, final int position){

        disLoading();

        CommonModel.getInstance().getStudentDetail(studentId,new BaseListener(StudentDetailBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    StudentDetailBean bean = (StudentDetailBean) infoObj;
                    if (!TextUtils.isEmpty(bean.fatheruid)){
                        EventAboutPeople parentFromStudent1 = new EventAboutPeople(bean.fatherAvatar,bean.fatherName,bean.fatheruid, 4);
                        mEditAdapter.mList.get(position).parentList.add(parentFromStudent1);
                    }

                    if (!TextUtils.isEmpty(bean.motheruid)){
                        EventAboutPeople parentFromStudent2 = new EventAboutPeople(bean.motherAvatar,bean.motherName,bean.motheruid, 4);
                        mEditAdapter.mList.get(position).parentList.add(parentFromStudent2);
                    }

                    mEditAdapter.update(position);
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

    private void uploadImages(){

        CommonModel.getInstance().uploadManyWithLuBan(this, mHandler, mImagePaths, 1, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (listObj != null){
                    mUploadedImages = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("图片上传成功");

                    if (mAudioLocalFileList.size() == 0){
                        commit();
                    }else{
                        uploadAudio();
                    }

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("上传图片失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void uploadAudio(){

        ArrayList<String> tempAudioList = new ArrayList<>();
        for (LocalFile localFile :mAudioLocalFileList){
            tempAudioList.add(localFile.localPath);
        }

        CommonModel.getInstance().uploadMany(tempAudioList, 2, new BaseListener(UploadFileBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                if (listObj != null){

                    mUploadFiles = (ArrayList<UploadFileBean>)listObj;
                    ToastUtil.show("音频上传成功");

                    commit();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("上传音频失败"+ (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    public void finishActivity(View view){
        finish();
    }
}
