package unit.moudle.classevent.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.DealEntity;
import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/26.
 */

public class ClassEventDetailHolder extends BaseHolder<DealEntity> {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.decuct_score)
    TextView decuctScore;
    @BindView(R.id.notify_layout)
    FlexboxLayout notifyLayout;
    @BindView(R.id.punish_layout)
    FlexboxLayout punishLayout;
    @BindView(R.id.status_icon)
    ImageView statusIcon;

    public ClassEventDetailHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_class_event_detail_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, DealEntity data) {

    }
}
