package unit.moudle.classevent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.Event;
import unit.moudle.classevent.adapter.ClassEventAdapter;
import unit.moudle.classevent.ptr.ClassEventPtr;
import unit.moudle.classevent.view.ClassEventView;
import unit.widget.HeadView;
import unit.widget.SpaceItemDecoration;

/**
 * Created by lei on 2018/6/26.
 * 班级事件页面
 */

public class PutiClassEventActivity extends PutiActivity implements ClassEventView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.status_choose)
    LinearLayout VStatusChoose;
    @BindView(R.id.class_choose)
    LinearLayout VClassChoose;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.status)
    TextView TStatus;
    @BindView(R.id.class_name)
    TextView TClassName;


    private ClassEventPtr mPtr;
    private ArrayList<Event> mData;
    private ClassEventAdapter mAdapter;
    @Override
    public int getContentView() {
        return R.layout.puti_class_event_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new ClassEventPtr(this, this);
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
        headview.setTitle("班级事件");

        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mAdapter == null){
            mAdapter = new ClassEventAdapter(this,mData);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(ViewUtils.dip2px(this,10)));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        mPtr.star();
    }


    @OnClick({R.id.status_choose, R.id.class_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.status_choose:
                mPtr.showStatusDialog(this,VStatusChoose);
                break;
            case R.id.class_choose:
                mPtr.showClassDialog(VClassChoose);
                break;
        }
    }

    @Override
    public void setStatus(String status) {
        TStatus.setText(status);
    }

    @Override
    public void setClassName(String className) {
        TClassName.setText(className);
    }

    @Override
    public void succuess(ArrayList<Event> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

}
