package com.puti.education.ui.uiStudent;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.puti.education.R;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ThreadUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.ItemContainer;
import com.puti.education.widget.TimeDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/5/19 0019.
 */

public class PracticeAddActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_practice_name)
    EditText mEtTitle;
    @BindView(R.id.tv_result)
    EditText mEtResult;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.itemcontainer_photo)
    ItemContainer mContainerPhoto;



    private ArrayList<String> mImagePaths;
    private ArrayList<UploadFileBean> mUploadedImages;

    @Override
    public int getLayoutResourceId() {
        return R.layout.practice_add_layout;
    }

    @Override
    public void initVariables() {
        mImagePaths = new ArrayList<>();
        mImagePaths.add(Constant.IMAGE_ADD_FLAG);
        mUploadedImages = new ArrayList<>();
    }

    @Override
    public void initViews() {
        mTitleTv.setText("实践记录");

        setPhotoMore();
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.back_frame)
    public void finishActivity(){
        this.finish();
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
        setPhotoMore();
    }

    private void setPhotoMore(){
        mContainerPhoto.removeAllViews();
        View itemView;
        ViewGroup.LayoutParams params;
        int size = mImagePaths.size();
        for (int j= 0; j<size; j++){
            itemView = LayoutInflater.from(this).inflate(R.layout.item_container, null);
            params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 99), DisPlayUtil.dip2px(this, 99));
            itemView.setLayoutParams(params);
            itemView.setTag(j);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int)view.getTag();
                    String urlvalue = mImagePaths.get(index);
                    if (urlvalue.equals(Constant.IMAGE_ADD_FLAG)){
                        PhotoPickerIntent intent = new PhotoPickerIntent(PracticeAddActivity.this);
                        intent.setSelectModel(SelectModel.MULTI);
                        intent.setShowCarema(true); // 是否显示拍照
                        intent.setMaxTotal(Constant.IMAGE_MAX_NUMBER); // 最多选择照片数量，默认为9
                        intent.setSelectedPaths(mImagePaths); // 已选中的照片地址， 用于回显选中状态
                        startActivityForResult(intent, Constant.REQUEST_CAMERA_CODE);
                    }else{
                        PhotoPreviewIntent intent = new PhotoPreviewIntent(PracticeAddActivity.this);
                        intent.setCurrentItem(index);
                        intent.setPhotoPaths(mImagePaths);
                        startActivityForResult(intent, Constant.REQUEST_PREVIEW_CODE);
                    }
                }

            });

            ImageView imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
            ImageView imgDelete= (ImageView)itemView.findViewById(R.id.img_right_delete);
            TextView tvHint    = (TextView)itemView.findViewById(R.id.head_tv);
            if (j != (size-1)){
                tvHint.setVisibility(View.GONE);
                imgDelete.setVisibility(View.VISIBLE);
                File f = new File(mImagePaths.get(j));
                ImgLoadUtil.displayLocalPictrue(this, R.mipmap.ic_picture, f, imgPhoto);

                imgDelete.setTag(R.id.position_key, j);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int)view.getTag(R.id.position_key);
                        mImagePaths.remove(pos);
                        refreshPhoto();
                    }
                });
            }
            mContainerPhoto.addView(itemView);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CAMERA_CODE && data != null){
            ArrayList<String> tempIamges = (ArrayList<String>)data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            if (tempIamges != null && tempIamges.size() > 0){
                mImagePaths.clear();
                mImagePaths.addAll(tempIamges);
                mImagePaths.add(Constant.IMAGE_ADD_FLAG);
                int size = mImagePaths.size();
                ThreadUtil.runAtMain(new Runnable() {
                    @Override
                    public void run() {
                        refreshPhoto();
                    }
                });

                //File f = new File(tempIamges.get(0));
                //ImgLoadUtil.displayLocalPictrue(this, R.mipmap.ic_picture, f, mImgPhoto);
                LogUtil.d("", "photo size: " + size);
            }

        }
    }

    @OnClick(R.id.btn_commit)
    public void commitRecord(){
        String title = mEtTitle.getText().toString();
        if (TextUtils.isEmpty(title)){
            ToastUtil.show("请输入活动名称");
            return;
        }

        String result = mEtResult.getText().toString();
        if (TextUtils.isEmpty(result)){
            ToastUtil.show("请输入活动成果");
            return;
        }

        String actionTime = mTvTime.getText().toString();
        if (TextUtils.isEmpty(actionTime)){
            ToastUtil.show("请输入活动时间");
            return;
        }

        if (mImagePaths != null && mImagePaths.size() > 1){
            uploadImages();
        }else{
            commit();
        }

    }

    private void commit(){
        String title = mEtTitle.getText().toString();
        if (TextUtils.isEmpty(title.trim())){
            ToastUtil.show("请输入活动名称");
            return;
        }

        String result = mEtResult.getText().toString();
        if (TextUtils.isEmpty(result.trim())){
            ToastUtil.show("请输入活动成果");
            return;
        }

        String actionTime = mTvTime.getText().toString();
        if (TextUtils.isEmpty(actionTime.trim())){
            ToastUtil.show("请输入活动时间");
            return;
        }

        String actionContent = mEtContent.getText().toString();

        ArrayList<String> tempImages = null;
        if (mUploadedImages != null && mUploadedImages.size() > 0){
            tempImages = new ArrayList<String>();
            for (UploadFileBean ufile: mUploadedImages){
                if (ufile != null){
                    tempImages.add(ufile.url);
                }
            }
        }

        StudentModel.getInstance().addPracticeRecord(title, result, actionTime, actionContent, tempImages, new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                ToastUtil.show("添加实践记录成功");
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("添加实践记录失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }


    private void uploadImages(){
        disLoading();
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
                ToastUtil.show("上传图片失败 " +(TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

}
