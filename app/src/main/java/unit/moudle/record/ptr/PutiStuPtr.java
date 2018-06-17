package unit.moudle.record.ptr;

import android.content.Context;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;

import unit.api.PutiCommonModel;
import unit.entity.StudentInfo;
import unit.moudle.record.view.PutiStuView;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiStuPtr implements BaseMvpPtr {

    private Context mContext;
    private PutiStuView mView;

    public PutiStuPtr(Context mContext, PutiStuView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        queryPortrait();
        queryBaseInfo();
    }

    @Override
    public void stop() {

    }

    //查询学生画像
    private void queryPortrait(){
        PutiCommonModel.getInstance().queryPortrait(mView.getStudentUid(),"",new BaseListener());
    }

    private void queryBaseInfo(){
        PutiCommonModel.getInstance().queryStuInfo(mView.getStudentUid(),"",new BaseListener(StudentInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
            }
        });
    }
}
