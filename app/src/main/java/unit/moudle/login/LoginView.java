package unit.moudle.login;


import com.puti.education.base.BaseMvpView;

import unit.entity.VerifyInfo;

/**
 * Created by ${lei} on 2018/6/2.
 */
public interface LoginView extends BaseMvpView {
    void setLocationDesc(String desc);
    void showVerify(boolean show, VerifyInfo verifyInfo);
    String getEditPsw();
    String getVerify();
    String getEditAccount();
}
