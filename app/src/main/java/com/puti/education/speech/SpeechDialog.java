package com.puti.education.speech;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.util.LogUtil;
import com.puti.education.widget.FullDialog;

/**
 * Created by lenovo on 2017/11/15.
 */

public class SpeechDialog extends Dialog implements View.OnClickListener {

    private  Context mContext;

    private TextView mSure;
    private TextView mCancle;
    private ImageView mSpeech;
    private EditText  mEdit;
    public SpeechDialog(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }
    private void initView(){
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.speech_dialog, null);
        mSure = (TextView) contentView.findViewById(R.id.sure);
        mCancle = (TextView) contentView.findViewById(R.id.cancel);
        mSpeech = (ImageView) contentView.findViewById(R.id.speech);
        mEdit = (EditText) contentView.findViewById(R.id.edit_tv);

        mSpeech.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mCancle.setOnClickListener(this);

        mSpeech.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onSpeechListener != null){
                    onSpeechListener.speech();
                }
                return true;
            }
        });
        setContentView(contentView);
    }

    public void setEdit(String s){
        mEdit.setText(s);
    }

    public void showDialog(){
        super.show();
        mEdit.setText("");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure:
                if (onSpeechListener != null){
                    onSpeechListener.sure(mEdit.getText().toString());
                }
                break;
            case R.id.cancel:
                if (onSpeechListener != null){
                    onSpeechListener.cancle();
                }
                break;
            case R.id.speech:
                LogUtil.d("lei","点击录音");
                if (onSpeechListener != null){
                    onSpeechListener.speech();
                }
                break;
        }
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
