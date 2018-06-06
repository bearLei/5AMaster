package unit.home.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.HomeCountEntity;

/**
 * Created by lei on 2018/6/6.
 * 首页用户统计信息
 */

public class HomeCountHolder extends BaseHolder<HomeCountEntity> implements View.OnClickListener {

    @BindView(R.id.week_new_event)
    TextView weekNewEvent;
    @BindView(R.id.my_post)
    TextView myPost;
    @BindView(R.id.forward_personal_info)
    LinearLayout VForwardPersonalInfo;

    public HomeCountHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_home_count_holder);
        ButterKnife.bind(this,mRootView);
        VForwardPersonalInfo.setOnClickListener(this);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, HomeCountEntity data) {

       if (data == null){
           return;
       }
        weekNewEvent.setText(String.valueOf(data.getWeekCount()));
        myPost.setText(String.valueOf(data.getMyPost()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forward_personal_info:
                // TODO: 2018/6/6 跳转个人信息
                break;
        }
    }
}
