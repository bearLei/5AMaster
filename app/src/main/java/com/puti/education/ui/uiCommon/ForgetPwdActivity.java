package com.puti.education.ui.uiCommon;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.puti.education.bean.ResponseBean;
import com.puti.education.bean.Role;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.SpinnerPopWindow;
import com.puti.education.R;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 */

public class ForgetPwdActivity extends BaseActivity {

    private final String TAG = ForgetPwdActivity.class.getName();

    @BindView(R.id.tv_mobile_number)
    EditText mMobile;
    @BindView(R.id.tv_verify_value)
    EditText mVerifyValue;
    @BindView(R.id.tv_new_pwd)
    EditText mNewPwd;
    @BindView(R.id.tv_new_pwd_again)
    EditText mNewPwdAagin;
    @BindView(R.id.tv_verify_code)
    TextView mBtnVerifyCode;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;

    private Role mRole = null;
    private SpinnerPopWindow mSpinnerPopWindow;
    private List<Role> mRoleList;
    private int mSecondCount = 60;
    private String mSchoolId ;
    private String mLoginName = null;

    Runnable mUpdateCall = new Runnable() {

        @Override
        public void run() {
            if (mSecondCount > 0) {

                mSecondCount--;
                mBtnVerifyCode.setEnabled(false);
                mBtnVerifyCode.setText("" +mSecondCount+ "秒");
                mBtnVerifyCode.postDelayed(this, 1000);
            }else
            {
                mSecondCount = 60;
                mBtnVerifyCode.setEnabled(true);
                mBtnVerifyCode.setText("获取验证码");
            }

        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_forget_pwd;
    }
    @Override
    public void initVariables() {
        mSchoolId = this.getIntent().getStringExtra("schoolid");
        mLoginName= this.getIntent().getStringExtra("loginname");
    }

    @OnClick(R.id.btn_commit)
    public void modifyFwd(){
        if (TextUtils.isEmpty(mSchoolId)){
            ToastUtil.show("学校ID出错 " + mSchoolId);
            return;
        }
        if (TextUtils.isEmpty(mLoginName)){
            ToastUtil.show("用户名为空 ");
            return;
        }

        String mobile = mMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)){
            ToastUtil.show("手机号为空 ");
            return;
        }

        String verifycode = mVerifyValue.getText().toString();
        if (TextUtils.isEmpty(verifycode)){
            ToastUtil.show("验证码为空 ");
            return;
        }

        String newpwd = mNewPwd.getText().toString();
        if (TextUtils.isEmpty(newpwd)){
            ToastUtil.show("新密码为空 ");
            return;
        }

        String newpwdagain = mNewPwdAagin.getText().toString();
        if (TextUtils.isEmpty(newpwdagain)){
            ToastUtil.show("再次输入的新密码为空 ");
            return;
        }
        if (!newpwd.equals(newpwdagain)){
            ToastUtil.show("两次输入的新密码不匹配");
            return;
        }

        CommonModel.getInstance().forgetPwd(mLoginName, mobile, verifycode, newpwd, new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                ToastUtil.show("修改密码成功,请重新登录" );
                finish();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("修改密码出错 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }

    @Override
    public void initViews() {
        setRoleView(1);
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.back_layout)
    public void backLast(){
        finish();
    }

    @OnClick(R.id.tv_verify_code)
    public void getVerifyCode(){
        String mobile = mMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)){
            ToastUtil.show("请输入手机号码");
            return;
        }

        sendVerifyCode(mobile);

    }

    public void setRoleView(int type){
        switch (type){
            case 2://学生
                mMobile.setHint("家长手机号");
                break;
        }
    }

    private void sendVerifyCode(String mobile){
        CommonModel.getInstance().getVerifyCode(mobile, new BaseListener(ResponseBean.class){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                mBtnVerifyCode.postDelayed(mUpdateCall, 0);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("获取验证码失败 " + (TextUtils.isEmpty(errorMessage)?"":errorMessage));
            }
        });
    }
}
