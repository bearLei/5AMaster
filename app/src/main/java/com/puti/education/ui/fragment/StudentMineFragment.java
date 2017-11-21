package com.puti.education.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.StudentResponseInfo;
import com.puti.education.bean.VersionInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.ui.uiCommon.MsgListActivity;
import com.puti.education.ui.uiStudent.PracticeListActivity;
import com.puti.education.ui.uiStudent.StudentInofActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/18 0018.
 *
 *  学生 "我的"
 */

public class StudentMineFragment extends BaseFragment{

    @BindView(R.id.mine_record_tv)
    TextView mPracticeTv;
    @BindView(R.id.mine_name_tv)
    TextView mNameTv;
    @BindView(R.id.mine_class_name_tv)
    TextView mClassNameTv;
    @BindView(R.id.mine_head_img)
    ImageView mHeadImg;
    @BindView(R.id.tvVersion)
    TextView mTvVersion;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_mime;
    }

    @Override
    public void initVariables() {
//        String userAvatar = ConfigUtil.getInstance(this.getActivity()).get(Constant.KEY_USER_AVATAR, "");
//        if (!TextUtils.isEmpty(userAvatar)){
//           ImgLoadUtil.displayPic(R.mipmap.ic_avatar_default, userAvatar, mHeadImg);
//        }
    }

    @Override
    public void initViews(View view) {
        String version = "当前版本:V" + ConfigUtil.getAllVersion(this.getActivity());
        mTvVersion.setText(version);
        mPracticeTv.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void loadData() {
        getStudentInfo();
    }

    @OnClick(R.id.mine_msg_center_tv)
    public void gotoMsgCenterClick(){
        Intent intent = new Intent(getActivity(), MsgListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_person_info_tv)
    public void personalInfoClick(){
        Intent intent = new Intent(getActivity(), StudentInofActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_record_tv)
    public void gotoPraticeList(){
        Intent intent = new Intent(getActivity(), PracticeListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_loginout_btn)
    public void logout(){
        loginOutRequest();
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
                    mNameTv.setText(studentResponseInfo.name);
                    mClassNameTv.setText(studentResponseInfo.className);
                    ImgLoadUtil.displayCirclePic(R.drawable.img_circle_bg,null,mHeadImg);
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

    //注销
    private void loginOutRequest(){

        disLoading("正在注销...");

        CommonModel.getInstance().logout(new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        ConfigUtil.getInstance(getActivity()).clearSearchParams();//清空登录信息
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                },2000);

            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR : errorMessage);
                getActivity().finish();
            }
        });
    }
}
