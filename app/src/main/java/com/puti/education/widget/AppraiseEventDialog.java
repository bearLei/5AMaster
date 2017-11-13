package com.puti.education.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.puti.education.R;

/**
 * Created by xjbin on 2017/5/18 0018.
 *
 * 事件评价dialog
 *
 */

public class AppraiseEventDialog extends BaseDialog {

    public interface SureClickListener{
        void click(String str);
    }

    SureClickListener sureClickListener;

    public void setSureClickListener(SureClickListener sureClickListener) {
        this.sureClickListener = sureClickListener;
    }

    public AppraiseEventDialog(Context context) {
        super(context);
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_apprise_event;
    }

    @Override
    public void setting() {

        final EditText editText = (EditText) findViewById(R.id.apprise_edit);
        Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        Button sureBtn = (Button) findViewById(R.id.sure_btn);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureClickListener != null){
                    sureClickListener.click(editText.getText().toString());
                    dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
