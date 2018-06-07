package unit.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;

/**
 * Created by lei on 2018/6/7.
 */

public class SettingItem extends RelativeLayout {

    private TextView title;
    private TextView brief;
    private ImageView nextIcon;

    public SettingItem(Context context) {
        this(context,null);
    }
    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.puti_setting_item,this,true);
        title = (TextView) findViewById(R.id.title);
        nextIcon = (ImageView) findViewById(R.id.next_icon);
        brief = (TextView)findViewById(R.id.brief);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.SettingItem);
        if (ta != null){
            String titleRes = ta.getString(R.styleable.SettingItem_title);
            String briefRes = ta.getString(R.styleable.SettingItem_brief);
            boolean showIconRes = ta.getBoolean(R.styleable.SettingItem_showIcon,true);
            title.setText(titleRes);
            brief.setText(briefRes);
            nextIcon.setVisibility(showIconRes ? VISIBLE : GONE);
            ta.recycle();
        }
    }

    //手动设置
    public void updateBrief(String desc){
        brief.setVisibility(VISIBLE);
        brief.setText(desc);
    }

}
