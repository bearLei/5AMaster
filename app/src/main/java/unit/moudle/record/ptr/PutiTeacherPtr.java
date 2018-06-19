package unit.moudle.record.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;

import unit.api.PutiCommonModel;
import unit.moudle.record.view.PutiTeacherView;
import unit.util.UserInfoUtils;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiTeacherPtr implements BaseMvpPtr {

    private Context mContext;
    private PutiTeacherView mView;

    public PutiTeacherPtr(Context mContext, PutiTeacherView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        queryTeaBaseInfo();
    }

    @Override
    public void stop() {

    }

    private void queryTeaBaseInfo(){
        PutiCommonModel.getInstance().queryTeaInfo(UserInfoUtils.getUid(),new BaseListener());
    }

}
