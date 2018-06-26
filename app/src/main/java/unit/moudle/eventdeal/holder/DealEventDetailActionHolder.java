package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

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

    private EventDealCallBack mEventDealCallBack;
    public DealEventDetailActionHolder(Context context) {
        super(context);
    }

    public DealEventDetailActionHolder(Context context, EventDealCallBack mEventDealCallBack) {
        super(context);
        this.mEventDealCallBack = mEventDealCallBack;
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
        name.setText(data.getStudentName());
        className.setText(data.getClassName());
    }

    @OnClick({R.id.reject, R.id.sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reject:
                if (mEventDealCallBack != null){
                    mEventDealCallBack.reject();
                }
                break;
            case R.id.sure:
                if (mEventDealCallBack != null){
                    mEventDealCallBack.sure();
                }
                break;
        }
    }
}
