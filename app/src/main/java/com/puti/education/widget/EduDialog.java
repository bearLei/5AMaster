package com.puti.education.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.util.DisPlayUtil;


/**
 * 起询问或请求作用的对话框
 *
 * @author pangfq
 */
public class EduDialog extends Dialog {

    private TextView mTitleTextView;// 标题
    private View mTitleUnderline;// 标题的下划线
    private TextView mContentTextView;// 正文内容
    private TextView mSubContentTextView;// 子内容
    private View mIconView;// 图标
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private Context mContext;
    private View mCheckBoxGorup;// 单选框组
    private CheckBox mCheckBox;// 单选框
    private TextView mCBTextView;// 单选框文字
    private ImageView mCloseIcon;
    // ---------------------------------构造函数

    /**
     * 只显示正文内容 *
     */
    public EduDialog(Context context, String content) {
        this(context, content, null, -1, null, null);
    }

    public EduDialog(Context context, int contentResId) {
        this(context, context.getResources().getString(contentResId), null, -1,
                null, null);
    }

    /**
     * 显示正文内容、图标 *
     */
    public EduDialog(Context context, String content, int iconResId) {
        this(context, content, null, iconResId, null, null);
    }

    /**
     * 只显示标题、正文内容 *
     */
    public EduDialog(Context context, String title, String content) {
        this(context, content, null, -1, title, null);
    }

    /**
     * 只显示正文内容、单选框 *
     */
    public EduDialog(String content, String checkbox, Context context) {
        this(context, content, null, -1, null, checkbox);
    }

    public EduDialog(Context context, int contentResId, int iconResId) {
        this(context, context.getResources().getString(contentResId), null,
                iconResId, null, null);
    }

    public EduDialog(Context context, String content, String subContent,
                     int iconResId, String title, String checkbox) {
        super(context, R.style.FilterDialogTheme);
        mContext = context;
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_edu, null);
        mCloseIcon = (ImageView) contentView.findViewById(R.id.dialog_close);
        mCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTitleTextView = (TextView) contentView
                .findViewById(R.id.title_textview);
        if (title != null && !title.isEmpty()) {
            mTitleTextView.setVisibility(View.VISIBLE);
            mTitleTextView.setText(title);
        } else {
            mTitleTextView.setVisibility(View.GONE);
        }
        // 正文内容
        mContentTextView = (TextView) contentView
                .findViewById(R.id.content_textview);
        mContentTextView.setText(content);
        // 设置TextView可以滚动
        mContentTextView.setMovementMethod(ScrollingMovementMethod
                .getInstance());
        // 子内容
        mSubContentTextView = (TextView) contentView
                .findViewById(R.id.sub_content_textview);
        if (subContent == null) {
            mSubContentTextView.setVisibility(View.GONE);
        } else {
            mSubContentTextView.setText(subContent);
        }
        // 图标
        mIconView = contentView.findViewById(R.id.icon_view);
        if (iconResId == -1) {
            mIconView.setVisibility(View.GONE);
        } else {
            mIconView.setBackgroundResource(iconResId);
        }
        mCheckBoxGorup = contentView.findViewById(R.id.checkbox_group);
        if (checkbox != null && !checkbox.isEmpty()) {
            mCheckBoxGorup.setVisibility(View.VISIBLE);
            mCheckBox = (CheckBox) contentView.findViewById(R.id.checkbox);
            mCheckBox.setChecked(false);
            mCBTextView = (TextView) contentView
                    .findViewById(R.id.checkbox_textview);
            mCBTextView.setText(checkbox);
            mCheckBoxGorup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                }
            });
        } else {
            mCheckBoxGorup.setVisibility(View.GONE);
        }

        // Positive Button
        mPositiveBtn = (Button) contentView.findViewById(R.id.btn_positive);
        // Negative Button
        mNegativeBtn = (Button) contentView.findViewById(R.id.btn_negative);

        setContentView(contentView);
        WindowManager.LayoutParams windowparams = getWindow().getAttributes();
        getWindow().setGravity(Gravity.CENTER);
        Rect rect = new Rect();
        View decorView = getWindow().getDecorView();
        decorView.getWindowVisibleDisplayFrame(rect);
        windowparams.width = DisPlayUtil.getScreenWidth((Activity) mContext) - DisPlayUtil.dip2px(mContext,86f);
        getWindow().setAttributes(windowparams);
    }
    public EduDialog(Context context, int contentResId, int subContentResId,
                     int iconResId) {

        this(context, context.getResources().getString(contentResId), context
                        .getResources().getString(subContentResId), iconResId, null,
                null);

    }

    // ------------------------------------------外部接口
    public void show() {
        if (!((Activity) mContext).isFinishing()) {
            if(mPositiveBtn != null && mPositiveBtn.getVisibility() == View.VISIBLE &&
                    mNegativeBtn != null && mNegativeBtn.getVisibility() == View.VISIBLE){
                mCloseIcon.setVisibility(View.GONE);
            }else{
                mCloseIcon.setVisibility(View.VISIBLE);
            }
            super.show();
        }
    }

    /**
     * 返回单选框的状态
     */
    public boolean isCheckboxChecked() {
        return mCheckBox != null && mCheckBox.isChecked();
    }

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(View view);
    }

    public interface OnNegativeButtonClickListener {
        void onNegativeButtonClick(View view);
    }

    public interface OnButtonClickListener extends
            OnPositiveButtonClickListener, OnNegativeButtonClickListener {
    }

    private OnButtonClickListener mListener;

    /**
     * @param listener
     * @param negativeBtnText NegativeButton显示的文字
     * @param positiveBtnText PositiveButton显示的文字
     */
    public void setOnButtonClickListener(OnButtonClickListener listener,
                                         String negativeBtnText, String positiveBtnText) {
        if (listener == null)
            return;
        mListener = listener;

        mPositiveBtn.setText(positiveBtnText);
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.onPositiveButtonClick(view);
            }
        });
        mNegativeBtn.setText(negativeBtnText);
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.onNegativeButtonClick(view);
            }
        });
    }

    /**
     * 设置监听器
     *
     * @param listener             监听器
     * @param negativeBtnTextResId NegativeButton显示的文字ResID
     * @param positiveBtnTextResId PositiveButton显示的文字ResID
     */
    public void setOnButtonClickListener(OnButtonClickListener listener,
                                         int negativeBtnTextResId, int positiveBtnTextResId) {
        setOnButtonClickListener(listener,
                mContext.getResources().getString(negativeBtnTextResId),
                mContext.getResources().getString(positiveBtnTextResId));
    }

    private OnPositiveButtonClickListener mPositiveListener;

    public void setOnPositiveButtonClickListener(
            OnPositiveButtonClickListener listener, String positiveBtnText) {
        if (listener == null)
            return;
        mPositiveListener = listener;

        mNegativeBtn.setVisibility(View.GONE);
        mPositiveBtn.setText(positiveBtnText);
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPositiveListener.onPositiveButtonClick(view);
            }
        });
    }

    public void setOnPositiveButtonClickListener(
            OnPositiveButtonClickListener listener, int negativeBtnTextResId) {
        setOnPositiveButtonClickListener(listener, mContext.getResources()
                .getString(negativeBtnTextResId));
    }

    private OnNegativeButtonClickListener mNegativeListener;

    public void setOnNegativeButtonClickListener(
            OnNegativeButtonClickListener listener, String negativeBtnText) {
        if (listener == null)
            return;
        mNegativeListener = listener;
        mNegativeBtn.setVisibility(View.VISIBLE);
        mNegativeBtn.setText(negativeBtnText);
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mNegativeListener.onNegativeButtonClick(view);
            }
        });
    }

    public void setOnNegativeButtonClickListener(
            OnNegativeButtonClickListener listener, int negativeBtnTextResId) {
        setOnNegativeButtonClickListener(listener, mContext.getResources()
                .getString(negativeBtnTextResId));
    }

    /**
     * 设置正文内容颜色 *
     */
    public void setContentColor(int colorResId) {

        if (mContentTextView != null) {

            mContentTextView.setTextColor(mContext.getResources().getColor(
                    colorResId));

        }

    }

    /**
     * 显示简单的对话框
     *
     * @param title   标题
     * @param content 内容
     */
    public static void showSimpleDialog(Context context, String title,
                                        String content) {
        final EduDialog dialog = new EduDialog(context, title, content);
        dialog.setOnPositiveButtonClickListener(
                new OnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(View view) {
                        dialog.dismiss();
                    }
                }, "确定");
        dialog.show();
    }
    /**
     * 含有回调接口的的对话框
     *
     * @param title   标题
     * @param content 内容
     * @param listener 回掉接口
     */
    public static void showDialog(Context context, String title,
                                  String content, final deleteOnClickListener listener) {
        final EduDialog dialog = new EduDialog(context, title, content);
        dialog.setOnPositiveButtonClickListener(
                new OnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(View view) {
                        listener.delete();
                        dialog.dismiss();
                    }
                }, "确定");
        dialog.setOnNegativeButtonClickListener(new OnNegativeButtonClickListener() {
            @Override
            public void onNegativeButtonClick(View view) {
                dialog.dismiss();
            }
        },R.string.cancel);
        dialog.show();
    }
    /**
     * 显示简单的对话框
     *
     * @param content 内容
     */
    public static void showSimpleDialog(Context context, String content) {
        final EduDialog dialog = new EduDialog(context, content);
        dialog.setOnPositiveButtonClickListener(
                new OnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(View view) {
                        dialog.dismiss();
                    }
                }, "确定");
        dialog.show();
    }

    /**
     * 显示错误对话框
     *
     * @param context
     * @param strResId
     */
    public static void showErrorDialog(Context context, int strResId) {
        final EduDialog dialog = new EduDialog(context, strResId, R.mipmap.dialog_failed_ic);
        dialog.setOnPositiveButtonClickListener(
                new OnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(View view) {
                        dialog.dismiss();
                    }
                }, "确定");
        dialog.show();
    }
   public   interface deleteOnClickListener{
        public void delete();
    }

    public void setCloseIconVisibility(int visibility){
        if(mCloseIcon != null){
            mCloseIcon.setVisibility(visibility);
        }
    }
}
