package unit.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;

import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;

/**
 * Created by lei on 2018/6/26.
 */

public class TeaQueryManager {

    private QueryDefaultCallBack callBack;
    private ArrayList<ClassSimple> mClassList;

    public TeaQueryManager(QueryDefaultCallBack callBack) {
        this.callBack = callBack;
    }

    public void queryClass(){
        PutiTeacherModel.getInstance().getClass("",new BaseListener(ClassSimple.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mClassList = (ArrayList<ClassSimple>) listObj;
                //默认拉取第一个班级的事件
                if (callBack != null && mClassList != null && mClassList.size() > 0) {
                    callBack.defaultClass(mClassList.get(0).getUID(), mClassList.get(0).getName());
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                ToastUtil.show("拉取班级列表失败");
            }
        });
    }

    //班级筛选列表
    public void showClassDialog(Context mContext, final View view){
        ArrayList<String> list = new ArrayList<>();
        int size = mClassList.size();
        for (int i = 0; i < size; i++) {
            list.add(mClassList.get(i).getName());
        }
        final CommonDropView dropView = new CommonDropView(mContext,view,list);
        dropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String uid = mClassList.get(position).getUID();
                String name = mClassList.get(position).getName();
                if (callBack != null) {
                    callBack.defaultClass(uid, name);
                }
                dropView.dismiss();
            }
        });
        dropView.showAsDropDown(view);
    }


    public interface QueryDefaultCallBack{
        void defaultClass(String uid,String name);
    }

}
