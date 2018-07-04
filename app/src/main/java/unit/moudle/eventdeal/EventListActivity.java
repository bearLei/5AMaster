package unit.moudle.eventdeal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.Event;
import unit.moudle.eventdeal.adapter.EventsSureAdapter;
import unit.moudle.eventdeal.ptr.EventListPtr;
import unit.moudle.eventdeal.view.EventListView;
import unit.widget.EmptyView;
import unit.widget.HeadView;
import unit.widget.LoadingView;
import unit.widget.SpaceItemDecoration;

/**
 * Created by lei on 2018/6/17.
 */

public class EventListActivity extends PutiActivity implements EventListView {
    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.desc)
    TextView TDesc;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.empty_view)
    EmptyView emptyView;
    @BindView(R.id.loading_view)
    LoadingView loadingView;

    private EventListPtr mPtr;
    private EventsSureAdapter mAdapter;
    private ArrayList<Event> mData;

    @Override
    public int getContentView() {
        return R.layout.puti_event_list;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new EventListPtr(this, this);
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
        headview.setTitle("事件确认");

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new EventsSureAdapter(this, mData);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(ViewUtils.dip2px(this, 15)));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        mPtr.star();
        showLoading();
    }

    @Override
    public void setDesc(String desc) {
        TDesc.setText(desc);
    }

    @Override
    public void setClassName(String name) {
        className.setText(name);
    }

    @Override
    public void success(ArrayList<Event> events) {
        hideLoading();
        if (events == null ||events.size() == 0){
            showEmptyView();
        }
        mData.clear();
        mData.addAll(events);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.class_name)
    public void onClick() {
        mPtr.showClassDialog(className);
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showErrorDataView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.showNoDataView("暂无数据");
    }

}
