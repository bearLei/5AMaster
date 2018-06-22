package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.DealEventMain;
import unit.entity.EventMainTier;

/**
 * Created by lei on 2018/6/22.
 */

public class DealEventDetailHeadHolder extends BaseHolder<DealEventMain> {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.desc)
    TextView desc;

    public DealEventDetailHeadHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_head_holder);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRootView.setLayoutParams(params);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, DealEventMain data) {
        if (data == null){
            return;
        }
        time.setText(data.getTime());
        address.setText(data.getAddress());
        desc.setText(data.getDescription());
    }
}
