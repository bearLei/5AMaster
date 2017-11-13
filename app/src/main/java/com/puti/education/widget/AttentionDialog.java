package com.puti.education.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.ConsultExpert;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/5/16 0016.
 *
 * 图片，文字 记录
 */

public class AttentionDialog extends BaseDialog {

    ConsultExpert mConsultExpert;

    public AttentionDialog(Context context, ConsultExpert consultExpert) {
        super(context);
        this.mConsultExpert = consultExpert;
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_attention_layout;
    }

    @Override
    public void setting() {

        TextView wxNameTv = (TextView) findViewById(R.id.wx_name_tv);
        Button sureBtn = (Button) findViewById(R.id.sure_btn);
        Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        ImageView imageView = (ImageView) findViewById(R.id.ewm_img);
        ImgLoadUtil.displayCirclePic(R.mipmap.ic_picture,mConsultExpert.barcode,imageView);
        wxNameTv.setText(TextUtils.isEmpty(mConsultExpert.name)?"暂无":mConsultExpert.name);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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


