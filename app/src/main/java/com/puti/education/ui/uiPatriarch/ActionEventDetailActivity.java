package com.puti.education.ui.uiPatriarch;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.PracticeTrain;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.ItemContainer;
import com.puti.education.widget.SpinnerPop;
import com.puti.education.widget.TimeDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/6/2 0015.
 * 家长行为事件查看
 */

public class ActionEventDetailActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.itemcontainer_photo)
    ItemContainer mContainerPhoto;
    @BindView(R.id.itemcontainer_people)
    LinearLayout mContainerPeople;
    @BindView(R.id.tv_starttime)
    TextView mTvStartTime;
    @BindView(R.id.tv_endtime)
    TextView mTvEndTime;
    @BindView(R.id.tv_event_type)
    TextView mTvType;
    @BindView(R.id.editText)
    TextView mTvDesc;

    public static final String ACTION_LEAVE         = "请假";
    public static final String ACTION_COOPERATION   = "协同请求";
    public static final String ACTION_APPLY_PROVE   = "申请与证明";

    private int mID;
    private String mTime, mDesc;
    private int mEventType = -1;
    private ArrayList<String> mImagePaths = new ArrayList<>();
    private List<EventAboutPeople> mHeadList = null;


    @Override
    public int getLayoutResourceId() {
        return R.layout.action_event_detail_layout;
    }

    @Override
    public void initVariables() {
        mImagePaths.add(Constant.IMAGE_ADD_FLAG);
        mHeadList = new ArrayList<>();
        setPhotoMore();

        mTypeList.add(ACTION_LEAVE);
        mTypeList.add(ACTION_COOPERATION);
        mTypeList.add(ACTION_APPLY_PROVE);

        mID = this.getIntent().getIntExtra("id", -1);

    }

    @Override
    public void initViews() {
        mUiTitle.setText("记录");
    }



    @Override
    public void loadData() {
        getActionEventDetail();
    }


    private void refreshPhoto(){
        setPhotoMore();
    }

    private void setPhotoMore(){
        if (mImagePaths == null){
            return;
        }
        View itemView;
        ViewGroup.LayoutParams params;
        mContainerPhoto.removeAllViews();
        int size = mImagePaths.size();
        for (int j= 0; j<size; j++){
            itemView = LayoutInflater.from(this).inflate(R.layout.item_container, null);
            params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 99), DisPlayUtil.dip2px(this, 99));
            itemView.setLayoutParams(params);

            LinearLayout layoutBg = (LinearLayout)itemView.findViewById(R.id.layout_parent_container);
            ImageView imgPhoto = (ImageView)itemView.findViewById(R.id.img_photo);

            TextView tvHint    = (TextView)itemView.findViewById(R.id.head_tv);

            tvHint.setVisibility(View.GONE);

            ImgLoadUtil.displayPic(R.mipmap.ic_picture, mImagePaths.get(j), imgPhoto);

            mContainerPhoto.addView(itemView);
        }

    }


    private void getActionEventDetail(){
        if (mID <= 0){
            ToastUtil.show("详情ID错误 " + mID);
            return;
        }
        PatriarchModel.getInstance().getActionEventDetail(mID, new BaseListener(PracticeTrain.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                if (infoObj != null){
                    PracticeTrain detail = (PracticeTrain)infoObj;
                    mTvStartTime.setText(detail.startTime);
                    mTvEndTime.setText(detail.endTime);
                    if (detail.type == 30){
                        mTvType.setText(ACTION_LEAVE);
                    }else if (detail.type == 31){
                        mTvType.setText(ACTION_COOPERATION);
                    }else if (detail.type == 32){
                        mTvType.setText(ACTION_APPLY_PROVE);
                    }
                    mTvDesc.setText(detail.content);
                    mImagePaths = (ArrayList<String>)detail.photo;
                    refreshPhoto();
                    mHeadList = detail.childs;
                    refreshHeadList();
                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
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
                if (type.equals(ACTION_LEAVE)){
                    mEventType = 30;
                }else if (type.equals(ACTION_COOPERATION)){
                    mEventType = 31;
                }else if (type.equals(ACTION_APPLY_PROVE)){
                    mEventType = 32;
                }

            }
        });

    }

    private void refreshHeadList(){
        setHeadMore();
    }

    private void setHeadMore(){
        if (mHeadList == null){
            return;
        }
        View subView = null;
        LinearLayout subLayout = null;
        mContainerPeople.removeAllViews();
        int currentRow = -1;
        int size = mHeadList.size();
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
        itemView = LayoutInflater.from(this).inflate(R.layout.item_subitem_avatar, null);
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 65), DisPlayUtil.dip2px(this, 99));
        itemView.setLayoutParams(params);

        ImageView avatar = (ImageView)itemView.findViewById(R.id.img_avatar);
        TextView nameTv = (TextView)itemView.findViewById(R.id.tv_name);

        avatar.setImageResource(R.mipmap.add_people_btn_bg);
        itemView.setTag(R.id.onclick_parent_view, parentView);
        if (index >= 0 && index < mHeadList.size()){
            EventAboutPeople stu = mHeadList.get(index);
            nameTv.setText(stu.name);
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default, stu.avatar, avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout subLayout = (LinearLayout)view.getTag(R.id.onclick_parent_view);
                    //subLayout.removeView(view); //删除操作
                }
            });

        }
        return itemView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CAMERA_CODE && data != null){
            ArrayList<String> tempIamges = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            if (tempIamges != null && tempIamges.size() > 0){
                mImagePaths.clear();
                mImagePaths.addAll(tempIamges);
                mImagePaths.add(Constant.IMAGE_ADD_FLAG);
                int size = mImagePaths.size();
                refreshPhoto();
                LogUtil.d("", "photo size: " + size);
            }

        }
    }


}
