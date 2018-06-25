package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/25.
 * 事件确认详情-扣分holder
 */

public class DealEventDetailDeductHolder extends BaseHolder<Event2Involved> {

    @BindView(R.id.score_one)
    TextView scoreOne;
    @BindView(R.id.score_two)
    TextView scoreTwo;
    @BindView(R.id.score_three)
    TextView scoreThree;
    @BindView(R.id.edit_score)
    EditText editScore;
    @BindView(R.id.refer_score)
    TextView referScore;

    public DealEventDetailDeductHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_deduct_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Event2Involved data) {

    }

    @OnClick({R.id.score_one, R.id.score_two, R.id.score_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.score_one:
                break;
            case R.id.score_two:
                break;
            case R.id.score_three:
                break;
        }
    }
}
