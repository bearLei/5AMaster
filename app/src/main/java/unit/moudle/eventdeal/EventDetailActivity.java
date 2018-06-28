package unit.moudle.eventdeal;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import unit.entity.Event;
import unit.entity.Event2Involved;
import unit.moudle.eventdeal.adapter.DealEventDetailAdapter;
import unit.moudle.eventdeal.ptr.EventDetailPtr;
import unit.moudle.eventdeal.view.EventDetailView;
import unit.widget.HeadView;
import unit.widget.SpaceItemDecoration;

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

    private DealEventDetailAdapter mAdapter;
    private ArrayList<Event2Involved> mData;
    @Override
    public int getContentView() {
        return R.layout.puti_deal_event_detail_activity;
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
        headview.showRightTV(true);
        headview.setRightColor(R.color.base_39BCA1);
        headview.setRightCallBack(new HeadView.HeadViewRightCallBack() {
            @Override
            public void click() {
                DealsDialog dialog = new DealsDialog(EventDetailActivity.this);
                dialog.setData(mData.get(0));
                dialog.show();
            }
        });
        if (mData == null){
            mData = new ArrayList<>();
        }

        if (mAdapter == null){
            mAdapter = new DealEventDetailAdapter(this,mData);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(ViewUtils.dip2px(this,10)));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void Star() {
        mPtr.star();
    }

    @Override
    public void setTitle(String title) {
        if (eventDetail != null){
            StringBuilder builder = new StringBuilder();
            builder.append(eventDetail.getEventTypeName())
                    .append("(")
                    .append(title)
                    .append("人)");
            headview.setTitle(builder.toString());
        }
    }

    @Override
    public void getHeadHolderView(View view) {
        headLayout.removeAllViews();
        headLayout.addView(view);
    }

    @Override
    public void success(ArrayList<Event2Involved> data) {
        mData.clear();
        mData.addAll(data);
        EventDealManager.needDealEventId = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            EventDealManager.needDealEventId.add(mData.get(i).getEvent2InvolvedUID());
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getEventId() {
        if (eventDetail != null) {
            return eventDetail.getEventUID();
        }
        return "";
    }
}
