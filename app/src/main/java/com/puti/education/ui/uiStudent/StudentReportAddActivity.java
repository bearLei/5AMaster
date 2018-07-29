package com.puti.education.ui.uiStudent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventType;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.Teacher;
import com.puti.education.bean.UploadFileBean;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonListActivityNew;
import com.puti.education.ui.uiTeacher.chooseperson.ChoosePersonParameter;
import com.puti.education.ui.uiTeacher.chooseperson.event.ChooseCompleteEvent;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.DropWithBackView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/17 0017.
 * 学校举报模块
 */

public class StudentReportAddActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.back_frame)
    FrameLayout mBackLayout;
    @BindView(R.id.itemcontainer_involver)
    LinearLayout mContainerInvolver;
    @BindView(R.id.itemcontainer_people)
    LinearLayout mContainerPeople;
    @BindView(R.id.itemcontainer_photo)
    LinearLayout mContainerPhoto;
    @BindView(R.id.editText)
    EditText mEtDesc;

    private String mStudentId;
    private String mDesc;

    private List<EventAboutPeople> mKnownPeopleList = null;
    private List<EventAboutPeople> mReportPeopleList = null;

    private ArrayList<String> mImagePaths = new ArrayList<>();
    private ArrayList<UploadFileBean> mUploadedImages;
    private ArrayList<String> mTeacherIds = new ArrayList<String>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.student_report_event_layout;
    }

    @Override
    public void initVariables() {
        mStudentId = ConfigUtil.getInstance(this).get(Constant.KEY_USER_ID, "");
        mReportPeopleList = new ArrayList<>();
        mKnownPeopleList  = new ArrayList<>();
        mEventTypeList    = new ArrayList<>();

        refreshPhoto();
        setInvolverMore();
        setKnownMore();

    }

    @Override
    public void initViews() {
        mUiTitle.setText("匿名举报");
    }


    private void resetWidget(){
        mTeacherIds.clear();
        mImagePaths.clear();
        mKnownPeopleList.clear();
        mReportPeopleList.clear();

        mEtDesc.setText("");

        refreshPhoto();
        setInvolverMore();
        setKnownMore();
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
            subView = addPhotoMore(j, subLayout);
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

    private View addPhotoMore(int index, LinearLayout layout){
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
                if (index < 0 || index >= mImagePaths.size()){
                    PhotoPickerIntent intent = new PhotoPickerIntent(StudentReportAddActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(Constant.IMAGE_MAX_NUMBER); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(mImagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, Constant.REQUEST_CAMERA_CODE);
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(StudentReportAddActivity.this);
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


    @Override
    public void loadData() {

    }

    private void refreshKnownPeople(){
        setKnownMore();
    }

    private void refreshReportPeople(){
        setInvolverMore();
    }

    private void setInvolverMore(){
        View subView = null;
        LinearLayout subLayout = null;
        mContainerInvolver.removeAllViews();
        int currentRow = -1;
        int size = mReportPeopleList.size();
        if (size <= 0){
            size++; //+1 用于显示添加图标
        }

        for (int j= 0; j<size; j++){
            int row  = (j/4);
            subView = addAvatar(j, subLayout);
            if (currentRow == row){
                if (subLayout != null){
                    subLayout.addView(subView);
                }
            }else{
                currentRow = row;
                subLayout = addSubLayout();
                subLayout.addView(subView);
                mContainerInvolver.addView(subLayout);
            }
        }
    }

    private void setKnownMore(){
        View subView = null;
        LinearLayout subLayout = null;
        mContainerPeople.removeAllViews();
        int currentRow = -1;
        int size = mKnownPeopleList.size();
        if (size <= 0){
            size++; //+1 用于显示添加图标
        }

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

    private View addAvatar(final int index, View parentView){

        View itemView;
        ViewGroup.LayoutParams params;
        itemView = LayoutInflater.from(this).inflate(R.layout.item_subitem_avatar, null);
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 65), DisPlayUtil.dip2px(this, 99));
        itemView.setLayoutParams(params);
        ImageView imgDel = (ImageView)itemView.findViewById(R.id.iv_del);
        ImageView avatar = (ImageView)itemView.findViewById(R.id.img_avatar);
        TextView nameTv = (TextView)itemView.findViewById(R.id.tv_name);

        avatar.setImageResource(R.mipmap.add_people_btn_bg);
        itemView.setTag(R.id.onclick_parent_view, parentView);
        if (index >= 0 && index < mReportPeopleList.size()){
            imgDel.setVisibility(View.VISIBLE);
            EventAboutPeople stu = mReportPeopleList.get(index);
            nameTv.setText(stu.name);
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default, stu.avatar, avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout subLayout = (LinearLayout)view.getTag(R.id.onclick_parent_view);
                    //subLayout.removeView(view); //删除操作
                }
            });
            imgDel.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mReportPeopleList.remove(index);
                    refreshReportPeople();
                }
            });
        }else{
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseStudentTeacher(ChoosePersonParameter.REFER_STU_REPORT_ADD_STU);
                }
            });
        }
        return itemView;
    }

    private View addKnownAvatar(final int index, View parentView){

        View itemView;
        ViewGroup.LayoutParams params;
        itemView = LayoutInflater.from(this).inflate(R.layout.item_subitem_avatar, null);
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(this, 65), DisPlayUtil.dip2px(this, 99));
        itemView.setLayoutParams(params);

        ImageView imgDel = (ImageView)itemView.findViewById(R.id.iv_del);
        ImageView avatar = (ImageView)itemView.findViewById(R.id.img_avatar);
        TextView nameTv = (TextView)itemView.findViewById(R.id.tv_name);

        avatar.setImageResource(R.mipmap.add_people_btn_bg);
        itemView.setTag(R.id.onclick_parent_view, parentView);
        if (index >= 0 && index < mKnownPeopleList.size()){
            imgDel.setVisibility(View.VISIBLE);
            EventAboutPeople stu = mKnownPeopleList.get(index);
            nameTv.setText(stu.name);
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default, stu.avatar, avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout subLayout = (LinearLayout)view.getTag(R.id.onclick_parent_view);
                    //subLayout.removeView(view); //删除操作
                }
            });
            imgDel.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mKnownPeopleList.remove(index);
                    refreshKnownPeople();
                }
            });
        }else{
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseStudentTeacher(ChoosePersonParameter.REFER_STU_REPORT_ADD_TEA);
                }
            });
        }
        return itemView;
    }

    @OnClick(R.id.btn_commit)
    public void commitRecord(){
        if (mReportPeopleList.size() <= 0){
            ToastUtil.show("请选择被举报的人");
            return;
        }

        mDesc = mEtDesc.getText().toString();
        if (TextUtils.isEmpty(mDesc)){
            //
        }

        if (mImagePaths != null && mImagePaths.size() > 0){
            uploadImages();
        }else{
            commit();
        }

    }

    private void uploadImages(){
        disLoading("正在提交...", false);
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
                ToastUtil.show("上传资料图片失败 "  + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    private void commit(){
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

        ArrayList<String> tempKnownStudent = null;
        ArrayList<String> tempKnownTeacher = null;
        if (mKnownPeopleList != null && mKnownPeopleList.size() > 0){
            tempKnownStudent = new ArrayList<>();
            tempKnownTeacher = new ArrayList<>();
            for (EventAboutPeople stu: mKnownPeopleList){
                if (stu != null && stu.type == 1){
                    tempKnownTeacher.add(stu.uid);
                }else if (stu != null && stu.type == 2){
                    tempKnownStudent.add(stu.uid);
                }
            }
        }

        StudentModel.getInstance().reportEvent(mReportPeopleList, mEventTyPeId, mDesc, tempImages,
                tempKnownStudent, tempKnownTeacher, new BaseListener(ResponseBean.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                ToastUtil.show("举报成功");
                resetWidget();
                Intent intent = new Intent();
                intent.setAction(Constant.BROADCAST_REFRESH_REPORT);
                StudentReportAddActivity.this.sendBroadcast(intent);
                finish();

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show("举报失败 "  + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }



    private List<EventType> mEventTypeList = null;
    private DropWithBackView mEventTypeListPop;
    private int mEventTyPeId = -1;

    //事件类型
    private void getEventTypeList(){

        if (mEventTypeList != null && mEventTypeList.size() > 0){
            //showEventTypePop();
            return;
        }

        disLoading();

        CommonModel.getInstance().getEventTypeList(new BaseListener(EventType.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();

                if (listObj != null){
                    List<EventType> typeList = (List<EventType>) listObj;
                    mEventTypeList=typeList;
                    //showEventTypePop();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }


    private void chooseStudentTeacher(int type){
        Intent intent = new Intent(this,ChoosePersonListActivityNew.class);
        intent.putExtra(ChoosePersonParameter.REFER,type);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_HEADTEACHER && data != null){
            Bundle bundle = data.getExtras();
            Teacher headTeacher = (Teacher)bundle.getSerializable(Constant.KEY_TEACHER);

            if (headTeacher != null && !TextUtils.isEmpty(headTeacher.uid)){
                boolean isFound = false;
                for (String tempId: mTeacherIds){
                    if (tempId.equals(headTeacher.uid)){
                        isFound = true;
                    }
                }

                if (!isFound){
                    mTeacherIds.add(headTeacher.uid);
                    mImagePaths.add(headTeacher.avatar);
                }

            }
        }else if (requestCode == Constant.REQUEST_CODE_BOTH_INVOLOVER && data != null){
            EventAboutPeople people = (EventAboutPeople)data.getSerializableExtra(Key.BEAN);
            boolean isFound = false;
            if (mKnownPeopleList != null && mKnownPeopleList.size() > 0){
                for (EventAboutPeople people1: mKnownPeopleList){
                    if (people1 != null && people1.uid.equals(people.uid)&& people1.type == people.type){
                        isFound = true;
                    }
                }
            }

            if (!isFound){
                mKnownPeopleList.add(people);
                refreshKnownPeople();
            }
        }else if (requestCode == Constant.REQUEST_CAMERA_CODE && data != null){
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

    //接收选择学生或者选择老师后的事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(ChooseCompleteEvent event){
        if (event != null){
            ArrayList<EventAboutPeople> involvePeoples = event.getmList();
            if (involvePeoples == null || involvePeoples.size() <= 0){
                return;
            }
            boolean isFound = false;
            if (mReportPeopleList != null && mReportPeopleList.size() > 0){
                for (EventAboutPeople people1: mReportPeopleList){
                    if (people1 != null && people1.uid.equals(involvePeoples.get(0).uid) && people1.type == involvePeoples.get(0).type){
                        isFound = true;
                    }
                }
            }
            if (!isFound){
                mReportPeopleList.add(involvePeoples.get(0));
                refreshReportPeople();
            }
        }
    }
}
