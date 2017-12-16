package com.puti.education.nation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lei on 2017/12/16.
 */

public class NationChooseActivity extends BaseActivity {

    @BindView(R.id.gridView)
    GridView gridView;

    private NationChooseAdapter mAdapter;
    private List<String> mList;
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_nation_choose;
    }

    @Override
    public void initVariables() {
        if (mList == null){
            mList = new ArrayList<>();
        }
        String[] items = getResources().getStringArray(R.array.nation_list);
        mList.clear();
        mList.addAll(Arrays.asList(items));
    }

    @Override
    public void initViews() {
        if (mAdapter == null){
            mAdapter = new NationChooseAdapter(this,mList);
        }
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = mList.get(position);
                Intent intent = new Intent();
                intent.putExtra("result",result);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }
}
