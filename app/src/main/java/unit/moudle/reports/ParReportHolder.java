package unit.moudle.reports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.ReportInfo;

/**
 * Created by lei on 2018/7/5.
 */

public class ParReportHolder extends BaseHolder<ReportInfo> {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.subject)
    TextView subject;
    @BindView(R.id.desc)
    TextView desc;

    public ParReportHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_report_holder_item);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRootView.setLayoutParams(params);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, ReportInfo data) {
        if (data == null){
            return;
        }

        time.setText(data.getTime());
        subject.setText(data.getTitle());
        desc.setText(data.getDescription());
    }
}
