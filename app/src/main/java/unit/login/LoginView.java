package unit.login;


import com.puti.education.base.BaseMvpView;

/**
 * Created by ${lei} on 2018/6/2.
 */
public interface LoginView extends BaseMvpView {
    void setLocationDesc(String desc);
    void changeAccountStatus(boolean selected);
    void changePswStatus(boolean selected);
    void changeVerifyStatus(boolean selected);
    void showVerify(boolean show);
}
