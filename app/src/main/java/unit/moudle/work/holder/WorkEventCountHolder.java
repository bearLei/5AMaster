package unit.moudle.work.holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import unit.entity.PutiWeekEventImp;
import unit.entity.WeekEvent;
import unit.moudle.work.LineChartManager;
import unit.widget.MyMarkerView;

/**
 * Created by lei on 2018/7/22.
 */

public class WorkEventCountHolder extends BaseHolder<PutiWeekEventImp> {


    @BindView(R.id.chart_view)
    LineChart mChart;
    @BindView(R.id.desc)
    TextView desc;

    private Map<Integer, Integer> mChartData = new HashMap<>();
    private LineChartManager manager;
    public WorkEventCountHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        View view = InflateService.g().inflate(R.layout.puti_week_check_count_holder);
        ButterKnife.bind(this, view);
        // no description text
        int screenWid = ViewUtils.getScreenWid(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (screenWid*1.5),ViewUtils.dip2px(mContext,200));
        mChart.setLayoutParams(params);
        MyMarkerView mv = new MyMarkerView(mContext, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
        return view;
    }

    @Override
    protected void updateUI(Context context, PutiWeekEventImp data) {
        List<WeekEvent> eventList = data.getWeekEvents();
        PutiWeekEventImp.Summary summary = data.getSummary();
        int size = eventList.size();

        //设置desc
        StringBuilder builder = new StringBuilder();
        builder.append("本学期共发生事件 ")
                .append(String.valueOf(summary.getTotal()))
                .append(" 件").append("    其中重点事件 ");

        StringBuilder importBuilder = new StringBuilder();
        importBuilder.append(String.valueOf(summary.getImportant()))
                .append(" 件");
        SpannableString normalString = new SpannableString(builder.toString());
        normalString.setSpan(new StyleSpan(Typeface.NORMAL),0,normalString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString = new SpannableString(importBuilder.toString());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,importBuilder.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED),0,importBuilder.toString().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        desc.setText("");
        desc.append(normalString);
        desc.append(spannableString);

        List<Integer> xValue = new ArrayList<>();
        List<Integer> yValue = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            WeekEvent weekEvent = eventList.get(i);
            xValue.add(weekEvent.getWeekIndex());
            yValue.add(weekEvent.getEventCount());
        }
        manager = new LineChartManager(mChart);
        manager.showLineChart(xValue,yValue,"",mContext.getResources().getColor(R.color.base_666666));
    }
}
