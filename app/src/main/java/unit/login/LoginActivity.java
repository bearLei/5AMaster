package unit.login;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;

/**
 * Created by ${lei} on 2018/6/2.
 * 登录页面
 */
public class LoginActivity extends PutiActivity {
    @BindView(R.id.location_desc)
    TextView TLocationDesc;//位置信息
    @BindView(R.id.desc)
    TextView TDesc;//文案描述
    @BindView(R.id.edit_account)
    EditText TEditAccount;//账号
    @BindView(R.id.edit_psw)
    EditText TEditPsw;//密码
    @BindView(R.id.edit_verify)
    EditText TEditVerify;//验证码
    @BindView(R.id.verify)
    ImageView IVerify;//验证码图片
    @BindView(R.id.verify_layout)
    LinearLayout VerifyLayout;
    @BindView(R.id.login)
    TextView TLogin;//登录按钮

    @Override
    public int getContentView() {
        return R.layout.puti_login;
    }

    @Override
    public void BindPtr() {

    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {

    }

    @Override
    public void Star() {

    }
}
