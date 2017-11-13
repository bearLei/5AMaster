package com.puti.education.ui.uiStudent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.StudentHomeFragment;
import com.puti.education.ui.fragment.StudentMineFragment;
import com.puti.education.ui.fragment.StudentReportListFragment;
import com.puti.education.ui.fragment.StudentSosFragment;
import com.puti.education.ui.fragment.SurveyFragment;

/**
 * 学生模块入口
 */

public class StudentMainActivity extends BaseActivity{

    private FragmentTabHost mTabHost;
    private Class[] mFragments = new Class[] {StudentHomeFragment.class ,SurveyFragment.class,
            StudentSosFragment.class, StudentReportListFragment.class ,StudentMineFragment.class};
    private int[] mTabSelectors = new int[] {R.drawable.main_bottom_tab_home , R.drawable.main_bottom_tab_survey,
            R.drawable.main_bottom_tab_sos, R.drawable.main_bottom_tab_report, R.drawable.main_bottom_tab_mine };
    private String[] mTabSpecs = new String[] {"thome", "survey", "sos", "report","mine" };
    private String[] mTabName = null;

    @Override
    public int getLayoutResourceId() {
        return R.layout.student_main_layout;
    }

    @Override
    public void initVariables() {
        mTabName = new String[] { this.getString(R.string.navi_home) ,
                this.getString(R.string.navi_survey),this.getString(R.string.navi_sos),
                this.getString(R.string.navi_report),this.getString(R.string.navi_mine) };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        addTab();

        int tabIndex  = this.getIntent().getIntExtra("tabindex", 0);
        mTabHost.setCurrentTab(tabIndex);
    }

    @Override
    public void loadData() {

    }

    private void addTab() {
        for (int i = 0; i < 5; i++) {
            View tabIndicator = getLayoutInflater().inflate(
                    R.layout.tab_indicator, null);
            ImageView imageView = (ImageView) tabIndicator
                    .findViewById(R.id.img_location);
            imageView.setImageResource(mTabSelectors[i]);
            TextView tv_name = (TextView)tabIndicator
                    .findViewById(R.id.tab_name);
            tv_name.setText(mTabName[i]);
            mTabHost.addTab(
                    mTabHost.newTabSpec(mTabSpecs[i])
                            .setIndicator(tabIndicator), mFragments[i], null);
        }
    }

    public void gotoReview(){
        mTabHost.setCurrentTab(1);
    }

}
