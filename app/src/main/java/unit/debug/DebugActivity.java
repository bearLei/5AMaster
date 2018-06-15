package unit.debug;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.moudle.eventregist.PutiChooseStuActivity;

/**
 * Created by lei on 2018/6/14.
 */

public class DebugActivity extends PutiActivity {
    @BindView(R.id.test_choose_stu)
    TextView testChooseStu;

    @Override
    public int getContentView() {
        return R.layout.puti_debug_activity;
    }

    @Override
    public void BindPtr() {

    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {

    }

    @Override
    public void Star() {

    }


    @OnClick(R.id.test_choose_stu)
    public void onClick() {
        startActivity(new Intent(this,PutiChooseStuActivity.class));
    }
}
