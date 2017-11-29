package com.puti.education.ui.uiPatriarch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puti.education.R;
import com.puti.education.bean.EventAddress;
import com.puti.education.bean.EventType;
import com.puti.education.bean.LocalFile;
import com.puti.education.bean.ParentInfo;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.Student;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.netFrame.netModel.UploadModel;
import com.puti.education.speech.SpeechUtil;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.VideoRecordActivity;
import com.puti.education.ui.uiTeacher.AddEventWithPictrueTextActivity;
import com.puti.education.ui.uiTeacher.SystemAudioListChooseActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.DropWithBackView;
import com.puti.education.widget.EventAddressListDialog;
import com.puti.education.widget.RatingSmallBarView;
import com.puti.education.widget.TimeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/6/2 0015.
 * 家长及学生行为普通事件录入添加
 */

public class ActionEventAddActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.itemcontainer_people)
    LinearLayout mContainerPeople;
    @BindView(R.id.event_time_tv)
    TextView mTvTime;
    @BindView(R.id.event_source_address_edit)
    EditText mEtAddress;
    @BindView(R.id.choose_address_frame)
    FrameLayout mLayoutAddress;
    @BindView(R.id.event_choose_eventtype_tv)
    TextView mTvType;
    @BindView(R.id.event_des_tv)
    EditText mEtDesc;
    @BindView(R.id.rbv_event_level)
    RatingSmallBarView mRateBar;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.tv_child_hint)
    TextView mTvNameHint;

    @BindView(R.id.layout_record)
    LinearLayout mLayoutRecord;
    @BindView(R.id.text_pic_record_linear)
    LinearLayout mTextPicLinear;
    @BindView(R.id.media_record_linear)
    LinearLayout mediaRecordLinear;
    @BindView(R.id.video_record_linear)
    LinearLayout mVideoRecordLinear;
    @BindView(R.id.speech_input)
    TextView mSpeechInput;


    private String mChildId;
    private String mTime, mAddress, mLat, mLng, mDesc;
    private int mEventType = -1, mEventLevel = -1;
    private ArrayList<String> mImagePaths = new ArrayList<>();
    private List<Student> mChildList = null;
    private int mRole = -1;

    private String mImageTextStr;
    private ArrayList<LocalFile> mAudioLocalFileList;
    private String mVideoPaths;

    private ArrayList<UploadFileBean> mUploadedImages;
    private ArrayList<UploadFileBean> mUploadAudios;
    private ArrayList<UploadFileBean> mUploadVideos;

    @Override
    public int getLayoutResourceId() {
        return R.layout.action_event_add_layout;
    }

    @Override
    public void initVariables() {
        mChildList = new ArrayList<>();
        mTypeList = new ArrayList<>();
        mAudioLocalFileList = new ArrayList<>();
        mUploadAudios = new ArrayList<>();
        mUploadVideos = new ArrayList<>();

        setHeadMore();

        mLat = this.getIntent().getStringExtra("lat");
        mLng = this.getIntent().getStringExtra("lng");

        mRole = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);

    }

    @Override
    public void initViews() {
        mUiTitle.setText("新增普通事件");
        mLayoutAddress.setVisibility(View.GONE); //家长不能选择地点

        mRateBar.setOnRatingListener(new RatingSmallBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mEventLevel = RatingScore;
            }
        });
    }


    @Override
    public void loadData() {
        if (mRole == Constant.ROLE_PARENTS){
            mTvNameHint.setText("相关联的小孩");
            getParentInfo();
        }else if (mRole == Constant.ROLE_STUDENT){
            mTvNameHint.setText("事件关联的学生");
            getSelfInfo();
        }

    }

    private void getSelfInfo(){
        Student self = new Student();
        self.isSelected = true;
        self.uid = ConfigUtil.getInstance(this).get(Constant.KEY_USER_ID, "");
        self.avatar= ConfigUtil.getInstance(this).get(Constant.KEY_USER_AVATAR, "");
        self.name = ConfigUtil.getInstance(this).get(Constant.KEY_USER_NAME, "");
        mChildList.add(self);
        refreshChildList();
    }

    @OnClick(R.id.event_choose_eventtype_tv)
    public void chooseType(){
        getEventTypeList();
    }

    @OnClick(R.id.speech_input)
    public void speech(){

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
                mEtDesc.append(s);
            }
        });
    }
    @OnClick(R.id.event_time_tv)
    public void chooseStartTime(){
        TimeDialog timeDialog = null;
        if (timeDialog == null){
            timeDialog = new TimeDialog(this,mTvTime);
        }
        timeDialog.show();
    }

    //事件类型
    private void getEventTypeList(){

        if (mTypeList != null && mTypeList.size() > 0){
            showEventTypePop();
            return;
        }

        disLoading();

        CommonModel.getInstance().getEventTypeList(new BaseListener(EventType.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();

                if (listObj != null){
                    List<EventType> typeList = (List<EventType>) listObj;
                    if (typeList != null && typeList.size() > 0){
                        for (EventType typeone: typeList){
                            if (!typeone.bAbnormal){
                                mTypeList.add(typeone);
                            }

                        }
                    }

                    showEventTypePop();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }

    @OnClick(R.id.btn_commit)
    public void commitRecord(){
        if (mEventType <= 0){
            ToastUtil.show("请选择记录类型");
            return;
        }

        mTime = mTvTime.getText().toString();
        if (TextUtils.isEmpty(mTime)){
            ToastUtil.show("请选择事件开始时间");
            return;
        }

        mAddress = mEtAddress.getText().toString();
        if (TextUtils.isEmpty(mAddress)){
            ToastUtil.show("请选择事件地点");
            return;
        }

        mDesc = mEtDesc.getText().toString();
        if (TextUtils.isEmpty(mDesc)){
            //
        }

        if (mImagePaths == null || mImagePaths.size() <= 0){
            if (mAudioLocalFileList.size() == 0){
                if (TextUtils.isEmpty(mVideoPaths)){
                    commit();
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

    private void uploadImages(){
        ArrayList<String> tempImages = new ArrayList<String>();
        for (String str: mImagePaths){
            if (!TextUtils.isEmpty(str) && !str.equals(Constant.IMAGE_ADD_FLAG)){
                tempImages.add(str);
            }
        }

        this.disLoading("正在上传图片");
        mBtnCommit.setEnabled(false);
        CommonModel.getInstance().uploadMany(tempImages, 0, new BaseListener(UploadFileBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                mBtnCommit.setEnabled(true);
                hideLoading();

                if (listObj != null){
                    mUploadedImages = (ArrayList<UploadFileBean>)listObj;
                    if (mAudioLocalFileList.size() == 0){
                        if (TextUtils.isEmpty(mVideoPaths)){
                            commit();
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
                mBtnCommit.setEnabled(true);
                hideLoading();
                ToastUtil.show("上传资料图片失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
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
                        commit();
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
                    commit();

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

    private void commit(){
        if (mRole == Constant.ROLE_PARENTS){
            commitParentEvent();
        }else if (mRole == Constant.ROLE_STUDENT){
            commitChildEvent();
        }
    }

    private void commitParentEvent(){
        ArrayList<String> tempImages = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            tempImages = new ArrayList<String>();
            for (UploadFileBean ufile: mUploadedImages){
                if (ufile != null){
                    tempImages.add(ufile.url);
                }
            }
        }

        boolean isChooseChild = false;
        int childcount = 0;
        if (mChildList != null && mChildList.size() > 0){
            for (Student one: mChildList){
                if (one.isSelected){
                    isChooseChild = true;
                    mChildId = one.uid;
                    childcount++;
                }
            }
        }

        if (!isChooseChild){
            ToastUtil.show("请选择小孩");
            return;
        }

        if (childcount > 1){
            ToastUtil.show("只能选择一个小孩");
            return;
        }

        mBtnCommit.setEnabled(false);
        PatriarchModel.getInstance().addActionEventRecord(mEventType,mEventLevel, mChildId, mTime, mAddress, mLng, mLat, mDesc,
                mImageTextStr, mUploadedImages, mUploadAudios, mUploadVideos, new BaseListener(ResponseBean.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        mBtnCommit.setEnabled(true);
                        ToastUtil.show("添加小孩事件成功");
                        finish();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        mBtnCommit.setEnabled(true);
                        ToastUtil.show("添加小孩事件失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                    }
                });
    }

    private void commitChildEvent(){
        ArrayList<String> tempImages = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            tempImages = new ArrayList<String>();
            for (UploadFileBean ufile: mUploadedImages){
                if (ufile != null){
                    tempImages.add(ufile.url);
                }
            }
        }

        boolean isChooseChild = false;
        int childcount = 0;
        if (mChildList != null && mChildList.size() > 0){
            for (Student one: mChildList){
                if (one.isSelected){
                    isChooseChild = true;
                    mChildId = one.uid;
                    childcount++;
                }
            }
        }

        if (!isChooseChild){
            ToastUtil.show("请选择小孩");
            return;
        }

        if (childcount > 1){
            ToastUtil.show("只能选择一个小孩");
            return;
        }

        mBtnCommit.setEnabled(false);
        StudentModel.getInstance().addNormalEvent(mEventType,mEventLevel, mTime, mAddress, mLng, mLat, mDesc,
                mImageTextStr, mUploadedImages, mUploadAudios, mUploadVideos, new BaseListener(ResponseBean.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        mBtnCommit.setEnabled(true);
                        ToastUtil.show("添加普通事件成功");
                        finish();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        mBtnCommit.setEnabled(true);
                        ToastUtil.show("添加普通事件失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                    }
                });
    }

    private void refreshChildList(){
        setHeadMore();
    }

    private void setHeadMore(){
        View subView = null;
        LinearLayout subLayout = null;
        mContainerPeople.removeAllViews();
        int currentRow = -1;
        int size = mChildList.size();
        for (int j= 0; j<size; j++){
            int row  = (j/4);
            subView = addKnownAvatar(j, subLayout);
            if (currentRow == row){
                if (subLayout != null){
                    subLayout.addView(subView);
                }
            }else{
                currentRow = row;
                subLayout = addSubLayout();
                subLayout.addView(subView);
                mContainerPeople.addView(subLayout);
            }
        }

    }


    private LinearLayout addSubLayout(){
        LinearLayout subLayout = null;
        ViewGroup.LayoutParams params;
        subLayout = new LinearLayout(this);
        params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisPlayUtil.dip2px(this, 99));
        subLayout.setLayoutParams(params);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        return subLayout;
    }

    private View addKnownAvatar(final int index, View parentView){

        View itemView;
        ViewGroup.LayoutParams params;
        itemView = LayoutInflater.from(this).inflate(R.layout.item_parent, null);
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 65), DisPlayUtil.dip2px(this, 99));
        itemView.setLayoutParams(params);

        LinearLayout itemContainer = (LinearLayout)itemView.findViewById(R.id.item_container);
        ImageView headImg = (ImageView)itemView.findViewById(R.id.grid_head_img);
        TextView nameTv = (TextView)itemView.findViewById(R.id.grid_head_name_tv);
        ImageView signImg = (ImageView)itemView.findViewById(R.id.prent_sign_img);

        Student child = mChildList.get(index);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,child.avatar,headImg);
        nameTv.setText(TextUtils.isEmpty(child.name) ?"暂无":child.name);
        if (mRole == Constant.ROLE_PARENTS){
            itemContainer.setOnClickListener(new ItemClickLinstener(signImg,index));
        }else if (mRole == Constant.ROLE_STUDENT){
            signImg.setVisibility(View.GONE);
        }

        itemView.setTag(R.id.onclick_parent_view, parentView);

        return itemView;
    }

    private void getParentInfo(){
        disLoading();
        PatriarchModel.getInstance().getParnetInfo(new BaseListener(ParentInfo.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();
                if (infoObj != null){
                    ParentInfo parentInfo = (ParentInfo)infoObj;
                    if (parentInfo.childList != null && parentInfo.childList.size() >0){
                        mChildList = parentInfo.childList;
                        refreshChildList();
                    }

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
                super.requestFailed(status, code, errorMessage);

            }
        });
    }

    private class ItemClickLinstener implements View.OnClickListener{

        ImageView checImg;
        int position;

        public ItemClickLinstener(ImageView checImg,int position){
            this.checImg = checImg;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Student parentChild = mChildList.get(position);
            if (parentChild.isSelected){
                parentChild.isSelected = false;
                checImg.setImageResource(R.mipmap.ic_item_unselected);
            }else{
                parentChild.isSelected = true;
                checImg.setImageResource(R.mipmap.ic_item_selected);
            }

        }
    }

    //创建类型选择popwindow
    private DropWithBackView mTypeSpinnerPop;
    private ArrayList<EventType> mTypeList = new ArrayList<>();

    private void showEventTypePop(){

        if (mTypeList != null && mTypeList.size() > 0){

            if (mTypeSpinnerPop == null){
                mTypeSpinnerPop = new DropWithBackView(this, mTvType, mTypeList);
                mTypeSpinnerPop.setPopOnItemClickListener(new DropWithBackView.PopOnItemClickListener() {
                    @Override
                    public void onItemClick(EventType eventobjecdt) {
                        EventType eventtype = eventobjecdt;
                        if (eventtype != null) {
                            mEventType = eventtype.id;
                            mTvType.setText(eventtype.name);
                        }

                        mTypeSpinnerPop.dismiss();
                    }
                });
            }
            mTypeSpinnerPop.showAsDropDown(mTvType);

        }else{
            ToastUtil.show("暂无数据");
        }

    }

    private EventAddressListDialog mEventAddressListDialog = null;

    @OnClick(R.id.choose_address_frame)
    public void chooseEventAddressClick(){

        if (mEventAddressListDialog == null){

            getEventAddressList();

        }else{

            mEventAddressListDialog.show();
        }

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

                        mEventAddressListDialog = new EventAddressListDialog(ActionEventAddActivity.this,tempList);
                        mEventAddressListDialog.show();
                        mEventAddressListDialog.setMyItemOnclickListener(new EventAddressListDialog.MyItemOnclickListener() {
                            @Override
                            public void onItemClick(int position) {
                                EventAddress eventAddress = tempList.get(position);
                                mEtAddress.setText(eventAddress.address);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case Constant.CODE_RESULT_VIDEO:
            {
                mVideoPaths = data.getStringExtra(Key.RECORD_VIDEO);
            }
            break;
            case Constant.CODE_RESULT_IMG_TEXT:
            {
                mImagePaths.clear();
                mImageTextStr = data.getStringExtra(Key.RECORD_IMG_TEXT);
                List<String> tempImgList = data.getStringArrayListExtra(Key.BEAN);
                mImagePaths.addAll(tempImgList);
                LogUtil.i("imgepath","" + mImagePaths.toString());
            }
            break;
            case Constant.CODE_RESULT_MEDIA:
            {
                mAudioLocalFileList.clear();
                List<LocalFile> temImgList =data.getParcelableArrayListExtra(Key.BEAN);
                mAudioLocalFileList.addAll(temImgList);
            }
            break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
           startSpeech();
        }else {
            Toast.makeText(this,"用户拒绝了权限",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
