package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

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
    TextView mSao;

    private EventAboutPeopleAdapter mInvolvePeopleAdapter;
    private ArrayList<EventAboutPeople> mList  ;//知情人列表
    @Override
    public int getLayoutResourceId() {
        return R.layout.add_event_zxing_activity;
    }

    @Override
    public void initVariables() {

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
        ZxingUtil.g().startZxing(this);
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

    private EventAboutPeople opearateInfo(ZxingUserInfo info){
        if (info == null) return null;
        EventAboutPeople eventAboutPeople = new EventAboutPeople();
        eventAboutPeople.uid = info.UID;
        eventAboutPeople.name = info.Name;

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
}