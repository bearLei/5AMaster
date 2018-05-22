package com.puti.education.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.appupdate.PopUpDialog;
import com.puti.education.bean.TeacherPersonInfo;
import com.puti.education.common.BingPhoneListener;
import com.puti.education.common.PassWordUtil;
import com.puti.education.event.UpdateUserInfoEvent;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiCommon.ForgetPwdActivity;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.ui.uiCommon.MsgListActivity;
import com.puti.education.ui.uiTeacher.DetectiveListActivity;
import com.puti.education.ui.uiTeacher.ReportListActivity;
import com.puti.education.ui.uiTeacher.TeacherPersonalInfoActivity;
import com.puti.education.R;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.EduDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/18 0018.
 *
 *  教师 "我的"
 */

public class TeacherMineFragment extends BaseFragment{

    @BindView(R.id.mine_head_img)
    ImageView headImg;
    @BindView(R.id.mine_name_tv)
    TextView mNameTv;
    @BindView(R.id.mine_class_name_tv)
    TextView mClassNameTv;
    @BindView(R.id.mine_loginout_btn)
    Button mLoginOutBtn;
    @BindView(R.id.tvVersion)
    TextView mTvVersion;
    @BindView(R.id.layout_detective)
    LinearLayout mLayoutDetective;
    @BindView(R.id.layout_report)
    LinearLayout mLayoutReport;
    @BindView(R.id.mine_record_tv)
    TextView mTvTrainList;
    @BindView(R.id.layout_change_pwd)
    LinearLayout mLayoutChangePwd;//修改密码

    private TeacherPersonInfo teacherPersonInfo;//教师信息bean

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_mime;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view) {
        String version = "当前版本:V" + ConfigUtil.getAllVersion(this.getActivity());
        mTvVersion.setText(version);

        int role = ConfigUtil.getInstance(this.getActivity()).get(Constant.KEY_ROLE_TYPE, -1);
        if (role == Constant.ROLE_STUDENT){
            mTvTrainList.setVisibility(View.VISIBLE);
        }else{
            mTvTrainList.setVisibility(View.GONE);
        }



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void loadData() {
        getTeacherInfo();
    }

    @OnClick(R.id.mine_msg_center_tv)
    public void gotoMsgCenterClick(){
        Intent intent = new Intent(getActivity(), MsgListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_person_info_tv)
    public void personalInfoClick(){
        Intent intent = new Intent(getActivity(), TeacherPersonalInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_detective)
    public void detectiveInfoClick(){
        Intent intent = new Intent(getActivity(), DetectiveListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_report)
    public void reportInfoClick(){
        Intent intent = new Intent(getActivity(), ReportListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_loginout_btn)
    public void loginOutClick(){
        ReqlogoutDialog();
    }

    //修改密码
    @OnClick(R.id.layout_change_pwd)
    public void changePwd(){
        //没检查到手机号码就弹窗去绑定
        if (teacherPersonInfo != null && !TextUtils.isEmpty(teacherPersonInfo.phone)){
            Intent intent = new Intent();
            intent.putExtra("schoolid", ConfigUtil.getInstance(getActivity()).get(Constant.KEY_SCHOOL_ID, ""));
            intent.putExtra("loginname", ConfigUtil.getInstance(getActivity()).get(Constant.KEY_LOGIN_NAME,""));
            intent.setClass(getActivity(), ForgetPwdActivity.class);
            startActivity(intent);
        }else {
            PassWordUtil.g().showDialog(getActivity(), new BingPhoneListener() {
                @Override
                public void bind() {
                    Intent intent = new Intent(getActivity(), TeacherPersonalInfoActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    //显示教师基本信息
    private void showTeacherInfo(TeacherPersonInfo info){
        if (info == null)
            return;
        mNameTv.setText(info.name);
        if (info.bStudentAffairs){
            mLayoutDetective.setVisibility(View.VISIBLE);
            mLayoutReport.setVisibility(View.VISIBLE);
        }
        //mClassNameTv.setText(TextUtils.isEmpty(info.classes) ? "暂无":info.className);
    }

    //获取教师信息
    private void getTeacherInfo(){
        disLoading();
        TeacherModel.getInstance().getTeacherInfo(new BaseListener(TeacherPersonInfo.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    teacherPersonInfo = (TeacherPersonInfo) infoObj;
                    showTeacherInfo(teacherPersonInfo);
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
           getTeacherInfo();
        }
    }

}
