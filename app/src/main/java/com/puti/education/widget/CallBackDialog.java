package com.puti.education.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.Feedback;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/5/16 0016.
 *
 * 图片，文字 记录
 */

public class CallBackDialog extends BaseDialog {

    private Feedback mFeedback;
    private int mType;//1家长，2专家

    public CallBackDialog(Context context, Feedback feedback) {
        super(context);
        mFeedback = feedback;
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_callback_layout;
    }

    @Override
    public void setting() {

        TextView textView = (TextView) findViewById(R.id.record_text_tv);
        TextView dateTv = (TextView) findViewById(R.id.date_tv);
        Button sureBtn = (Button) findViewById(R.id.sure_btn);
        ImageView imageView = (ImageView) findViewById(R.id.record_img);

        if (mFeedback.ImageList != null && mFeedback.ImageList.size() > 0){
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_picture,mFeedback.ImageList.get(0),imageView);
        }

        if (this.mFeedback != null){
            textView.setText(this.mFeedback.desc == null ? "暂无":this.mFeedback.desc);
            dateTv.setText(this.mFeedback.time  == null ? "暂无":this.mFeedback.time);
        }
        dateTv.setText(TextUtils.isEmpty(mFeedback.time) ? "暂无":mFeedback.time);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}


