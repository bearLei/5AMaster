package unit.moudle.eventregist.holder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.speech.SpeechUtil;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.permission.PermissionUtil;

/**
 * Created by lei on 2018/6/11.
 * 事件登记详情-描述Holder
 */

public class EventDescHolder extends BaseHolder<Object> {
    @BindView(R.id.voice)
    ImageView voice;
    @BindView(R.id.edit_desc)
    EditText editDesc;

    public EventDescHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_event_detail_desc_holder);
        ButterKnife.bind(this, mRootView);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starSpeech();
            }
        });
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, Object data) {

    }


    private void starSpeech(){
        PermissionUtil.g().requestPermissions((Activity) mContext, new PermissionUtil.PermissionCallBack() {
            @Override
            public void success() {
                new SpeechUtil(mContext).createDialog(mContext, new SpeechUtil.SpeechResultCallBack() {
                    @Override
                    public void result(String s) {
                        editDesc.setText(s);
                    }
                });
            }

            @Override
            public void fail() {

            }
        }, Manifest.permission.RECORD_AUDIO);

        PermissionUtil.g().setRationaleListener(new RationaleListener() {
            @Override
            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                PermissionUtil.g().showRequestPermissionRationaleDialog((Activity) mContext,
                        "你已拒绝过语音权限，没有语音权限无法录音！",
                        rationale);
            }
        });
    }

    public String getDesc(){
        return editDesc.getText().toString();
    }
}
