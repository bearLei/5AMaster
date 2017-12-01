package com.puti.education.ui.uiTeacher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.puti.education.R;
import com.puti.education.adapter.EventAboutPeopleAdapter;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.speech.SpeechUtil;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.uiCommon.EventDutyChooseActivity;
import com.puti.education.ui.uiCommon.EventTypeChooseActivity;
import com.puti.education.util.LogUtil;
import com.puti.education.widget.GridViewForScrollView;
import com.puti.education.zxing.ZxingUserInfo;
import com.puti.education.zxing.ZxingUtil;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by lei on 2017/11/16.
 */

public class AddEventZxingActivity extends BaseActivity implements View.OnClickListener {
    public static final String ZXING_LIST = "zxing_list";//二维码扫描的结果列表


    @BindView(R.id.involved_people_grid)
    GridViewForScrollView mGridView;
    @BindView(R.id.ok)
    TextView mOk;
    @BindView(R.id.goon)
    TextView mGoOn;
    @BindView(R.id.zxing_sao)
    ImageView mSao;

    private EventAboutPeopleAdapter mInvolvePeopleAdapter;
    private ArrayList<EventAboutPeople> mList  ;//知情人列表
    private int refer;// 1 是普通进入   2 教师端新建事件重新选人进入
    private boolean mbAbnormal ;//事件是否异常
    @Override
    public int getLayoutResourceId() {
        return R.layout.add_event_zxing_activity;
    }

    @Override
    public void initVariables() {
        starZxing();
        parseIntent();
    }

    private void parseIntent(){
        if (getIntent() != null){
            refer = getIntent().getIntExtra("refer",1);
            mbAbnormal = getIntent().getBooleanExtra("eventType",false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initViews() {
        mGridView.setVisibility(View.GONE);
        mSao.setVisibility(View.VISIBLE);

        mSao.setOnClickListener(this);
        mOk.setOnClickListener(this);
        mGoOn.setOnClickListener(this);
        if (mInvolvePeopleAdapter == null){
            mInvolvePeopleAdapter = new EventAboutPeopleAdapter(this);
        }
        if (mList == null){
            mList = new ArrayList<>();
        }
        mInvolvePeopleAdapter.setmList(mList);
        mGridView.setAdapter(mInvolvePeopleAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mList.remove(position);
                   refresh();
            }
        });
    }

    @Override
    public void loadData() {

    }

    private void starZxing(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddEventZxingActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }else {
            ZxingUtil.g().startZxing(this);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.goon:
                starZxing();
                break;
            case R.id.ok:
                if (refer == 2){
                    if (mbAbnormal) {
                        Intent intent = new Intent(this, EventDutyChooseActivity.class);
                        intent.putExtra(ZXING_LIST, (Serializable) mList);
                        intent.putExtra("refer", 2);
                        intent.putExtra("type", 3);
                        startActivityForResult(intent, TeacherAddEventActivity.CODE_ZXING);
                    }else {
                        Intent intent = new Intent();
                        opearteLDutyist("1", "主要责任人");
                        intent.putExtra(AddEventZxingActivity.ZXING_LIST, (Serializable) mList);
                        setResult(TeacherAddEventActivity.CODE_ZXING,intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(this, EventTypeChooseActivity.class);
                    intent.putExtra(ZXING_LIST, (Serializable) mList);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.zxing_sao:
                starZxing();
                break;
        }
    }
    private void opearteLDutyist(String duty,String dutyName){
        if (mList != null && mList.size() > 0){
            for (int i = 0; i < mList.size(); i++) {
                EventAboutPeople eventAboutPeople =mList.get(i);
                eventAboutPeople.dutyType = duty;
                eventAboutPeople.involveType = dutyName;
            }
        }
    }
    /**
     *
     * @param info 二维码扫描结果
     * @return 转换成可以识别的 学生信息
     */
    private EventAboutPeople opearateInfo(ZxingUserInfo info){
        if (info == null) return null;
        EventAboutPeople eventAboutPeople = new EventAboutPeople();
        eventAboutPeople.uid = info.Personnel_UID;
        eventAboutPeople.name = info.Name;
        eventAboutPeople.isPeople = true;
        eventAboutPeople.type = info.Personnel_type;
        return eventAboutPeople;
    }
    private void refresh(){
        if (mList.size() > 0){
            mGridView.setVisibility(View.VISIBLE);
            mSao.setVisibility(View.GONE);
        }else {
            mGridView.setVisibility(View.GONE);
            mSao.setVisibility(View.VISIBLE);
        }
        mInvolvePeopleAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ZxingUtil.REQUEST_CODE:
                ZxingUtil.g().onActivityResult(requestCode, resultCode, data, new ZxingUtil.ZxingCallBack() {
                    @Override
                    public void result(String result) {
                        try {
                            ZxingUserInfo info = JSON.parseObject(result, ZxingUserInfo.class);
//                    //不是学生就不处理了
                            if (info.Personnel_type != 2){
                                return;
                            }
                            EventAboutPeople eventAboutPeople = opearateInfo(info);
                            for (int i = 0; i < mList.size(); i++) {
                                EventAboutPeople tempPeople = mList.get(i);
                                if (tempPeople.uid.equals(eventAboutPeople.uid)) return;
                            }
                            mList.add(eventAboutPeople);
                            refresh();
                        }catch (JSONException e){
                            LogUtil.d("lei","二维码解析错误");
                        }
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
            case TeacherAddEventActivity.CODE_ZXING:
                setResult(TeacherAddEventActivity.CODE_ZXING,data);
                finish();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                starZxing();

            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
