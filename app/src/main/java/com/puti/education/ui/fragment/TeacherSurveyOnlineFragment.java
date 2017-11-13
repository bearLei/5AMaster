package com.puti.education.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.ui.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/18 0018.
 *
 * 教师 在线调查
 */

public class TeacherSurveyOnlineFragment extends BaseFragment{

    @BindView(R.id.title_textview)
    TextView mTitleTv;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_teacher_survey_online;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view) {
        mTitleTv.setText("在线调查");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void loadData() {

    }
}
