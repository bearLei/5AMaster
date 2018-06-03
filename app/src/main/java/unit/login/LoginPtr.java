package unit.login;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.base.BaseMvpView;


/**
 * Created by ${lei} on 2018/6/2.
 */
public class LoginPtr implements BaseMvpPtr {

    private Context mContext;
    private BaseMvpView mView;

    public LoginPtr(Context mContext, BaseMvpView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }


}
