package unit.moudle.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.base.holder.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import unit.entity.MessageEntity;

/**
 * Created by lei on 2018/6/7.
 * 消息Holder
 */

public class MessageHolder extends BaseHolder<MessageEntity> {

    @BindView(R.id.title)
    TextView title;

    public MessageHolder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected View initView(Context context) {
        mRootView = InflateService.g().inflate(R.layout.puti_msg_holder);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void updateUI(Context context, MessageEntity data) {
        if (data == null) {
            return;
        }
        title.setText(data.getContent());
    }
}
