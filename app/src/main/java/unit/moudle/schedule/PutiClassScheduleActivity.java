package unit.moudle.schedule;

import android.view.View;
import android.widget.*;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.response.PageInfo;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.CommonDropView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import unit.api.PutiCommonModel;
import unit.api.PutiTeacherModel;
import unit.entity.ClassSimple;
import unit.entity.CursorInfo;
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
    @BindView(R.id.list_header)
    ScheduleItemLayout listHeader;
    @BindView(R.id.list_monday)
    ScheduleItemLayout listMonday;
    @BindView(R.id.list_thusday)
    ScheduleItemLayout listThusday;
    @BindView(R.id.list_wednesday)
    ScheduleItemLayout listWednesday;
    @BindView(R.id.list_thursday)
    ScheduleItemLayout listThursday;
    @BindView(R.id.list_friday)
    ScheduleItemLayout listFriday;
    @BindView(R.id.list_saturday)
    ScheduleItemLayout listSaturday;
    @BindView(R.id.list_sunday)
    ScheduleItemLayout listSunday;


    private ArrayList<ClassSimple> mClassList;

    private ArrayList<CursorInfo> mHeadList;
    private ArrayList<CursorInfo> mMonDayList;
    private ArrayList<CursorInfo> mThusDayList;
    private ArrayList<CursorInfo> mWedNesDayList;
    private ArrayList<CursorInfo> mThursDayList;
    private ArrayList<CursorInfo> mFriDayList;
    private ArrayList<CursorInfo> mSaturDayList;
    private ArrayList<CursorInfo> mSunDayList;
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

        initList();

    }

    @Override
    public void Star() {
        queryClass();
    }

    private void setClassName(String name) {
        fillterClass.setText(name);
    }

    public void queryClass() {
        PutiTeacherModel.getInstance().getClass("", new BaseListener(ClassSimple.class) {
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                super.responseListResult(infoObj, listObj, pageInfo, code, status);
                mClassList = (ArrayList<ClassSimple>) listObj;
                //默认拉取第一个班级的课表
                setClassName(mClassList.get(0).getName());
                queryCoursInfo(mClassList.get(0).getUID());
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
                queryCoursInfo(uid);
                dropView.dismiss();
            }
        });
        dropView.showAsDropDown(view);
    }

    public void queryCoursInfo(String classId) {
        PutiCommonModel.getInstance().queryCoursInfo(classId, "", new BaseListener(CursorInfo.class) {
            @Override
            public void responseListResult(Object infoObj, Object listObj, PageInfo pageInfo, int code, boolean status) {
                ArrayList<CursorInfo> list = (ArrayList<CursorInfo>) listObj;
                buildData(list);
                StringBuilder builder = new StringBuilder();
                builder.append("本班本周共有").append(list.size()).append("节课");
                weekClass.setText(builder.toString());
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }

    @OnClick(R.id.fillter_class)
    public void onClick() {
        showClassDialog(fillterClass);
    }

    private void initList(){
        if (mHeadList == null){
            mHeadList = new ArrayList<>();
        }
        if (mMonDayList == null){
            mMonDayList = new ArrayList<>();
        }


        if (mThusDayList == null){
            mThusDayList = new ArrayList<>();
        }

        if (mWedNesDayList == null){
            mWedNesDayList = new ArrayList<>();
        }

        if (mThursDayList == null){
            mThursDayList = new ArrayList<>();
        }

        if (mFriDayList == null){
            mFriDayList = new ArrayList<>();
        }

        if (mSaturDayList == null){
            mSaturDayList = new ArrayList<>();
        }

        if (mSunDayList == null){
            mSunDayList = new ArrayList<>();
        }
    }


    private void buildData(ArrayList<CursorInfo> data){
        if (data == null || data.size() == 0) return;

        mHeadList.clear();
        mMonDayList.clear();
        mThusDayList.clear();
        mWedNesDayList.clear();
        mThursDayList.clear();
        mFriDayList.clear();
        mSaturDayList.clear();
        mSunDayList.clear();

        mMonDayList.add(buildDealt("星期一"));
        mThusDayList.add(buildDealt("星期二"));
        mWedNesDayList.add(buildDealt("星期三"));
        mThursDayList.add(buildDealt("星期四"));
        mFriDayList.add(buildDealt("星期五"));
        mSaturDayList.add(buildDealt("星期六"));
        mSunDayList.add(buildDealt("星期日"));

        for (int i = 0; i < data.size(); i++) {
            CursorInfo cursorInfo = data.get(i);
            switch (cursorInfo.getWeekday()){
                case 1:
                    mMonDayList.add(cursorInfo);
                    break;
                case 2:
                    mThusDayList.add(cursorInfo);
                    break;
                case 3:
                    mWedNesDayList.add(cursorInfo);
                    break;
                case 4:
                    mThursDayList.add(cursorInfo);
                    break;
                case 5:
                    mFriDayList.add(cursorInfo);
                    break;
                case 6:
                    mSaturDayList.add(cursorInfo);
                    break;
                case 7:
                    mSunDayList.add(cursorInfo);
                    break;
            }
        }

        for (int i = 0; i < 11; i++) {
            CursorInfo cursorInfo = new CursorInfo();
            cursorInfo.setCourseName(String.valueOf(i));
            mHeadList.add(cursorInfo);
        }
        while (mMonDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mMonDayList.add(cursorInfo);
        }
        while (mThusDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mThusDayList.add(cursorInfo);
        }
        while (mWedNesDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mWedNesDayList.add(cursorInfo);
        }
        while (mThursDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mThursDayList.add(cursorInfo);
        }
        while (mFriDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mFriDayList.add(cursorInfo);
        }
        while (mSaturDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mSaturDayList.add(cursorInfo);
        }
        while (mSunDayList.size() < 11){
            CursorInfo cursorInfo = new CursorInfo();
            mSunDayList.add(cursorInfo);
        }

//        listHeader.removeAllViews();
//        listMonday.removeAllViews();
//        listThusday.removeAllViews();
//        listWednesday.removeAllViews();
//        listThursday.removeAllViews();
//        listFriday.removeAllViews();
//        listSaturday.removeAllViews();
//        listSunday.removeAllViews();

        listHeader.setAdapter(mHeadList,true);
        listMonday.setAdapter(mMonDayList,false);
        listThusday.setAdapter(mThusDayList,false);
        listWednesday.setAdapter(mWedNesDayList,false);
        listThursday.setAdapter(mThursDayList,false);
        listFriday.setAdapter(mFriDayList,false);
        listSaturday.setAdapter(mSaturDayList,false);
        listSunday.setAdapter(mSunDayList,false);
    }

    private CursorInfo buildDealt(String title){
        CursorInfo cursorInfo = new CursorInfo();
        cursorInfo.setCourseName(title);
        return cursorInfo;
    }

}
