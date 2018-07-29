package com.puti.education.appupdate;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.puti.education.R;


public class PopUpDialog extends Dialog {
    public interface DialogBuilder{
        int getContent();
        void getView(View v);
    }

	public static final String TAG = "PopUpDialog";
    private DialogBuilder dialogBuilder;

	private Context mContext;
	private LayoutInflater mInflater;

	private Handler mHandler;
	private TranslateAnimation animation;
	/**
	 * PopUpDialog根View
	 */
	private View mRootView;

	/**
	 * 下方actionview
	 */
	private LinearLayout mContentView;

	private OnDismissListener mDismissListener = null;


	private int mAnimationTime = 300;
	private boolean mIsMenuMode;


	protected PopUpDialog(Context context) {
		this(context, false,null);
	}
	
	

	protected PopUpDialog(Context context, boolean isMenuMode, DialogBuilder builder) {
		super(context, R.style.MenuDialogStyle);
        this.dialogBuilder = builder;
		mContext = context;
		mIsMenuMode = isMenuMode;
		mInflater = LayoutInflater.from(context);
		mHandler = new Handler(Looper.getMainLooper());

		mRootView = mInflater.inflate(R.layout.dialog_share, null);
        //todo 点击时间
        mContentView = (LinearLayout) mRootView
                .findViewById(R.id.action_sheet_actionView);
        if(dialogBuilder!=null){
            buildDialog(mContentView);
        }

        mRootView.setOnClickListener(mDefaultDismissListener);
        super.setContentView(mRootView);
    }

    private void buildDialog(LinearLayout mContentView) {
        if(mContentView!=null){
            mContentView.removeAllViews();
        }
        View content = null;
        try{
            content = mInflater.inflate(dialogBuilder.getContent(), null);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(content!=null){
            LinearLayout.LayoutParams lpName = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lpName.gravity = Gravity.CENTER_VERTICAL;
            mContentView.addView(content,lpName);
            dialogBuilder.getView(content);
        }
    }

    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mIsMenuMode) {
			try {
				dismiss();
			} catch (Exception e) {
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * 原Dialog方法,已无作用
	 * 
	 * @deprecated
	 */
	@Override
	public void setContentView(View view) {
		throw new UnsupportedOperationException("this method is not support");
	}

	/**
	 * 原Dialog方法,已无作用.
	 * 
	 * @deprecated
	 */
	@Override
	public void setContentView(int layoutResID) {
		throw new UnsupportedOperationException("this method is not support");
	}

	/**
	 * 原Dialog方法,已无作用
	 * @deprecated
	 */
	@Override
	public void setTitle(int resId) {
		throw new UnsupportedOperationException("this method is not support");
	}

	/**
	 * @deprecated
	 */
	@Override
	public void setTitle(CharSequence title) {
		throw new UnsupportedOperationException("this method is not support");
	}

	@Override
	public void show() {
		super.show();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				/**
				 * 播放View动画从下升起
				 */
                if(mContentView!=null){
                    mContentView.setVisibility(View.VISIBLE);
                }
				 animation = new TranslateAnimation(0, 0, mContentView.getHeight(), 0);
				animation.setFillEnabled(true);

                //IDE BUG WILL FIX IN 1.4 https://code.google.com/p/android/issues/detail?id=177611
				animation.setInterpolator(AnimationUtils.loadInterpolator(
						mContext, android.R.anim.decelerate_interpolator));
				animation.setDuration(mAnimationTime);
				mContentView.startAnimation(animation);
			}
		}, 100);

	}

	@Override
	public void dismiss() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				 animation = new TranslateAnimation(0, 0, 0, mContentView.getHeight());
                //IDE BUG WILL FIX IN 1.4 https://code.google.com/p/android/issues/detail?id=177611
				animation.setInterpolator(AnimationUtils.loadInterpolator(
						mContext, android.R.anim.decelerate_interpolator));
				animation.setDuration(200);
				animation.setFillAfter(true);
				mContentView.startAnimation(animation);

				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
						try {
							PopUpDialog.super.dismiss();
						} catch (Exception e) {
							// 异常不做任何处理，防止崩溃即可
						}
                        if(mContentView!=null){
                            mContentView.setVisibility(View.INVISIBLE);
                        }
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationStart(Animation animation) {
					}

				});
			}
		}, 0);
	}


	/**
	 * AcitonSheet消失监听器
	 */
	public interface OnDismissListener {
		/**
		 * PopUpDialog消失时调用
		 */
        void onDismiss();
	}

	/**
	 */
	public interface OnButtonClickListener {

		/**
		 * 已经选中的View被点击的回调
		 * 
		 * @param clickedView
		 * @param which
		 *            id从0,1,2..开始,根据不同的id设置响应事件
		 */
        void OnClick(View clickedView, int which);
	}

	/**
	 * 对话框按钮点击事件监听
	 */

	/**
	 * 默认的关闭对话框监听器
	 */
	private View.OnClickListener mDefaultDismissListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
			if (mDismissListener != null) {
				mDismissListener.onDismiss();
			}
		}
	};

	/**
	 * 创建普通的PopUpDialog，默认点击空白区域会消失
	 */
	public static PopUpDialog create(Context context) {
		final PopUpDialog dialog = new PopUpDialog(context, false,null);
		return dialog;
	}

    /**
     * 创建普通的PopUpDialog，默认点击空白区域会消失
     */
    public static PopUpDialog create(Context context, DialogBuilder builder) {
        final PopUpDialog dialog = new PopUpDialog(context, false,builder);
        return dialog;
    }

	/**
	 * 控制是否开启空白点击消失，true消失，false不消失；
	 */
	public void setOutsideDismissEnable(boolean enable) {
		if (enable) {
			mRootView.setOnClickListener(mDefaultDismissListener);
		} else {
			mRootView.setOnClickListener(null);
		}
	}

}
