package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.QuestionnaireDetailAdapter;
import com.puti.education.adapter.QuestionnaireEtDetailAdapter;
import com.puti.education.bean.Question;
import com.puti.education.bean.Questionnaire;
import com.puti.education.bean.ResponseBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiStudent.PracticeAddActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ThreadUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.ItemContainer;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 * 异常事件问卷
 */

public class QtExceptionEventDetailActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.recycler_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_right)
    TextView mTvCommit;


    private String mId = null;
    private Questionnaire mQtDetail;
    private View mItemView;
    private ItemContainer mContainer;
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> mImagePaths = new ArrayList<>();
    private ArrayList<Question> mQtExceptions = new ArrayList<>();

    private QuestionnaireEtDetailAdapter mDetailAdapter;

    @Override
    public int getLayoutResourceId() {
        return R.layout.qt_exception_detail_layout;
    }

    @Override
    public void initVariables() {
        mImagePaths.add("000000");
        int tid = getIntent().getIntExtra("id", -1);

        if (TextUtils.isEmpty(mId)){
            ToastUtil.show("UID为空");
            return;
        }
        //setMore();
    }

    @Override
    public void initViews() {
        mUiTitle.setText("问卷详情");
        mTvCommit.setVisibility(View.VISIBLE);

        mDetailAdapter = new QuestionnaireEtDetailAdapter(this);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDetailAdapter);
        mDetailAdapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        mItemView = LayoutInflater.from(this).inflate(R.layout.item_qt_photo_detail,null);
        mContainer = (ItemContainer)mItemView.findViewById(R.id.itemcontainer);
        setPhotoMore();
        mDetailAdapter.addFooterView(mItemView);
    }

    @Override
    public void loadData() {
        getQtDetail(mId);
    }

    private void refreshPhoto(){
        setPhotoMore();
    }

    private void setPhotoMore(){
        mContainer.removeAllViews();
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
                    ToastUtil.show("这是第" +(index) + "个控件！！");
                    String urlvalue = mImagePaths.get(index);
                    if (urlvalue.equals(Constant.IMAGE_ADD_FLAG)){
                        int size = mImagePaths.size();
                        int limitNum = (Constant.IMAGE_MAX_NUMBER-size+1);
                        if (limitNum == 0){
                            return;
                        }
                        PhotoPickerIntent intent = new PhotoPickerIntent(QtExceptionEventDetailActivity.this);
                        intent.setSelectModel(SelectModel.MULTI);
                        intent.setShowCarema(true); // 是否显示拍照
                        intent.setMaxTotal(limitNum); // 最多选择照片数量，默认为9
                        intent.setSelectedPaths(mImagePaths); // 已选中的照片地址， 用于回显选中状态
                        startActivityForResult(intent, Constant.REQUEST_CAMERA_CODE);
                    }else{
                        PhotoPreviewIntent intent = new PhotoPreviewIntent(QtExceptionEventDetailActivity.this);
                        intent.setCurrentItem(index);
                        intent.setPhotoPaths(mImagePaths);
                        startActivityForResult(intent, Constant.REQUEST_PREVIEW_CODE);
                    }
                }

            });

            ImageView imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
            TextView tvHint    = (TextView)itemView.findViewById(R.id.head_tv);
            if (j != (size-1)){
                tvHint.setVisibility(View.GONE);
                File f = new File(mImagePaths.get(j));
                ImgLoadUtil.displayLocalPictrue(this, R.mipmap.ic_picture, f, imgPhoto);
            }
            mContainer.addView(itemView);
        }

    }

    private void getQtDetail(String uid){
        CommonModel.getInstance().getQuestionnaireDetail(uid, new BaseListener(Questionnaire.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (infoObj != null){
                    mQtDetail = (Questionnaire) infoObj;
                    mTvTitle.setText(mQtDetail.title);
                    mTvContent.setText(mQtDetail.desc);
                    mDetailAdapter.setDataList(mQtDetail.items);
                }
            }
        });
    }

    @OnClick(R.id.tv_right)
    public void commitQt(){
        CommonModel.getInstance().questionnaireCommit(mId, mQtDetail.items, null, new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                if (infoObj != null){
                    ResponseBean respon = (ResponseBean) infoObj;
                    Intent intent = new Intent();
                    intent.putExtra("result", respon.result);
                    intent.setClass(QtExceptionEventDetailActivity.this, QuestionnaireResult.class);
                    startActivity(intent);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("提交问题失败");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CAMERA_CODE && data != null){
            ArrayList<String> tempIamges = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
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

                LogUtil.d("", "photo size: " + size);
            }
        }
    }


}
