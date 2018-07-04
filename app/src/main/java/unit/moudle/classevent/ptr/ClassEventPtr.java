package unit.moudle.classevent.ptr;

import android.content.Context;
import android.view.View;

import com.puti.education.base.BaseMvpPtr;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import unit.api.PutiCommonModel;
import unit.entity.Event;
import unit.entity.PutiEvents;
import unit.moudle.classevent.view.ClassEventView;
import unit.util.TeaQueryManager;

/**
 * Created by lei on 2018/6/26.
 */

public class ClassEventPtr implements BaseMvpPtr{

    private Context mContext;
    private ClassEventView mView;

    private TeaQueryManager manager;
    private Map<String,Integer> statusMap ;//状态的Map

    private String mCurrentClassUid;//当前选中的班级id
    private int mCurrentStatus = -1;//当前状态 初始默认状态-1 查询全部
    public ClassEventPtr(Context mContext, ClassEventView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void star() {
        if (manager == null){
            manager = new TeaQueryManager(new TeaQueryManager.QueryDefaultCallBack() {
                @Override
                public void defaultClass(String uid, String name) {
                    mCurrentClassUid = uid;
                    queryEvent();
                }
            });
        }

        if (statusMap == null){
            statusMap = new LinkedHashMap<>();
        }
        statusMap.put("全部",-1);
        statusMap.put("已拒绝",0);
        statusMap.put("处理中",1);
        statusMap.put("审核中",2);
        statusMap.put("追踪中",3);
        statusMap.put("已完结",4);
        queryClass();
    }

    @Override
    public void stop() {

    }

    //筛选班级列表
    public void queryClass(){
        if (manager != null){
            manager.queryClass();
        }
    }

    public void showClassDialog(View view){
        if (manager != null){
            manager.showClassDialog(mContext,view);
        }
    }
    //状态筛选列表
    public void showStatusDialog(Context mContext,View view){
        final ArrayList<String> list = new ArrayList<>();
        Iterator<Map.Entry<String,Integer>> iterator = statusMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
           list.add(entry.getKey());
        }
        final CommonDropView dropView = new CommonDropView(mContext,view,list);
        dropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String statusName = list.get(position);
                mCurrentStatus= statusMap.get(statusName);
                mView.setStatus(statusName);
                queryEvent();
                dropView.dismiss();
            }
        });
        dropView.showAsDropDown(view);
    }


    private void queryEvent(){
        PutiCommonModel.getInstance().queryEvent(mCurrentClassUid,mCurrentStatus,1, Integer.MAX_VALUE,new BaseListener(PutiEvents.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                PutiEvents events = (PutiEvents) infoObj;
                ArrayList<Event> eventList = (ArrayList<Event>) events.getEvents();
                handleResult(eventList);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {

                ToastUtil.show(errorMessage);
                mView.showErrorView();
            }
        });
    }

    private void handleResult(ArrayList<Event> eventList){
        mView.succuess(eventList);

    }

}
