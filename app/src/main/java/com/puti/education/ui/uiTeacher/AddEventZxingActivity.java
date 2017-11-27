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
import com.puti.education.ui.BaseActivity;
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
    @Override
    public int getLayoutResourceId() {
        return R.layout.add_event_zxing_activity;
    }

    @Override
    public void initVariables() {
        starZxing();
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
                Intent intent = new Intent(this, EventTypeChooseActivity.class);
                intent.putExtra(ZXING_LIST,(Serializable) mList);
                startActivity(intent);
                break;
            case R.id.zxing_sao:
                starZxing();
                break;
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
        eventAboutPeople.uid = info.UID;
        eventAboutPeople.name = info.Name;
        eventAboutPeople.isPeople = true;

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
        ZxingUtil.g().onActivityResult(requestCode, resultCode, data, new ZxingUtil.ZxingCallBack() {
            @Override
            public void result(String result) {
                try {
                    ZxingUserInfo info = JSON.parseObject(result, ZxingUserInfo.class);
                    EventAboutPeople eventAboutPeople = opearateInfo(info);
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
