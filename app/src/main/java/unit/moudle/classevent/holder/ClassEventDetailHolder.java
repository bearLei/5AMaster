package unit.moudle.classevent.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.DealEntity;
import unit.entity.Event2Involved;

/**
 * Created by lei on 2018/6/26.
 */

public class ClassEventDetailHolder extends BaseHolder<DealEntity> {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.decuct_score)
    TextView decuctScore;
    @BindView(R.id.notify_layout)
    FlexboxLayout notifyLayout;
    @BindView(R.id.punish_layout)
    FlexboxLayout punishLayout;
    @BindView(R.id.status_icon)
    ImageView statusIcon;

    public ClassEventDetailHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_class_event_detail_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, DealEntity data) {
        if (data == null){
            return;
        }
        name.setText(TextUtils.isEmpty(data.getStudentName()) ? "" : data.getStudentName());
        className.setText(TextUtils.isEmpty(data.getClassName()) ? "" : data.getClassName());
        decuctScore.setText(String.valueOf(data.getScore()));
        notifyLayout.removeAllViews();
        punishLayout.removeAllViews();
        if (data.isNeedValid()){
            notifyLayout.addView(buildItem("学生处"));
        }
        if (data.isNeedParentNotice()){
            notifyLayout.addView(buildItem("家长"));
        }
        if (data.isNeedPsycholog()){
            notifyLayout.addView(buildItem("心理老师"));
        }
        if (!TextUtils.isEmpty(data.getPunishment())){
            punishLayout.addView(buildItem(data.getPunishment()));
        }

    }


    private View buildItem(String title){
        LinearLayout item = (LinearLayout) InflateService.g().inflate(R.layout.puti_notify_item);
        TextView tv = (TextView) item.findViewById(R.id.item);
        tv.setText(title);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewUtils.dip2px(mContext,100),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.flexShrink = 1.0f;
        params.flexGrow = 1.0f;
        item.setSelected(true);
        item.setLayoutParams(params);
        return item;
    }
}
