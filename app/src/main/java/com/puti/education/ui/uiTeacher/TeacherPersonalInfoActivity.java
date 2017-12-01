package com.puti.education.ui.uiTeacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.puti.education.bean.ClassInfo;
import com.puti.education.bean.EditTeacherInofResponse;
import com.puti.education.bean.TeacherPersonInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.R;
import com.puti.education.ui.uiStudent.StudentInofActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/4/20 0020.
 *
 * 教师端 个人信息 查看，编辑
 */

public class TeacherPersonalInfoActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.right_img)
    ImageView mOperImg;
    @BindView(R.id.info_commint_btn)
    Button mCommitBtn;
    @BindView(R.id.frame_img)
    FrameLayout rightFrame;

    @BindView(R.id.personnal_info_teacherid_tv)
    EditText mTeacherNumEdit;
    @BindView(R.id.personnal_info_school_tv)
    EditText mSchoolEdit;
    @BindView(R.id.personnal_info_department_tv)
    EditText mDeparmentEdit;
    @BindView(R.id.personnal_info_professional_tv)
    EditText mProfessionalEdit;
    @BindView(R.id.personnal_info_classname_tv)
    EditText mClassEdit;
    @BindView(R.id.personnal_info_workage_tv)
    EditText mWorkAgeEdit;
    @BindView(R.id.personnal_info_worktime_tv)
    EditText mStartWorkTimeEdit;
    @BindView(R.id.personnal_info_name_tv)
    EditText mNamEdit;
    @BindView(R.id.personnal_info_id_tv)
    EditText mIdCardEdit;
    @BindView(R.id.personnal_info_sex_tv)
    TextView mSexTv;
    @BindView(R.id.personnal_info_national_tv)
    EditText mNationalEdit;
    @BindView(R.id.personnal_info_connact_tv)
    EditText mConnactEdit;
    @BindView(R.id.personnal_info_birth_date_tv)
    EditText mBirthEdit;
    @BindView(R.id.personnal_info_age_tv)
    EditText mAgeEdit;
    @BindView(R.id.personnal_info_address_tv)
    TextView mAddressEdit;
    @BindView(R.id.personnal_info_contry_tv)
    EditText mcontryEdit;
    @BindView(R.id.personnal_info_is_married_tv)
    TextView mMarriedTv;
    private boolean mIsEdit;
    private TeacherPersonInfo mTeacherInfo = null;
    private List<String> mSexList = new ArrayList<>();
    private List<String> mMarryList = new ArrayList<>();
    private List<String> mAddressList = new ArrayList<>();
    private CommonDropView mDropViewSex, mDropViewMarry,mDropViewAddress;

    private void setWidgetListEnable(boolean enable){
        if (!enable) {
            mTeacherNumEdit.setEnabled(enable);
            mSchoolEdit.setEnabled(enable);
            mDeparmentEdit.setEnabled(enable);
            mProfessionalEdit.setEnabled(enable);
            mClassEdit.setEnabled(enable);
            mWorkAgeEdit.setEnabled(enable);
            mStartWorkTimeEdit.setEnabled(enable);
            mAgeEdit.setEnabled(enable);
        }

        mNamEdit.setEnabled(enable);
        mSexTv.setEnabled(enable);
        mIdCardEdit.setEnabled(enable);
        mBirthEdit.setEnabled(enable);
        mConnactEdit.setEnabled(enable);
        mAddressEdit.setEnabled(enable);
        mcontryEdit.setEnabled(enable);
        mMarriedTv.setEnabled(enable);
        mNationalEdit.setEnabled(enable);
    }

    private void setWidgetValue(TeacherPersonInfo teacherPersonInfo){
        mTeacherNumEdit.setText(teacherPersonInfo.code);
        mSchoolEdit.setText(teacherPersonInfo.school);
        mProfessionalEdit.setText(teacherPersonInfo.professional);
        if (teacherPersonInfo.classList != null && teacherPersonInfo.classList.size() > 0){
            String classlist = "";
            for (ClassInfo claainfo: teacherPersonInfo.classList){
                classlist += claainfo.name +" ";
            }
             mClassEdit.setText(classlist);
        }

        mWorkAgeEdit.setText(teacherPersonInfo.workingAge);
        mStartWorkTimeEdit.setText(teacherPersonInfo.startYear);
        mNamEdit.setText(teacherPersonInfo.name);
        mIdCardEdit.setText(teacherPersonInfo.idcard);
        mSexTv.setText(teacherPersonInfo.sex);
        mBirthEdit.setText(teacherPersonInfo.birthday);
        mConnactEdit.setText(teacherPersonInfo.phone);
        //mAgeEdit.setText(teacherPersonInfo.age+"");
        mAddressEdit.setText(teacherPersonInfo.nature);
        mcontryEdit.setText(teacherPersonInfo.nationality);
        mMarriedTv.setText(teacherPersonInfo.maritalStatus);
        mNationalEdit.setText(teacherPersonInfo.national);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_teacher_personal_info;
    }

    @Override
    public void initVariables() {

        setWidgetListEnable(false);
    }

    @Override
    public void initViews() {
        mTitleTv.setText("个人资料");
        mOperImg.setImageResource(R.mipmap.ic_edit);
        if (!mIsEdit){
            mCommitBtn.setVisibility(View.GONE);
        }

        mSexList.clear();
        mSexList.add("男");
        mSexList.add("女");

        mSexTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEdit){
                    return;
                }
                if (mDropViewSex == null){
                    mDropViewSex = new CommonDropView(TeacherPersonalInfoActivity.this, mSexTv, mSexList);
                    mDropViewSex.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String sexname = mSexList.get(position);
                            mSexTv.setText(sexname);
                            mDropViewSex.dismiss();
                        }
                    });
                }
                mDropViewSex.showAsDropDown(mSexTv);
            }
        });

        mMarryList.clear();
        mMarryList.add("未婚");
        mMarryList.add("已婚");
        mMarriedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEdit){
                    return;
                }
                if (mDropViewMarry == null){
                    mDropViewMarry = new CommonDropView(TeacherPersonalInfoActivity.this, mMarriedTv, mMarryList);
                    mDropViewMarry.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String sexname = mMarryList.get(position);
                            mMarriedTv.setText(sexname);
                            mDropViewMarry.dismiss();
                        }
                    });
                }
                mDropViewMarry.showAsDropDown(mMarriedTv);
            }
        });

        mAddressList.clear();
        mAddressList.add("农村");
        mAddressList.add("城镇");
        mAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEdit){
                    return;
                }
                if (mDropViewAddress == null){
                    mDropViewAddress = new CommonDropView(TeacherPersonalInfoActivity.this, mAddressEdit, mAddressList);
                    mDropViewAddress.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String sexname = mAddressList.get(position);
                            mAddressEdit.setText(sexname);
                            mDropViewAddress.dismiss();
                        }
                    });
                }
                mDropViewAddress.showAsDropDown(mAddressEdit);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadData() {

        getTeacherInfo();

    }

    //编辑
    @OnClick(R.id.frame_img)
    public void operateClick(View view){
        mIsEdit = true;
        setWidgetListEnable(true);
        rightFrame.setVisibility(View.GONE);
        mCommitBtn.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.info_commint_btn)
    public void commitInfoClick(){
        modifyTeacherInfo();
    }


    private void getTeacherInfo(){
        disLoading();
        TeacherModel.getInstance().getTeacherInfo(new BaseListener(TeacherPersonInfo.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    TeacherPersonInfo teacherPersonInfo = (TeacherPersonInfo) infoObj;
                    setWidgetValue(teacherPersonInfo);
                }else{
                    ToastUtil.show(Constant.REQUEST_FAILED_STR);
            }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    private String createParamStr(){

        JSONObject jsonObject = new JSONObject();
        if (!isEmptyStr(mNamEdit)){
            jsonObject.put("name",mNamEdit.getText().toString());
        }else{
            jsonObject.put("name", "");
        }

        String strSex = mSexTv.getText().toString();
        if (TextUtils.isEmpty(strSex)){
            strSex = "";
        }else if (strSex.equals("男")){
            strSex = "M";
        }else if (strSex.equals("女")){
            strSex = "F";
        }
        jsonObject.put("sex",strSex);

        if (!isEmptyStr(mIdCardEdit)){
            jsonObject.put("idcard",mIdCardEdit.getText().toString());
        }else{
            jsonObject.put("idcard", "");
        }

        if (!isEmptyStr(mNationalEdit)){
            jsonObject.put("nation",mNationalEdit.getText().toString());
        }else{
            jsonObject.put("nation", "");
        }

        if (!isEmptyStr(mConnactEdit)){
            jsonObject.put("mobile",mConnactEdit.getText().toString());
        }else{
            jsonObject.put("mobile", "");
        }

        if (!isEmptyStr(mBirthEdit)){
            jsonObject.put("birthday",mBirthEdit.getText().toString());
        }else{
            jsonObject.put("birthday", "");
        }

        if (!isEmptyStr(mcontryEdit)){
            jsonObject.put("stateless",mcontryEdit.getText().toString());
        }else{
            jsonObject.put("stateless", "");
        }


        String strMarry = mMarriedTv.getText().toString();
        if (strMarry != null && strMarry.equals("已婚")){
            jsonObject.put("maritalStatus",true);
        }else {
            jsonObject.put("maritalStatus",false);
        }

        if (!isEmptyStr(mAddressEdit)){
            jsonObject.put("registerType",mAddressEdit.getText().toString());
        }else{
            jsonObject.put("registerType","");
        }

        return jsonObject.toString();

    }

    private boolean isEmptyStr(TextView editText){
        if (TextUtils.isEmpty(editText.getText().toString())){
            return true;
        }else{
            return false;
        }
    }

    private void modifyTeacherInfo(){

        String paramsStr = createParamStr();
        if (paramsStr == null){
            return;
        }

        disLoading();
        TeacherModel.getInstance().modifyTeacherInfo(paramsStr,new BaseListener(EditTeacherInofResponse.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                ToastUtil.show("编辑成功");
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });

    }


    @OnClick(R.id.back_frame)
    public void finishActivityClick(){
        finish();
    }
}
