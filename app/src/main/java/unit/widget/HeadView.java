package unit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;

/**
 * Created by lei on 2018/6/8.
 */

public class HeadView extends RelativeLayout {
    private TextView back;
    private TextView title;

    private HeadViewCallBack callBack;

    public HeadView(Context context) {
        this(context,null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        View view = LayoutInflater.from(context).inflate(R.layout.puti_head_view, this,true);
        back = (TextView) view.findViewById(R.id.back);
        title = (TextView) view.findViewById(R.id.title);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.PutiHeadView);
        if (ta != null){
            String titleRes = ta.getString(R.styleable.PutiHeadView_head_title);
            title.setText(titleRes);

            boolean showBackIcon = ta.getBoolean(R.styleable.PutiHeadView_showBackIcon,true);
            back.setVisibility(showBackIcon ? VISIBLE : GONE);
            ta.recycle();
        }

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null){
                    callBack.backClick();
                }
            }
        });

    }
    public void setTitle(String desc){
        title.setText(desc);
    }
    public void setCallBack(HeadViewCallBack callBack) {
        this.callBack = callBack;
    }

    public interface HeadViewCallBack{
        void backClick();
    }

}
