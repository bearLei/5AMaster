package com.puti.education.ui.uiTeacher.chooseperson;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.ui.BaseFragment;
import com.puti.education.ui.uiTeacher.chooseperson.mul.MulChooseFragment;
import com.puti.education.ui.uiTeacher.chooseperson.single.SingleChooseFragment;

/**
 * Created by len on 2017/12/30.
 * 教师端 异常事件选择人物列表的页面
 */

public class ChoosePersonListActivityNew extends BaseActivity  {

    private boolean mbAbnormal = false;  //是否异常事件，默认为普通事件
    private int refer;//页面启动来源
    private String mDutyType; //主要责任人，次要责任人，证人，知情人，举报人，

    @Override
    public int getLayoutResourceId() {
        return R.layout.choose_person_activity;
    }

    @Override
    public void initVariables() {
        ChoosePersonParameter.LIMIT = 0;
        parseIntent();
    }
    private LinearLayoutManager manager;
    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {

    }



    public void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mbAbnormal = intent.getBooleanExtra(ChoosePersonParameter.EVENT_ABNORMOL, false);
            //判断是否从责任等级处转入
            if (intent.hasExtra(ChoosePersonParameter.DUTY_TYPE)) {
                mDutyType = intent.getStringExtra(ChoosePersonParameter.DUTY_TYPE);
            }
            //页面启动来源
            if (intent.hasExtra(ChoosePersonParameter.REFER)){
                refer = intent.getIntExtra(ChoosePersonParameter.REFER,0);
            }
            if (refer != ChoosePersonParameter.REFER_DUTY_ALL ){
                BaseFragment fragment = SingleChooseFragment.newInstance(mbAbnormal,mDutyType,refer);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commitAllowingStateLoss();
            }else {
                BaseFragment fragment = MulChooseFragment.newInstance(mbAbnormal,mDutyType,refer);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commitAllowingStateLoss();
            }
        }
    }



}
