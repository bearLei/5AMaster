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
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

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

/**
 * Created by lei on 2018/7/22.
 */

public class WorkEventCountHolder extends BaseHolder<PutiWeekEventImp> {


    @BindView(R.id.chart_view)
    LineChartView chartView;
    @BindView(R.id.desc)
    TextView desc;

    private Map<Integer, Integer> mChartData = new HashMap<>();

    public WorkEventCountHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        View view = InflateService.g().inflate(R.layout.puti_week_check_count_holder);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void updateUI(Context context, PutiWeekEventImp data) {
        List<WeekEvent> eventList = data.getWeekEvents();
        PutiWeekEventImp.Summary summary = data.getSummary();
        int size = eventList.size();
//        for (int i = 0; i < size; i++) {
//            if (i < 5) {
//                eventList.get(i).setEventCount(2);
//            } else if (5 <= i && i <= 10){
//                eventList.get(i).setEventCount(5);
//            }else {
//                eventList.get(i).setEventCount(10);
//            }
//        }
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


        for (int i = 0; i < size; i++) {
            WeekEvent weekEvent = eventList.get(i);
            mChartData.put(weekEvent.getWeekIndex(), weekEvent.getEventCount());
        }

        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        int index = 0;
        for (Integer integerData : mChartData.values()) {
            values.add(new PointValue(index, integerData));
            index++;
        }
        Line line = new Line(values);
        line.setHasLabels(false);
        //折线的颜色
        line.setColor(ChartUtils.COLOR_BLUE);
        //折线图上每个数据点的形状
        line.setShape(ValueShape.CIRCLE);
        //设置数据点颜色
        line.setPointColor(ChartUtils.COLOR_RED);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasPoints(false);
        //是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasLines(true);
        line.setStrokeWidth(1);
        lines.add(line);
        //图形数据加载
        LineChartData lineChartData = new LineChartData(lines);

        lineChartData.setAxisYLeft(new Axis());
        lineChartData.setAxisXBottom(new Axis());
        //把数据放到控件中
        chartView.setZoomEnabled(true);
        chartView.setScrollEnabled(true);
        chartView.setLineChartData(lineChartData);
    }
}
