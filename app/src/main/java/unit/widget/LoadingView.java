package unit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;


/**
 * Created by alexlong on 16/5/17.
 */
public class LoadingView extends RelativeLayout {

    private TextView loading_text;
    private ImageView loading_animation;
    private AnimationDrawable mLoadAnimationDrawable;

    public LoadingView(Context context) {

        this(context, null);

    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(
                R.layout.fb_loading_view, this, true);
        loading_text = (TextView) findViewById(R.id.loading_text);
        loading_animation = (ImageView) findViewById(R.id.fb_loading_animation);
        mLoadAnimationDrawable = (AnimationDrawable) loading_animation.getDrawable();

        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.loading_view);
            String loadingText = a.getString(R.styleable.loading_view_loadingText);
            if(!TextUtils.isEmpty(loadingText)){
                loading_text.setText(loadingText);
            }

            a.recycle();
        }
        if(!isInEditMode()){
            show();
        }else{
            //UI 预览不显示
            this.setVisibility(View.GONE);
        }
    }

    public void show(){
        this.setVisibility(View.VISIBLE);
        mLoadAnimationDrawable.start();
    }

    public void dismiss(){
        mLoadAnimationDrawable.stop();
        this.setVisibility(View.GONE);
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility == View.VISIBLE){
            mLoadAnimationDrawable.start();
        }else{
            mLoadAnimationDrawable.stop();
        }
        super.setVisibility(visibility);
    }
}
