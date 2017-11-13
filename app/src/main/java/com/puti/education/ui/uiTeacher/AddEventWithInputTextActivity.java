package com.puti.education.ui.uiTeacher;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.Key;

import butterknife.BindView;

/**
 * Created by xjbin on 2017/5/23 0023.
 *
 * 新增事件--添加文字记录
 */

public class AddEventWithInputTextActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.sure_btn)
    Button commitBtn;
    @BindView(R.id.text_input_tv)
    EditText mInputEdit;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_addevent_with_input_text;
    }

    @Override
    public void initVariables() {
        String recordTextStr = getIntent().getStringExtra(Key.RECORD_TEXT);
        if (!TextUtils.isEmpty(recordTextStr)){
            mInputEdit.setText(recordTextStr);
            mInputEdit.setSelection(recordTextStr.length());
        }
    }

    @Override
    public void initViews() {

        mTitleTv.setText("添加文字记录");

         commitBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String str = mInputEdit.getText().toString() == null ? "":mInputEdit.getText().toString();
                 Intent intent = new Intent();
                 intent.putExtra(Key.RECORD_TEXT,str);
                 setResult(Constant.CODE_RESULT_IMG_TEXT,intent);
                 finish();
             }
         });
    }

    @Override
    public void loadData() {

    }

    public void finishActivity(View view){
        finish();
    }
}
