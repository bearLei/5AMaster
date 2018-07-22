package unit.moudle.eventdeal.holder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.api.PutiTeacherModel;
import unit.base.BaseResponseInfo;
import unit.entity.Event2Involved;
import unit.eventbus.DealEventDissEvent;
import unit.eventbus.PutiEventBus;
import unit.moudle.eventdeal.EventDealManager;
import unit.moudle.eventdeal.EventDetailActivity;
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
    @BindView(R.id.container)
    LinearLayout container;

    private DealEventDetailActionHolder mDealEventDetailActionHolder;
    private DealEventDetailDeductHolder mDealEventDetailDeductHolder;
    private DealEventDetailNotifyHolder mDealEventDetailNotifyHolder;
    private DealEventDetailPunishHolder mDealEventDetailPunishHolder;

    private String mEventUid;

    private boolean isBatch;//是否是批量

    public DealEventDetailPeopleHolder(Context context) {
        super(context);
    }

    public DealEventDetailPeopleHolder(Context context, boolean isBatch) {
        super(context);
        this.isBatch = isBatch;
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_deal_event_detail_people_holder);
        ButterKnife.bind(this, mRootView);
        if (mContext instanceof EventDetailActivity) {
            mEventUid = ((EventDetailActivity) mContext).getEventId();
        }
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Event2Involved data) {
        if (mDealEventDetailActionHolder == null) {
            mDealEventDetailActionHolder = new DealEventDetailActionHolder(context, this, isBatch);
        }
        if (mDealEventDetailDeductHolder == null) {
            mDealEventDetailDeductHolder = new DealEventDetailDeductHolder(context);
        }
        if (mDealEventDetailNotifyHolder == null) {
            mDealEventDetailNotifyHolder = new DealEventDetailNotifyHolder(context);
        }
        if (mDealEventDetailPunishHolder == null) {
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
        if (mData.getStatus() != 0) {
            actionLayout.addView(oprateView(mDealEventDetailActionHolder.getRootView()));
        } else {
            actionLayout.addView(oprateView(mDealEventDetailActionHolder.getRootView()));
            deductLayout.addView(oprateView(mDealEventDetailDeductHolder.getRootView()));
            notifyLayout.addView(oprateView(mDealEventDetailNotifyHolder.getRootView()));
            punishLayout.addView(oprateView(mDealEventDetailPunishHolder.getRootView()));
        }
    }

    private View oprateView(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return view;
    }

    @Override
    public void reject() {
        String dealJson;
        if (!isBatch) {
            dealJson = buildDealJson(false,
                    mDealEventDetailPunishHolder.getPunish(),
                    mDealEventDetailNotifyHolder.needValid(),
                    mDealEventDetailNotifyHolder.needParentNotice(),
                    mDealEventDetailNotifyHolder.needPsy(),
                    mDealEventDetailDeductHolder.getScore());
            dealResult(dealJson, getData().getEvent2InvolvedUID());
        } else {
            dealJson = buildDealsJson(false,
                    mDealEventDetailPunishHolder.getPunish(),
                    mDealEventDetailNotifyHolder.needValid(),
                    mDealEventDetailNotifyHolder.needParentNotice(),
                    mDealEventDetailNotifyHolder.needPsy(),
                    mDealEventDetailDeductHolder.getScore());
            dealsResult(dealJson);
        }
    }

    @Override
    public void sure() {
        String dealJson;
        if (!isBatch) {
            dealJson = buildDealJson(true,
                    mDealEventDetailPunishHolder.getPunish(),
                    mDealEventDetailNotifyHolder.needValid(),
                    mDealEventDetailNotifyHolder.needParentNotice(),
                    mDealEventDetailNotifyHolder.needPsy(),
                    mDealEventDetailDeductHolder.getScore());
            dealResult(dealJson, getData().getEvent2InvolvedUID());
        } else {
            dealJson = buildDealsJson(true,
                    mDealEventDetailPunishHolder.getPunish(),
                    mDealEventDetailNotifyHolder.needValid(),
                    mDealEventDetailNotifyHolder.needParentNotice(),
                    mDealEventDetailNotifyHolder.needPsy(),
                    mDealEventDetailDeductHolder.getScore());
            dealsResult(dealJson);
        }
    }

    /**
     * '
     * <p>
     * {
     * "Event2InvolvedUID": "string",
     * "Confirm": true,
     * "Reason": "string",
     * "Punishment": "string",
     * "NeedValid": true,
     * "NeedParentNotice": true,
     * "NeedPsycholog": true,
     * "PunishmentInfo": "string",
     * "Score": 0
     * }
     */
    private String buildDealJson(boolean confirm, String punishment,
                                 boolean needValid, boolean needParentNotice,
                                 boolean needPsy, int score) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Event2InvolvedUID", getData().getEvent2InvolvedUID());
            jsonObject.put("Confirm", confirm);
            jsonObject.put("Reason", "");
            jsonObject.put("Punishment", punishment);
            jsonObject.put("NeedValid", needValid);
            jsonObject.put("NeedParentNotice", needParentNotice);
            jsonObject.put("NeedPsycholog", needPsy);
            jsonObject.put("Score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //批量处理事件
    private String buildDealsJson(boolean confirm, String punishment,
                                  boolean needValid, boolean needParentNotice,
                                  boolean needPsy, int score) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EventUID", mEventUid);
            jsonObject.put("Confirm", confirm);
            jsonObject.put("Reason", "");
            jsonObject.put("Punishment", punishment);
            jsonObject.put("NeedValid", needValid);
            jsonObject.put("NeedParentNotice", needParentNotice);
            jsonObject.put("NeedPsycholog", needPsy);
            jsonObject.put("Score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    private void dealResult(String dealJson, final String eventId) {

        PutiTeacherModel.getInstance().dealEvent(dealJson, new BaseListener(BaseResponseInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);
                ToastUtil.show("事件处理成功");
                if (EventDealManager.needDealEventId.contains(eventId)){
                    EventDealManager.needDealEventId.remove(eventId);
                }
                dissMissView();
                if (EventDealManager.needDealEventId.size() == 0){
                    if (mContext instanceof Activity){
                        ((Activity) mContext).finish();
                    }
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }

    private void dealsResult(String dealJson) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
        PutiTeacherModel.getInstance().dealsEvent(dealJson, new BaseListener(BaseResponseInfo.class) {
            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                ToastUtil.show("批量处理成功");
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                ToastUtil.show(errorMessage);
            }
        });
    }


    private void dissMissView(){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.hide_item);
        container.startAnimation(animation);
        container.setVisibility(View.GONE);
    }

}
