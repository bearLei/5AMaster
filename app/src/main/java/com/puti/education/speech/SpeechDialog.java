package com.puti.education.speech;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ViewUtils;

/**
 * Created by lenovo on 2017/11/15.
 */

public class SpeechDialog extends Dialog  {

    private  Context mContext;
    private ImageView mSpeech;
    private TextView title;
    private ImageView mVoice;
    public SpeechDialog(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public SpeechDialog( Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initView();
    }

    private void initView(){
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.speech_dialog, null);

        mSpeech = (ImageView) contentView.findViewById(R.id.speech);
        title = (TextView) contentView.findViewById(R.id.title);
        mVoice = (ImageView) contentView.findViewById(R.id.voice);



        mSpeech.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onSpeechListener != null){
                    onSpeechListener.speech();
                }
                mVoice.setVisibility(View.VISIBLE);
                title.setText("正在语音输入");
                return true;
            }
        });

        getWindow().setGravity(Gravity.CENTER);

//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        int screenWidth = ViewUtils.getScreenWid(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                screenWidth-ViewUtils.dip2px(mContext,30),
                ViewUtils.dip2px(mContext,200));
        super.setContentView(contentView,params);
    }


    public void showDialog(){
        super.show();
    }




    private SpeechOnClickListener onSpeechListener;

    public void setOnSpeechListener(SpeechOnClickListener onSpeechListener) {
        this.onSpeechListener = onSpeechListener;
    }

    public interface SpeechOnClickListener  {
        void sure(String s);
        void cancle();
        void speech();
   }
}
