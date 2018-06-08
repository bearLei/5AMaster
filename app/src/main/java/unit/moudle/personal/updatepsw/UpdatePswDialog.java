package unit.moudle.personal.updatepsw;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.UserInfo;
import com.puti.education.R;
import com.puti.education.listener.BaseListener;
import com.puti.education.ui.uiCommon.LoginActivity;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import unit.api.PutiCommonModel;
import unit.base.BaseResponseInfo;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/8.
 */

public class UpdatePswDialog extends Dialog {

    @BindView(R.id.edit_old_psw)
    EditText editOldPsw;
    @BindView(R.id.edit_new_psw)
    EditText editNewPsw;
    @BindView(R.id.edit_psw_again)
    EditText editPswAgain;
    @BindView(R.id.update)
    TextView update;
    private Context mContext;

    public UpdatePswDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public UpdatePswDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected UpdatePswDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.puti_update_psw_dialog, null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewUtils.getScreenWid(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);
    }

    @OnFocusChange({R.id.edit_old_psw, R.id.edit_new_psw, R.id.edit_psw_again})
    public void onFocusChange(View view, boolean hasFoucus) {
        if (this.isShowing()) {
            view.setBackground(hasFoucus ? mContext.getResources().getDrawable(R.drawable.puti_login_edit_selected_bg)
                    : mContext.getResources().getDrawable(R.drawable.puti_login_edit_unselected_bg));
        }
    }

    @OnClick(R.id.update)
    public void onClick() {
        String oldPsw = editOldPsw.getText().toString();//旧密码
        String newPsw = editNewPsw.getText().toString();//新密码
        String newPswAgagin = editPswAgain.getText().toString();//重复新密码

        if (TextUtils.isEmpty(oldPsw)){
            ToastUtil.show(R.string.puti_update_old_psw);
            return;
        }

        if (TextUtils.isEmpty(newPsw)){
            ToastUtil.show(R.string.puti_update_new_psw);
            return;
        }

        if (TextUtils.isEmpty(newPswAgagin)){
            ToastUtil.show(R.string.puti_update_new_psw_again);
            return;
        }

        if (!newPsw.equals(newPswAgagin)){
            ToastUtil.show(R.string.puti_update_comparison_psw);
            return;
        }

        PutiCommonModel.getInstance().updatePsw(
                UserInfoUtils.getUid()
                ,oldPsw
                ,newPsw
        ,new BaseListener(BaseResponseInfo.class){
                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        ToastUtil.show(R.string.puti_update_success);
                        handleResult();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        ToastUtil.show(errorMessage);
                    }
                });
    }

    //登出登录
    private void handleResult(){
        PutiCommonModel.getInstance().logout(new BaseListener(){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                dismiss();
                UserInfoUtils.setUserInfo(null);
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage);
            }
        });
    }

}
