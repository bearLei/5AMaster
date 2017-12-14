package com.puti.education.ui.uiPatriarch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.R;
import com.puti.education.adapter.ChildInfoListAdapter;
import com.puti.education.bean.ParentInfo;
import com.puti.education.bean.Student;
import com.puti.education.event.UpdateUserInfoEvent;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ListViewMeasureUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.TimeChooseUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/13 0013.
 * <p>
 * 家长个人信息
 */

public class ParentInfoActivity extends BaseActivity {

    private final static int CHOOSE_CHILD_REQUET = 1;
    private final static int CHOOSE_CHILD_RESULT = 2;

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.right_img)
    ImageView mOperImg;
    @BindView(R.id.info_commint_btn)
    Button mCommitBtn;
    @BindView(R.id.frame_img)
    FrameLayout rightFrame;

    @BindView(R.id.parent_name_tv)
    TextView mNameTv;
    @BindView(R.id.parent_id_tv)
    TextView mIdTv;
    @BindView(R.id.parent_nation_tv)
    TextView mNationTv;
    @BindView(R.id.parent_sex_tv)
    TextView mSexTv;
    @BindView(R.id.parent_connact_tv)
    TextView mConnactTv;
    @BindView(R.id.parent_birth_date_tv)
    TextView mBirthDateTv;
    @BindView(R.id.parent_age_tv)
    TextView mAgeTv;
    @BindView(R.id.parent_register_type_tv)
    TextView mRegisterTypeTv;
    @BindView(R.id.parent_register_tv)
    TextView mRegisterTv;
    @BindView(R.id.parent_now_house_address_tv)
    TextView mHouseAddressTv;
    @BindView(R.id.parent_education_tv)
    TextView mEducationTv;
    @BindView(R.id.parent_job_tv)
    TextView mJobTv;
    @BindView(R.id.personnal_volunteer_num_tv)
    TextView mVolunteerTv;
    @BindView(R.id.parent_childs_num_tv)
    TextView childsNumtv;

    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.birth_layout_gp)
    RelativeLayout birthLayoutGp;

    private int style = 1;
    private boolean mIsEdit = false;

    ChildInfoListAdapter childInfoListAdapter;
    ParentInfo parentInfo = null;

    private List<String> mSexList = new ArrayList<>();
    private List<String> mAddressList = new ArrayList<>();
    private CommonDropView mDropViewSex, mDropViewMarry, mDropViewAddress;

    @Override
    public int getLayoutResourceId() {
        return R.layout.parent_info_activity;
    }

    @Override
    public void initVariables() {
        getParentInfo();
    }

    @OnClick(R.id.back_frame)
    public void finishActivity(View v) {
        this.finish();
    }

    @Override
    public void initViews() {
        mTitleTv.setText("个人信息");
        if (!mIsEdit) {
            mCommitBtn.setVisibility(View.GONE);
        }
        setEnableStyle(false);
        setViewListener();

    }

    private void setViewListener() {
        mSexList.clear();
        mSexList.add("男");
        mSexList.add("女");
        mSexTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEdit) {
                    return;
                }
                if (mDropViewSex == null) {
                    mDropViewSex = new CommonDropView(ParentInfoActivity.this, mSexTv, mSexList);
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
        mAddressList.clear();
        mAddressList.add("农村");
        mAddressList.add("城镇");
        mRegisterTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEdit) {
                    return;
                }
                if (mDropViewAddress == null) {
                    mDropViewAddress = new CommonDropView(ParentInfoActivity.this, mRegisterTypeTv, mAddressList);
                    mDropViewAddress.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String sexname = mAddressList.get(position);
                            mRegisterTypeTv.setText(sexname);
                            mDropViewAddress.dismiss();
                        }
                    });
                }
                mDropViewAddress.showAsDropDown(mRegisterTypeTv);
            }
        });
    }

    @OnClick(R.id.info_commint_btn)
    public void commitBtnClick() {
        modifyParentInfo();
    }

    private void showParentInfo(ParentInfo parentInfo) {
        mNameTv.setText(parentInfo.name);
        mIdTv.setText(parentInfo.idCard);
        mNationTv.setText(parentInfo.nation);
        mSexTv.setText(parentInfo.sex);
        mConnactTv.setText(parentInfo.mobile);
        mBirthDateTv.setText(parentInfo.birthday);
        mAgeTv.setText(parentInfo.age);
        mRegisterTypeTv.setText(parentInfo.accountNature);
        mRegisterTv.setText(parentInfo.register);
        mHouseAddressTv.setText(parentInfo.address);
        mEducationTv.setText(parentInfo.education);
        mJobTv.setText(parentInfo.job);
        mVolunteerTv.setText(parentInfo.volunteerNumber);
    }

    private void setEnableStyle(boolean isEnable) {

        if (!isEnable) {
            mVolunteerTv.setEnabled(isEnable);
        }

        mNameTv.setEnabled(isEnable);
        mIdTv.setEnabled(isEnable);
        mNationTv.setEnabled(isEnable);
        mSexTv.setEnabled(isEnable);
        mConnactTv.setEnabled(isEnable);
        mBirthDateTv.setEnabled(isEnable);
        mAgeTv.setEnabled(isEnable);
        mRegisterTypeTv.setEnabled(isEnable);

        mRegisterTv.setEnabled(isEnable);
        mHouseAddressTv.setEnabled(isEnable);
        mEducationTv.setEnabled(isEnable);
        mJobTv.setEnabled(isEnable);

        if (isEnable){
            birthLayoutGp.setClickable(true);
        }else {
            birthLayoutGp.setClickable(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        Student student = (Student) data.getSerializableExtra(Key.BEAN);
        String className = data.getStringExtra(Key.CHILD_CLASS_NAME);
        student.className = className;

        if (parentInfo != null && parentInfo.childList != null) {
            parentInfo.childList.add(student);
            childInfoListAdapter = new ChildInfoListAdapter(ParentInfoActivity.this);
            childInfoListAdapter.setmList(parentInfo.childList);
            mLv.setAdapter(childInfoListAdapter);
            ListViewMeasureUtil.measureListViewWrongHeight(mLv);
            childsNumtv.setText(parentInfo.childList.size() + "");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.frame_img)
    public void editClick() {
        if (style == 1) {
            style = 2;
//            mOperImg.setImageResource(R.mipmap.ic_add);
            mOperImg.setVisibility(View.GONE);
            mIsEdit = true;
            if (childInfoListAdapter != null) {
                childInfoListAdapter.setEdit(mIsEdit);
                childInfoListAdapter.notifyDataSetChanged();
            }
            mCommitBtn.setVisibility(View.VISIBLE);
            setEnableStyle(true);
        }

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void modifyParentInfo() {

        String params = createParams();
        if (params == null) {
            return;
        }

        disLoading();

        PatriarchModel.getInstance().modifyParentInfo(params, new BaseListener() {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();
                if (infoObj != null) {
                    ToastUtil.show("编辑成功");
                    EventBus.getDefault().post(new UpdateUserInfoEvent());
                    finish();
                } else {
                    ToastUtil.show("编辑失败，请重试");
                }

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(TextUtils.isEmpty(errorMessage) ? Constant.REQUEST_FAILED_STR : errorMessage);

                super.requestFailed(status, code, errorMessage);
            }
        });

    }

    private void getParentInfo() {

        disLoading();
        PatriarchModel.getInstance().getParnetInfo(new BaseListener(ParentInfo.class) {

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();

                if (infoObj != null) {
                    parentInfo = (ParentInfo) infoObj;
                    showParentInfo(parentInfo);
                    childsNumtv.setText("0");
                    if (parentInfo.childList != null && parentInfo.childList.size() > 0) {

                        childsNumtv.setText(parentInfo.childList.size() + "");
                        childInfoListAdapter = new ChildInfoListAdapter(ParentInfoActivity.this);
                        childInfoListAdapter.setEdit(mIsEdit);
                        mLv.setAdapter(childInfoListAdapter);
                        childInfoListAdapter.setmList(parentInfo.childList);
                        ListViewMeasureUtil.measureListViewWrongHeight(mLv);
                        if (parentInfo.childList != null && parentInfo.childList.size() > 0) {
                            childInfoListAdapter.setmList(parentInfo.childList);
                            childInfoListAdapter.notifyDataSetChanged();
                        }

                        childInfoListAdapter.setDelClickLitener(new ChildInfoListAdapter.DelClickLitener() {
                            @Override
                            public void onclick(int position) {
                                parentInfo.childList.remove(position);
                                childInfoListAdapter.setmList(parentInfo.childList);
                                mLv.setAdapter(childInfoListAdapter);
                                ListViewMeasureUtil.measureListViewWrongHeight(mLv);
                            }
                        });
                    }

                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
                super.requestFailed(status, code, errorMessage);

            }
        });
    }

    private String createParams() {

        if (TextUtils.isEmpty(mNameTv.getText().toString())) {
            ToastUtil.show("输入名字");
            return null;
        }

        if (TextUtils.isEmpty(mIdTv.getText().toString())) {
            ToastUtil.show("输入身份证");
            return null;
        }

        if (TextUtils.isEmpty(mConnactTv.getText().toString())) {
            ToastUtil.show("输入电话");
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", mNameTv.getText().toString());
        jsonObject.put("idcard", mIdTv.getText().toString());
        jsonObject.put("sex", mSexTv.getText().toString());
        jsonObject.put("nation", mNationTv.getText().toString());
        jsonObject.put("mobile", mConnactTv.getText().toString());
        jsonObject.put("birthday", mBirthDateTv.getText().toString());
        jsonObject.put("age", mAgeTv.getText().toString());
        jsonObject.put("accountNature", mRegisterTypeTv.getText().toString());
        jsonObject.put("register", mRegisterTv.getText().toString());
        jsonObject.put("address", mHouseAddressTv.getText().toString());
        jsonObject.put("education", mEducationTv.getText().toString());
        jsonObject.put("job", mJobTv.getText().toString());

        JSONArray childJsonArray = new JSONArray();

        for (Student stu : parentInfo.childList) {
            childJsonArray.add(stu.uid);
        }
        jsonObject.put("childList", childJsonArray);

        LogUtil.i("params", jsonObject.toString());
        return jsonObject.toString();
    }

    //出生日期
    @OnClick(R.id.birth_layout_gp)
    public void onClick() {
        TimeChooseUtil timeChooseUtil = new TimeChooseUtil();
        timeChooseUtil.showTimeDialog(this, mBirthDateTv,true);
    }
}
