package unit.moudle.personal.feedback;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.PutiActivity;
import com.puti.education.listener.BaseListener;
import com.puti.education.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.api.PutiCommonModel;
import unit.widget.HeadView;

/**
 * Created by lei on 2018/6/8.
 * 反馈页面
 */

public class FeedBackActivity extends PutiActivity implements View.OnClickListener {

    private static final int Max = 500;

    @BindView(R.id.edit_tv)
    EditText editTv;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.commit)
    TextView commit;
    @BindView(R.id.headview)
    HeadView headview;

    @Override
    public int getContentView() {
        return R.layout.puti_feed_back_activity;
    }

    @Override
    public void BindPtr() {

    }

    @Override
    public void ParseIntent() {

    }

    @Override
    public void AttachPtrView() {

    }

    @Override
    public void DettachPtrView() {

    }

    @Override
    public void InitView() {
        headview.setCallBack(new HeadView.HeadViewCallBack() {
            @Override
            public void backClick() {
                finish();
            }
        });
        commit.setOnClickListener(this);
        editTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                count.setText(String.valueOf(length));
            }
        });
    }

    @Override
    public void Star() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                PutiCommonModel.getInstance().commitSuggestion(editTv.getText().toString(), new BaseListener() {
                    @Override
                    public void responseResult(Object infoObj, Object listObj, int code, boolean status) {
                        super.responseResult(infoObj, listObj, code, status);
                        ToastUtil.show("提交成功");
                        finish();
                    }

                    @Override
                    public void requestFailed(boolean status, int code, String errorMessage) {
                        super.requestFailed(status, code, errorMessage);
                        ToastUtil.show(errorMessage);
                    }
                });
                break;
        }
    }
}
