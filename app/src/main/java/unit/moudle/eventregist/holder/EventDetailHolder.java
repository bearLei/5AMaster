package unit.moudle.eventregist.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.EventDetail;
import unit.moudle.eventregist.PutiChooseDetailActivity;

/**
 * Created by lei on 2018/6/9.
 */

public class EventDetailHolder extends BaseHolder<EventDetail> {

    @BindView(R.id.title)
    TextView title;

    private int mType;
    public EventDetailHolder(Context context) {
        super(context);
    }

    public EventDetailHolder(Context context, int mType) {
        super(context);
        this.mType = mType;
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_holder);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewUtils.dip2px(mContext,50));
        mRootView.setLayoutParams(params);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, final EventDetail data) {
        if (data == null){
            return;
        }
        title.setText(data.getTypeName());
        switch (mType){
            case 1:
                title.setTextColor(mContext.getResources().getColor(R.color.base_39BCA1));
                break;
            case 2:
                title.setTextColor(mContext.getResources().getColor(R.color.base_f03c28));
                break;
            case 3:
                title.setTextColor(mContext.getResources().getColor(R.color.base_5a5a5a));
                break;
            case 4:
                title.setTextColor(mContext.getResources().getColor(R.color.base_4577dc));
                break;
            default:
                title.setTextColor(mContext.getResources().getColor(R.color.base_39BCA1));
                break;
        }

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/6/11 跳转
                Intent intent = new Intent(mContext, PutiChooseDetailActivity.class);
                intent.putExtra(PutiChooseDetailActivity.Parse_Intent, (Serializable) data);
                mContext.startActivity(intent);
            }
        });

    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }
}
