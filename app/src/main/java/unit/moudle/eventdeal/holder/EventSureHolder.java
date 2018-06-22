package unit.moudle.eventdeal.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Event;
import unit.moudle.eventdeal.EventDetailActivity;

/**
 * Created by lei on 2018/6/22.
 * 事件确认-事件列表
 */

public class EventSureHolder extends BaseHolder<Event> {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.students)
    TextView students;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.desc)
    TextView desc;

    public EventSureHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_sure_holder);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRootView.setLayoutParams(params);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, final Event data) {
        if (data == null) {
            return;
        }

        time.setText(data.getTime());
        type.setText(data.getEventTypeName());
        students.setText(data.getStudentNames());
        address.setText(data.getAddress());
        desc.setText(data.getDescription());

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.Parse_Intent,data);
                mContext.startActivity(intent);
            }
        });
    }
}
