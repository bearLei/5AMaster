package unit.moudle.eventregist.ptr;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.puti.education.base.BaseMvpPtr;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;
import com.puti.education.zxing.ZxingUserInfo;
import com.puti.education.zxing.ZxingUtil;

import java.util.ArrayList;

import unit.api.PutiCommonModel;
import unit.base.BaseResponseInfo;
import unit.entity.Student;
import unit.moudle.eventregist.holder.ChooseStuHolder;
import unit.moudle.eventregist.holder.EventDescHolder;
import unit.moudle.eventregist.holder.EventEvidenceHolder;
import unit.moudle.eventregist.holder.EventTimeAndSpaceHolder;
import unit.moudle.eventregist.view.AddEventDetailView;

/**
 * Created by lei on 2018/6/11.
 */

public class AddEventDetailPtr implements BaseMvpPtr {

    private Context mContext;
    private AddEventDetailView mView;

    private ChooseStuHolder mChooseStuHolder;//选择学生holder
    private EventTimeAndSpaceHolder mTimeAndSpaceHolder;//时间和地点Holder
    private EventDescHolder mDescHolder;//描述holder
    private EventEvidenceHolder mEvidenceHolder;//佐证holder


    private ArrayList<Student> mChooseStuList;
    public AddEventDetailPtr(Context mContext, AddEventDetailView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }



    @Override
    public void star() {
        initChooseStuHolder();
        initTimeAndSpaceHolder();
        initDescHolder();
        initEvidenceHolder();
    }

    @Override
    public void stop() {

    }

    /**
     * 初始化选择学生holder
     */
    private void initChooseStuHolder(){
        if (mChooseStuHolder == null){
            mChooseStuHolder = new ChooseStuHolder(mContext);
            mChooseStuHolder.setList(new ArrayList<Student>());
        }
        View view = mChooseStuHolder.getRootView();
        oprateSize(view);
        mView.addChooseStuView(view);
    }

    /**
     * 初始化时间地点holder
     */
    private void initTimeAndSpaceHolder(){
        if (mTimeAndSpaceHolder == null){
            mTimeAndSpaceHolder = new EventTimeAndSpaceHolder(mContext);
            mTimeAndSpaceHolder.setData(true);
        }
        View view = mTimeAndSpaceHolder.getRootView();
        oprateSize(view);
        mView.addTimeAndSpaceView(view);
    }

    /**
     * 初始化描述holder
     */
    private void initDescHolder(){
        if (mDescHolder == null){
            mDescHolder = new EventDescHolder(mContext);
        }
        View view = mDescHolder.getRootView();
        oprateSize(view);
        mView.addDescView(view);
    }

    /**
     * 初始化佐证holder
     */
    private void initEvidenceHolder(){
        if (mEvidenceHolder == null){
            mEvidenceHolder = new EventEvidenceHolder(mContext);
        }
        View view = mEvidenceHolder.getRootView();
        oprateSize(view);
        mView.addEvidenceView(view);
    }

    private void oprateSize(View view){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
    }

    public void evidenceActivityResult(int requestCode, int resultCode, Intent data){
        if (mEvidenceHolder != null){
            mEvidenceHolder.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setChooseStu(ArrayList<Student> list){
        if (mChooseStuList == null){
            mChooseStuList = new ArrayList<>();
        }
//        mChooseStuList.clear();

        mChooseStuList.addAll(list);
        mChooseStuHolder.setList(mChooseStuList);
    }

    public void addEvent(){
        addEvent(mTimeAndSpaceHolder.getTime(),
                mTimeAndSpaceHolder.getPlaceUid(),
                mTimeAndSpaceHolder.getAddress(),
                mDescHolder.getDesc(),mView.getEventType());
    }

    /**
     *
     * @param Time 时间
     * @param PlaceUID 地点id
     * @param Address 地点
     * @param Description 事件描述
     * @param EventType 事件类型
     */
    public void addEvent(String Time,String PlaceUID,
                         String Address,String Description,
                         String EventType){

            if (TextUtils.isEmpty(Time)){
                ToastUtil.show("请输入时间");
                return;
            }
        if (TextUtils.isEmpty(Address)){
            ToastUtil.show("请输入地点");
            return;
        }
        if (TextUtils.isEmpty(Description)){
            ToastUtil.show("事件描述不能为空");
            return;
        }
        if (mChooseStuList == null || mChooseStuList.size() == 0){
            ToastUtil.show("请选择涉事学生");
            return;
        }
        String eventStr = buildJson(Time, PlaceUID, Address, Description, EventType);
        PutiCommonModel.getInstance().addEvent(eventStr,new BaseListener(BaseResponseInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show("提交失败："+errorMessage);
            }
        });


    }


    private String buildJson(String Time,String PlaceUID,
                             String Address,String Description,
                             String EventType){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Time",Time);
        jsonObject.put("PlaceUID",PlaceUID);
        jsonObject.put("Address",Address);
        jsonObject.put("Description",Description);
        jsonObject.put("EventType",EventType);

        JSONArray students = new JSONArray();
        for (Student student :mChooseStuList) {
            students.add(student.toString());
        }
        jsonObject.put("Students",students);

        jsonObject.put("Evidences",mEvidenceHolder.getEvidenceJson());

        return jsonObject.toString();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZxingUtil.g().onActivityResult(requestCode, resultCode, data, new ZxingUtil.ZxingCallBack() {
            @Override
            public void result(String result) {
                try {
                    ZxingUserInfo info = JSON.parseObject(result, ZxingUserInfo.class);
                    Student student = new Student();
                    student.setStudentName(info.Name);
                    student.setStudentUID(info.UID);
                    if (mChooseStuList == null){
                        mChooseStuList = new ArrayList<Student>();
                    }
                    mChooseStuList.add(student);
                    mChooseStuHolder.setList(mChooseStuList);
                }catch (JSONException e){
                    LogUtil.d("lei","二维码解析错误");
                }
            }

            @Override
            public void fail() {

            }
        });
    }
}
