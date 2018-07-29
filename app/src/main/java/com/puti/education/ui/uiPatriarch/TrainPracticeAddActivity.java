package com.puti.education.ui.uiPatriarch;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.puti.education.R;
import com.puti.education.bean.ClassInfo;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.Role;
import com.puti.education.bean.Teacher;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.ChooseHeadListActivity;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;
import com.puti.education.widget.ItemContainer;
import com.puti.education.widget.SpinnerPop;
import com.puti.education.widget.SpinnerPopWindow;
import com.puti.education.widget.TimeDialog;
import com.puti.education.widget.TimePopupWindow;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 * 培训实践记录添加
 */

public class TrainPracticeAddActivity extends BaseActivity {

    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.itemcontainer_photo)
    LinearLayout mContainerPhoto;
    @BindView(R.id.tv_theme)
    EditText mEtTheme;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_content)
    EditText mEtContent;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_address)
    EditText mEtAddress;
    @BindView(R.id.tv_result)
    EditText mEtResult;


    public static final String PRACTICE_RECORD_NAME = "实践记录";
    public static final String TRAIN_RECORD_NAME    = "培训记录";

    public CommonDropView mCommonDropView;

    private String mStudentId;
    private String mTheme, mTime, mAddress, mContent, mResult;
    private int mTrainType = 1; //
    private ArrayList<String> mImagePaths = new ArrayList<>();
    private ArrayList<UploadFileBean> mUploadedImages;


    @Override
    public int getLayoutResourceId() {
        return R.layout.train_practice_add_layout;
    }

    @Override
    public void initVariables() {
        setPhotoMoreLayout();

        mTypeList.add(PRACTICE_RECORD_NAME);
        mTypeList.add(TRAIN_RECORD_NAME);

        mStudentId = ConfigUtil.getInstance(this).get(Constant.KEY_USER_ID, "");

    }

    @Override
    public void initViews() {
        mUiTitle.setText("添加培训实践");

        mEtContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEtResult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }}
        );
        mEtContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }}
        );

    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.tv_type)
    public void chooseType(){
        List<String> dataList = new ArrayList<>();
        dataList.add("培训");
        dataList.add("实践");
        if (mCommonDropView == null){
            mCommonDropView = new CommonDropView(TrainPracticeAddActivity.this, mTvType, dataList);
            mCommonDropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0){
                        mTrainType = 1;
                        mTvType.setText("培训");
                    }else{
                        mTrainType = 2;
                        mTvType.setText("实践");
                    }
                    mCommonDropView.dismiss();
                }
            });
        }
        mCommonDropView.showAsDropDown(mTvType);
    }

    public void chooseEventType(){
        createPopWindow();
    }

    @OnClick(R.id.tv_time)
    public void chooseTime(){
        TimeDialog timeDialog = null;
        if (timeDialog == null){
            timeDialog = new TimeDialog(this,mTvTime);
        }
        timeDialog.show();
    }

    private void refreshPhoto(){
        setPhotoMoreLayout();
    }

    private void setPhotoMoreLayout(){
        View subView = null;
        LinearLayout subLayout = null;
        mContainerPhoto.removeAllViews();
        int currentRow = -1;
        int size = mImagePaths.size();
        size++;
        for (int j= 0; j<size; j++){
            int row  = (j/3);
            subView = setPhotoMore(j, subLayout);
            if (currentRow == row){
                if (subLayout != null){
                    subLayout.addView(subView);
                }
            }else{
                currentRow = row;
                subLayout = addSubLayout();
                subLayout.addView(subView);
                mContainerPhoto.addView(subLayout);
            }
        }
    }

    private View setPhotoMore(int index, LinearLayout layout){
        View itemView;
        ViewGroup.LayoutParams params;

        itemView = LayoutInflater.from(this).inflate(R.layout.item_container, null);
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 85), DisPlayUtil.dip2px(this, 85));
        itemView.setLayoutParams(params);
        itemView.setTag(index);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = (int)view.getTag();

                if (index < 0 || index >= mImagePaths.size()){
                    PhotoPickerIntent intent = new PhotoPickerIntent(TrainPracticeAddActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(Constant.IMAGE_MAX_NUMBER_6); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(mImagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, Constant.REQUEST_CAMERA_CODE);
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(TrainPracticeAddActivity.this);
                    intent.setCurrentItem(index);
                    intent.setPhotoPaths(mImagePaths);
                    startActivityForResult(intent, Constant.REQUEST_PREVIEW_CODE);
                }
            }
        });

        LinearLayout layoutBg = (LinearLayout)itemView.findViewById(R.id.layout_parent_container);
        ImageView imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
        ImageView imgDelete= (ImageView)itemView.findViewById(R.id.img_right_delete);
        TextView tvHint    = (TextView)itemView.findViewById(R.id.head_tv);
        if (index >= 0 && index < (mImagePaths.size())){
            tvHint.setVisibility(View.GONE);
            imgDelete.setVisibility(View.VISIBLE);
            layoutBg.setBackgroundResource(R.color.white);
            File f = new File(mImagePaths.get(index));
            ImgLoadUtil.displayLocalPictrue(this, R.mipmap.ic_picture, f, imgPhoto);

            imgDelete.setTag(R.id.position_key, index);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int)view.getTag(R.id.position_key);
                    mImagePaths.remove(pos);
                    refreshPhoto();
                }
            });
        }
        return itemView;

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

    private void chooseHeadTeacher(){
        Intent intent = new Intent(this,ChooseHeadListActivity.class);
        startActivityForResult(intent, Constant.REQUEST_CODE_HEADTEACHER);
    }

    @OnClick(R.id.btn_commit)
    public void commitRecord(){
        mTheme = mEtTheme.getText().toString();
        if (TextUtils.isEmpty(mTheme)){
            ToastUtil.show("请输入事件主题");
            return;
        }

        mTime = mTvTime.getText().toString();
        if (TextUtils.isEmpty(mTime)){
            ToastUtil.show("请选择事件时间");
            return;
        }

        mAddress  = mEtAddress.getText().toString();
        if (TextUtils.isEmpty(mAddress)){
            ToastUtil.show("请选择事件地点");
            return;
        }

        mContent = mEtContent.getText().toString();
        if (TextUtils.isEmpty(mContent)){
            ToastUtil.show("请输入事件内容");
            return;
        }

        mResult = mEtResult.getText().toString();

        if (mImagePaths != null && mImagePaths.size() > 0){
            uploadImages();
        }else{
            commit();
        }


    }

    private void uploadImages(){
        disLoading("正在上传图片", false);
        ArrayList<String> tempImages = new ArrayList<String>();
        for (String str: mImagePaths){
            if (!TextUtils.isEmpty(str) && !str.equals(Constant.IMAGE_ADD_FLAG)){
                tempImages.add(str);
            }
        }

        CommonModel.getInstance().uploadMany(tempImages, 0, new BaseListener(UploadFileBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                if (listObj != null){
                    mUploadedImages = (ArrayList<UploadFileBean>)listObj;
                    commit();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("上传资料图片失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void commit(){
        int role = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);
        if (role == Constant.ROLE_PARENTS){
            commitParentTrain();
        }else if (role == Constant.ROLE_STUDENT){
            commitStudentTrain();
        }
    }

    private void commitParentTrain(){
        disLoading("正在提交...", false);
        ArrayList<String> tempImages = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            tempImages = new ArrayList<String>();
            for (UploadFileBean ufile: mUploadedImages){
                if (ufile != null){
                    tempImages.add(ufile.fileuid);
                }
            }
        }

        PatriarchModel.getInstance().addTrain(mTrainType, mTheme, mAddress, mTime, mContent, mResult, tempImages,
                 new BaseListener(ResponseBean.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        hideLoading();
                        ToastUtil.show("添加培训实践成功");
                        finish();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        hideLoading();
                        ToastUtil.show("添加培训实践失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                    }
                });
    }

    private void commitStudentTrain(){
        disLoading("正在提交...", false);
        ArrayList<String> tempImages = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            tempImages = new ArrayList<String>();
            for (UploadFileBean ufile: mUploadedImages){
                if (ufile != null){
                    tempImages.add(ufile.fileuid);
                }
            }
        }

        StudentModel.getInstance().addTrain(mTrainType, mTheme, mAddress, mTime, mContent, mResult, tempImages,
                new BaseListener(ResponseBean.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        hideLoading();
                        ToastUtil.show("添加培训实践成功");
                        finish();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        hideLoading();
                        ToastUtil.show("添加培训实践失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                    }
                });
    }


    //创建类型选择popwindow
    private SpinnerPop mSpinnerPop;
    private ArrayList<String> mTypeList = new ArrayList<>();
    public void createPopWindow(){

        if (mSpinnerPop == null){
            mSpinnerPop = new SpinnerPop(this);
            mSpinnerPop.setWidth(mTvType.getMeasuredWidth());
            mSpinnerPop.refreshData(mTypeList);
            mSpinnerPop.showAsDropDown(mTvType);
        }else{
            mSpinnerPop.showAsDropDown(mTvType);
        }
        mSpinnerPop.setMyOnItemClickListener(new SpinnerPop.MyOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mSpinnerPop.dismiss();
                String type = mTypeList.get(position);
                mTvType.setText(type);
                if (type.equals(TRAIN_RECORD_NAME)){
                    //mEventType = 1;
                }else if (type.equals(PRACTICE_RECORD_NAME)){
                    //mEventType = 2;
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CAMERA_CODE && data != null){
            ArrayList<String> tempIamges = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            if (tempIamges != null && tempIamges.size() > 0){
                mImagePaths.clear();
                mImagePaths.addAll(tempIamges);
                int size = mImagePaths.size();
                refreshPhoto();
                LogUtil.d("", "photo size: " + size);
            }

        }
    }


}
