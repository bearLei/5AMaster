package com.puti.education.ui.uiTeacher;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.App;
import com.puti.education.appupdate.AppUpdateUtil;
import com.puti.education.bean.TeacherPersonInfo;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.CommonModel;
import com.puti.education.netFrame.netModel.TeacherModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.fragment.HomeSchoolSynergyFragment;
import com.puti.education.ui.fragment.SurveyFragment;
import com.puti.education.ui.fragment.TeacherEventListFragment;
import com.puti.education.ui.fragment.TeacherHomeFragment;
import com.puti.education.ui.fragment.TeacherMineFragment;
import com.puti.education.ui.fragment.TeacherSurveyOnlineFragment;
import com.puti.education.R;
import com.puti.education.ui.fragment.TeacherSynergyFragment;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

/**
 * 家长模块入口
 */

public class TeacherMainActivity extends BaseActivity {

    private FragmentTabHost mTabHost;
    private Class[] mFragments = new Class[]{TeacherHomeFragment.class, TeacherEventListFragment.class,
            SurveyFragment.class, TeacherSynergyFragment.class, TeacherMineFragment.class};
    private int[] mTabSelectors = new int[]{R.drawable.main_bottom_tab_home, R.drawable.main_bottom_tab_event,
            R.drawable.main_bottom_tab_survey, R.drawable.main_bottom_tab_cooperation, R.drawable.main_bottom_tab_mine};
    private String[] mTabSpecs = new String[]{"thome", "event", "survey", "cooperation", "mine"};
    private String[] mTabName = null;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_teacher_main;
    }

    @Override
    public void initVariables() {

        mTabName = new String[]{this.getString(R.string.navi_home), this.getString(R.string.navi_event),
                this.getString(R.string.navi_survey_online),this.getString(R.string.navi_cooperation), this.getString(R.string.navi_mine)};

    }

    private void addTab() {
        for (int i = 0; i < 5; i++) {
            View tabIndicator = getLayoutInflater().inflate(R.layout.tab_indicator, null);
            ImageView imageView = (ImageView) tabIndicator.findViewById(R.id.img_location);
            imageView.setImageResource(mTabSelectors[i]);
            TextView tv_name = (TextView) tabIndicator.findViewById(R.id.tab_name);
            tv_name.setText(mTabName[i]);
            mTabHost.addTab(mTabHost.newTabSpec(mTabSpecs[i]).setIndicator(tabIndicator), mFragments[i], null);
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadData() {
        int roletype = ConfigUtil.getInstance(this).get(Constant.KEY_ROLE_TYPE, -1);
        if (roletype == Constant.ROLE_TEACHER){
            String userUid = ConfigUtil.getInstance(this).get(Constant.KEY_USER_ID, "");
            getTeacherDetail(userUid);
        }
        AppUpdateUtil.g().queryVersion(this);
    }

    public void gotoReview(){
        mTabHost.setCurrentTab(2);
    }

    public void gotoTab(int tab){
        mTabHost.setCurrentTab(tab);
    }

    private void getTeacherDetail(String teacherId){
        if (TextUtils.isEmpty(teacherId)){
            return;
        }

        disLoading();

        TeacherModel.getInstance().getTeacherInfo(new BaseListener(TeacherPersonInfo.class){
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                hideLoading();
                if (infoObj != null){
                    TeacherPersonInfo bean = (TeacherPersonInfo) infoObj;
                    ConfigUtil.getInstance(TeacherMainActivity.this).put(Constant.KEY_IS_STUDENT_AFFAIRS, bean.bStudentAffairs);
                    ConfigUtil.getInstance(TeacherMainActivity.this).commit();
                    if (bean != null && bean.classList != null){
                        App mApp = (App)TeacherMainActivity.this.getApplication();
                        mApp.setClassList(bean.classList);
                    }

                }else{
                    ToastUtil.show("获取教师详情失败");
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                hideLoading();
                ToastUtil.show(errorMessage == null ? Constant.REQUEST_FAILED_STR:errorMessage);
            }
        });
    }


}
