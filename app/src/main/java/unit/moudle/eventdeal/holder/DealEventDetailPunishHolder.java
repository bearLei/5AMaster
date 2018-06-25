package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

/**
 * Created by lei on 2018/6/25.
 * 事件确认详情-惩处older
 */

public class DealEventDetailPunishHolder extends BaseHolder<Event2Involved> {
    @BindView(R.id.warning)
    TextView warning;
    @BindView(R.id.serious_warnig)
    TextView seriousWarnig;
    @BindView(R.id.gig)
    TextView gig;
    @BindView(R.id.big_gig)
    TextView bigGig;
    @BindView(R.id.detention)
    TextView detention;
    @BindView(R.id.discourag)
    TextView discourag;
    @BindView(R.id.drop_out_school)
    TextView dropOutSchool;

    private boolean mSelectedWarning;
    private boolean mSelectedSeriousWarnig;
    private boolean mSelectedGig;
    private boolean mSelectedBigGig;
    private boolean mSelectedDetention;
    private boolean mSelectedDiscourag;
    private boolean mSelectedDropOutSchool;

    public DealEventDetailPunishHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_punish_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Event2Involved data) {

    }

    @OnClick({R.id.warning, R.id.serious_warnig, R.id.gig, R.id.big_gig, R.id.detention, R.id.discourag, R.id.drop_out_school})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.warning:
                mSelectedWarning = !mSelectedWarning;
                oprateDrawble(warning,mSelectedWarning);
                break;
            case R.id.serious_warnig:
                mSelectedSeriousWarnig = !mSelectedSeriousWarnig;
                oprateDrawble(seriousWarnig,mSelectedSeriousWarnig);
                break;
            case R.id.gig:
                mSelectedGig = !mSelectedGig;
                oprateDrawble(gig,mSelectedGig);
                break;
            case R.id.big_gig:
                mSelectedBigGig = !mSelectedBigGig;
                oprateDrawble(bigGig,mSelectedBigGig);
                break;
            case R.id.detention:
                mSelectedDetention = !mSelectedDetention;
                oprateDrawble(detention,mSelectedDetention);
                break;
            case R.id.discourag:
                mSelectedDiscourag = !mSelectedDiscourag;
                oprateDrawble(discourag,mSelectedDiscourag);
                break;
            case R.id.drop_out_school:
                mSelectedDropOutSchool = !mSelectedDropOutSchool;
                oprateDrawble(dropOutSchool,mSelectedDropOutSchool);
                break;
        }
    }

    private void oprateDrawble(TextView view,boolean selected){
        Drawable drawable;
        if (selected){
            drawable = mContext.getResources().getDrawable(R.drawable.puti_check_39bca1);
        }else {
            drawable = mContext.getResources().getDrawable(R.drawable.puti_check_cdcdcd);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable,null,null,null);
    }

}
