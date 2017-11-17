package com.puti.education.ui.uiTeacher;

import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.puti.education.R;
import com.puti.education.adapter.BaseCommonListAdapter;
import com.puti.education.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/11/16.
 */

public class AddEventZxingActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    LRecyclerView recyclerView;
    @BindView(R.id.ok)
    TextView mOk;
    @BindView(R.id.goon)
    TextView mGoOn;

    @Override
    public int getLayoutResourceId() {
        return R.layout.add_event_zxing_activity;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {

    }
}
