package com.puti.education.ui.uiStudent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.Text;
import com.puti.education.R;
import com.puti.education.bean.StudentResponseInfo;
import com.puti.education.event.UpdateUserInfoEvent;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.TeacherEventListFragment;
import com.puti.education.util.Constant;
import com.puti.education.util.TimeChooseUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.citychoose.CityChooseDialog;
import com.puti.education.widget.CommonDropView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/13 0013.
 *
 *  学生信息
 */

public class StudentInofActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.right_img)
    ImageView mOperImg;
    @BindView(R.id.info_commint_btn)
    Button mCommitBtn;
    @BindView(R.id.frame_img)
    FrameLayout rightFrame;

    //学籍信息
    @BindView(R.id.personnal_info_stu_num_tv)
    EditText mStuNumTv;
    @BindView(R.id.personnal_info_stu_school_tv)
    EditText mStuSchoolTv;
    @BindView(R.id.personnal_info_stu_department_tv)
    EditText mStuDepartmentTv;
    @BindView(R.id.personnal_info_stu_professional_tv)
    EditText mStuProTv;
    @BindView(R.id.personnal_info_classname_tv)
    EditText mStuClassTv;

    //个人信息
    @BindView(R.id.personnal_info_stu_name_tv)
    EditText mStuNameTv;
    @BindView(R.id.personnal_info_stu_id_tv)
    EditText mIdTv;
    @BindView(R.id.personnal_info_stu_sex_tv)
    TextView mSexTv;
    @BindView(R.id.personnal_info_stu_national_tv)
    EditText mStuNationTv;
    @BindView(R.id.personnal_info_stu_connact_tv)
    EditText mStuPhoneTv;

    @BindView(R.id.birth_layout_gp)
    RelativeLayout VBirthLayoutGp;//生日
    @BindView(R.id.personnal_info_stu_birth_date_tv)
    TextView mBirthTv;
    @BindView(R.id.personnal_info_stu_age_tv)
    EditText mStuAgeTv;
    @BindView(R.id.personnal_info_stu_address_tv)
    EditText mStuRegisterTv;

    //家庭信息
    @BindView(R.id.guardian_tv)
    EditText mGuarDianPeopleTv;
    @BindView(R.id.relation_tv)
    EditText mRelationShipTv;
    @BindView(R.id.census_register_gp)
    RelativeLayout VCensusRegisterGp;//户籍
    @BindView(R.id.register_tv)
    TextView mFamilyTv;
    @BindView(R.id.address_layout_gp)
    RelativeLayout VAddressLayoutGp;//地址
    @BindView(R.id.now_address_tv)
    TextView mNowAddressTv;
    @BindView(R.id.now_address_detetail)
    EditText mNowAddressDetailTv;//详细地址
    @BindView(R.id.father_name_tv)
    EditText mFatherNameTv;
    @BindView(R.id.father_education_tv)
    EditText mFatherEducationTv;
    @BindView(R.id.father_job_tv)
    EditText mFatherJobTv;
    @BindView(R.id.father_yg_tv)
    EditText mFatherYgNumberTv;
    @BindView(R.id.mother_name_tv)
    EditText mMotherNameTv;
    @BindView(R.id.mother_education_tv)
    EditText mMotherEducationTv;
    @BindView(R.id.mather_job_tv)
    EditText mMotherJobTv;
    @BindView(R.id.mather_yg_tv)
    EditText mMotherYgNumberTv;

    //住宿信息
    @BindView(R.id.is_stay_tv)
    EditText mStuIsStayTv;
    @BindView(R.id.room_num_tv)
    EditText mRoomNumberTv;
    @BindView(R.id.room_style_tv)
    EditText mRoomStyleTv;
    @BindView(R.id.house_num_tv)
    EditText mHouseNumTv;

    private boolean mIsEdit = false;

    private CommonDropView mDropViewSex;

    private List<String> mSexList = new ArrayList<>();

    private CityChooseDialog cityChooseDialog ;  // 选择户籍的选择窗口


    private void setNoChangeView(boolean enable){
        mStuIsStayTv.setFocusable(false);
        mStuIsStayTv.setFocusableInTouchMode(false);
        mRoomNumberTv.setFocusable(enable);
        mRoomNumberTv.setFocusableInTouchMode(false);
        mRoomStyleTv.setFocusable(enable);
        mRoomStyleTv.setFocusableInTouchMode(false);
        mHouseNumTv.setFocusable(enable);
        mHouseNumTv.setFocusableInTouchMode(false);
    }

    private void setViewsEnable(boolean enable){

        if (!enable) {
            mStuSchoolTv.setEnabled(enable);
            mStuDepartmentTv.setEnabled(enable);
            mStuProTv.setEnabled(enable);
            mStuClassTv.setEnabled(enable);
            mStuNumTv.setEnabled(enable);

            mFatherNameTv.setEnabled(enable);
            mFatherEducationTv.setEnabled(enable);
            mFatherJobTv.setEnabled(enable);
            mFatherYgNumberTv.setEnabled(enable);
            mMotherNameTv.setEnabled(enable);
            mMotherEducationTv.setEnabled(enable);
            mMotherJobTv.setEnabled(enable);
            mMotherYgNumberTv.setEnabled(enable);
            ;
        }
        mStuNameTv.setEnabled(enable);
        mIdTv.setEnabled(enable);
        mSexTv.setEnabled(enable);

        mStuNationTv.setEnabled(enable);
        mStuPhoneTv.setEnabled(enable);
        mBirthTv.setEnabled(enable);
        mStuAgeTv.setEnabled(enable);
        mStuRegisterTv.setEnabled(enable);

        mGuarDianPeopleTv.setEnabled(enable);
        mRelationShipTv.setEnabled(enable);
//        mFamilyTv.setEnabled(enable);
        mNowAddressDetailTv.setEnabled(enable);
        mNowAddressTv.setEnabled(enable);


        if (enable){
            VAddressLayoutGp.setClickable(true);
            VCensusRegisterGp.setClickable(true);
            VBirthLayoutGp.setClickable(true);
        }else {
            VAddressLayoutGp.setClickable(false);
            VCensusRegisterGp.setClickable(false);
            VBirthLayoutGp.setClickable(false);
        }

    }

    private void showStuInfo(StudentResponseInfo info){

        mStuNumTv.setText(info.number);
        mStuSchoolTv.setText(info.school);
        mStuDepartmentTv.setText("");//系
        mStuProTv.setText(info.major);//专业
        mStuClassTv.setText(info.className);

        mStuNameTv.setText(info.name);
        mIdTv.setText(info.idcard);
        mSexTv.setText(info.sex);
        mStuNationTv.setText(info.nation);
        mStuPhoneTv.setText(info.mobile);
        mBirthTv.setText(info.birthday);
        mStuAgeTv.setText(info.age);
        mStuRegisterTv.setText(info.register);

        mGuarDianPeopleTv.setText(info.guardian);
        mRelationShipTv.setText(info.relativeWith);
        mFamilyTv.setText(info.register);
        mNowAddressTv.setText(info.familyAddress);
        mNowAddressDetailTv.setText(info.familyAddress);
        mFatherNameTv.setText(info.fatherName);
        mFatherEducationTv.setText(info.fatherEdu);
        mFatherJobTv.setText(info.fatherTitle);
        mFatherYgNumberTv.setText(info.fatherNumber);
        mMotherNameTv.setText(info.motherName);
        mMotherEducationTv.setText(info.motherEdu);
        mMotherJobTv.setText(info.motherTitle);
        mMotherYgNumberTv.setText(info.motherNumber);
        mStuIsStayTv.setText(info.isBoarding == true ?"是" :"否");
        mRoomNumberTv.setText(info.boardingName);
        mRoomStyleTv.setText(info.boardingSize);
        mHouseNumTv.setText(info.boardingHouse);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_student_personal_info;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews() {
        if (cityChooseDialog == null){
            cityChooseDialog = new CityChooseDialog();
            cityChooseDialog.init(this);
        }

        mTitleTv.setText("个人资料");
        mOperImg.setImageResource(R.mipmap.ic_edit);
        if (!mIsEdit){
            mCommitBtn.setVisibility(View.GONE);
        }

        setNoChangeView(false);
        setViewsEnable(false);

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
                    mDropViewSex = new CommonDropView(StudentInofActivity.this, mSexTv, mSexList);
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

    }



    @Override
    public void loadData() {
        getStudentInfo();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @OnClick(R.id.frame_img)
    public void editInfoClick(){
        mIsEdit = true;
        mCommitBtn.setVisibility(View.VISIBLE);
        rightFrame.setVisibility(View.GONE);
        setViewsEnable(true);
    }
    //出生日期选择
    @OnClick(R.id.birth_layout_gp)
    public void onBirthChoose(){
        TimeChooseUtil timeChooseUtil = new TimeChooseUtil();
        timeChooseUtil.showTimeDialog(this,mBirthTv);
    }

    //户籍点击
    @OnClick(R.id.census_register_gp)
    public void censusRegister(){
        if (cityChooseDialog != null){
            cityChooseDialog.showPickerView(this, new CityChooseDialog.ChooseResultCallBack() {
                @Override
                public void result(String s,String detail) {
                    mFamilyTv.setText(s);
                }
            });
        }
    }
    //地址点击
    @OnClick(R.id.address_layout_gp)
    public void addressChoose(){
        if (cityChooseDialog != null){
            cityChooseDialog.showPickerView(this, new CityChooseDialog.ChooseResultCallBack() {
                @Override
                public void result(String s, String detail) {
                    mNowAddressTv.setText(s);
                    mNowAddressDetailTv.setText(detail);
                }
            });
        }
    }

    @OnClick(R.id.info_commint_btn)
    public void commitClick(){
        modifyStudentInfo();
    }

    //修改信息
    private void modifyStudentInfo(){
        String str = params();
        if (str == null){
            return;
        }

        disLoading();
        StudentModel.getInstance().modifyStudentInfo(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                hideLoading();
                ToastUtil.show("修改成功");
                EventBus.getDefault().post(new UpdateUserInfoEvent());
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });

    }

    private void getStudentInfo(){

        disLoading();
        StudentModel.getInstance().getStudentDetail(new BaseListener(StudentResponseInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();

                if (infoObj != null){
                    StudentResponseInfo studentResponseInfo  = (StudentResponseInfo) infoObj;
                    showStuInfo(studentResponseInfo);
                }else{
                    ToastUtil.show("请求失败");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
            }
        });
    }

    //修改学生信息
    private String params(){

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name",TextUtils.isEmpty(mStuNameTv.getText().toString()) ? "":mStuNameTv.getText().toString());
        jsonObject.put("num",TextUtils.isEmpty(mStuNumTv.getText().toString()) ? "":mStuNumTv.getText().toString());
        jsonObject.put("idcard",TextUtils.isEmpty(mIdTv.getText().toString()) ? "":mIdTv.getText().toString());

        String strSex = mSexTv.getText().toString();
        if (TextUtils.isEmpty(strSex)){
            strSex = "";
        }else if (strSex.equals("男")){
            strSex = "M";
        }else if (strSex.equals("女")){
            strSex = "F";
        }
        jsonObject.put("sex",strSex);
        jsonObject.put("nation",TextUtils.isEmpty(mStuNationTv.getText().toString()) ? "":mStuNationTv.getText().toString());
        jsonObject.put("mobile",TextUtils.isEmpty(mStuPhoneTv.getText().toString()) ? "":mStuPhoneTv.getText().toString());
        jsonObject.put("birthday",TextUtils.isEmpty(mBirthTv.getText().toString()) ? "":mBirthTv.getText().toString());

        String strAge = mStuAgeTv.getText().toString();
        if (!TextUtils.isEmpty(strAge)){
            jsonObject.put("age",Integer.parseInt(strAge));
        }else{
            jsonObject.put("age",0);
        }

        jsonObject.put("accountNature",TextUtils.isEmpty(mStuRegisterTv.getText().toString()) ? "":mStuRegisterTv.getText().toString());
        jsonObject.put("guardian",TextUtils.isEmpty(mGuarDianPeopleTv.getText().toString()) ? "":mGuarDianPeopleTv.getText().toString());
        jsonObject.put("relativeWith",TextUtils.isEmpty(mRelationShipTv.getText().toString()) ? "":mRelationShipTv.getText().toString());
        jsonObject.put("register",TextUtils.isEmpty(mFamilyTv.getText().toString()) ? "":mFamilyTv.getText().toString());
        jsonObject.put("familyAddress",TextUtils.isEmpty(mNowAddressDetailTv.getText().toString()) ? "":mNowAddressDetailTv.getText().toString());

        jsonObject.put("fatherName",TextUtils.isEmpty(mFatherNameTv.getText().toString()) ? "":mFatherNameTv.getText().toString());
        jsonObject.put("fatherEdu",TextUtils.isEmpty(mFatherEducationTv.getText().toString()) ? "" : mFatherEducationTv.getText().toString());
        jsonObject.put("fatherTitle",TextUtils.isEmpty(mFatherJobTv.getText().toString()) ? "" : mFatherJobTv.getText().toString());
        jsonObject.put("fatherNumber",TextUtils.isEmpty(mFatherYgNumberTv.getText().toString()) ? "" : mFatherYgNumberTv.getText().toString());
        jsonObject.put("motherName",TextUtils.isEmpty(mMotherNameTv.getText().toString()) ? "" : mMotherNameTv.getText().toString());
        jsonObject.put("motherEdu",TextUtils.isEmpty(mMotherEducationTv.getText().toString()) ? "" : mMotherEducationTv.getText().toString());
        jsonObject.put("motherTitle",TextUtils.isEmpty(mMotherJobTv.getText().toString()) ? "" : mMotherJobTv.getText().toString());
        jsonObject.put("motherNumber",TextUtils.isEmpty(mMotherYgNumberTv.getText().toString()) ? "" : mMotherYgNumberTv.getText().toString());

        jsonObject.put("isBoarding",TextUtils.isEmpty(mStuIsStayTv.getText().toString()) ? "" : mStuIsStayTv.getText().toString());

        String stuIsStay = mStuIsStayTv.getText().toString();
        if (!TextUtils.isEmpty(stuIsStay)){
            boolean isStaySchool = false;
            if (stuIsStay.equals("是")){
                isStaySchool = true;
            }
            jsonObject.put("isBoarding", isStaySchool);
        }else{
            jsonObject.put("isBoarding",false);
        }
        jsonObject.put("boardingNumber",TextUtils.isEmpty(mRoomNumberTv.getText().toString()) ? "" : mRoomNumberTv.getText().toString());
        jsonObject.put("boardingSize",TextUtils.isEmpty(mRoomStyleTv.getText().toString()) ? "" : mRoomStyleTv.getText().toString());
        jsonObject.put("bedNumber",TextUtils.isEmpty(mHouseNumTv.getText().toString()) ? "" : mHouseNumTv.getText().toString());

        return jsonObject.toString();
    }

    @OnClick(R.id.back_frame)
    public void finishActivityClick(){
        finish();
    }
}
