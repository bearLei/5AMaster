package com.puti.education.ui.uiPatriarch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.Practice;
import com.puti.education.bean.PracticeTrain;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.ChooseHeadListActivity;
import com.puti.education.ui.uiCommon.PhotoReviewActivity;
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
import com.puti.education.widget.TimeDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 * 培训实践记录详情
 */

public class TrainPracticeDetailActivity extends BaseActivity {


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
    @BindView(R.id.btn_commit)
    Button mBtnCommit;


    public static final String PRACTICE_RECORD_NAME = "实践记录";
    public static final String TRAIN_RECORD_NAME    = "培训记录";

    public CommonDropView mCommonDropView;
    private String mStudentId;
    private String mUid;
    private int mTrainType = 1; //
    private ArrayList<String> mImagePaths = new ArrayList<>();
    private ArrayList<UploadFileBean> mUploadedImages;
    private Practice mTrainDetail;


    @Override
    public int getLayoutResourceId() {
        return R.layout.train_practice_add_layout;
    }

    @Override
    public void initVariables() {
        setPhotoMoreLayout();

        mTypeList.add(PRACTICE_RECORD_NAME);
        mTypeList.add(TRAIN_RECORD_NAME);

        mUid = this.getIntent().getStringExtra("uid");
        mTrainDetail = (Practice)this.getIntent().getSerializableExtra(Key.BEAN);

    }

    @Override
    public void initViews() {
        mUiTitle.setText("培训实践详情");
        mBtnCommit.setVisibility(View.GONE);

        mEtTheme.clearFocus();
        mEtTheme.setFocusable(false);

        mTvType.setClickable(false);
        mEtContent.clearFocus();
        mEtContent.setFocusable(false);
        mTvTime.setClickable(false);
        mEtAddress.clearFocus();
        mEtAddress.setFocusable(false);
        mEtResult.clearFocus();
        mEtResult.setFocusable(false);

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
        if (!TextUtils.isEmpty(mUid)){
            getTrainDetail();
        }else if (mTrainDetail != null){
            updateUI(mTrainDetail, Constant.ROLE_TEACHER);
        }

    }

    public void chooseType(){
        List<String> dataList = new ArrayList<>();
        dataList.add("培训");
        dataList.add("实践");
        if (mCommonDropView == null){
            mCommonDropView = new CommonDropView(TrainPracticeDetailActivity.this, mTvType, dataList);
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
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 90), DisPlayUtil.dip2px(this, 90));
        itemView.setLayoutParams(params);
        itemView.setTag(index);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = (int)view.getTag();
                PhotoPreviewIntent intent = new PhotoPreviewIntent(TrainPracticeDetailActivity.this);
                intent.setCurrentItem(index);
                intent.setPhotoPaths(mImagePaths);
                startActivityForResult(intent, Constant.REQUEST_PREVIEW_CODE);
            }
        });

        LinearLayout layoutBg = (LinearLayout)itemView.findViewById(R.id.layout_parent_container);
        ImageView imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
        ImageView imgDelete= (ImageView)itemView.findViewById(R.id.img_right_delete);
        TextView tvHint    = (TextView)itemView.findViewById(R.id.head_tv);
        if (index >= 0 && index < (mImagePaths.size())){
            tvHint.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
            layoutBg.setBackgroundResource(R.color.white);
            String imageurl = mImagePaths.get(index);
            ImgLoadUtil.displayPic(R.mipmap.ic_picture, imageurl, imgPhoto);
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



    private void uploadImages(){
        disLoading("正在上传图片");
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

    private void getTrainDetail(){
        int role = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);
        if (role == Constant.ROLE_PARENTS){
            getParentTrain();
        }else if (role == Constant.ROLE_STUDENT){
            getStudentTrain();
        }
    }

    private void getParentTrain(){

        PatriarchModel.getInstance().getPracticeTrainDetail(mUid,
                new BaseListener(Practice.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        Practice tempObj = (Practice)infoObj;
                        if (tempObj != null){
                            updateUI(tempObj, Constant.ROLE_PARENTS);
                        }

                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        ToastUtil.show("获取培训实践失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                    }
                });
    }

    private void getStudentTrain(){

        StudentModel.getInstance().getPracticeDetail(mUid,
                new BaseListener(Practice.class){

                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        Practice tempObj = (Practice)infoObj;
                        if (tempObj != null){
                            updateUI(tempObj, Constant.ROLE_STUDENT);
                        }

                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        ToastUtil.show("获取培训实践失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
                    }
                });
    }

    private void updateUI(Practice obj, int type){
        if (type == Constant.ROLE_TEACHER){
            mEtTheme.setText(obj.title);
            mEtContent.setText(obj.content);
        }else{
            mEtTheme.setText(obj.name);
            mEtContent.setText(obj.desc);
        }

        mTvType.setText(obj.typename);
        mTvTime.setText(obj.time);
        mEtAddress.setText(obj.address);
        mEtResult.setText(obj.result);
        mImagePaths = (ArrayList<String>) obj.photo;
        refreshPhoto();
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


}
