package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Event2Involved;
import unit.moudle.eventdeal.callback.EventDealCallBack;

/**
 * Created by lei on 2018/6/22.
 * 事件确认详情-涉事学生holder
 */

public class DealEventDetailPeopleHolder extends BaseHolder<Event2Involved> implements EventDealCallBack {

    @BindView(R.id.action_layout)
    LinearLayout actionLayout;
    @BindView(R.id.deduct_layout)
    LinearLayout deductLayout;
    @BindView(R.id.notify_layout)
    LinearLayout notifyLayout;
    @BindView(R.id.punish_layout)
    LinearLayout punishLayout;

    private DealEventDetailActionHolder mDealEventDetailActionHolder;
    private DealEventDetailDeductHolder mDealEventDetailDeductHolder;
    private DealEventDetailNotifyHolder mDealEventDetailNotifyHolder;
    private DealEventDetailPunishHolder mDealEventDetailPunishHolder;

    public DealEventDetailPeopleHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_detail_people_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Event2Involved data) {
        if (mDealEventDetailActionHolder == null){
            mDealEventDetailActionHolder = new DealEventDetailActionHolder(context,this);
        }
        if (mDealEventDetailDeductHolder == null){
            mDealEventDetailDeductHolder = new DealEventDetailDeductHolder(context);
        }
        if (mDealEventDetailNotifyHolder == null){
            mDealEventDetailNotifyHolder = new DealEventDetailNotifyHolder(context);
        }
        if (mDealEventDetailPunishHolder == null){
            mDealEventDetailPunishHolder = new DealEventDetailPunishHolder(context);
        }

        mDealEventDetailActionHolder.setData(data);
        mDealEventDetailDeductHolder.setData(data);
        mDealEventDetailNotifyHolder.setData(data);
        mDealEventDetailPunishHolder.setData(data);

        actionLayout.removeAllViews();
        deductLayout.removeAllViews();
        notifyLayout.removeAllViews();
        punishLayout.removeAllViews();

        actionLayout.addView(oprateView(mDealEventDetailActionHolder.getRootView()));
        deductLayout.addView(oprateView(mDealEventDetailDeductHolder.getRootView()));
        notifyLayout.addView(oprateView(mDealEventDetailNotifyHolder.getRootView()));
        punishLayout.addView(oprateView(mDealEventDetailPunishHolder.getRootView()));
    }

    private View oprateView(View view){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return view;
    }

    @Override
    public void reject() {

    }

    @Override
    public void sure() {

    }

    /**'
     *
     * {
     "Event2InvolvedUID": "string",
     "Confirm": true,
     "Reason": "string",
     "Punishment": "string",
     "NeedValid": true,
     "NeedParentNotice": true,
     "NeedPsycholog": true,
     "PunishmentInfo": "string",
     "Score": 0
     }
     */
    private String buildDealJson(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"Event2InvolvedUID\":\"").append("").append("\"");
        sb.append(",\"Confirm\":\"").append("").append("\"");
        sb.append("\"Reason\":\"").append("").append("\"");
        sb.append(",\"Punishment\":\"").append("").append("\"");
        sb.append("\"NeedValid\":\"").append("").append("\"");
        sb.append(",\"NeedParentNotice\":\"").append("").append("\"");
        sb.append(",\"NeedPsycholog\":\"").append("").append("\"");
        sb.append("\"PunishmentInfo\":\"").append("").append("\"");
        sb.append(",\"Score\":\"").append("").append("\"");
        sb.append("}");
        return sb.toString();
    }

    //批量处理事件
    private String buildDealsJson(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"EventUID\":\"").append("").append("\"");
        sb.append(",\"Confirm\":\"").append("").append("\"");
        sb.append("\"Reason\":\"").append("").append("\"");
        sb.append(",\"Punishment\":\"").append("").append("\"");
        sb.append("\"NeedValid\":\"").append("").append("\"");
        sb.append(",\"NeedParentNotice\":\"").append("").append("\"");
        sb.append(",\"NeedPsycholog\":\"").append("").append("\"");
        sb.append("\"PunishmentInfo\":\"").append("").append("\"");
        sb.append(",\"Score\":\"").append("").append("\"");
        sb.append("}");
        return sb.toString();
    }

}
