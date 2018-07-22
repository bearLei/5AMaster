package unit.moudle.work.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.baidu.platform.comapi.map.F;
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
import lecho.lib.hellocharts.model.AxisValue;
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

    private Map<Integer,Integer> mChartData = new HashMap<>();
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
        int size = eventList.size();
        for (int i = 0; i < size; i++) {
            if (i < 5) {
                eventList.get(i).setEventCount(2);
            } else if (5 <= i && i <= 10){
                eventList.get(i).setEventCount(5);
            }else {
                eventList.get(i).setEventCount(10);
            }
        }

        for (int i = 0; i < size; i++) {
            WeekEvent weekEvent = eventList.get(i);
            mChartData.put(weekEvent.getWeekIndex(),  weekEvent.getEventCount());
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
