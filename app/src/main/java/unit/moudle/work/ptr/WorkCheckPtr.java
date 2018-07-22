package unit.moudle.work.ptr;

import android.content.Context;
import android.view.View;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;
import java.util.List;

import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;
import unit.entity.PutiWeekEventImp;
import unit.entity.WeekEvent;
import unit.moudle.work.holder.WorkEventCountHolder;
import unit.moudle.work.view.WorkCheckView;

/**
 * Created by lei on 2018/6/19.
 */

public class WorkCheckPtr implements BaseMvpPtr {
    private Context mContext;
    private WorkCheckView mView;

    private ArrayList<ClassSimple> mClassList;

    private WorkEventCountHolder workEventCountHolder;

    public WorkCheckPtr(Context mContext, WorkCheckView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        initWorkEventCountHolder();
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
                //默认拉取第一个班级的课表
                getWeekEvent(mClassList.get(0).getUID());
                mView.setClassName(mClassList.get(0).getName());
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("拉取班级列表失败");
            }
        });
    }

    //班级筛选列表
    public void showClassDialog(View view) {
        ArrayList<String> list = new ArrayList<>();
        int size = mClassList.size();
        for (int i = 0; i < size; i++) {
            list.add(mClassList.get(i).getName());
        }
        final CommonDropView dropView = new CommonDropView(mContext, view, list);
        dropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String uid = mClassList.get(position).getUID();
                String name = mClassList.get(position).getName();
                mView.setClassName(name);
                getWeekEvent(uid);
                dropView.dismiss();
            }
        });
        dropView.showAsDropDown(view);
    }

    private void getWeekEvent(String classUid){
        mView.showLoading();
        PutiTeacherModel.getInstance().getWeekEvent(classUid,new BaseListener(PutiWeekEventImp.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                PutiWeekEventImp weekEventImp = (PutiWeekEventImp) infoObj;
                if (weekEventImp != null && workEventCountHolder != null){
                   workEventCountHolder.setData(weekEventImp);
                }
                mView.showSuccessView();
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show(errorMessage);
                mView.hideLoading();
                mView.showErrorView();
            }
        });
    }

    private void initWorkEventCountHolder(){
        if (workEventCountHolder == null){
            workEventCountHolder = new WorkEventCountHolder(mContext);
        }
        mView.addChartView(workEventCountHolder.getRootView());
    }

}
