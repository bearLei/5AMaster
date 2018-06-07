package unit.moudle.home;

import android.widget.LinearLayout;
import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import butterknife.BindView;
import unit.util.StatusBarUtil;

/**
 * Created by lei on 2018/6/4.
 */

public class HomeActivity extends PutiActivity {
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    public int getContentView() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.puti_base_actitivity_container;
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
        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void Star() {

    }
}
