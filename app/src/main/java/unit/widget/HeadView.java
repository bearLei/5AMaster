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
    private Context context;
    private TextView back;
    private TextView title;
    private TextView rightTv;

    private HeadViewCallBack callBack;
    private HeadViewRightCallBack rightCallBack;
    public HeadView(Context context) {
        this(context,null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        this.context  = context;
        View view = LayoutInflater.from(context).inflate(R.layout.puti_head_view, this,true);
        back = (TextView) view.findViewById(R.id.back);
        title = (TextView) view.findViewById(R.id.title);
        rightTv = (TextView) view.findViewById(R.id.right_tv);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.PutiHeadView);
        if (ta != null){
            String titleRes = ta.getString(R.styleable.PutiHeadView_head_title);
            title.setText(titleRes);

            boolean showBackIcon = ta.getBoolean(R.styleable.PutiHeadView_showBackIcon,true);
            back.setVisibility(showBackIcon ? VISIBLE : GONE);

            boolean showRightTv = ta.getBoolean(R.styleable.PutiHeadView_showRightTv, false);
            rightTv.setVisibility(showRightTv ? VISIBLE : GONE);

            String rightTitle = ta.getString(R.styleable.PutiHeadView_right_title);
            rightTv.setText(rightTitle);
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

        rightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightCallBack != null){
                    rightCallBack.click();
                }
            }
        });

    }
    public void setTitle(String desc){
        title.setText(desc);
    }
    public void showRightTV(boolean show){
        rightTv.setVisibility(show ? VISIBLE : GONE);
    }
    public void setRightTV(String title){
        rightTv.setText(title);
    }
    public void setRightColor(int color){
        rightTv.setTextColor(context.getResources().getColor(color));
    }

    public void setCallBack(HeadViewCallBack callBack) {
        this.callBack = callBack;
    }

    public interface HeadViewCallBack{
        void backClick();
    }

    public void setRightCallBack(HeadViewRightCallBack rightCallBack) {
        this.rightCallBack = rightCallBack;
    }

    public interface HeadViewRightCallBack{
        void click();
    }

}
