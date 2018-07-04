package unit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;


/**
 * 无数据的上图下文
 */
public class EmptyView extends RelativeLayout {
    public static final int DRAWABLE_DEFAULT = R.drawable.emptyview_default;
    public static final int DRAWABLE_NETWORK = R.drawable.emptyview_network;

    private TextView empty_text;
    private ImageView empty_image;
    private TextView empty_btn;
    private View root_empty_view;
    private FrameLayout bottom_view;


    public EmptyView(Context context) {

        this(context, null);

    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(
                R.layout.fb_empty_view_new, this, true);
        empty_text = (TextView) findViewById(R.id.empty_text);
        empty_image = (ImageView) findViewById(R.id.empty_image);
        empty_btn = (TextView) findViewById(R.id.empty_btn);
        bottom_view = (FrameLayout) findViewById(R.id.bottom_view);
        root_empty_view = findViewById(R.id.root_empty_view);

        boolean show = false;
        if (attrs != null) {
            if (attrs != null) {
                TypedArray a = context.obtainStyledAttributes(attrs,
                        R.styleable.empty_view);
                String emptyText = a.getString(R.styleable.empty_view_emptyText);
                int topImageRes = a.getResourceId(R.styleable.empty_view_drawableTop, 0);
                int colorBG = a.getResourceId(R.styleable.empty_view_bgcolor, 0);
                if (colorBG != 0) {
                    root_empty_view.setBackgroundColor(getResources().getColor(colorBG));
                }

                if (!TextUtils.isEmpty(emptyText)) {
                    empty_text.setText(emptyText);
                }
                if (topImageRes > 0) {
                    empty_image.setImageResource(topImageRes);
                }
                show = a.getBoolean(R.styleable.empty_view_show, false);

                a.recycle();
            }
        }

        if (!show) {
            dismiss();
        }
    }

    // ---------------------Protected------------------------------
    protected int getDrawableNetwork() {
        return DRAWABLE_NETWORK;
    }

    protected int getDrawableNoData() {
        return DRAWABLE_DEFAULT;
    }

    // ---------------------Public------------------------------

    /**
     * 不带有btn的emptyview
     *
     * @param tip   提示
     * @param resID 图片资源
     */
    public void showNoDataView(String tip, int resID) {
        if (resID > 0) {
            empty_text.setText(tip);
            empty_image.setImageResource(resID);
            empty_btn.setVisibility(View.GONE);
        }
        this.setVisibility(View.VISIBLE);
        root_empty_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void showNoDataView(String tip, int resID, OnClickListener listener) {
        showNoDataView(tip, resID);
        root_empty_view.setOnClickListener(listener);
    }

    public void showNoDataView(String tip) {
        showNoDataView(tip, getDrawableNoData());
    }

    public static class UserRetryEvent {
    }

    /**
     * 带有btn的emptyview
     *
     * @param tip      提示
     * @param resID    图片
     * @param btnText  按钮文字
     * @param listener 按钮的点击响应
     */
    public void showNoDataView(String tip, int resID, String btnText, OnClickListener listener) {
        showNoDataView(tip, resID);
        if (!TextUtils.isEmpty(btnText)) {
            empty_btn.setText(btnText);
            if (listener != null) {
                empty_btn.setOnClickListener(listener);
            }
            empty_btn.setVisibility(View.VISIBLE);
        }
        root_empty_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 带有btn的emptyview
     *
     * @param listener 背景的点击重试响应
     */
    public void showErrorDataView(final OnClickListener listener) {
        showNoDataView("重试", getDrawableNetwork());
        root_empty_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });

    }

    /**
     * 带有btn的emptyview
     *
     * @param listener 背景的点击重试响应
     */
    public void showErrorDataView(String tip, final OnClickListener listener) {
        showNoDataView(tip, getDrawableNetwork());
        root_empty_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });

    }





    /**
     * 消失
     */
    public void dismiss() {
        this.setVisibility(View.GONE);
        empty_image.setImageResource(0);
    }





    public void setEmptyBtnBg(int resId) {
        empty_btn.setBackgroundResource(resId);
    }

    public void setEmptyBtnTextColor(int resId) {
        empty_btn.setTextColor(getResources().getColor(resId));
    }

    public void setEmptyBtnSize(int width, int height) {
        empty_btn.getLayoutParams().width = width;
        empty_btn.getLayoutParams().height = height;
        requestLayout();
    }

    public void setEmptyBtnMargins(int left, int top, int right, int bottom) {
        ((MarginLayoutParams) empty_btn.getLayoutParams()).setMargins(left, top, right, bottom);
        requestLayout();
    }

    public void setBottomView(View v){
        bottom_view.removeAllViews();
        if(v != null){
            bottom_view.setVisibility(VISIBLE);
            bottom_view.addView(v);
        } else {
            bottom_view.setVisibility(GONE);
        }
    }

    public void setEmptyImageMargins(int left, int top, int right, int bottom) {
        ((MarginLayoutParams) empty_image.getLayoutParams()).setMargins(left, top, right, bottom);
        requestLayout();
    }

}
