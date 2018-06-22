package unit.moudle.eventdeal;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Event;
import unit.moudle.eventdeal.ptr.EventDetailPtr;
import unit.moudle.eventdeal.view.EventDetailView;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/22.
 */

public class EventDetailActivity extends PutiActivity implements EventDetailView {

    public static final String Parse_Intent = "parse_intent";

    @BindView(R.id.headview)
    HeadView headview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.head_layout)
    LinearLayout headLayout;

    private Event eventDetail;
    private EventDetailPtr mPtr;

    @Override
    public int getContentView() {
        return R.layout.puti_event_happened_detail_activity;
    }

    @Override
    public void BindPtr() {
        if (mPtr == null) {
            mPtr = new EventDetailPtr(this, this);
        }
    }

    @Override
    public void ParseIntent() {
        if (getIntent() != null) {
            eventDetail = (Event) getIntent().getSerializableExtra(Parse_Intent);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        headview.setRightTV("批量确认");
        headview.setRightColor(R.color.base_39BCA1);
        headview.setRightCallBack(new HeadView.HeadViewRightCallBack() {
            @Override
            public void click() {

            }
        });

    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void getHeadHolderView(View view) {
        headLayout.removeAllViews();
        headLayout.addView(view);
    }

    @Override
    public void success() {

    }

    @Override
    public String getEventId() {
        if (eventDetail != null) {
            return eventDetail.getEventUID();
        }
        return "";
    }

}
