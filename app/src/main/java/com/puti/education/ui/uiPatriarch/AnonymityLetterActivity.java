package com.puti.education.ui.uiPatriarch;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.puti.education.R;
import com.puti.education.listener.BaseListener;
import com.puti.education.netFrame.netModel.PatriarchModel;
import com.puti.education.ui.BaseActivity;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xjbin on 2017/5/25 0025.
 */

public class AnonymityLetterActivity extends BaseActivity{

    @BindView(R.id.title_textview)
    TextView mTitleTv;
    @BindView(R.id.letter_title_edit)
    EditText mTitleEdit;
    @BindView(R.id.letter_content_edit)
    EditText mContentEdit;

    @OnClick(R.id.commit_btn)
    public void commitBtnClick(){
       writeLetter();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_anonymity_letter;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews() {
        mContentEdit.addTextChangedListener(textWatcher);
        mTitleTv.setText("匿名书信");
    }

    @Override
    public void loadData() {

    }

    private void writeLetter(){

        String strTitle = mTitleEdit.getText().toString();
        if (TextUtils.isEmpty(strTitle)){
            ToastUtil.show("标题不能为空");
            return;
        }
        if (len > 255){
            ToastUtil.show("您输入的字符数超出限制");
            return;
        }
        String strContent = mContentEdit.getText().toString();
        if (TextUtils.isEmpty(strContent)){
            ToastUtil.show("内容不能为空");
            return;
        }

        disLoading();
        Map<String, String> mapstr = new HashMap<>();
        String schooluid = ConfigUtil.getInstance(this).get(Constant.KEY_SCHOOL_ID, null);
        if (TextUtils.isEmpty(schooluid)){
            ToastUtil.show("学校uid不能为空");
            return;
        }
        mapstr.put("schooluid", schooluid);
        mapstr.put("title", strTitle);
        mapstr.put("content", strContent);
        String str = JSONObject.toJSONString(mapstr);
        PatriarchModel.getInstance().writeLetter(str,new BaseListener(){

            @Override
            public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                super.responseResult(infoObj, listObj, code, status);

                hideLoading();
                if (infoObj != null){
                    ToastUtil.show("已提交");
                    finish();
                }else{
                    ToastUtil.show(Constant.REQUEST_FAILED_STR);
                }
            }

            @Override
            public void requestFailed(boolean status, int code, String errorMessage) {
                super.requestFailed(status, code, errorMessage);
                hideLoading();
            }
        });

    }

    private int len = 0;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            len = 0;
            int count = s.length();
            Editable editable = mContentEdit.getText();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' '){
                    len = len+1;
                }else if (s.charAt(i) < 128){
                    len = len + 2;
                }else {
                    len = len + 2;
                }
            }

            if (len > 255){
                ToastUtil.show("您输入的字符数已经超出限制");
            }
        }
    };

}
