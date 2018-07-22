package unit.moudle.work.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.PutiUnUsedEntity;

/**
 * Created by lei on 2018/7/22.
 */

public class WorkUnUsedHolder extends BaseHolder<PutiUnUsedEntity> {

    @BindView(R.id.week_container)
    FlexboxLayout weekContainer;
    @BindView(R.id.month_container)
    FlexboxLayout monthContainer;
    @BindView(R.id.term_container)
    FlexboxLayout termContainer;

    public WorkUnUsedHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        View view = InflateService.g().inflate(R.layout.work_unused_holder);
        ButterKnife.bind(this, view);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weekContainer.setLayoutParams(param);
        monthContainer.setLayoutParams(param);
        termContainer.setLayoutParams(param);
        return view;
    }

    @Override
    protected void updateUI(Context context, PutiUnUsedEntity data) {
        if (data == null){
            return;
        }
        List<String> monthList = data.getMonthList();
        List<String> termList = data.getTermList();
        List<String> weekList = data.getWeekList();
        monthContainer.removeAllViews();
        weekContainer.removeAllViews();
        termContainer.removeAllViews();

        for (String s : weekList) {
            buildItem(s,weekContainer);
        }

        for (String s : monthList) {
            buildItem(s,monthContainer);
        }

        for (String s : termList) {
            buildItem(s,termContainer);
        }
    }

    private void buildItem(String title, ViewGroup container){
        View view = InflateService.g().inflate(R.layout.work_un_used_event_item);
        FlexboxLayout.LayoutParams param = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.leftMargin = ViewUtils.dip2px(mContext,15);
        param.rightMargin = ViewUtils.dip2px(mContext,15);
        param.bottomMargin = ViewUtils.dip2px(mContext,15);
        view.setLayoutParams(param);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(title);
        container.addView(view);
    }

}
