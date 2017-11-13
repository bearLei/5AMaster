package com.puti.education.ui.uiPatriarch;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.HomeSchoolSynergyFragment;
import com.puti.education.ui.fragment.ParentHomeFragment;
import com.puti.education.ui.fragment.PatriarchMineFragment;
import com.puti.education.ui.fragment.SurveyFragment;

/**
 * 家长模块入口
 */
public class PatriarchMainActivity extends BaseActivity {

    private FragmentTabHost mTabHost;
    private Class[] mFragments = new Class[] {ParentHomeFragment.class ,SurveyFragment.class,
            HomeSchoolSynergyFragment.class ,PatriarchMineFragment.class};
    private int[] mTabSelectors = new int[] {R.drawable.main_bottom_tab_home , R.drawable.main_bottom_tab_survey,
            R.drawable.main_bottom_tab_cooperation, R.drawable.main_bottom_tab_mine };
    private String[] mTabSpecs = new String[] {"thome", "survey", "cooperation", "mine" };
    private String[] mTabName = null;

    @Override
    public int getLayoutResourceId() {
        return R.layout.student_main_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public void initVariables() {
        mTabName = new String[] { this.getString(R.string.navi_home) ,
                this.getString(R.string.navi_survey),
                this.getString(R.string.navi_cooperation),this.getString(R.string.navi_mine) };
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
        for (int i = 0; i < 4; i++) {
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
