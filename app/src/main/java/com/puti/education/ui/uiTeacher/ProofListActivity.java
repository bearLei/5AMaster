package com.puti.education.ui.uiTeacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.adapter.ProofListAdapter;
import com.puti.education.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 佐证列表
 */

public class ProofListActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private ProofListAdapter mProofListAdapter;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_proof_list;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews() {

        mTitleTv.setText("佐证记录");

        mProofListAdapter = new ProofListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mProofListAdapter);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadData() {

    }
}
