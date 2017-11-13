package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.adapter.BasicRecylerAdapter;
import com.puti.education.adapter.InvolvedPeopleChooseListAdapter;
import com.puti.education.bean.EventDeailInvolvedPeople;
import com.puti.education.bean.EventDetailBean;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Key;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/20 0020.
 *
 * 涉事人选择
 *
 */

public class InvolvedPeopleChooseActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.involved_people_rv)
    RecyclerView mRv;

    List<EventDetailBean> list;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_involved_people_list;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews() {
        mTitleTv.setText("选择");

        list = (List<EventDetailBean>)getIntent().getSerializableExtra(Key.BEAN);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        InvolvedPeopleChooseListAdapter adapter = new InvolvedPeopleChooseListAdapter(this);
        mRv.setAdapter(adapter);
//        adapter.setDataList(list);
//        adapter.setMyItemOnclickListener(new BasicRecylerAdapter.MyItemOnclickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent();
//                intent.putExtra(Key.BEAN,list.get(position));
//                setResult(CommitAuditActivity.RESULT_CHOOSE_INVOLVED_PEOPLE,intent);
//                finish();
//            }
//        });
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.back_frame)
    public void finishActiviytClick(){
        finish();
    }
}
