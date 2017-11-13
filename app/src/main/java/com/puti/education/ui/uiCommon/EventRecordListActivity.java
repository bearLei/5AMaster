package com.puti.education.ui.uiCommon;

import android.content.Intent;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by icebery on 2017/4/15 0015.
 */

public class EventRecordListActivity extends BaseActivity {

    @BindView(R.id.title_textview)
    TextView mUiTitle;
    @BindView(R.id.recycler_detail)
    TextView mRecyclerView;

    @OnClick(R.id.img_event_record)
    public void startEventRecord(){
        Intent intent = new Intent();
        intent.setClass(this, EventRecordListActivity.class);
        startActivity(intent);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.event_record_list_layout;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews() {
        mUiTitle.setText("问卷详情");
    }

    @Override
    public void loadData() {

    }

}
