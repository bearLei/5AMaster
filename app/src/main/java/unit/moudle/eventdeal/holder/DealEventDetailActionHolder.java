package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.Event2Involved;
import unit.moudle.eventdeal.callback.EventDealCallBack;

/**
 * Created by lei on 2018/6/25.
 * 事件确认详情-驳回-确认holder
 */

public class DealEventDetailActionHolder extends BaseHolder<Event2Involved> {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.reject)
    TextView reject;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.action_layout)
    LinearLayout actionLayout;

    private EventDealCallBack mEventDealCallBack;

    private boolean isBatch;//是否批量

    public DealEventDetailActionHolder(Context context) {
        super(context);
    }


    public DealEventDetailActionHolder(Context context, EventDealCallBack mEventDealCallBack) {
        super(context);
        this.mEventDealCallBack = mEventDealCallBack;
    }

    public DealEventDetailActionHolder(Context context, EventDealCallBack mEventDealCallBack, boolean isBatch) {
        super(context);
        this.mEventDealCallBack = mEventDealCallBack;
        this.isBatch = isBatch;
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_action_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Event2Involved data) {
        if (isBatch){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dip2px(mContext,40));
            params.gravity = Gravity.CENTER;
            actionLayout.setLayoutParams(params);
        }

        if (!isBatch) {
            name.setText(data.getStudentName());
            className.setText(data.getClassName());
            name.setVisibility(View.VISIBLE);
            className.setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.GONE);
            className.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.reject, R.id.sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reject:
                if (mEventDealCallBack != null) {
                    mEventDealCallBack.reject();
                }
                break;
            case R.id.sure:
                if (mEventDealCallBack != null) {
                    mEventDealCallBack.sure();
                }
                break;
        }
    }
}
