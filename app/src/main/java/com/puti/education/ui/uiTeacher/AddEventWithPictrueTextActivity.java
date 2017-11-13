package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.UploadFileListAdapter;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/23 0023.
 *
 */

public class AddEventWithPictrueTextActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.sure_btn)
    Button sureBtn;
    @BindView(R.id.pic_rv)
    RecyclerView mRv;
    @BindView(R.id.text_input_tv)
    EditText mInputEdit;

    private ArrayList<String> mImagePaths;
    private UploadFileListAdapter mUploadListAdapter;

    private String mText;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_addevent_with_pic_text;
    }

    @Override
    public void initVariables() {
        mImagePaths = getIntent().getStringArrayListExtra(Key.BEAN);
        mText = getIntent().getStringExtra(Key.RECORD_IMG_TEXT);
    }

    @Override
    public void initViews() {
        mTitleTv.setText("图文");
        mRightTv.setVisibility(View.VISIBLE);
        mRightTv.setText("添加图片");
        mInputEdit.setText(mText);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRv.setLayoutManager(gridLayoutManager);
        mUploadListAdapter = new UploadFileListAdapter(this);
        mRv.setAdapter(mUploadListAdapter);
        mUploadListAdapter.setDataList(mImagePaths);
        mUploadListAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                mImagePaths.remove(position);
                mUploadListAdapter.delete(position);
            }
        });
    }

    //选择图片
    @OnClick(R.id.tv_right)
    public void choosePicClick(){
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(Constant.IMAGE_MAX_NUMBER); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(mImagePaths); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, Constant.REQUEST_CAMERA_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null){
            return;
        }

        if (requestCode == Constant.REQUEST_CAMERA_CODE){
            ArrayList<String> tempIamges = (ArrayList<String>)data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            if (tempIamges != null && tempIamges.size() > 0){
                mImagePaths.clear();
                mImagePaths.addAll(tempIamges);
                mUploadListAdapter.setDataList(mImagePaths);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //确定
    @OnClick(R.id.sure_btn)
    public void sureBtnClick(){
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Key.BEAN,mImagePaths);
        intent.putExtra(Key.RECORD_IMG_TEXT,TextUtils.isEmpty(mInputEdit.getText().toString()) ? "":mInputEdit.getText().toString());
        setResult(Constant.CODE_RESULT_IMG_TEXT,intent);
        finish();
    }

    @Override
    public void loadData() {

    }



    private void commitInfo(){

    }
}
