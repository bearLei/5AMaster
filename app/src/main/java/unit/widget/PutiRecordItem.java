package unit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;

/**
 * Created by lei on 2018/6/18.
 */

public class PutiRecordItem extends LinearLayout {
    private TextView TTitle,TDesc;

    public PutiRecordItem(Context context) {
       this(context,null);
    }

    public PutiRecordItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.puti_record_item,this,true);
        TTitle = (TextView) findViewById(R.id.title);
        TDesc = (TextView)findViewById(R.id.desc);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.PutiRecordItem);
        if (ta != null){
            String titleRes = ta.getString(R.styleable.PutiRecordItem_record_title);
            String descRes = ta.getString(R.styleable.PutiRecordItem_record_desc);
            TTitle.setText(titleRes);
            TDesc.setText(descRes);
            ta.recycle();
        }
    }

    public void setTTitle(String title){
        TTitle.setText(title);
    }

    public void setTDesc(String desc){
        TDesc.setText(desc);
    }

}
