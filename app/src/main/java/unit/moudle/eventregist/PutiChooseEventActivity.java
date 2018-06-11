package unit.moudle.eventregist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.EventMainTier;
import unit.entity.EventTypeEntity;
import unit.moudle.eventregist.adapter.EventChooseAdapter;
import unit.moudle.eventregist.ptr.ChooseEventPtr;
import unit.moudle.eventregist.view.ChooseEventView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/8.
 * 事件登记-事件选择页面
 */

public class PutiChooseEventActivity extends PutiActivity implements ChooseEventView {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.headview)
    HeadView headview;

    private ChooseEventPtr mPtr;


    private ArrayList<EventMainTier> mData;
    private EventChooseAdapter mAdapter;
    @Override
    public int getContentView() {
        return R.layout.puti_choose_event_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null){
            mPtr = new ChooseEventPtr(this,this);
        }
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

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mAdapter == null){
            mAdapter = new EventChooseAdapter(this,mData);
        }
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void handleResult(ArrayList<EventMainTier> list) {
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<EventMainTier> getList() {
        return mData;
    }

    @Override
    public void putPullStatus(int position) {

    }

    @Override
    public void removePullStatus(int position) {

    }

    @Override
    public void setJumpMainPosition(int position) {

    }

    @Override
    public void setJumpSecondPosition(int position) {

    }
}
