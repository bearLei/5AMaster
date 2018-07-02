package unit.moudle.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.PutiFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lei on 2018/6/6.
 * 首页
 */

public class HomeFragment extends PutiFragment implements HomeView {


    @BindView(R.id.home_head_layout)
    LinearLayout VHeadLayout;
    @BindView(R.id.count_layout)
    LinearLayout VCountLayout;
    @BindView(R.id.power_layout)
    LinearLayout VPowerLayout;
    @BindView(R.id.feedback_layout)
    LinearLayout VFeedbackLayout;
    @BindView(R.id.tool_layout)
    LinearLayout VToolLayout;

    private HomePtr mPtr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) InflateService.g().inflate(R.layout.puti_home_fragment);
        ButterKnife.bind(this, rootView);
        bindPtr();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPtr.stop();
    }

    private void bindPtr(){
        if (mPtr == null){
            mPtr = new HomePtr(getActivity(),this);
        }
        star();
    }

    private void star(){
        mPtr.star();
    }

    /*********************************/

    @Override
    public void addHeadLayout(View view) {
        VHeadLayout.removeAllViews();
        VHeadLayout.addView(view);
    }

    @Override
    public void addCountLayout(View view) {
        VCountLayout.removeAllViews();
        VCountLayout.addView(view);
    }

    @Override
    public void addPowerLayout(View view) {
        VPowerLayout.removeAllViews();
        VPowerLayout.addView(view);
    }

    @Override
    public void addFeedBackLayout(View view) {
        VFeedbackLayout.removeAllViews();
        VFeedbackLayout.addView(view);
    }

    @Override
    public void addToolLayout(View view) {
        VToolLayout.removeAllViews();
        VToolLayout.addView(view);
    }



}
