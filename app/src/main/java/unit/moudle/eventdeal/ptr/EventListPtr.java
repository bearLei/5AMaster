package unit.moudle.eventdeal.ptr;

import android.content.Context;
import android.content.Intent;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;

import unit.api.PutiCommonModel;
import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;
import unit.moudle.eventdeal.view.EventListView;

/**
 * Created by lei on 2018/6/17.
 */

public class EventListPtr implements BaseMvpPtr {
    private Context mContext;
    private EventListView mView;

    private ArrayList<ClassSimple> mClassList;

    public EventListPtr(Context mContext, EventListView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        queryClass();
    }

    @Override
    public void stop() {

    }

    public void queryClass(){
        PutiTeacherModel.getInstance().getClass("",new BaseListener(ClassSimple.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mClassList = (ArrayList<ClassSimple>) listObj;
                //默认拉取第一个班级的事件
                queryEvent(mClassList.get(0).getUID());
                mView.setClassName(mClassList.get(0).getName());
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("拉取班级列表失败");
            }
        });
    }

    private void queryEvent(String classUid){
        PutiCommonModel.getInstance().queryEvent(classUid,-1,1, Integer.MAX_VALUE,new BaseListener());
    }

}
