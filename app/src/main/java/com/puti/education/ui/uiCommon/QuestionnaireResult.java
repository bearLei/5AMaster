package com.puti.education.ui.uiCommon;

import android.text.TextUtils;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;

import org.w3c.dom.Text;

import butterknife.BindView;


/**
 * Created by icebery on 2017/5/19 0019.
 */

public class QuestionnaireResult extends BaseActivity {
    @BindView(R.id.tv_result)
    TextView mTvResult;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_qt_result_layout;
    }

    @Override
    public void initVariables() {
        String result = this.getIntent().getStringExtra("result");
        if (TextUtils.isEmpty(result)){
            mTvResult.setText("目前还没有结果");
        }else{
            mTvResult.setText(result);
        }
    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {

    }
}

