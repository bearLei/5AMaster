package com.puti.education.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.bean.TextRecord;


/**
 * Created by xjbin on 2017/5/16 0016.
 * 文字记录dialog
 */

public class TextRecordDialog extends BaseDialog {

    //TextRecord mRecord;
    String mContent;

    public TextRecordDialog(Context context,String text) {
        super(context);
       // this.mContent = mRecord;
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_text_record;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setting() {
        TextView textView = (TextView) findViewById(R.id.record_text_tv);
        TextView dateTv = (TextView) findViewById(R.id.date_tv);
        Button greenBtn = (Button) findViewById(R.id.sure_btn);

//        if (mRecord != null){
//            textView.setText(mRecord.desc == null ? "暂无":mRecord.desc);
//            dateTv.setText(mRecord.time  == null ? "暂无":mRecord.time);
//        }

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
