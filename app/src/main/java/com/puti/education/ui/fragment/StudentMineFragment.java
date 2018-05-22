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
import com.puti.education.common.BingPhoneListener;
import com.puti.education.common.PassWordUtil;
import com.puti.education.event.UpdateUserInfoEvent;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.StudentModel;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.ForgetPwdActivity;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.ui.uiCommon.MsgListActivity;
import com.puti.education.ui.uiStudent.PracticeListActivity;
import com.puti.education.ui.uiStudent.StudentInofActivity;
import com.puti.education.ui.uiTeacher.TeacherPersonalInfoActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.EduDialog;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private StudentResponseInfo studentResponseInfo;
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
        ReqlogoutDialog();
    }

    //修改密码
    @OnClick(R.id.layout_change_pwd)
    public void changePwd(){
        //没检查到手机号码就弹窗去绑定
        if ( studentResponseInfo!= null && !TextUtils.isEmpty(studentResponseInfo.mobile)){
            Intent intent = new Intent();
            intent.putExtra("schoolid", ConfigUtil.getInstance(getActivity()).get(Constant.KEY_SCHOOL_ID, ""));
            intent.putExtra("loginname", ConfigUtil.getInstance(getActivity()).get(Constant.KEY_LOGIN_NAME,""));
            intent.setClass(getActivity(), ForgetPwdActivity.class);
            startActivity(intent);
        }else {
            PassWordUtil.g().showDialog(getActivity(), new BingPhoneListener() {
                @Override
                public void bind() {
                    Intent intent = new Intent(getActivity(), StudentInofActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    private void getStudentInfo(){

        disLoading();
        StudentModel.getInstance().getStudentDetail(new BaseListener(StudentResponseInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();

                if (infoObj != null){
                    studentResponseInfo  = (StudentResponseInfo) infoObj;
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
    private void ReqlogoutDialog(){
        final EduDialog dialog = new EduDialog(getActivity(),"确定退出登录吗？");
        dialog.setOnButtonClickListener(new EduDialog.OnButtonClickListener() {
            @Override
            public void onNegativeButtonClick(View view) {
                loginOutRequest();
                dialog.dismiss();
            }

            @Override
            public void onPositiveButtonClick(View view) {
                dialog.dismiss();
            }
        },"确定","取消");
        dialog.show();
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(UpdateUserInfoEvent event){
        if (event != null){
            getStudentInfo();
        }
    }
}
