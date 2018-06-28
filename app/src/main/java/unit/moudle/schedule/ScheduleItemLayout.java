package unit.moudle.schedule;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.util.ViewUtils;

import java.util.ArrayList;

import unit.entity.CursorInfo;

/**
 * Created by lei on 2018/6/28.
 */

public class ScheduleItemLayout extends LinearLayout {

    private Context mContext;

    public ScheduleItemLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    public ScheduleItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    public void setAdapter(ArrayList<CursorInfo> data,boolean needHideFirst){
        setOrientation(LinearLayout.VERTICAL);
        removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                buildItem(data.get(i).getCourseName(),true,needHideFirst);
            }else {
                buildItem(data.get(i).getCourseName(),false,false);
            }
        }
    }

    private void buildItem(String title,boolean needStrick,boolean needHideFirst){
        View view =  InflateService.g().inflate(R.layout.puti_cursor_item);
        TextView textView = (TextView) view.findViewById(R.id.cursor_name);
        if (needStrick){
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        if (needHideFirst){
            textView.setVisibility(GONE);
        }else {
            textView.setVisibility(VISIBLE);
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewUtils.dip2px(mContext,70));
        view.setLayoutParams(params);
        textView.setText(title);
        addView(view);
    }
}
