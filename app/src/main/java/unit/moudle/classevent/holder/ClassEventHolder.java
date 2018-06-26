package unit.moudle.classevent.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.Event;
import unit.moudle.classevent.PutiClassEventDetailActivity;

/**
 * Created by lei on 2018/6/26.
 */

public class ClassEventHolder extends BaseHolder<Event> {

    @BindView(R.id.time_desc)
    TextView timeDesc;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.students)
    TextView students;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.deduct)
    TextView deduct;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.status_icon)
    ImageView statusIcon;

    public ClassEventHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_class_event_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, final Event data) {
        if (data == null){
            return;
        }
        timeDesc.setText(data.getTime());
        type.setText("类型：   "+data.getEventTypeName());
        students.setText("学生：   "+data.getStudentNames());
        address.setText("地点：   "+data.getAddress());
        deduct.setText("扣分：   "+data.getScores());
        desc.setText("描述：   "+data.getDescription());

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PutiClassEventDetailActivity.class);
                intent.putExtra(PutiClassEventDetailActivity.Parse_Intent,data);
                mContext.startActivity(intent);
            }
        });
    }
}
