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
import com.puti.education.bean.Practice;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.PhotoReviewActivity;
import com.puti.education.ui.uiPatriarch.TrainPracticeDetailActivity;
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
 * Created by icebery on 2017/5/27 0019.
 */

public class PracticeDetailActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_practice_name)
    TextView mEtTitle;
    @BindView(R.id.tv_result)
    TextView mEtResult;
    @BindView(R.id.et_content)
    TextView mEtContent;
    @BindView(R.id.itemcontainer_photo)
    ItemContainer mContainerPhoto;

    private String mId;
    private ArrayList<String> mImagePaths;

    @Override
    public int getLayoutResourceId() {
        return R.layout.practice_detail_layout;
    }

    @Override
    public void initVariables() {
        mId = this.getIntent().getStringExtra("id");
        mImagePaths = new ArrayList<>();
    }

    @Override
    public void initViews() {
        mTitleTv.setText("实践记录详情");
    }

    @Override
    public void loadData() {
        getPracticeDetail();
    }

    private void getPracticeDetail(){
        if (TextUtils.isEmpty(mId)){
            ToastUtil.show("实践详情ID为空");
            return;
        }
        StudentModel.getInstance().getPracticeDetail(mId, new BaseListener(Practice.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (infoObj != null){
                    Practice tempObj = (Practice)infoObj;
                    mTvTime.setText(TextUtils.isEmpty(tempObj.time)?"":tempObj.time);
                    mEtTitle.setText(TextUtils.isEmpty(tempObj.title)?"":tempObj.title);
                    mEtResult.setText(TextUtils.isEmpty(tempObj.result)?"":tempObj.result);
                    mEtContent.setText(TextUtils.isEmpty(tempObj.content)?"":tempObj.content);
                    mImagePaths = (ArrayList<String>) tempObj.photo;
                    refreshPhoto();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("获取详情出错");
            }
        });
    }


    @OnClick(R.id.back_frame)
    public void finishActivity(){
        this.finish();
    }


    private void refreshPhoto(){
        setPhotoMore();
    }

    private void setPhotoMore(){
        if (mImagePaths == null || mImagePaths.size() <= 0){
            return;
        }
        mContainerPhoto.removeAllViews();
        View itemView;
        ViewGroup.LayoutParams params;
        int size = mImagePaths.size();
        for (int j= 0; j<size; j++){
            itemView = LayoutInflater.from(this).inflate(R.layout.item_container, null);
            params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 99), DisPlayUtil.dip2px(this, 99));
            itemView.setLayoutParams(params);
            itemView.setTag(j);

            ImageView imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
            TextView tvHint    = (TextView)itemView.findViewById(R.id.head_tv);

            tvHint.setVisibility(View.GONE);
            ImgLoadUtil.displayPic(R.mipmap.ic_picture,mImagePaths.get(j), imgPhoto);

            imgPhoto.setTag(R.id.position_key, j);
            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int)view.getTag(R.id.position_key);
                    Intent intent = new Intent();
                    intent.putExtra("type", 1);
                    intent.putExtra("url", mImagePaths.get(pos));
                    intent.setClass(PracticeDetailActivity.this, PhotoReviewActivity.class);
                    startActivity(intent);

                }
            });

            mContainerPhoto.addView(itemView);
        }

    }



}
