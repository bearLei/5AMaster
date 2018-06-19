package unit.moudle.schedule;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/19.
 */

public class PutiClassScheduleActivity extends PutiActivity {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.week_class)
    TextView weekClass;
    @BindView(R.id.fillter_class)
    TextView fillterClass;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private ArrayList<ClassSimple> mClassList;

    @Override
    public int getContentView() {
        return R.layout.puti_class_schedule_acitivity;
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
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        headview.setTitle("班级课表");
    }

    @Override
    public void Star() {
        queryClass();
    }

    private void setClassName(String name) {
        fillterClass.setText(name);
    }

    public void queryClass(){
        PutiTeacherModel.getInstance().getClass("",new BaseListener(ClassSimple.class){
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mClassList = (ArrayList<ClassSimple>) listObj;
                //默认拉取第一个班级的课表
                setClassName(mClassList.get(0).getName());
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
        final CommonDropView dropView = new CommonDropView(this, view, list);
        dropView.setPopOnItemClickListener(new CommonDropView.PopOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String uid = mClassList.get(position).getUID();
                String name = mClassList.get(position).getName();
                setClassName(name);

                dropView.dismiss();
            }
        });
        dropView.showAsDropDown(view);
    }


    @OnClick(R.id.fillter_class)
    public void onClick() {
        showClassDialog(fillterClass);
    }
}
